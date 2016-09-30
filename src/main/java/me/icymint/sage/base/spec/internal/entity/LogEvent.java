package me.icymint.sage.base.spec.internal.entity;

import me.icymint.sage.base.spec.entity.BaseLogEvent;

import java.util.Map;

/**
 * Created by daniel on 2016/9/30.
 */
public class LogEvent extends BaseLogEvent<LogEvent> {
    private Map<String, Object> logMap;

    public Map<String, Object> getLogMap() {
        return logMap;
    }

    public LogEvent setLogMap(Map<String, Object> logMap) {
        this.logMap = logMap;
        return this;
    }

    @Override
    public String toString() {
        return "LogEvent{" +
                "logMap=" + logMap +
                "} " + super.toString();
    }

    @Override
    protected LogEvent getSelf() {
        return this;
    }
}
