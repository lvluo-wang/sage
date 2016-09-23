package me.icymint.sage.user.data.mapper;

import me.icymint.sage.user.data.provider.TokenSqlProvider;
import me.icymint.sage.user.spec.entity.Token;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;

/**
 * Created by daniel on 16/9/4.
 */
@Mapper
public interface TokenMapper {

    @InsertProvider(type = TokenSqlProvider.class, method = "save")
    @SelectKey(statement = "SELECT NEXTVAL('SEQ_TOKEN_ID')", keyProperty = "id", before = true, resultType = Long.class)
    int save(Token token);

    @SelectProvider(type = TokenSqlProvider.class, method = "findOne")
    Token findOne(@Param("id") Long tokenId);

    @DeleteProvider(type = TokenSqlProvider.class, method = "delete")
    void delete(Long tokenId);

    @DeleteProvider(type = TokenSqlProvider.class, method = "deleteBySessionId")
    void deleteBySessionId(String sessionId);
}
