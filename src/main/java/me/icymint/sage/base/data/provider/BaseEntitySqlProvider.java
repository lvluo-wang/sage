package me.icymint.sage.base.data.provider;

import me.icymint.sage.base.spec.def.Bool;
import me.icymint.sage.base.spec.entity.BaseEntity;

/**
 * Created by daniel on 16/9/5.
 */
public abstract class BaseEntitySqlProvider<T extends BaseEntity> extends BaseLogEntitySqlProvider<T> {

    protected final SQL onCreate(T t, SQL sql) {
        sql = sql
                .VALUES_IF("UPDATE_TIME", "#{updateTime}", t.getUpdateTime() != null)
                .VALUES_IF("IS_DELETED", "#{isDeleted}", t.getIsDeleted() != null);
        onCreate2(t, sql);
        return sql;
    }

    protected abstract SQL onCreate2(T t, SQL sql);

    @Override
    protected final SQL onFind(SQL sql) {
        sql = sql.WHERE("IS_DELETED='" + Bool.N + "'");
        onFind2(sql);
        return sql;
    }

    protected abstract SQL onFind2(SQL sql);

    protected final SQL deleteOne() {
        return new SQL()
                .UPDATE(getEntityTable())
                .SET("IS_DELETED='" + Bool.Y + "'")
                .SET("UPDATE_TIME=CURRENT_TIMESTAMP")
                .WHERE("IS_DELETED='" + Bool.N + "'");
    }

    public final String update(T t) {
        return updateFrom(t)
                .WHERE("ID=#{id}")
                .toString();
    }

    protected final SQL updateFrom(T t) {
        SQL sql = new SQL()
                .UPDATE(getEntityTable())
                .SET_IF("UPDATE_TIME=#{updateTime}",
                        t.getUpdateTime() != null,
                        "UPDATE_TIME=CURRENT_TIMESTAMP")
                .SET_IF("IS_DELETED=#{isDeleted}", t.getIsDeleted() == Bool.Y);
        onUpdate(t, sql);
        sql.WHERE("IS_DELETED='" + Bool.N + "'");
        return sql;
    }

    protected abstract SQL onUpdate(T t, SQL sql);
}
