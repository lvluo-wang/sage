package me.icymint.sage.user.data.mapper;

import me.icymint.sage.user.data.provider.EventSqlProvider;
import me.icymint.sage.user.spec.entity.Event;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by daniel on 2016/9/23.
 */
@Mapper
public interface EventMapper {

    @InsertProvider(type = EventSqlProvider.class, method = "create")
    @SelectKey(statement = "SELECT NEXTVAL('SEQ_EVENT_ID')", keyProperty = "id", before = true, resultType = Long.class)
    int create(Event dbEvent);

    @SelectProvider(type = EventSqlProvider.class, method = "findAllWithNewCreated")
    List<Event> findAllWithNewCreated(RowBounds rowBounds);

    @UpdateProvider(type = EventSqlProvider.class, method = "update")
    int update(Event event);

    @SelectProvider(type = EventSqlProvider.class, method = "findOneForUpdate")
    Event findOneForUpdate(Long id);

    @SelectProvider(type = EventSqlProvider.class, method = "findByEventId")
    Event findByEventId(@Param("eventId") String eventId);
}
