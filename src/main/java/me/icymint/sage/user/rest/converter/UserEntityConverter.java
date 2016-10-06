package me.icymint.sage.user.rest.converter;

import me.icymint.sage.base.spec.api.EntityConverterInterface;
import me.icymint.sage.user.rest.resource.ClaimResource;
import me.icymint.sage.user.rest.resource.GroupResource;
import me.icymint.sage.user.rest.resource.IdentityResource;
import me.icymint.sage.user.rest.resource.TokenResource;
import me.icymint.sage.base.spec.annotation.EntityConverter;
import me.icymint.sage.user.spec.entity.Claim;
import me.icymint.sage.user.spec.entity.Identity;
import me.icymint.sage.user.spec.entity.Token;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * This class define Entity converter
 * Created by daniel on 2016/10/1.
 *
 * @see me.icymint.sage.base.rest.aspect.DefaultResourceHandler
 */
@Mapper
public interface UserEntityConverter extends EntityConverterInterface {

    @Mapping(source = "identity.description", target = "name")
    GroupResource convertGroup(Identity identity);

    List<GroupResource> convertGroup(List<Identity> identity);

    @EntityConverter
    IdentityResource convert(Identity identity);

    @EntityConverter
    ClaimResource convert(Claim claim);

    @EntityConverter
    TokenResource convert(Token token);
}
