package me.icymint.sage.user.data.mapper;

import me.icymint.sage.user.data.provider.IdentitySqlProvider;
import me.icymint.sage.user.spec.entity.Identity;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by daniel on 16/9/3.
 */
@Mapper
public interface IdentityMapper {

    @InsertProvider(type = IdentitySqlProvider.class, method = "create")
    @SelectKey(statement = "SELECT NEXTVAL('SEQ_IDENTITY_ID')", keyProperty = "id", before = true, resultType = Long.class)
    int create(Identity identity);

    @SelectProvider(type = IdentitySqlProvider.class, method = "findOne")
    Identity findOne(@Param("id") Long id);

    @SelectProvider(type = IdentitySqlProvider.class, method = "findAll")
    List<Identity> findAll(RowBounds rowBounds);

    @SelectProvider(type = IdentitySqlProvider.class, method = "findByUsernameForUpdate")
    Identity findByUsernameForUpdate(@Param("username") String username);

    @SelectProvider(type = IdentitySqlProvider.class, method = "findGroupIds")
    List<Long> findGroupIds(RowBounds rowBounds);
}
