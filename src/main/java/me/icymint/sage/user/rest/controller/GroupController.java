package me.icymint.sage.user.rest.controller;

import me.icymint.sage.base.spec.entity.Pageable;
import me.icymint.sage.base.spec.internal.api.RuntimeContext;
import me.icymint.sage.user.core.service.IdentityServiceImpl;
import me.icymint.sage.user.rest.request.GroupRequest;
import me.icymint.sage.user.rest.resource.GroupResource;
import me.icymint.sage.user.spec.annotation.CheckToken;
import me.icymint.sage.user.spec.annotation.Permission;
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
import java.util.List;

/**
 * Created by daniel on 2016/10/4.
 */
@Permission(Privilege.ADMIN)
@RestController
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    IdentityServiceImpl identityService;
    @Autowired
    RuntimeContext runtimeContext;

    @CheckToken
    @GetMapping(value = "/privileges", produces = MediaType.APPLICATION_JSON_VALUE)
    public Privilege[] findAllPrivileges() {
        return Privilege.values();
    }

    @CheckToken
    @GetMapping(value = "/roles", produces = MediaType.APPLICATION_JSON_VALUE)
    public RoleType[] findAllRoles() {
        return RoleType.values();
    }

    @CheckToken
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Long> groups(Pageable pageable) {
        return identityService.findGroupIds(pageable);
    }

    @CheckToken
    @GetMapping(value = "/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GroupResource findOne(@PathVariable("groupId") Long groupId) {
        return build(identityService.findGroup(groupId));
    }

    private GroupResource build(Identity group) {
        if (group == null) {
            return null;
        }
        return new GroupResource()
                .setId(group.getId())
                .setName(group.getDescription())
                .setCreateTime(group.getCreateTime());
    }

    @CheckToken
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public GroupResource createGroup(@Valid @RequestBody GroupRequest groupRequest) {
        return build(identityService.createGroup(runtimeContext.getClientId(),
                runtimeContext.getUserId(),
                groupRequest.getName(),
                groupRequest.getRoleTypeList(),
                groupRequest.getPrivilegeList()));
    }


}
