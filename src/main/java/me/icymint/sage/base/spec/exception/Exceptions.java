package me.icymint.sage.base.spec.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by daniel on 16/9/2.
 */
public class Exceptions {

    private static final Logger logger = LoggerFactory.getLogger(Exceptions.class);

    public static void catching(Throwable exp) {
        catching(exp, true);
    }

    public static void catching(Throwable exp, boolean needPrintStacktrace) {
        if (!needPrintStacktrace) {
            logger.warn("Catching exception {}[{}]: {}", exp.getClass(), getId(exp), exp.getLocalizedMessage());
        } else {
            logger.warn("Catching exception {}[{}]: {}", exp.getClass(), getId(exp), exp);
        }
    }

    private static String getId(Throwable exp) {
        return (exp instanceof ServiceException) ? ((ServiceException) exp).getId() : "-";
    }

    public static ServiceException wrap(Throwable throwable) {
        return wrap(throwable, new UnspecifiedException());
    }

    public static <T extends Throwable> T wrap(Throwable cause, T ex) {
        logger.warn("Wrapping exception {} to {}[{}]", cause.getClass(), ex.getClass(), getId(ex));
        ex.initCause(cause);
        return ex;
    }

    public static <T extends ServiceException> ServiceException wrap(Throwable cause, T ex) {
        if (ServiceException.class.isAssignableFrom(cause.getClass())) {
            return (ServiceException) cause;
        }

        logger.warn("Wrapping exception {} to {}[{}]", cause.getClass(), ex.getClass(), getId(ex));

        ex.initCause(cause);
        return ex;
    }
}
