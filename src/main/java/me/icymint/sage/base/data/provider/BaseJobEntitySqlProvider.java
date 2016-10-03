package me.icymint.sage.base.data.provider;

import me.icymint.sage.base.spec.entity.BaseJobEntity;

/**
 * Created by daniel on 2016/9/24.
 */
public abstract class BaseJobEntitySqlProvider<T extends BaseJobEntity<T>> extends BaseEntitySqlProvider<T> {

    @Override
    protected final SQL onCreate2(T t, SQL sql) {
        if (t.getOwnerId() == null) {
            t.setOwnerId(0L);
        }
        sql.VALUES_IF("NEXT_SCAN_TIME", "#{nextScanTime}", t.getNextScanTime() != null);
        onCreate3(t, sql);
        return sql;
    }

    protected abstract SQL onCreate3(T t, SQL sql);

    @Override
    protected final SQL onUpdate(T t, SQL sql) {
        sql.SET_IF("NEXT_SCAN_TIME=#{nextScanTime}", t.getNextScanTime() != null);
        onUpdate2(t, sql);
        return sql;
    }

    protected abstract SQL onUpdate2(T t, SQL sql);
}
