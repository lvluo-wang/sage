package me.icymint.sage.base.rest.request;

/**
 * Created by daniel on 2016/9/30.
 */
public class LoginHashRequest {
    private Long id;
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
