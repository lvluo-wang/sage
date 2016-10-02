package me.icymint.sage.base.rest.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by daniel on 16/9/6.
 */
public class PasswordRequest {
    @NotNull
    @Size(min = 8, max = 64)
    @ApiModelProperty(example = "12345678")
    private String password;

    public String getPassword() {
        return password;
    }

    public PasswordRequest setPassword(String password) {
        this.password = password;
        return this;
    }
}
