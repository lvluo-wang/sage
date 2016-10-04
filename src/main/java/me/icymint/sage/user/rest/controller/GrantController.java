package me.icymint.sage.user.rest.controller;

import io.swagger.annotations.Api;
import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.base.spec.internal.api.RuntimeContext;
import me.icymint.sage.user.core.service.GrantService;
import me.icymint.sage.user.spec.annotation.CheckToken;
import me.icymint.sage.user.spec.annotation.Permission;
import me.icymint.sage.user.spec.def.Privilege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by daniel on 2016/10/4.
 */
@Api(tags = Magics.API_ADMIN)
@Permission(Privilege.ADMIN)
@RestController
@RequestMapping("/grants")
public class GrantController {
    @Autowired
    GrantService grantService;
    @Autowired
    RuntimeContext runtimeContext;

    @CheckToken
    @GetMapping(value = "/{ownerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Long> groupsByOwnerId(@PathVariable("ownerId") Long ownerId) {
        if (ownerId == null) {
            ownerId = runtimeContext.getUserId();
        }
        return grantService.findGroupIdsByOwnerId(ownerId);
    }

    @CheckToken
    @PostMapping(value = "/{ownerId}/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void grant(@PathVariable("ownerId") Long ownerId, @PathVariable("groupId") Long groupId) {
        grantService.grant(ownerId, groupId);
    }

    @CheckToken
    @DeleteMapping(value = "/{ownerId}/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void revoke(@PathVariable("ownerId") Long ownerId, @PathVariable("groupId") Long groupId) {
        grantService.revoke(ownerId, groupId);
    }

}
