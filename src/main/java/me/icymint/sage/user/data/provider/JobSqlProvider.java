package me.icymint.sage.user.data.provider;

import me.icymint.sage.base.data.provider.BaseJobEntitySqlProvider;
import me.icymint.sage.base.data.provider.SQL;
import me.icymint.sage.base.spec.def.Bool;
import me.icymint.sage.base.spec.internal.entity.Job;

/**
 * Created by daniel on 2016/10/1.
 */
public class JobSqlProvider extends BaseJobEntitySqlProvider<Job> {
    @Override
    protected String getEntityTable() {
        return "T_JOB";
    }

    @Override
    protected SQL onCreate3(Job job, SQL sql) {
        return sql
                .VALUES_IF("INSTANCE_ID", "#{instanceId}", job.getInstanceId() != null)
                .VALUES_IF("IS_ACTIVE", "#{isActive}", job.getIsActive() != null)
                .VALUES_IF("EXPIRE_TIME", "#{expireTime}", job.getExpireTime() != null);
    }

    public final String lockJob() {
        return new SQL()
                .UPDATE(getEntityTable())
                .SET("INSTANCE_ID=#{instanceId}")
                .SET("EXPIRE_TIME=#{expireTime}")
                .SET("UPDATE_TIME=CURRENT_TIMESTAMP")
                .WHERE("IS_ACTIVE='" + Bool.Y + "'")
                .WHERE("ID=#{id}")
                .WHERE("IS_DELETED='" + Bool.N + "'")
                .WHERE("INSTANCE_ID IS NULL OR EXPIRE_TIME<CURRENT_TIMESTAMP OR INSTANCE_ID=#{instanceId}")
                .toString();
    }

    @Override
    protected SQL onWhere2(SQL sql) {
        return sql;
    }

    @Override
    protected SQL onUpdate2(Job job, SQL sql) {
        return sql
                .SET_IF("INSTANCE_ID=#{instanceId}", job.getInstanceId() != null)
                .SET_IF("IS_ACTIVE=#{isActive}", job.getIsActive() != null)
                .SET_IF("EXPIRE_TIME=#{expireTime}", job.getExpireTime() != null);
    }
}
