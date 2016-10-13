package me.icymint.sage.base.spec.exception;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

/**
 * Created by daniel on 16/9/4.
 */
public class UnauthorizedException extends ServiceException {
    public UnauthorizedException(MessageSource messageSource, Enum<?> code, Object... args) {
        super(messageSource, code, args);
        this.setHttpStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
