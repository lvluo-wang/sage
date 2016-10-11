package me.icymint.sage.base.rest.controller;

import me.icymint.sage.base.rest.resource.HelloResource;
import me.icymint.sage.base.spec.api.Clock;
import me.icymint.sage.base.spec.def.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by daniel on 16/9/2.
 */
@Controller
public class ApiController {

    @Autowired
    Clock clock;
    @Autowired
    Environment environment;

    @GetMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public HelloResource hello() {
        return new HelloResource()
                .setGreet("Hello, world!")
                .setIsFriend(Bool.Y)
                .setIsDescription(Bool.Y)
                .setNow(clock.now());
    }

    @GetMapping("/{user:^(?!app).*}")
    public String home() {
        return "index";
    }


}