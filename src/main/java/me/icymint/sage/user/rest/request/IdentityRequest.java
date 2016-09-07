package me.icymint.sage.user.rest.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by daniel on 16/9/5.
 */
public class IdentityRequest {
    @NotNull
    private Long cid;
    @Pattern(regexp = "[0-9a-zA-Z][._0-9a-zA-Z]{3,31}")
    private String username;
    @NotNull
    @Size(min = 8, max = 32)
    private String salt;
    @NotNull
    @Size(min = 16, max = 64)
    private String password;

    public Long getCid() {
        return cid;
    }

    public IdentityRequest setCid(Long cid) {
        this.cid = cid;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public IdentityRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getSalt() {
        return salt;
    }

    public IdentityRequest setSalt(String salt) {
        this.salt = salt;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public IdentityRequest setPassword(String password) {
        this.password = password;
        return this;
    }
}
