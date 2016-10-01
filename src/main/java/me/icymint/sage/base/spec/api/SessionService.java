package me.icymint.sage.base.spec.api;

import me.icymint.sage.base.spec.internal.api.RuntimeContext;

/**
 * Created by daniel on 16/9/6.
 */
public interface SessionService {
    String fetchSession(RuntimeContext context);
}
