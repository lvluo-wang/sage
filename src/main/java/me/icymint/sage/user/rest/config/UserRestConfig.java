package me.icymint.sage.user.rest.config;

import me.icymint.sage.user.rest.converter.UserRestConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mapstruct.factory.Mappers.getMapper;

/**
 * Created by daniel on 2016/10/2.
 */
@Configuration
public class UserRestConfig {
    @Bean
    public UserRestConverter userRestConverter() {
        return getMapper(UserRestConverter.class);
    }
}
