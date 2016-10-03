package me.icymint.sage.user.data.mapper;

import me.icymint.sage.user.data.provider.TokenSqlProvider;
import me.icymint.sage.user.spec.entity.Token;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * Created by daniel on 16/9/4.
 */
@Mapper
public interface TokenMapper {

    @InsertProvider(type = TokenSqlProvider.class, method = "create")
    @SelectKey(statement = "SELECT NEXTVAL('SEQ_TOKEN_ID')", keyProperty = "id", before = true, resultType = Long.class)
    int create(Token token);

    @SelectProvider(type = TokenSqlProvider.class, method = "findOne")
    Token findOne(@Param("id") Long tokenId);

    @DeleteProvider(type = TokenSqlProvider.class, method = "delete")
    void delete(@Param("id") Long tokenId);

    @SelectProvider(type = TokenSqlProvider.class, method = "findBySessionIdAndClientId")
    List<Long> findBySessionIdAndClientId(@Param("sessionId") String sessionId, @Param("clientId") Long clientId);
}
