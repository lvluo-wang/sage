package me.icymint.sage.user.data.provider;

import me.icymint.sage.base.data.provider.BaseSqlProvider;
import me.icymint.sage.base.data.provider.SQL;
import me.icymint.sage.user.spec.entity.Session;

/**
 * Created by daniel on 16/9/6.
 */
public class SessionSqlProvider extends BaseSqlProvider<Session> {
    @Override
    protected String getEntityTable() {
        return "T_SESSION";
    }

    @Override
    protected SQL onSave2(Session session, SQL sql) {
        return sql
                .VALUES("SESSION_ID", "#{sessionId}")
                .VALUES("EXPIRE_TIME", "#{expireTime}")
                .VALUES_IF("CLIENT_ID", "#{clientId}", session.getClientId() != null)
                .VALUES_IF("IP", "#{ip}", session.getIp() != null)
                .VALUES_IF("TIME_ZONE", "#{timeZone}", session.getTimeZone() != null);
    }

    @Override
    protected SQL onFind2(SQL sql) {
        return sql;
    }

    @Override
    protected SQL onUpdate(Session session, SQL sql) {
        return sql
                .SET_IF("CLIENT_ID=#{clientId}", session.getClientId() != null)
                .SET_IF("IP=#{ip}", session.getIp() != null)
                .SET_IF("TIME_ZONE=#{timeZone}", session.getTimeZone() != null);
    }

    public String findBySessionIdForUpdate() {
        return findBySessionId() + " FOR UPDATE";
    }

    public String findBySessionId() {
        return new SQL()
                .SELECT("*")
                .FROM(getEntityTable())
                .WHERE("SESSION_ID=#{sessionId}")
                .toString();
    }

    public String existsSessionId() {
        return new SQL()
                .SELECT("COUNT(1)")
                .FROM(getEntityTable())
                .WHERE("SESSION_ID=#{sessionId}")
                .toString();
    }
}
