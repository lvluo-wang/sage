package me.icymint.sage.base.spec.internal.entity;

import me.icymint.sage.base.spec.entity.BaseLogEvent;

import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 2016/9/30.
 */
public class LogEvent extends BaseLogEvent<LogEvent> {
    private Object result;
    private String method;
    private List<Argument> arguments;
    private Map<String, Object> additionalInformation;

    public Object getResult() {
        return result;
    }

    public LogEvent setResult(Object result) {
        this.result = result;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public LogEvent setMethod(String method) {
        this.method = method;
        return this;
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    public LogEvent setArguments(List<Argument> arguments) {
        this.arguments = arguments;
        return this;
    }

    public Map<String, Object> getAdditionalInformation() {
        return additionalInformation;
    }

    public LogEvent setAdditionalInformation(Map<String, Object> additionalInformation) {
        this.additionalInformation = additionalInformation;
        return this;
    }

    @Override
    public String toString() {
        return "LogEvent{" +
                "result='" + result + '\'' +
                ", method='" + method + '\'' +
                ", arguments=" + arguments +
                ", additionalInformation=" + additionalInformation +
                "} " + super.toString();
    }

    @Override
    protected LogEvent getSelf() {
        return this;
    }

    public static class Argument {
        private String className;
        private String argumentName;
        private Object argument;

        public String getClassName() {
            return className;
        }

        public Argument setClassName(String className) {
            this.className = className;
            return this;
        }

        public String getArgumentName() {
            return argumentName;
        }

        public Argument setArgumentName(String argumentName) {
            this.argumentName = argumentName;
            return this;
        }

        public Object getArgument() {
            return argument;
        }

        public Argument setArgument(Object argument) {
            this.argument = argument;
            return this;
        }
    }
}
