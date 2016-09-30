package me.icymint.sage.base.data.provider;

import me.icymint.sage.base.spec.entity.BaseJobEntity;

/**
 * Created by daniel on 2016/9/24.
 */
public abstract class BaseJobEntitySqlProvider<T extends BaseJobEntity<T>> extends BaseEntitySqlProvider<T> {

    @Override
    protected final SQL onSave2(T t, SQL sql) {
        if (t.getOwnerId() == null) {
            t.setOwnerId(0L);
        }
        return onSave3(t, sql
                .VALUES_IF("NEXT_SCAN_TIME", "#{nextScanTime}", t.getNextScanTime() != null));
    }

    protected abstract SQL onSave3(T t, SQL sql);

    @Override
    protected final SQL onUpdate2(T t, SQL sql) {
        return onUpdate3(t, sql
                .SET_IF("NEXT_SCAN_TIME=#{nextScanTime}", t.getNextScanTime() != null));
    }

    protected abstract SQL onUpdate3(T t, SQL sql);
}
