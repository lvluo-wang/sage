package me.icymint.sage.base.data.provider;

import me.icymint.sage.base.spec.entity.BaseLogEntity;

/**
 * Created by daniel on 16/9/5.
 */
public abstract class BaseLogSqlProvider<T extends BaseLogEntity> {

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

    protected SQL onFind(SQL sql) {
        return sql;
    }

    public final String findOne() {
        SQL sql = new SQL()
                .SELECT("*")
                .FROM(getEntityTable())
                .WHERE("ID=#{id}");
        onFind(sql);
        return sql.toString();
    }

    public final String findByOwnerId() {
        SQL sql = new SQL()
                .SELECT("*")
                .FROM(getEntityTable())
                .WHERE("OWNER_ID=#{ownerId}");
        onFind(sql);
        return sql.toString();
    }


    public final String findAll() {
        SQL sql = new SQL()
                .SELECT("*")
                .FROM(getEntityTable());
        onFind(sql);
        return sql.toString();
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
