package me.icymint.sage.base.data.provider;

import me.icymint.sage.base.spec.entity.BaseLogEntity;

/**
 * Created by daniel on 16/9/5.
 */
public abstract class BaseLogEntitySqlProvider<T extends BaseLogEntity> {

    protected abstract String getEntityTable();

    public final String save(T t) {
        SQL sql = new SQL()
                .INSERT_INTO(getEntityTable())
                .VALUES("ID", "#{id}")
                .VALUES("OWNER_ID", "#{ownerId}")
                .VALUES_IF("CREATE_TIME", "#{createTime}", t.getCreateTime() != null);
        onSave(t, sql);
        return sql.toString();
    }

    protected abstract SQL onSave(T t, SQL sql);

    protected abstract SQL onFind(SQL sql);

    protected final SQL selectFrom() {
        return onFind(new SQL()
                .SELECT("*")
                .FROM(getEntityTable()));
    }

    public final String findOne() {
        return selectFrom()
                .WHERE("ID=#{id}")
                .toString();

    }

    public final String findByOwnerId() {
        return selectFrom()
                .WHERE("OWNER_ID=#{ownerId}")
                .toString();
    }


    public final String findAll() {
        return selectFrom().toString();
    }


    protected SQL deleteBy() {
        return new SQL()
                .DELETE_FROM(getEntityTable());
    }

    public final String delete() {
        return deleteBy()
                .WHERE("ID=#{id}")
                .toString();
    }

    public String exists() {
        return new SQL()
                .SELECT("COUNT(1)")
                .FROM(getEntityTable())
                .WHERE("ID=#{id}")
                .toString();
    }
}
