package me.icymint.sage.user.spec.api;

import me.icymint.sage.user.spec.entity.Token;

/**
 * Created by daniel on 16/9/4.
 */
public interface TokenService {

    Token login(Long identity, Long clientId, String nonce, Long timestamp, String signature);

    Token findOne(Long tokenId);

    boolean isExpire(Long tokenId);

    void expire(Long tokenId);
}
