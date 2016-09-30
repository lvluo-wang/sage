package me.icymint.sage.user.data.mapper;

import me.icymint.sage.user.data.provider.JobSqlProvider;
import me.icymint.sage.user.spec.entity.Job;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.time.Instant;

/**
 * Created by daniel on 2016/10/1.
 */
@Mapper
public interface JobMapper {

    @InsertProvider(type = JobSqlProvider.class, method = "save")
    @SelectKey(statement = "SELECT NEXTVAL('SEQ_JOB_ID')", keyProperty = "id", before = true, resultType = Long.class)
    int save(Job job);

    @UpdateProvider(type = JobSqlProvider.class, method = "update")
    int update(Job event);

    @SelectProvider(type = JobSqlProvider.class, method = "findOneForUpdate")
    Job findOneForUpdate(Long id);

    @SelectProvider(type = JobSqlProvider.class, method = "findOne")
    Job findOne(Long id);

    @UpdateProvider(type = JobSqlProvider.class, method = "lockJob")
    int lockJob(@Param("id") Long jobId, @Param("runnerId") String runnerId, @Param("expireTime") Instant instant);
}
