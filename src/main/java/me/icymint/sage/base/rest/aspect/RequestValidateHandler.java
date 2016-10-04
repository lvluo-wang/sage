package me.icymint.sage.base.rest.aspect;

import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.base.spec.exception.InvalidArgumentException;
import me.icymint.sage.base.spec.internal.api.SageValidator;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import static java.util.stream.Collectors.joining;

/**
 * Created by daniel on 16/9/5.
 */
@Aspect
@Component
@Order(Magics.AOP_ORDER_REQUEST_VALIDATE)
public class RequestValidateHandler implements SageValidator {

    private final Logger logger = LoggerFactory.getLogger(RequestValidateHandler.class);

    @Autowired
    ApplicationContext context;
    @SuppressWarnings("SpringJavaAutowiringInspection")
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
            return new InvalidArgumentException(context, bindingResult
                    .getFieldErrors()
                    .stream()
                    .sorted((x, y) -> x.getField().compareTo(y.getField()))
                    .map(f -> f.getField() + ":" + f.getDefaultMessage())
                    .collect(joining(";")));
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
