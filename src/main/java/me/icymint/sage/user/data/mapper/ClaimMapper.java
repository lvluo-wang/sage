package me.icymint.sage.user.data.mapper;

import me.icymint.sage.user.data.provider.ClaimSqlProvider;
import me.icymint.sage.user.spec.def.ClaimType;
import me.icymint.sage.user.spec.def.RoleType;
import me.icymint.sage.user.spec.entity.Claim;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * Created by daniel on 16/9/5.
 */
@Mapper
public interface ClaimMapper {

    @InsertProvider(type = ClaimSqlProvider.class, method = "save")
    @SelectKey(statement = "SELECT NEXTVAL('SEQ_CLAIM_ID')", keyProperty = "id", before = true, resultType = Long.class)
    int save(Claim claim);

    @SelectProvider(type = ClaimSqlProvider.class, method = "findOne")
    Claim findOne(@Param("id") Long id);

    @SelectProvider(type = ClaimSqlProvider.class, method = "findByTypeAndValue")
    Claim findOneByTypeAndValue(@Param("type") ClaimType type, @Param("value") String value);

    @SelectProvider(type = ClaimSqlProvider.class, method = "findByTypeAndValue")
    List<Claim> findAllByTypeAndValue(@Param("type") ClaimType type, @Param("value") String value);

    @SelectProvider(type = ClaimSqlProvider.class, method = "existRoles")
    boolean existRoles(@Param("ownerId") Long ownerId, @Param("roleTypes") RoleType[] roleTypes);
}