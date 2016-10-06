package me.icymint.sage.user.data.provider;

import me.icymint.sage.base.data.provider.BaseLogEntitySqlProvider;
import me.icymint.sage.base.data.provider.SQL;
import me.icymint.sage.user.spec.entity.Grant;

/**
 * Created by daniel on 2016/10/4.
 */
public class GrantSqlProvider extends BaseLogEntitySqlProvider<Grant> {
    @Override
    protected String getEntityTable() {
        return "T_GRANT";
    }

    @Override
    protected SQL onCreate(Grant grant, SQL sql) {
        return sql
                .VALUES("GROUP_ID", "#{groupId}");
    }

    @Override
    protected SQL onWhere(SQL sql) {
        return sql;
    }

    public String findGroupIdsByOwnerId() {
        return selectFrom("GROUP_ID")
                .WHERE("OWNER_ID=#{ownerId}")
                .toString();
    }

    public String deleteByOwnerIdAndGroupId() {
        return new SQL()
                .DELETE_FROM(getEntityTable())
                .WHERE("OWNER_ID=#{ownerId}")
                .WHERE("GROUP_ID=#{groupId}")
                .toString();
    }
}
