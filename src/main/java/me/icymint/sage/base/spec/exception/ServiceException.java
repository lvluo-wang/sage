package me.icymint.sage.base.spec.exception;

import me.icymint.sage.base.spec.def.BaseCode;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.UUID;

import static me.icymint.sage.base.util.Enums.getI18nKey;
import static me.icymint.sage.base.util.Enums.getI18nValue;

/**
 * Created by daniel on 16/9/2.
 */
public class ServiceException extends RuntimeException {
    private final Object[] args;
    private final MessageSource source;
    private final String id;
    private final Enum<?> errorCode;
    private Integer httpStatus;

    public ServiceException(Throwable e) {
        this(e, (MessageSource) null, e.getMessage());
    }

    public ServiceException(String code) {
        this((MessageSource) null, code);
    }

    public ServiceException(Enum<?> code) {
        this((Throwable) null, (MessageSource) null, code);
    }

    public ServiceException(MessageSource source, String code) {
        this(null, source, code);
    }

    public ServiceException(MessageSource source, Enum<?> code, Object... args) {
        this(null, source, code, args);
    }

    public ServiceException(Throwable ex, MessageSource source, Enum<?> code, Object... args) {
        super(getI18nKey(code == null ? BaseCode.UNKNOWN_EXCEPTION : code), ex);
        this.errorCode = code == null ? BaseCode.UNKNOWN_EXCEPTION : code;
        this.source = source;
        this.args = args;
        this.id = UUID.randomUUID().toString();
    }

    public ServiceException(Throwable ex, MessageSource source, String code) {
        this(ex, source, BaseCode.__, code);
    }

    public String getId() {
        return id;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public ServiceException setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    public String getMessage() {
        return getServerLocalizedMessage();
    }

    public String getServerLocalizedMessage() {
        return getI18nValue(source, Locale.getDefault(), super.getMessage(), args);
    }

    public Enum<?> getErrorCode() {
        return errorCode;
    }

    public String getCode() {
        return super.getMessage();
    }

    public String getLocalizedMessage() {
        return getI18nValue(source, LocaleContextHolder.getLocale(), super.getMessage(), args);
    }


}
