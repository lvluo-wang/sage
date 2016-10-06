package me.icymint.sage.user.data.mapper;

import me.icymint.sage.base.spec.entity.Pageable;
import me.icymint.sage.user.data.provider.GrantSqlProvider;
import me.icymint.sage.user.spec.entity.Grant;
import me.icymint.sage.user.spec.entity.Identity;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * Created by daniel on 2016/10/4.
 */
@Mapper
public interface GrantMapper {

    @InsertProvider(type = GrantSqlProvider.class, method = "create")
    @SelectKey(statement = "SELECT NEXTVAL('SEQ_GRANT_ID')", keyProperty = "id", before = true, resultType = Long.class)
    int create(Grant grant);

    @SelectProvider(type = GrantSqlProvider.class, method = "findGroupIdsByOwnerId")
    List<Long> findGroupIdsByOwnerId(@Param("ownerId") Long ownerId);

    @DeleteProvider(type = GrantSqlProvider.class, method = "deleteByOwnerIdAndGroupId")
    void deleteByOwnerIdAndGroupId(@Param("ownerId") Long ownerId, @Param("groupId") Long groupId);

    @SelectProvider(type = GrantSqlProvider.class, method = "findGroupsByOwnerIdPageable")
    List<Identity> findGroupsByOwnerIdPageable(@Param("ownerId") Long ownerId, Pageable pageable);
}
