package me.icymint.sage;

import me.icymint.sage.base.spec.entity.BaseEvent;

/**
 * Created by daniel on 2016/9/23.
 */
public class TestEvent extends BaseEvent<TestEvent> {
    private String message;

    public String getMessage() {
        return message;
    }

    public TestEvent setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    protected TestEvent getSelf() {
        return this;
    }

    @Override
    public String toString() {
        return "TestEvent{" +
                "message='" + message + '\'' +
                "} " + super.toString();
    }
}
