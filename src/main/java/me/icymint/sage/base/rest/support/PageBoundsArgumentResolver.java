package me.icymint.sage.base.rest.support;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.google.common.base.Splitter;
import me.icymint.sage.base.rest.util.QueryStrings;
import me.icymint.sage.base.spec.def.MagicConstants;
import org.springframework.core.MethodParameter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by daniel on 16/9/4.
 */
public class PageBoundsArgumentResolver extends AbstractNamedValueMethodArgumentResolver {
    private static final String PAGE = "page";
    private static final String SIZE = "size";
    private static final String TOTAL = "total";
    private static final String ORDERS = "orders";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return PageBounds.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
        return new NamedValueInfo("pageBounds", false, null);
    }

    @Override
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
        String text = request.getHeader(MagicConstants.HEADER_X_PAGING);
        if (!StringUtils.hasText(text)) {
            text = request.getNativeRequest(HttpServletRequest.class).getQueryString();
        }
        if (!StringUtils.hasText(text)) {
            return new PageBounds(1, MagicConstants.PAGE_MAX_LIMIT);
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
        int size = MagicConstants.PAGE_DEFAULT_LIMIT;
        if (!StringUtils.isEmpty(sizeStr)) {
            size = Integer.parseInt(sizeStr);
        }
        size = Math.min(size, MagicConstants.PAGE_MAX_LIMIT);
        size = Math.max(1, size);

        return new PageBounds(page, size, orders, containsTotal);
    }
}
