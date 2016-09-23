package me.icymint.sage.user.data.mapper;

import me.icymint.sage.base.data.provider.SQL;
import me.icymint.sage.user.spec.def.EventStatus;
import me.icymint.sage.user.spec.entity.Event;

/**
 * Created by daniel on 2016/9/23.
 */
public class EventSqlProvider {
    public String getEntityTable() {
        return "T_EVENT";
    }

    public final String save(Event t) {
        SQL sql = new SQL()
                .INSERT_INTO(getEntityTable())
                .VALUES("ID", "#{id}")
                .VALUES("EVENT_ID", "#{eventId}")
                .VALUES("ASYNC_EVENT_TYPE", "#{asyncEventType}")
                .VALUES_IF("PARENT_EVENT_ID", "#{parentEventId}", t.getParentEventId() != null)
                .VALUES_IF("BODY", "#{body}", t.getBody() != null)
                .VALUES_IF("STATUS", "#{status}", t.getStatus() != null)
                .VALUES_IF("CREATE_TIME", "#{createTime}", t.getCreateTime() != null)
                .VALUES_IF("NEXT_SCAN_TIME", "#{nextScanTime}", t.getNextScanTime() != null)
                .VALUES_IF("UPDATE_TIME", "#{updateTime}", t.getUpdateTime() != null);
        return sql.toString();
    }

    public String update(Event event) {
        return new SQL()
                .UPDATE(getEntityTable())
                .SET("UPDATE_TIME=CURRENT_TIMESTAMP")
                .SET_IF("NEXT_SCAN_TIME=#{nextScanTime}", event.getNextScanTime() != null)
                .SET_IF("STATUS=#{status}", event.getStatus() != null)
                .WHERE("ID=#{id}")
                .toString();
    }

    public String findAll() {
        return new SQL()
                .SELECT("*")
                .FROM(getEntityTable())
                .toString();
    }

    public String findAllWithNewCreated() {
        return new SQL()
                .SELECT("*")
                .FROM(getEntityTable())
                .WHERE("STATUS='" + EventStatus.CREATED + "'")
                .WHERE("NEXT_SCAN_TIME<=CURRENT_TIMESTAMP")
                .toString();
    }

    public String findOne() {
        return new SQL()
                .SELECT("*")
                .FROM(getEntityTable())
                .WHERE("ID=#{id}")
                .toString();
    }

    public String findByEventId() {
        return new SQL()
                .SELECT("*")
                .FROM(getEntityTable())
                .WHERE("EVENT_ID=#{eventId}")
                .toString();
    }


    public String findOneForUpdate() {
        return findOne() + " FOR UPDATE";
    }
}
