package me.icymint.sage.user.rest.resource;

import java.time.Instant;

/**
 * Created by daniel on 2016/10/4.
 */
public class GroupResource {
    private Long id;
    private String name;
    private Instant createTime;

    public Long getId() {
        return id;
    }

    public GroupResource setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public GroupResource setName(String name) {
        this.name = name;
        return this;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public GroupResource setCreateTime(Instant createTime) {
        this.createTime = createTime;
        return this;
    }

    @Override
    public String toString() {
        return "GroupResource{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
