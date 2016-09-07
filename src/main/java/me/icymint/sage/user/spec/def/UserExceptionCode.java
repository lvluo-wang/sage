package me.icymint.sage.user.spec.def;

import me.icymint.sage.base.spec.annotation.I18nEnum;

/**
 * Created by daniel on 16/9/4.
 */
@I18nEnum("user.error")
public enum UserExceptionCode {
    CLIENT_ID__ILLEGAL,
    TOKEN__NOT_FOUND,
    ACCESS_SPAN_NOT_VALID,
    ACCESS_TOKEN_ILLEGAL,
    IDENTITY_NULL,
    IDENTITY__IS_BLOCKED,
    IDENTITY__ILLEGAL,
    USERNAME__EXISTS,
    PASSWORD_IS_NULL,
    CLAIM_CREATE_FAILED,
    SALT_IS_NULL,
    IDENTITY_CREATE_FAILED,
    USERNAME__ILLEGAL,
    IDENTITY__NOT_FOUND,
    CLAIM_TYPE_NULL,
    USE_MULTI_CLAIM_QUERY_API_INSTEAD,
    MISSING_REQUEST_HEADER_;
}
