package me.icymint.sage.base.spec.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by daniel on 2016/10/5.
 */
public interface JsonEntity {
    @JsonProperty("_class")
    Class<? extends JsonEntity> getEntityClass();
}
