package me.icymint.sage.base.core.support;

import me.icymint.sage.base.spec.entity.BaseLogEntity;
import me.icymint.sage.base.spec.internal.api.ToString;
import org.springframework.stereotype.Component;

/**
 * Created by daniel on 2016/9/30.
 */
@Component
public class BaseEntityToString implements ToString<BaseLogEntity> {
    @Override
    public Class<BaseLogEntity> supportObject() {
        return BaseLogEntity.class;
    }

    @Override
    public String toString(BaseLogEntity object) {
        return "{id:" + object.getId() + "}";
    }
}
