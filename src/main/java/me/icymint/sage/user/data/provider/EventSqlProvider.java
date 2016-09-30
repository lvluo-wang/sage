package me.icymint.sage.user.data.provider;

import me.icymint.sage.base.data.provider.BaseJobEntitySqlProvider;
import me.icymint.sage.base.data.provider.SQL;
import me.icymint.sage.user.spec.def.EventStatus;
import me.icymint.sage.user.spec.entity.Event;

/**
 * Created by daniel on 2016/9/23.
 */
public class EventSqlProvider extends BaseJobEntitySqlProvider<Event> {
    public String getEntityTable() {
        return "T_EVENT";
    }

    @Override
    protected final SQL onFind2(SQL sql) {
        return sql;
    }

    public String findAllWithNewCreated() {
        return selectFrom()
                .WHERE("STATUS='" + EventStatus.CREATED + "'")
                .WHERE("NEXT_SCAN_TIME<=CURRENT_TIMESTAMP")
                .toString();
    }

    public String findByEventId() {
        return selectFrom()
                .WHERE("EVENT_ID=#{eventId}")
                .toString();
    }

    @Override
    protected final SQL onSave3(Event event, SQL sql) {
        return sql
                .VALUES_IF("STATUS", "#{status}", event.getStatus() != null)
                .VALUES("EVENT_ID", "#{eventId}")
                .VALUES_IF("PARENT_EVENT_ID", "#{parentEventId}", event.getParentEventId() != null)
                .VALUES_IF("BODY", "#{body}", event.getBody() != null)
                .VALUES("ASYNC_EVENT_TYPE", "#{asyncEventType}");
    }

    @Override
    protected final SQL onUpdate2(Event event, SQL sql) {
        return sql
                .SET_IF("STATUS=#{status}", event.getStatus() != null);
    }
}
