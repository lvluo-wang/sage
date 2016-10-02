package me.icymint.sage.base.rest.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import me.icymint.sage.base.rest.resource.ExampleResource;
import me.icymint.sage.base.rest.resource.HelloResource;
import me.icymint.sage.base.spec.api.Clock;
import me.icymint.sage.base.spec.def.Bool;
import me.icymint.sage.user.spec.def.ClaimType;
import me.icymint.sage.user.spec.entity.Claim;
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
                .setList(lists().toArray(new String[]{}));
    }

    private List<String> lists() {
        return Lists.newArrayList("A", "B", "C");
    }


    private List<Claim> claims() {
        return Lists.newArrayList(new Claim()
                .setId(0L)
                .setCreateTime(clock.now())
                .setType(ClaimType.USERNAME)
                .setValue("daniel")
                .setIsDeleted(Bool.N)
                .setUpdateTime(clock.now()));
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }


    @GetMapping(value = "/api-list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<String> apiList() {
        return lists();
    }


    @GetMapping(value = "/api/claim-list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Claim> claimList() {
        return claims();
    }

    @GetMapping(value = "/api/example", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ExampleResource example() {
        return new ExampleResource()
                .setClaimList(claims())
                .setClaimSet(Sets.newHashSet(claims()))
                .setClaims(claims().toArray(new Claim[]{}));
    }


}