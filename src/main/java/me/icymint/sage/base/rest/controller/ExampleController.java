package me.icymint.sage.base.rest.controller;

import com.google.common.collect.Lists;
import me.icymint.sage.base.spec.annotation.PageableView;
import me.icymint.sage.base.spec.api.Clock;
import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.user.spec.def.ClaimType;
import me.icymint.sage.user.spec.entity.Claim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by daniel on 2016/10/4.
 */
@RestController
@RequestMapping("/examples")
@ConditionalOnProperty(name = Magics.PROP_DEV_MODE, havingValue = "true")
public class ExampleController {
    @Autowired
    Clock clock;

    private List<String> lists() {
        return Lists.newArrayList("A", "B", "C");
    }


    private List<Claim> claims() {
        return Lists.newArrayList(new Claim()
                .setId(0L)
                .setCreateTime(clock.now())
                .setType(ClaimType.USERNAME)
                .setValue("daniel"));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> apiList() {
        return lists();
    }


    @GetMapping(value = "/claims", produces = MediaType.APPLICATION_JSON_VALUE)
    @PageableView
    public List<Claim> claimList() {
        return claims();
    }

}
