package me.icymint.sage.user.rest.context;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by daniel on 16/9/4.
 */
public class TokenContext {
    @NotNull
    private Long clientId;
    @NotNull
    private Long tokenId;
    @NotNull
    @Size(min = 8, max = 32)
    @Pattern(regexp = "[0-9a-zA-Z]{8,32}")
    private String nonce;
    @NotNull
    private Long timestamp;
    @NotNull
    private String sign;

    //Inject after auth succeed
    private Long ownerId;

    public Long getOwnerId() {
        return ownerId;
    }

    public TokenContext setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public Long getClientId() {
        return clientId;
    }

    public TokenContext setClientId(Long clientId) {
        this.clientId = clientId;
        return this;
    }

    public Long getTokenId() {
        return tokenId;
    }

    public TokenContext setTokenId(Long tokenId) {
        this.tokenId = tokenId;
        return this;
    }

    public String getNonce() {
        return nonce;
    }

    public TokenContext setNonce(String nonce) {
        this.nonce = nonce;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public TokenContext setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getSign() {
        return sign;
    }

    public TokenContext setSign(String sign) {
        this.sign = sign;
        return this;
    }
}
