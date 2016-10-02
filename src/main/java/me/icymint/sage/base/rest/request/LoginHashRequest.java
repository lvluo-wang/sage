package me.icymint.sage.base.rest.request;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by daniel on 2016/9/30.
 */
public class LoginHashRequest {
    private Long id;
    @ApiModelProperty(example = "1000")
    private Long clientId;
    private String accessSecret;

    public Long getId() {
        return id;
    }

    public LoginHashRequest setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getClientId() {
        return clientId;
    }

    public LoginHashRequest setClientId(Long clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public LoginHashRequest setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
        return this;
    }
}
