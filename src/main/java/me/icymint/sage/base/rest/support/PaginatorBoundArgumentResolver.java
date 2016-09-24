package me.icymint.sage.base.rest.support;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.google.common.base.Splitter;
import me.icymint.sage.base.rest.entity.PaginatorBound;
import me.icymint.sage.base.rest.util.QueryStrings;
import me.icymint.sage.base.spec.def.MagicConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.expression.ParseException;
import org.springframework.format.Formatter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by daniel on 16/9/4.
 */
@ControllerAdvice
public class PaginatorBoundArgumentResolver implements HandlerMethodArgumentResolver, Formatter<PaginatorBound> {
    private static final String PAGE = "page";
    private static final String SIZE = "size";
    private static final String TOTAL = "total";
    private static final String ORDERS = "orders";
    private static final int MAX_LIMIT = 100;
    private static final int DEFAULT_LIMIT = 10;
    private Logger logger = LoggerFactory.getLogger(PaginatorBoundArgumentResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return PaginatorBound.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String text = webRequest.getHeader(MagicConstants.X_PAGING);
        if (!StringUtils.hasText(text)) {
            text = webRequest.getNativeRequest(HttpServletRequest.class).getQueryString();
        }
        return parse(text, null);
    }

    @Override
    public PaginatorBound parse(String text, Locale locale) throws ParseException {
        logger.debug("PageBounds resolve:{}", text);
        if (!StringUtils.hasText(text)) {
            return new PaginatorBound(1, DEFAULT_LIMIT);
        }

        MultiValueMap<String, String> map = QueryStrings.parse(text);

        boolean containsTotal = false;

        List<Order> orders = new ArrayList<>();
        String regex = "([A-Z][_A-Z0-9]*)(:(DESC|ASC|desc|asc))?";
        Pattern pattern = Pattern.compile(regex);
        String totalStr = map.getFirst(TOTAL);
        String pageStr = map.getFirst(PAGE);
        String sizeStr = map.getFirst(SIZE);
        String orderStr = map.getFirst(ORDERS);

        if (!StringUtils.isEmpty(orderStr)) {
            Splitter.on(',')
                    .omitEmptyStrings()
                    .trimResults()
                    .splitToList(orderStr)
                    .forEach(orderArr -> {
                        Matcher matcher = pattern.matcher(orderArr);
                        if (matcher.matches()) {
                            String direction = matcher.group(3);
                            if (direction == null) {
                                direction = Order.Direction.ASC.name();
                            } else {
                                direction = direction.toUpperCase();
                            }
                            orders.add(new Order(matcher.group(1), Order.Direction.valueOf(direction), null));
                        }
                    });
        }

        if (!StringUtils.isEmpty(totalStr)) {
            containsTotal = Boolean.valueOf(totalStr);
        }

        int page = 1;
        if (!StringUtils.isEmpty(pageStr)) {
            page = Integer.parseInt(pageStr);
        }
        page = Math.max(1, page);
        int size = DEFAULT_LIMIT;
        if (!StringUtils.isEmpty(sizeStr)) {
            size = Integer.parseInt(sizeStr);
        }
        size = Math.min(size, MAX_LIMIT);
        size = Math.max(1, size);

        return new PaginatorBound(page, size, orders, containsTotal);
    }

    @Override
    public String print(PaginatorBound object, Locale locale) {
        if (object == null) {
            return null;
        }
        return "page=" + object.getPage() + "&size=" + object.getLimit() + "&total=" + object.isContainsTotalCount()
                + object.getOrders()
                .stream()
                .map(o -> "&" + o.getProperty() + ":" + o.getDirection())
                .collect(Collectors.joining());
    }
}
