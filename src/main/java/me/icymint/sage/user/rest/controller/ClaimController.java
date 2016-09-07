package me.icymint.sage.user.rest.controller;

import me.icymint.sage.base.spec.api.RuntimeContext;
import me.icymint.sage.user.rest.context.TokenContext;
import me.icymint.sage.user.spec.annotation.CheckToken;
import me.icymint.sage.user.spec.api.ClaimService;
import me.icymint.sage.user.spec.entity.Claim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * Created by daniel on 16/9/5.
 */
@RestController
@RequestMapping("/claims")
public class ClaimController {
    @Autowired
    ClaimService claimService;
    @Autowired
    RuntimeContext runtimeContext;
    @Autowired
    ApplicationContext context;

    @CheckToken
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Claim findOne(@PathVariable("id") Long id,
                         @RequestHeader("Authorization") TokenContext tokenContext) {
        Claim claim = claimService.findOne(id);
        if (claim != null
                && Objects.equals(claim.getOwnerId(), tokenContext.getOwnerId())) {
            return null;
        }
        return claim;
    }

}