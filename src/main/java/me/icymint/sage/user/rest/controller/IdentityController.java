package me.icymint.sage.user.rest.controller;

import me.icymint.sage.user.rest.request.IdentityRequest;
import me.icymint.sage.user.spec.api.IdentityService;
import me.icymint.sage.user.spec.def.ClaimType;
import me.icymint.sage.user.spec.entity.Identity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Identity register(@Valid @RequestBody IdentityRequest request) {
        return identityService.register(request.getCid(),
                request.getUsername(),
                request.getSalt(),
                request.getPassword());
    }

    @GetMapping(params = {"claim", "type"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Identity findByClaim(@RequestParam("claim") String claim,
                                @RequestParam("type") ClaimType type) {
        return identityService.findByClaim(claim, type);
    }


}
