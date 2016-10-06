package me.icymint.sage.base.util;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import me.icymint.sage.base.spec.annotation.I18nEnum;
import me.icymint.sage.base.spec.annotation.I18nLabel;
import me.icymint.sage.base.spec.def.Magics;
import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * Created by daniel on 16/9/2.
 */
public class Enums {

    private static final Pattern LAST_NAME = Pattern.compile("(.+\\.)+([A-Z][_A-Z0-9]*|_+)$");

    public static String getI18nKey(Enum<?> code) {
        if (code == null) {
            return null;
        }
        String prefix = getI18nKeyPrefix(code.getDeclaringClass());
        return prefix != null ? prefix + code.name() : null;
    }

    public static String getI18nKeyPrefix(Class<? extends Enum<?>> enumClass) {
        I18nEnum i18nEnum = enumClass.getAnnotation(I18nEnum.class);
        String prefix;
        if (i18nEnum == null || StringUtils.isEmpty(i18nEnum.value())) {
            prefix = enumClass.getName();
        } else {
            prefix = i18nEnum.value();
        }
        return Magics.PROP_PREFIX + prefix + ".";
    }

    public static String getI18nValue(MessageSource source, Locale locale, Enum<?> code, Object... args) {
        return getI18nValue(source, locale, getI18nKey(code), args);
    }

    public static String getI18nValue(MessageSource source, Locale locale, String code, Object... args) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        if (source == null) {
            return null;
        }
        String defaultCode = parseDefaultCode(code);
        return source.getMessage(code, args, defaultCode, locale);
    }

    private static String parseDefaultCode(String code) {
        if (code != null) {
            Matcher matcher = LAST_NAME.matcher(code);
            if (matcher.matches()) {
                String value = matcher.group(2);
                List<String> list = Splitter.on('_')
                        .trimResults()
                        .splitToList(value);
                if (value.length() + 1 == list.size()) {
                    return "{0}";
                }
                AtomicInteger adder = new AtomicInteger();
                return list.stream()
                        .map(word -> StringUtils.isEmpty(word) ? ("{" + adder.getAndIncrement() + "}") : word.toLowerCase())
                        .collect(joining(" "));
            }
        }
        return code;
    }

    public static Method findLabelMethod(Class<Enum<?>> enumClass) {
        for (Method method : enumClass.getDeclaredMethods()) {
            I18nLabel i18nLabel = method.getAnnotation(I18nLabel.class);
            if (i18nLabel != null && method.getParameterCount() == 0 && method.getReturnType() == String.class) {
                return method;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <E extends Enum<E>> List<EnumInfo<E>> getEnumInfoList(MessageSource source, Locale locale, Class<E> enumClass) {
        E[] es = (E[]) Array.newInstance(enumClass, 0);
        return getEnumInfoList(source, locale, EnumSet.allOf(enumClass).toArray(es));
    }

    public static <E extends Enum<E>> List<EnumInfo<E>> getEnumInfoList(MessageSource source, Locale locale, E[] enums) {
        if (enums == null || enums.length == 0) {
            return Lists.newArrayList();
        }
        return Stream.of(enums)
                .map(e -> new EnumInfo<E>()
                        .setName(e)
                        .setLabel(getI18nValue(source, locale, e)))
                .collect(Collectors.toList());
    }

    public static class EnumInfo<E> {
        private E name;
        private String label;

        public E getName() {
            return name;
        }

        public EnumInfo<E> setName(E name) {
            this.name = name;
            return this;
        }

        public String getLabel() {
            return label;
        }

        public EnumInfo<E> setLabel(String label) {
            this.label = label;
            return this;
        }

        @Override
        public String toString() {
            return "EnumInfo{" +
                    "name=" + name +
                    ", label='" + label + '\'' +
                    '}';
        }
    }
}
