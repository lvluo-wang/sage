package me.icymint.sage.base.rest.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import me.icymint.sage.base.spec.def.BaseCode;
import me.icymint.sage.base.spec.exception.ServiceException;

import static me.icymint.sage.base.util.Enums.getI18nKey;
import static me.icymint.sage.base.util.HMacs.encodeToHex;

/**
 * Created by daniel on 16/9/2.
 */
public class ResultResource {
    private final String id;
    private final String code;
    private final String message;
    @JsonProperty("_embedded")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ResultResource embedded;

    public ResultResource(Throwable se, boolean devMode) {
        ServiceException sex = se instanceof ServiceException ? (ServiceException) se : null;
        this.id = sex != null ? sex.getId() : se.getClass().getName();
        this.code = calculate(sex, devMode);
        this.message = se.getLocalizedMessage();
        Throwable cause = se.getCause();
        if (devMode && cause != null) {
            embedded = new ResultResource(cause, true);
        }
    }

    private String calculate(ServiceException sex, boolean devMode) {
        Enum<?> code = sex == null ? BaseCode.UNKNOWN_EXCEPTION : sex.getErrorCode();
        if (devMode) {
            return getI18nKey(code);
        }
        return encodeToHex(code.getClass().getName(), code.getClass().getName()).substring(0, 4)
                + encodeToHex(code.name(), code.name()).substring(0, 8);
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
