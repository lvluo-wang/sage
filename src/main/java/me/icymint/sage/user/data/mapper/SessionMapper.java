package me.icymint.sage.user.data.mapper;

import me.icymint.sage.user.data.provider.SessionSqlProvider;
import me.icymint.sage.user.spec.entity.Session;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

/**
 * Created by daniel on 16/9/6.
 */
@Mapper
public interface SessionMapper {

    @InsertProvider(type = SessionSqlProvider.class, method = "save")
    @SelectKey(statement = "SELECT NEXTVAL('SEQ_SESSION_ID')", keyProperty = "id", before = true, resultType = Long.class)
    int save(Session session);


    @UpdateProvider(type = SessionSqlProvider.class, method = "update")
    int update(Session session);

    @SelectProvider(type = SessionSqlProvider.class, method = "findOne")
    Session findOne(Long sessionId);

    @SelectProvider(type = SessionSqlProvider.class, method = "findBySessionIdForUpdate")
    Session findBySessionIdForUpdate(@Param("sessionId") String sessionId);

    @SelectProvider(type = SessionSqlProvider.class, method = "findBySessionId")
    Session findBySessionId(@Param("sessionId") String sessionId);

    @SelectProvider(type = SessionSqlProvider.class, method = "existsSessionId")
    boolean existsSessionId(@Param("sessionId") String sessionId);
}
