package me.icymint.sage.base.spec.exception;

import org.springframework.context.MessageSource;

/**
 * Created by daniel on 16/9/4.
 */
public class UnauthorizedException extends ServiceException {
    public UnauthorizedException(MessageSource messageSource, Enum<?> code, Object... args) {
        super(messageSource, code, args);
    }
}
