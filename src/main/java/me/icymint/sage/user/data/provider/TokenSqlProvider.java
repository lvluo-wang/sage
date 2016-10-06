package me.icymint.sage.user.data.provider;

import me.icymint.sage.base.data.provider.BaseEntitySqlProvider;
import me.icymint.sage.base.data.provider.SQL;
import me.icymint.sage.user.spec.entity.Token;

/**
 * Created by daniel on 16/9/4.
 */
public class TokenSqlProvider extends BaseEntitySqlProvider<Token> {
    public String getEntityTable() {
        return "T_TOKEN";
    }

    @Override
    protected SQL onCreate2(Token token, SQL sql) {
        return sql.VALUES("CLIENT_ID", "#{clientId}")
                .VALUES("ACCESS_SECRET", "#{accessSecret}")
                .VALUES("EXPIRE_TIME", "#{expireTime}")
                .VALUES_IF("TYPE", "#{type}", token.getType() != null)
                .VALUES_IF("IP", "#{ip}", token.getIp() != null)
                .VALUES_IF("TIME_ZONE", "#{timeZone}", token.getTimeZone() != null)
                .VALUES_IF("SESSION_ID", "#{sessionId}", token.getSessionId() != null);
    }

    @Override
    protected SQL onWhere2(SQL sql) {
        return sql;
    }

    @Override
    protected SQL onUpdate(Token token, SQL sql) {
        return sql;
    }

    public String findByClientId() {
        return selectFrom("ID")
                .WHERE("CLIENT_ID=#{clientId}")
                .toString();
    }
}
