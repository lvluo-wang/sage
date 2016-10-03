package me.icymint.sage.base.rest.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by daniel on 2016/9/30.
 */
public class LoginRequest {
    @NotNull
    @Min(0L)
    @ApiModelProperty(example = "10000")
    private Long identityId;
    @ApiModelProperty(example = "1000")
    @NotNull
    @Min(0L)
    private Long clientId;
    @NotNull
    @Size(min = 32, max = 64)
    private String password;

    public Long getIdentityId() {
        return identityId;
    }

    public LoginRequest setIdentityId(Long identityId) {
        this.identityId = identityId;
        return this;
    }

    public Long getClientId() {
        return clientId;
    }

    public LoginRequest setClientId(Long clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LoginRequest setPassword(String password) {
        this.password = password;
        return this;
    }
}
