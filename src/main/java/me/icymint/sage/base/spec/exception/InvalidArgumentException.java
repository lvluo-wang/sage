package me.icymint.sage.base.spec.exception;

import org.springframework.context.MessageSource;

/**
 * Created by daniel on 16/9/2.
 */
public class InvalidArgumentException extends ServiceException {

    public InvalidArgumentException(Throwable throwable, MessageSource source, Enum<?> code, Object... args) {
        super(throwable, source, code, args);
    }

    public InvalidArgumentException(MessageSource source, Enum<?> code, Object... args) {
        super(source, code, args);
    }

    public InvalidArgumentException(MessageSource messageSource, String code) {
        super(messageSource, code);
    }

    public InvalidArgumentException(Throwable e) {
        super(e);
    }
}
