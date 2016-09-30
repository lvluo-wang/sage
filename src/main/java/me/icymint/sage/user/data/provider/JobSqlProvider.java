package me.icymint.sage.user.data.provider;

import me.icymint.sage.base.data.provider.BaseEntitySqlProvider;
import me.icymint.sage.base.data.provider.SQL;
import me.icymint.sage.base.spec.def.Bool;
import me.icymint.sage.user.spec.entity.Job;

/**
 * Created by daniel on 2016/10/1.
 */
public class JobSqlProvider extends BaseEntitySqlProvider<Job> {
    @Override
    protected String getEntityTable() {
        return "T_JOB";
    }

    @Override
    protected SQL onSave2(Job job, SQL sql) {
        return sql
                .VALUES_IF("RUNNER_ID", "#{runnerId}", job.getRunnerId() != null)
                .VALUES_IF("IS_ACTIVE", "#{isActive}", job.getIsActive() != null)
                .VALUES_IF("EXPIRE_TIME", "#{expireTime}", job.getExpireTime() != null);
    }

    public final String lockJob() {
        return new SQL()
                .UPDATE(getEntityTable())
                .SET("RUNNER_ID=#{runnerId}")
                .SET("EXPIRE_TIME=#{expireTime}")
                .SET("UPDATE_TIME=CURRENT_TIMESTAMP")
                .WHERE("IS_ACTIVE='" + Bool.Y + "'")
                .WHERE("ID=#{id}")
                .WHERE("RUNNER_ID IS NULL OR EXPIRE_TIME<CURRENT_TIMESTAMP OR RUNNER_ID=#{runnerId}")
                .toString();
    }

    @Override
    protected SQL onFind2(SQL sql) {
        return sql;
    }

    @Override
    protected SQL onUpdate2(Job job, SQL sql) {
        return sql
                .SET_IF("RUNNER_ID=#{runnerId}", job.getRunnerId() != null)
                .SET_IF("IS_ACTIVE=#{isActive}", job.getIsActive() != null)
                .SET_IF("EXPIRE_TIME=#{expireTime}", job.getExpireTime() != null);
    }
}
