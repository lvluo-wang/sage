package me.icymint.sage.user.rest.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by daniel on 2016/10/14.
 */
public class GroupRenameRequest {
    @NotNull
    @Size(min = 2, max = 30)
    private String name;

    public String getName() {
        return name;
    }

    public GroupRenameRequest setName(String name) {
        this.name = name;
        return this;
    }
}
