package me.icymint.sage.user.rest.controller;

import me.icymint.sage.base.util.Enums;
import me.icymint.sage.user.spec.annotation.CheckToken;
import me.icymint.sage.user.spec.annotation.Permission;
import me.icymint.sage.user.spec.def.Privilege;
import me.icymint.sage.user.spec.def.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Created by daniel on 2016/10/6.
 */
@Permission(Privilege.ADMIN)
@RestController
@RequestMapping("/permissions")
public class PermissionController {

    @Autowired
    ApplicationContext context;

    @CheckToken
    @GetMapping(value = "/privileges", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Enums.EnumInfo<Privilege>> findAllPrivileges(@RequestParam(value = "role", required = false) RoleType roleType) {
        Privilege[] privileges;
        if (roleType == null) {
            privileges = Privilege.values();
        } else {
            privileges = Stream.of(Privilege.values())
                    .filter(p -> Stream
                            .of(p.getRoleTypes())
                            .anyMatch(r -> r == roleType))
                    .collect(toList())
                    .toArray(new Privilege[]{});
        }
        return Enums.getEnumInfoList(context, LocaleContextHolder.getLocale(), privileges);
    }

    @CheckToken
    @GetMapping(value = "/roles", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Enums.EnumInfo<RoleType>> findAllRoles(@RequestParam(value = "privilege", required = false) Privilege privilege) {
        RoleType[] roleTypes;
        if (privilege == null) {
            roleTypes = RoleType.values();
        } else {
            roleTypes = privilege.getRoleTypes();
        }
        return Enums.getEnumInfoList(context, LocaleContextHolder.getLocale(), roleTypes);
    }
}
