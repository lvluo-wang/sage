package me.icymint.sage.base.rest.support;

import me.icymint.sage.base.spec.api.SageValidator;
import me.icymint.sage.base.spec.exception.InvalidArgumentException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

/**
 * Created by daniel on 16/9/5.
 */
@Aspect
@Component
public class RequestValidateHandler implements SageValidator {

    private final Logger logger = LoggerFactory.getLogger(RequestValidateHandler.class);

    @Autowired
    ApplicationContext context;
    @Autowired
    Validator validator;

    @Before(value = "within(me.icymint.sage..*Controller) && args(..,bindingResult)", argNames = "bindingResult")
    public void check(BindingResult bindingResult) {
        InvalidArgumentException ex = checkExceptions(bindingResult);
        if (ex != null) {
            throw ex;
        }
    }


    public InvalidArgumentException checkExceptions(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.warn("Request {} failed!", bindingResult.getModel());
            FieldError err = bindingResult.getFieldError();
            return new InvalidArgumentException(context, err.getField() + ":" + err.getDefaultMessage());
        }
        return null;
    }

    @Override
    public void validate(Object target, String name) throws InvalidArgumentException {
        DataBinder binder = new DataBinder(target);
        binder.setValidator(validator);
        binder.validate();
        check(binder.getBindingResult());
    }
}
