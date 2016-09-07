package me.icymint.sage.base.spec.api;

import me.icymint.sage.base.spec.exception.InvalidArgumentException;

/**
 * Created by daniel on 16/9/5.
 */
public interface SageValidator {
    void validate(Object target, String name) throws InvalidArgumentException;
}
