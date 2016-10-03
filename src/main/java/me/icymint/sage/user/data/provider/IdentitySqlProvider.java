package me.icymint.sage.user.data.provider;

import me.icymint.sage.base.data.provider.BaseEntitySqlProvider;
import me.icymint.sage.base.data.provider.SQL;
import me.icymint.sage.user.spec.def.IdentityType;
import me.icymint.sage.user.spec.entity.Identity;

/**
 * Created by daniel on 16/9/3.
 */
public class IdentitySqlProvider extends BaseEntitySqlProvider<Identity> {
    public String getEntityTable() {
        return "T_IDENTITY";
    }

    @Override
    protected SQL onCreate2(Identity identity, SQL sql) {
        return sql.VALUES_IF("SALT", "#{salt}", identity.getSalt() != null)
                .VALUES_IF("PASSWORD", "#{password}", identity.getPassword() != null)
                .VALUES_IF("CREATE_ID", "#{createId}", identity.getCreateId() != null, "#{ownerId}")
                .VALUES_IF("TYPE", "#{type}", identity.getType() != null, "'" + IdentityType.USER + "'")
                .VALUES_IF("VALID_SECONDS", "#{validSeconds}", identity.getValidSeconds() != null)
                .VALUES_IF("IS_BLOCKED", "#{isBlocked}", identity.getIsBlocked() != null);
    }

    @Override
    protected SQL onFind2(SQL sql) {
        return sql;
    }

    @Override
    protected SQL onUpdate(Identity identity, SQL sql) {
        return sql.SET_IF("IS_BLOCKED=#{isBlocked}", identity.getIsBlocked() != null)
                .SET_IF("VALID_SECONDS=#{validSeconds}", identity.getValidSeconds() != null)
                .SET_IF("SALT=#{salt}", identity.getSalt() != null)
                .SET_IF("PASSWORD=#{password}", identity.getPassword() != null);
    }

    public String findByUsernameForUpdate() {
        return selectAllFrom()
                .WHERE("USERNAME=#{username}")
                .toString() + " FOR UPDATE";
    }
}
