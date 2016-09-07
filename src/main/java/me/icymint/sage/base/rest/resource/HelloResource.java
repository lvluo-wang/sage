package me.icymint.sage.base.rest.resource;

import me.icymint.sage.base.spec.annotation.ToLabel;
import me.icymint.sage.base.spec.def.Bool;

import java.time.Instant;
import java.util.List;

/**
 * Created by daniel on 16/9/2.
 */
public class HelloResource {
    private String greet;
    @ToLabel
    private Bool isFriend;
    @ToLabel(expend = false)
    private Bool isDescription;
    private Instant now;
    private List<String> list;


    public List<String> getList() {
        return list;
    }

    public HelloResource setList(List<String> list) {
        this.list = list;
        return this;
    }

    public Instant getNow() {
        return now;
    }

    public HelloResource setNow(Instant now) {
        this.now = now;
        return this;
    }

    public String getGreet() {
        return greet;
    }

    public HelloResource setGreet(String greet) {
        this.greet = greet;
        return this;
    }

    public Bool getIsFriend() {
        return isFriend;
    }

    public HelloResource setIsFriend(Bool isFriend) {
        this.isFriend = isFriend;
        return this;
    }

    public Bool getIsDescription() {
        return isDescription;
    }

    public HelloResource setIsDescription(Bool isDescription) {
        this.isDescription = isDescription;
        return this;
    }
}
