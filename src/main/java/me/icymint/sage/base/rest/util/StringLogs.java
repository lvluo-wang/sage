package me.icymint.sage.base.rest.util;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * Created by daniel on 16/9/6.
 */
public class StringLogs {

    public static void append(StringBuilder sb, String str) {
        sb.append(" ");

        if (StringUtils.isEmpty(str)) {
            sb.append("-");
        } else if (Pattern.compile("[ \t\n\r]").matcher(str).find()) {
            // if contains whitespaces, escape and quote
            sb.append("\"");
            sb.append(str.replace("\"", "\"\""));
            sb.append("\"");
        } else {
            sb.append(str);
        }
    }
}
