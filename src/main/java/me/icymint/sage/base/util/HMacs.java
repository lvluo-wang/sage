package me.icymint.sage.base.util;

import com.google.common.io.BaseEncoding;
import me.icymint.sage.base.spec.exception.InvalidArgumentException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Created by daniel on 16/9/6.
 */
public class HMacs {

    public static byte[] encode(String key, String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            return sha256_HMAC.doFinal(data.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new InvalidArgumentException(e);
        }
    }

    public static String encodeToBase64(String key, String data) {
        return Base64.getEncoder().encodeToString(encode(key, data));
    }

    public static String encodeToHex(String key, String data) {
        return BaseEncoding.base16().lowerCase().encode(encode(key, data));
    }
}
