package me.icymint.sage.user.rest.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by daniel on 16/9/5.
 */
public class TokenRequest {
    @NotNull
    private Long uid;
    @NotNull
    private Long cid;
    @NotNull
    @Size(min = 8, max = 32)
    @Pattern(regexp = "[0-9a-zA-Z]{8,32}")
    private String nonce;
    @NotNull
    private Long ts;
    @NotNull
    private String sign;

    public Long getUid() {
        return uid;
    }

    public TokenRequest setUid(Long uid) {
        this.uid = uid;
        return this;
    }

    public Long getCid() {
        return cid;
    }

    public TokenRequest setCid(Long cid) {
        this.cid = cid;
        return this;
    }

    public String getNonce() {
        return nonce;
    }

    public TokenRequest setNonce(String nonce) {
        this.nonce = nonce;
        return this;
    }

    public Long getTs() {
        return ts;
    }

    public TokenRequest setTs(Long ts) {
        this.ts = ts;
        return this;
    }

    public String getSign() {
        return sign;
    }

    public TokenRequest setSign(String sign) {
        this.sign = sign;
        return this;
    }
}
