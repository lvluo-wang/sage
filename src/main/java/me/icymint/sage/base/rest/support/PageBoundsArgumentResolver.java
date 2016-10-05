package me.icymint.sage.base.rest.support;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.icymint.sage.base.spec.annotation.PageableView;
import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.base.spec.entity.Pageable;
import org.apache.ibatis.session.RowBounds;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;
import org.springframework.web.util.UriUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toMap;

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
        return RowBounds.class.isAssignableFrom(parameter.getParameterType())
                || parameter.getMethodAnnotation(PageableView.class) != null;
    }

    @Override
    protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
        return new NamedValueInfo("pageBounds", false, null);
    }

    @Override
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
        String text = request.getHeader(Magics.HEADER_X_PAGING);
        Map<String, String> map;
        if (StringUtils.hasText(text)) {
            Splitter equalSplit = Splitter
                    .on("=")
                    .trimResults()
                    .omitEmptyStrings()
                    .limit(2);
            map = Splitter
                    .on("&")
                    .omitEmptyStrings()
                    .trimResults()
                    .splitToList(Strings.nullToEmpty(text))
                    .stream()
                    .map(equalSplit::splitToList)
                    .filter(l -> l.size() != 2)
                    .map(l -> new NameValue(l.get(0), decodeValue(l.get(1))))
                    .filter(nv -> nv.value != null)
                    .collect(toMap(nv -> nv.name, nv -> nv.value));
        } else {
            map = Maps.newHashMap();
            map.put(SIZE, request.getParameter(SIZE));
            map.put(PAGE, request.getParameter(PAGE));
            map.put(TOTAL, request.getParameter(TOTAL));
            map.put(ORDERS, request.getParameter(ORDERS));
        }
        int total = getInt(map.get(TOTAL), -1);
        int page = getInt(map.get(PAGE), 1);
        page = Math.max(1, page);
        int size = getInt(map.get(SIZE), Magics.PAGE_DEFAULT_LIMIT);
        size = Math.min(size, Magics.PAGE_MAX_LIMIT);
        size = Math.max(1, size);
        List<Order> orders = getOrders(map.get(ORDERS), Lists.newArrayList());
        return new Pageable(page, size, orders, total);
    }

    private List<Order> getOrders(String value, List<Order> orders) {
        if (!StringUtils.isEmpty(value)) {
            Pattern pattern = Pattern.compile("([A-Z][_A-Z0-9]*)(:(DESC|ASC|desc|asc))?");
            Splitter.on(',')
                    .omitEmptyStrings()
                    .trimResults()
                    .splitToList(value)
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
        return orders;
    }

    private int getInt(String value, int defaultValue) {
        if (!StringUtils.isEmpty(value)) {
            try {
                return Integer.valueOf(value);
            } catch (Exception e) {
                //Ignore
            }
        }
        return defaultValue;
    }

    private String decodeValue(String value) {
        try {
            return UriUtils.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    private class NameValue {
        private final String name;
        private final String value;

        NameValue(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }
}
