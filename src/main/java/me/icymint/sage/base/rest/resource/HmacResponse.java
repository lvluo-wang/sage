package me.icymint.sage.base.rest.resource;

/**
 * Created by daniel on 16/9/6.
 */
public class HmacResponse {
    private String salt;
    private String password;

    public String getSalt() {
        return salt;
    }

    public HmacResponse setSalt(String salt) {
        this.salt = salt;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public HmacResponse setPassword(String password) {
        this.password = password;
        return this;
    }
}
