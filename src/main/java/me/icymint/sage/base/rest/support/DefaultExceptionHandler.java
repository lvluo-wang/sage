package me.icymint.sage.base.rest.support;

import me.icymint.sage.base.rest.resource.ResultResource;
import me.icymint.sage.base.spec.def.BaseExceptionCode;
import me.icymint.sage.base.spec.exception.Exceptions;
import me.icymint.sage.base.spec.exception.InternalServerErrorException;
import me.icymint.sage.base.spec.exception.InvalidArgumentException;
import me.icymint.sage.base.spec.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerMethodExceptionResolver;
import org.springframework.web.util.NestedServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by daniel on 16/9/2.
 */
@ControllerAdvice
public class DefaultExceptionHandler extends AbstractHandlerMethodExceptionResolver {

    private final Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @Autowired
    ApplicationContext context;
    @Autowired
    RequestValidateHandler requestValidateHandler;

    @Override
    protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception ex) {
        return null;
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<ResultResource> handleException(Exception ex) throws Exception {
        ServiceException se = translate(ex);
        logError(se);
        return new ResponseEntity<>(new ResultResource(se), getStatus(ex));
    }

    private void logError(Exception ex) {
        StringBuilder id = new StringBuilder();
        boolean byBaseCause = false;
        for (Throwable throwable = ex; throwable != null; throwable = throwable.getCause()) {
            id.append(":");
            if (throwable instanceof ServiceException) {
                id.append(((ServiceException) throwable).getId());
                byBaseCause = true;
            } else {
                id.append("-");
            }
        }

        if (byBaseCause) {
            logger.warn(ServiceException.class.getSimpleName() + " Id:{}", id.substring(1), ex);
        } else {
            logger.error("Unhandled Exception [{}]", id.substring(1), ex);
        }
    }

    private ServiceException translate(Exception ex) {
        if (ex instanceof ServletRequestBindingException && ex.getMessage().contains("Missing request header 'Authorization'")) {
            ex = new InvalidArgumentException(ex, context, BaseExceptionCode.AUTHORIZATION_REQUIRED);
        } else if (ex instanceof NestedServletException) {
            Throwable cause = ex.getCause();
            if (cause instanceof ServiceException) {
                ex = (Exception) cause;
            } else {
                ex = new InvalidArgumentException(ex, context, BaseExceptionCode.PARAM__ILLEGAL);
            }
        } else if (ex instanceof DuplicateKeyException) {
            ex = new ServiceException(ex, context, BaseExceptionCode.DUPLICATE_KEY);
        } else if (ex instanceof MethodArgumentNotValidException) {
            @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
            InvalidArgumentException e = requestValidateHandler.checkExceptions(((MethodArgumentNotValidException) ex).getBindingResult());
            ex = e != null ? e : new InvalidArgumentException(ex, context, BaseExceptionCode.__, ex.getMessage());
        } else if (!(ex instanceof ServiceException)) {
            ex = new InternalServerErrorException(ex, context);
        }
        return (ServiceException) ex;
    }

    private HttpStatus getStatus(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        boolean checkAnnotation = true;

        if (ex instanceof ServiceException) {
            ServiceException sex = (ServiceException) ex;
            try {
                if (sex.getHttpStatus() != null) {
                    status = HttpStatus.valueOf(sex.getHttpStatus());
                    checkAnnotation = false;
                }
            } catch (Exception exx) {
                Exceptions.catching(exx);
            }
        }

        if (checkAnnotation) {
            ResponseStatus rs = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
            if (rs != null) {
                status = rs.code();
            }
        }

        return status;
    }
}
