package me.icymint.sage.base.rest.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import me.icymint.sage.base.spec.exception.ServiceException;

/**
 * Created by daniel on 16/9/2.
 */
public class ResultResource {

    @JsonProperty("_embedded")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ResultResource embedded;
    private final String id;
    private final String code;
    private final String message;

    public ResultResource(Throwable se) {
        ServiceException sex = se instanceof ServiceException ? (ServiceException) se : null;
        this.id = sex != null ? sex.getId() : se.getClass().getName();
        this.code = sex != null ? sex.getCode() : null;
        this.message = se.getLocalizedMessage();
        Throwable cause = se.getCause();
        if (cause != null) {
            embedded = new ResultResource(cause);
        }
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
