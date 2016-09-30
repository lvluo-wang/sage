package me.icymint.sage.base.data.provider;

import me.icymint.sage.base.spec.def.Bool;
import me.icymint.sage.base.spec.entity.BaseEntity;

/**
 * Created by daniel on 16/9/5.
 */
public abstract class BaseEntitySqlProvider<T extends BaseEntity> extends BaseLogEntitySqlProvider<T> {

    protected final SQL onSave(T t, SQL sql) {
        onSave2(t, sql.VALUES_IF("UPDATE_TIME", "#{updateTime}", t.getUpdateTime() != null)
                .VALUES_IF("IS_DELETED", "#{isDeleted}", t.getIsDeleted() != null));
        return sql;
    }

    protected abstract SQL onSave2(T t, SQL sql);

    public final String findOneForUpdate() {
        return findOne() + " FOR UPDATE";
    }

    @Override
    protected final SQL onFind(SQL sql) {
        onFind2(sql.WHERE("IS_DELETED='" + Bool.N + "'"));
        return sql;
    }

    protected abstract SQL onFind2(SQL sql);

    protected final SQL deleteBy() {
        return new SQL()
                .UPDATE(getEntityTable())
                .SET("IS_DELETED='" + Bool.Y + "'")
                .SET("UPDATE_TIME=CURRENT_TIMESTAMP");
    }

    public final String update(T t) {
        return onUpdate(t, new SQL()
                .UPDATE(getEntityTable()))
                .WHERE("ID=#{id}")
                .toString();
    }

    protected final SQL onUpdate(T t, SQL sql) {
        return onUpdate2(t, sql.SET_IF("UPDATE_TIME=#{updateTime}",
                t.getUpdateTime() != null,
                "UPDATE_TIME=CURRENT_TIMESTAMP"));
    }

    protected abstract SQL onUpdate2(T t, SQL sql);
}
