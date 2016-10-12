package me.icymint.sage.user.data.provider;

import me.icymint.sage.base.data.provider.BaseLogEntitySqlProvider;
import me.icymint.sage.base.data.provider.SQL;
import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.user.spec.def.ClaimType;
import me.icymint.sage.user.spec.entity.Claim;

/**
 * Created by daniel on 16/9/5.
 */
public class ClaimSqlProvider extends BaseLogEntitySqlProvider<Claim> {
    @Override
    protected String getEntityTable() {
        return "T_CLAIM";
    }

    @Override
    protected SQL onCreate(Claim claim, SQL sql) {
        return sql.VALUES("TYPE", "#{type}")
                .VALUES("VALUE", "#{value}")
                .VALUES("PRIMARY_KEY", "#{primaryKey}")
                .VALUES_IF("IS_VERIFIED", "#{isVerified}", claim.getIsVerified() != null);
    }

    @Override
    protected SQL onWhere(SQL sql) {
        return sql;
    }


    public String findByTypeAndValue() {
        return selectAllFrom()
                .WHERE("TYPE=#{type}")
                .WHERE("VALUE=#{value}")
                .toString();
    }

    public String findOneByOwnerIdAndTypeAndValue() {
        return selectAllFrom()
                .WHERE("TYPE=#{type}")
                .WHERE("VALUE=#{value}")
                .WHERE("OWNER_ID=#{ownerId}")
                .toString();
    }

    public String findUniqueByOwnerId() {
        return selectAllFrom()
                .WHERE("OWNER_ID=#{ownerId}")
                .WHERE("PRIMARY_KEY='" + Magics.CLAIM_GLOBAL_UNIQUE + "'")
                .toString();
    }

    public String findRolesByOwnerId() {
        return selectFrom("VALUE")
                .WHERE("OWNER_ID=#{ownerId}")
                .WHERE("TYPE='" + ClaimType.ROLE + "'")
                .toString();
    }

    public String findPrivilegesByOwnerId() {
        return selectFrom("VALUE")
                .WHERE("OWNER_ID=#{ownerId}")
                .WHERE("TYPE='" + ClaimType.PRIVILEGE + "'")
                .toString();
    }
}
