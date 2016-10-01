package me.icymint.sage.base.rest.util;

import me.icymint.sage.base.util.Exceptions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.util.Arrays;

/**
 * Created by daniel on 16/9/4.
 */
public class QueryStrings {
    public static MultiValueMap<String, String> parse(String queryString) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        if (StringUtils.hasText(queryString)) {
            Arrays.asList(queryString.split("&")).forEach(p -> {
                int equalIdx = p.indexOf('=');
                if (equalIdx > 0) {
                    String key = decode(p.substring(0, equalIdx).trim());
                    String value = "";
                    if (equalIdx + 1 < p.length()) {
                        value = decode(p.substring(equalIdx + 1).trim());
                    }
                    if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value)) {
                        map.add(key, value);
                    }
                }
            });
        }

        return map;
    }

    private static String decode(String str) {
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (Exception ex) {
            Exceptions.catching(ex);
        }
        return str;
    }
}
