package me.icymint.sage.user.data.provider;

import me.icymint.sage.base.data.provider.BaseEntitySqlProvider;
import me.icymint.sage.base.data.provider.SQL;
import me.icymint.sage.user.spec.def.ClaimType;
import me.icymint.sage.user.spec.def.RoleType;
import me.icymint.sage.user.spec.entity.Claim;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * Created by daniel on 16/9/5.
 */
public class ClaimSqlProvider extends BaseEntitySqlProvider<Claim> {
    @Override
    protected String getEntityTable() {
        return "T_CLAIM";
    }

    @Override
    protected SQL onSave2(Claim claim, SQL sql) {
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
    protected SQL onUpdate2(Claim claim, SQL sql) {
        return sql
                .SET_IF("IS_VERIFIED=#{isVerified}", claim.getIsVerified() != null);
    }

    public String findByTypeAndValue() {
        return onFind(new SQL()
                .SELECT("*")
                .FROM(getEntityTable())
                .WHERE("TYPE=#{type}")
                .WHERE("VALUE=#{value}"))
                .toString();
    }

    public String existRoles(Map<String, Object> params) {
        RoleType[] types = (RoleType[]) params.get("roleTypes");
        String values = Stream.of(types)
                .map(type -> "'" + type + "'")
                .collect(joining(","));
        return "SELECT COUNT(1) FROM "
                + getEntityTable() + " WHERE OWNER_ID=#{ownerId} AND TYPE='"
                + ClaimType.ROLE + "' AND VALUE IN (" + values + ")";
    }
}
