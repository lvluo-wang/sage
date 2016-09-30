package me.icymint.sage.base.rest.request;

/**
 * Created by daniel on 2016/9/30.
 */
public class LoginRequest {
    private Long identityId;
    private Long clientId;
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
