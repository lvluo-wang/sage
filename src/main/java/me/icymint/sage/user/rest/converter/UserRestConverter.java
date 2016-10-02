package me.icymint.sage.user.rest.converter;

import me.icymint.sage.base.spec.annotation.ResponseConverter;
import me.icymint.sage.user.rest.resource.ClaimResource;
import me.icymint.sage.user.rest.resource.IdentityResource;
import me.icymint.sage.user.rest.resource.TokenResource;
import me.icymint.sage.user.spec.entity.Claim;
import me.icymint.sage.user.spec.entity.Identity;
import me.icymint.sage.user.spec.entity.Token;
import org.mapstruct.Mapper;

/**
 * Created by daniel on 2016/10/1.
 */
@Mapper
public interface UserRestConverter extends ResponseConverter {

    IdentityResource to(Identity identity);

    ClaimResource to(Claim claim);

    TokenResource to(Token token);
}
