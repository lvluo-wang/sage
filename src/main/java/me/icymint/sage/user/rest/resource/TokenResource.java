package me.icymint.sage.user.rest.resource;

import java.time.Instant;

/**
 * Created by daniel on 2016/10/2.
 */
public class TokenResource {
    private Long id;
    private String accessSecret;
    private Instant expireTime;

    public Long getId() {
        return id;
    }

    public TokenResource setId(Long id) {
        this.id = id;
        return this;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public TokenResource setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
        return this;
    }

    public Instant getExpireTime() {
        return expireTime;
    }

    public TokenResource setExpireTime(Instant expireTime) {
        this.expireTime = expireTime;
        return this;
    }
}
