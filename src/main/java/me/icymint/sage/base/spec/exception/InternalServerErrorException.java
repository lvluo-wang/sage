package me.icymint.sage.base.spec.exception;

import org.springframework.context.MessageSource;

/**
 * Created by daniel on 16/9/2.
 */
public class InternalServerErrorException extends ServiceException {
    public InternalServerErrorException(Exception ex, MessageSource source) {
        super(ex, source, ex.getMessage());
    }
}
