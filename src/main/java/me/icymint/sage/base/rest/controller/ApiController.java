package me.icymint.sage.base.rest.controller;

import com.google.common.collect.Lists;
import me.icymint.sage.base.rest.resource.HelloResource;
import me.icymint.sage.base.spec.api.Clock;
import me.icymint.sage.base.spec.def.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by daniel on 16/9/2.
 */
@Controller
public class ApiController {

    @Autowired
    Clock clock;

    @GetMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public HelloResource hello() {
        return new HelloResource()
                .setGreet("Hello, world!")
                .setIsFriend(Bool.Y)
                .setIsDescription(Bool.Y)
                .setNow(clock.now())
                .setList(lists());
    }

    private List<String> lists() {
        return Lists.newArrayList("A", "B", "C");
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }
}