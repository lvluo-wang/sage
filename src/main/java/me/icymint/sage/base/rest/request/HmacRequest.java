package me.icymint.sage.base.rest.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by daniel on 16/9/6.
 */
public class HmacRequest {
    @NotNull
    @Size(min = 8, max = 64)
    private String password;

    public String getPassword() {
        return password;
    }

    public HmacRequest setPassword(String password) {
        this.password = password;
        return this;
    }
}
