package me.icymint.sage.base.spec.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Function;

/**
 * Created by daniel on 2016/10/2.
 *
 * @see org.springframework.stereotype.Controller
 * @see me.icymint.sage.base.rest.entity.PaginatorResponse
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)

public @interface ResponseView {

    /**
     * Change {@link org.springframework.stereotype.Controller} response Object to the target Type.
     *
     * @return the target type
     */
    Class<?> value();


    Class<? extends Function<?, ?>> converter() default NullFunction.class;

    /**
     * When Controller response Type is instance of {@link java.util.List},
     * then this option will determine whether or not change {@link java.util.List} to {@link me.icymint.sage.base.rest.entity.PaginatorResponse}
     * if this value is false, then please add a {@link io.swagger.annotations.ApiOperation} on the method
     *
     * @return
     */
    boolean convertList() default true;

    class NullFunction implements Function<Object, Object> {

        @Override
        public Object apply(Object o) {
            return o;
        }
    }
}
