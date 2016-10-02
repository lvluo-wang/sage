package me.icymint.sage.user.rest.controller;

import me.icymint.sage.base.spec.annotation.ResponseView;
import me.icymint.sage.base.spec.internal.api.RuntimeContext;
import me.icymint.sage.user.rest.request.IdentityRequest;
import me.icymint.sage.user.rest.resource.IdentityResource;
import me.icymint.sage.user.spec.annotation.CheckToken;
import me.icymint.sage.user.spec.api.IdentityService;
import me.icymint.sage.user.spec.def.ClaimType;
import me.icymint.sage.user.spec.entity.Identity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by daniel on 16/9/5.
 */
@RestController
@RequestMapping("/identities")
public class IdentityController {
    @Autowired
    IdentityService identityService;
    @Autowired
    RuntimeContext runtimeContext;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseView(IdentityResource.class)
    public Identity register(@Valid @RequestBody IdentityRequest request) {
        return identityService.register(request.getCid(),
                request.getUsername(),
                request.getSalt(),
                request.getPassword());
    }

    @CheckToken
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseView(IdentityResource.class)
    public Identity findOne() {
        return identityService.findOne(runtimeContext.getUserId());
    }


    @ResponseView(IdentityResource.class)
    @GetMapping(value = "/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Identity findByUsername(@PathVariable("username") String username) {
        return identityService.findByClaim(username, ClaimType.USERNAME);
    }


}
