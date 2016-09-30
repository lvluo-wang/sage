package me.icymint.sage.user.spec.def;

import me.icymint.sage.base.spec.annotation.I18nEnum;

/**
 * Created by daniel on 16/9/5.
 */
@I18nEnum("claim.type")
public enum ClaimType {
    ROLE(false),
    USERNAME,
    EMAIL,
    OPENID_QQ,
    OPENID_WE_CHAT,
    OPENID_WEIBO,
    OPENID_DOUBAN,;

    private final boolean globalUnique;

    ClaimType() {
        this(true);
    }

    ClaimType(boolean isGlobalUnique) {
        this.globalUnique = isGlobalUnique;
    }

    public boolean isGlobalUnique() {
        return globalUnique;
    }
}
