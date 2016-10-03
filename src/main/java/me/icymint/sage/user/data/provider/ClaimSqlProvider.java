package me.icymint.sage.user.data.provider;

import me.icymint.sage.base.data.provider.BaseEntitySqlProvider;
import me.icymint.sage.base.data.provider.SQL;
import me.icymint.sage.user.spec.def.ClaimType;
import me.icymint.sage.user.spec.entity.Claim;

/**
 * Created by daniel on 16/9/5.
 */
public class ClaimSqlProvider extends BaseEntitySqlProvider<Claim> {
    @Override
    protected String getEntityTable() {
        return "T_CLAIM";
    }

    @Override
    protected SQL onCreate2(Claim claim, SQL sql) {
        return sql.VALUES("TYPE", "#{type}")
                .VALUES("VALUE", "#{value}")
                .VALUES("PRIMARY_KEY", "#{primaryKey}")
                .VALUES_IF("IS_VERIFIED", "#{isVerified}", claim.getIsVerified() != null);
    }

    @Override
    protected SQL onFind2(SQL sql) {
        return sql;
    }

    @Override
    protected SQL onUpdate(Claim claim, SQL sql) {
        return sql
                .SET_IF("IS_VERIFIED=#{isVerified}", claim.getIsVerified() != null);
    }

    public String findByTypeAndValue() {
        return selectAllFrom()
                .WHERE("TYPE=#{type}")
                .WHERE("VALUE=#{value}")
                .toString();
    }

    public String findRolesByOwnerId() {
        return selectFrom("VALUE")
                .WHERE("OWNER_ID=#{ownerId}")
                .WHERE("TYPE='" + ClaimType.ROLE + "'")
                .toString();
    }
}
