package me.icymint.sage.user.spec.exception;

import me.icymint.sage.base.spec.exception.ServiceException;
import org.springframework.context.MessageSource;

/**
 * Created by daniel on 16/9/5.
 */
public class UserServiceException extends ServiceException {
    public UserServiceException(MessageSource messageSource, Enum<?> code, Object... args) {
        super(messageSource, code, args);
    }
}
