package me.icymint.sage.user.rest.controller;

import me.icymint.sage.base.spec.entity.Pageable;
import me.icymint.sage.base.spec.internal.api.RuntimeContext;
import me.icymint.sage.user.core.service.GrantService;
import me.icymint.sage.user.core.service.IdentityServiceImpl;
import me.icymint.sage.user.rest.request.GroupRequest;
import me.icymint.sage.user.rest.resource.GroupResource;
import me.icymint.sage.user.spec.annotation.CheckToken;
import me.icymint.sage.user.spec.annotation.Permission;
import me.icymint.sage.user.spec.def.IdentityType;
import me.icymint.sage.user.spec.def.Privilege;
import me.icymint.sage.user.spec.entity.Identity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.IntStream;

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
    ApplicationContext context;
    @Autowired
    GrantService grantService;
    @Autowired
    RuntimeContext runtimeContext;

    @CheckToken
    @GetMapping(params = "ownerId", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Long> findGroupsById(@RequestParam("ownerId") Long ownerId) {
        return grantService.findGroupIdsByOwnerId(ownerId);
    }

    @SuppressWarnings("unchecked")
    @CheckToken
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GroupResource> findGroups(Pageable pageable) {
        return build(identityService.findAll(IdentityType.GROUP, pageable));
    }

    @SuppressWarnings("unchecked")
    private List build(List groups) {
        IntStream.range(0, groups.size())
                .forEach(i -> {
                    groups.set(i, build((Identity) groups.get(i)));
                });
        return groups;
    }

    @CheckToken
    @GetMapping(value = "/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GroupResource findOne(@PathVariable("groupId") Long groupId) {
        return build(identityService.findOne(groupId, IdentityType.GROUP));
    }

    @CheckToken
    @PostMapping(value = "/{groupId}/{ownerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void grant(@PathVariable("ownerId") Long ownerId, @PathVariable("groupId") Long groupId) {
        grantService.grant(ownerId, groupId);
    }

    @CheckToken
    @DeleteMapping(value = "/{groupId}/{ownerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void revoke(@PathVariable("ownerId") Long ownerId, @PathVariable("groupId") Long groupId) {
        grantService.revoke(ownerId, groupId);
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


    @CheckToken
    @DeleteMapping("/{groupId}")
    public void delete(@PathVariable("groupId") Long groupId) {
        identityService.deleteGroup(groupId);
    }


}
