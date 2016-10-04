package me.icymint.sage.user.rest.controller;

import me.icymint.sage.base.spec.internal.api.RuntimeContext;
import me.icymint.sage.user.core.service.IdentityServiceImpl;
import me.icymint.sage.user.rest.request.IdentityRequest;
import me.icymint.sage.user.spec.annotation.CheckToken;
import me.icymint.sage.user.spec.def.ClaimType;
import me.icymint.sage.user.spec.def.Privilege;
import me.icymint.sage.user.spec.def.RoleType;
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
import java.util.Set;

/**
 * Created by daniel on 16/9/5.
 */
@RestController
@RequestMapping("/identities")
public class IdentityController {
    @Autowired
    IdentityServiceImpl identityService;
    @Autowired
    RuntimeContext runtimeContext;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Identity register(@Valid @RequestBody IdentityRequest request) {
        return identityService.register(request.getCid(),
                request.getUsername(),
                request.getSalt(),
                request.getPassword());
    }

    @GetMapping(value = "/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Identity findByUsername(@PathVariable("username") String username) {
        return identityService.findByClaim(username, ClaimType.USERNAME);
    }

    @CheckToken
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Identity findOne() {
        return identityService.findOne(runtimeContext.getUserId());
    }

    @CheckToken
    @GetMapping(value = "/privileges", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<Privilege> findPrivilegesById() {
        return identityService.findPrivilegesById(runtimeContext.getUserId());
    }

    @CheckToken
    @GetMapping(value = "/roles", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<RoleType> findRolesById() {
        return identityService.findRolesById(runtimeContext.getUserId());
    }

}
