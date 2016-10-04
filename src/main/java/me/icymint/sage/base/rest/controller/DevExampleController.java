package me.icymint.sage.base.rest.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import me.icymint.sage.base.rest.resource.ExampleResource;
import me.icymint.sage.base.spec.api.Clock;
import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.user.spec.annotation.CheckToken;
import me.icymint.sage.user.spec.annotation.Permission;
import me.icymint.sage.user.spec.def.ClaimType;
import me.icymint.sage.user.spec.def.Privilege;
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
@Api(tags = Magics.API_DEV)
@Permission(Privilege.DEV_API)
@RestController
@RequestMapping("/examples/admin")
@ConditionalOnProperty(name = Magics.PROP_DEV_MODE, havingValue = "true")
public class DevExampleController {
    @Autowired
    Clock clock;

    private List<Claim> claims() {
        return Lists.newArrayList(new Claim()
                .setId(0L)
                .setCreateTime(clock.now())
                .setType(ClaimType.USERNAME)
                .setValue("daniel"));
    }

    @CheckToken
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ExampleResource example() {
        return new ExampleResource()
                .setClaimList(claims())
                .setClaimSet(Sets.newHashSet(claims()))
                .setClaims(claims().toArray(new Claim[]{}));
    }

    @GetMapping(value = "/denied", produces = MediaType.APPLICATION_JSON_VALUE)
    public ExampleResource permission() {
        return new ExampleResource()
                .setClaimList(claims())
                .setClaimSet(Sets.newHashSet(claims()))
                .setClaims(claims().toArray(new Claim[]{}));
    }

}
