package me.icymint.sage.user.rest.controller;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import me.icymint.sage.base.spec.internal.api.RuntimeContext;
import me.icymint.sage.user.rest.request.GroupRequest;
import me.icymint.sage.user.spec.annotation.CheckToken;
import me.icymint.sage.user.spec.annotation.Permission;
import me.icymint.sage.user.spec.api.IdentityService;
import me.icymint.sage.user.spec.def.Privilege;
import me.icymint.sage.user.spec.def.RoleType;
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
import java.util.List;

/**
 * Created by daniel on 2016/10/4.
 */
@RestController
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    IdentityService identityService;
    @Autowired
    RuntimeContext runtimeContext;

    @CheckToken
    @Permission(Privilege.ADMIN)
    @GetMapping(value = "/privileges", produces = MediaType.APPLICATION_JSON_VALUE)
    public Privilege[] findPrivilegesById() {
        return Privilege.values();
    }

    @CheckToken
    @Permission(Privilege.ADMIN)
    @GetMapping(value = "/roles", produces = MediaType.APPLICATION_JSON_VALUE)
    public RoleType[] findRolesById() {
        return RoleType.values();
    }

    @CheckToken
    @Permission(Privilege.ADMIN)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Long> groups(PageBounds pageBounds) {
        return identityService.findGroupIds(pageBounds);
    }

    @CheckToken
    @Permission(Privilege.ADMIN)
    @GetMapping(value = "/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Identity findOne(@RequestParam("groupId") Long groupId) {
        return identityService.findGroup(groupId);
    }

    @CheckToken
    @Permission(Privilege.ADMIN)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Identity createGroup(@Valid @RequestBody GroupRequest groupRequest) {
        return identityService.createGroup(runtimeContext.getClientId(),
                runtimeContext.getUserId(),
                groupRequest.getName(),
                groupRequest.getRoleTypeList(),
                groupRequest.getPrivilegeList());
    }


}
