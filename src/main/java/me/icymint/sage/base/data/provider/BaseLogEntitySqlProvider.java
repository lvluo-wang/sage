package me.icymint.sage.base.data.provider;

import me.icymint.sage.base.spec.entity.BaseLogEntity;

/**
 * Created by daniel on 16/9/5.
 */
public abstract class BaseLogEntitySqlProvider<T extends BaseLogEntity> {

    protected abstract String getEntityTable();

    public final String create(T t) {
        SQL sql = new SQL()
                .INSERT_INTO(getEntityTable())
                .VALUES("ID", "#{id}")
                .VALUES("OWNER_ID", "#{ownerId}")
                .VALUES_IF("CREATE_TIME", "#{createTime}", t.getCreateTime() != null);
        onCreate(t, sql);
        return sql.toString();
    }

    protected abstract SQL onCreate(T t, SQL sql);

    protected abstract SQL onWhere(SQL sql);

    protected final SQL selectAllFrom() {
        return selectFrom("*");
    }

    protected final SQL selectFrom(String items) {
        SQL sql = new SQL()
                .SELECT(items)
                .FROM(getEntityTable());
        onWhere(sql);
        return sql;
    }

    public final String findOneForUpdate() {
        return findOne() + " FOR UPDATE";
    }

    public final String findOne() {
        return selectAllFrom()
                .WHERE("ID=#{id}")
                .toString();
    }

    public final String findOneByOwnerId() {
        return selectAllFrom()
                .WHERE("ID=#{id}")
                .WHERE("OWNER_ID=#{ownerId}")
                .toString();
    }

    public final String findByOwnerId() {
        return selectAllFrom()
                .WHERE("OWNER_ID=#{ownerId}")
                .toString();
    }

    public final String findAll() {
        return selectAllFrom().toString();
    }

    protected SQL deleteOne() {
        return new SQL()
                .DELETE_FROM(getEntityTable());
    }

    public final String delete() {
        return deleteOne()
                .WHERE("ID=#{id}")
                .toString();
    }

    public final String exists() {
        return selectFrom("COUNT(1)")
                .WHERE("ID=#{id}")
                .toString();
    }
}
