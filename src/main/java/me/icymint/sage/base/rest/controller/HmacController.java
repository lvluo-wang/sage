package me.icymint.sage.base.rest.controller;

import me.icymint.sage.base.core.util.HMacs;
import me.icymint.sage.base.rest.request.HmacRequest;
import me.icymint.sage.base.rest.resource.HmacResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by daniel on 16/9/6.
 */
@RestController
@RequestMapping("/hmacs")
public class HmacController {

    @Autowired
    Environment environment;

    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String salt() {
        return environment.getRequiredProperty("random.value").substring(0, 16);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HmacResponse hash(@RequestBody HmacRequest request) {
        String salt = salt();
        return new HmacResponse()
                .setSalt(salt)
                .setPassword(HMacs.encodeToHex(salt, request.getPassword()));
    }
}
