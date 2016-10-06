package me.icymint.sage.user.rest.controller;

import me.icymint.sage.base.spec.entity.Pageable;
import me.icymint.sage.base.spec.internal.api.RuntimeContext;
import me.icymint.sage.user.core.service.IdentityServiceImpl;
import me.icymint.sage.user.data.mapper.GrantMapper;
import me.icymint.sage.user.rest.converter.UserEntityConverter;
import me.icymint.sage.user.rest.request.GroupRequest;
import me.icymint.sage.user.rest.resource.GroupResource;
import me.icymint.sage.user.spec.annotation.CheckToken;
import me.icymint.sage.user.spec.annotation.Permission;
import me.icymint.sage.user.spec.def.IdentityType;
import me.icymint.sage.user.spec.def.Privilege;
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

/**
 * Created by daniel on 2016/10/4.
 */
@RestController
@Permission(Privilege.GROUP_QUERY)
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    IdentityServiceImpl identityService;
    @Autowired
    ApplicationContext context;
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    GrantMapper grantMapper;
    @Autowired
    RuntimeContext runtimeContext;
    @Autowired
    UserEntityConverter converter;


    @SuppressWarnings("unchecked")
    @CheckToken
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GroupResource> findGroupsById(@RequestParam(value = "ownerId", required = false) Long ownerId, Pageable pageable) {
        if (ownerId != null) {
            return converter.convertGroup(grantMapper.findGroupsByOwnerIdPageable(ownerId, pageable));
        } else {
            return converter.convertGroup(identityService.findAll(IdentityType.GROUP, pageable));
        }
    }

    @CheckToken
    @GetMapping(value = "/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GroupResource findOne(@PathVariable("groupId") Long groupId) {
        return converter.convertGroup(identityService.findOne(groupId, IdentityType.GROUP));
    }

    @CheckToken
    @Permission(Privilege.GROUP_MODIFY)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public GroupResource createGroup(@Valid @RequestBody GroupRequest groupRequest) {
        return converter.convertGroup(identityService.createGroup(runtimeContext.getClientId(),
                runtimeContext.getUserId(),
                groupRequest.getName(),
                groupRequest.getRoleTypeList(),
                groupRequest.getPrivilegeList()));
    }

    @CheckToken
    @Permission(Privilege.GROUP_MODIFY)
    @DeleteMapping("/{groupId}")
    public void delete(@PathVariable("groupId") Long groupId) {
        identityService.deleteGroup(groupId);
    }


}
