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
                .VALUES_IF("DESCRIPTION", "#{description}", identity.getDescription() != null)
                .VALUES_IF("CREATE_ID", "#{createId}", identity.getCreateId() != null, "#{ownerId}")
                .VALUES_IF("TYPE", "#{type}", identity.getType() != null, "'" + IdentityType.MEMBER + "'")
                .VALUES_IF("EXTENSION", "#{extension}", identity.getExtension() != null)
                .VALUES_IF("IS_BLOCKED", "#{isBlocked}", identity.getIsBlocked() != null);
    }

    @Override
    protected SQL onFind2(SQL sql) {
        return sql;
    }

    @Override
    protected SQL onUpdate(Identity identity, SQL sql) {
        return sql.SET_IF("IS_BLOCKED=#{isBlocked}", identity.getIsBlocked() != null)
                .SET_IF("EXTENSION=#{extension}", identity.getExtension() != null)
                .SET_IF("DESCRIPTION=#{description}", identity.getDescription() != null)
                .SET_IF("SALT=#{salt}", identity.getSalt() != null)
                .SET_IF("PASSWORD=#{password}", identity.getPassword() != null);
    }

    public String findByUsernameForUpdate() {
        return selectAllFrom()
                .WHERE("USERNAME=#{username}")
                .toString() + " FOR UPDATE";
    }

    public String findGroupIds() {
        return selectFrom("ID")
                .WHERE("TYPE='" + IdentityType.GROUP + "'")
                .toString();
    }
}
