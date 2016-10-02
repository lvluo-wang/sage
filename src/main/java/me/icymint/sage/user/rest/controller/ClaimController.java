package me.icymint.sage.user.rest.controller;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import me.icymint.sage.base.spec.annotation.ResponseView;
import me.icymint.sage.base.spec.internal.api.RuntimeContext;
import me.icymint.sage.user.rest.resource.ClaimResource;
import me.icymint.sage.user.spec.annotation.CheckToken;
import me.icymint.sage.user.spec.api.ClaimService;
import me.icymint.sage.user.spec.entity.Claim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @ResponseView(ClaimResource.class)
    public Claim findOne(@PathVariable("id") Long id) {
        return claimService.findOne(id, runtimeContext.getUserId());
    }

    @CheckToken
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseView(ClaimResource.class)
    public List<Claim> findAll(PageBounds pageBounds) {
        return claimService.findByOwnerId(runtimeContext.getUserId(), pageBounds);
    }

}
