package me.icymint.sage.base.data.handler;

import me.icymint.sage.user.spec.entity.IdentityExtension;
import org.apache.ibatis.type.MappedTypes;

/**
 * Created by daniel on 2016/10/3.
 */
@MappedTypes(IdentityExtension.class)
public class IdentityExtensionTypeHandler extends JsonTypeHandler<IdentityExtension> {

    @Override
    protected Class<IdentityExtension> getJsonClass() {
        return IdentityExtension.class;
    }
}
