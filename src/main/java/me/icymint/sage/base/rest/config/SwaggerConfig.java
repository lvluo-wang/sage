package me.icymint.sage.base.rest.config;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.primitives.Primitives;
import me.icymint.sage.base.rest.entity.PageableResponse;
import me.icymint.sage.base.rest.support.EntityConverters;
import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.base.util.Permissions;
import me.icymint.sage.user.spec.annotation.CheckToken;
import me.icymint.sage.user.spec.annotation.Permission;
import me.icymint.sage.user.spec.def.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.Array;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static me.icymint.sage.base.util.Classes.isValueClass;

/**
 * Created by daniel on 16/9/3.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Autowired
    EntityConverters restConverter;

    @Bean
    public Docket sageApi() {
        return build("sage-open-api", "Backend APIs for Sage", this::sageApi, null);
    }

    @Bean
    public Docket sageAuthApi() {
        return build("sage-user-api", "Backend Member APIs for Sage", b -> needToken(b) && !adminApi(b), a -> a.globalOperationParameters(Lists.newArrayList(new ParameterBuilder()
                .name(Magics.HEADER_AUTHORIZATION)
                .description("Token Auth")
                .parameterType("header")
                .modelRef(new ModelRef("string"))
                .required(true)
                .build())));
    }

    @Bean
    public Docket sageAdminApi() {
        return build("sage-admin-api", "Backend Administrator APIs for Sage", this::adminApi, a -> a.globalOperationParameters(Lists.newArrayList(new ParameterBuilder()
                .name(Magics.HEADER_AUTHORIZATION)
                .description("Token Auth")
                .parameterType("header")
                .modelRef(new ModelRef("string"))
                .required(true)
                .build())));
    }

    @Bean
    public Docket systemApi() {
        return build("sage-system-api", "System APIs for Sage", this::systemApi, null);
    }

    private boolean sageApi(RequestHandler requestHandler) {
        return !needToken(requestHandler) && !adminApi(requestHandler) && !systemApi(requestHandler);
    }

    private boolean systemApi(RequestHandler requestHandler) {
        HandlerMethod method = requestHandler.getHandlerMethod();
        return method != null && !method.getBeanType().getPackage().getName().startsWith("me.icymint.sage");
    }

    private boolean needToken(RequestHandler requestHandler) {
        HandlerMethod method = requestHandler.getHandlerMethod();
        return method != null && method.hasMethodAnnotation(CheckToken.class);
    }

    private boolean adminApi(RequestHandler requestHandler) {
        Permission permission = requestHandler.getHandlerMethod().getBeanType().getAnnotation(Permission.class);
        return Permissions.matchesRole(permission, role -> role == RoleType.ROLE_ADMIN);
    }

    private Docket build(String groupName, String description, Predicate<RequestHandler> selector, Function<Docket, Docket> handelr) {
        Docket docket = addConverters(new Docket(DocumentationType.SWAGGER_2)
                .groupName(groupName)
                .apiInfo(apiInfo(description)));
        if (handelr != null) {
            docket = handelr.apply(docket);
        }
        return docket.select()
                .apis(selector)
                .build();
    }

    private Docket addConverters(Docket docket) {
        TypeResolver typeResolver = new TypeResolver();
        restConverter.getConverterSets().forEach(cell -> {
            if (cell.getRowKey() != null
                    && !isValueClass(cell.getRowKey())) {
                //See https://github.com/springfox/springfox/commit/c8d4f251d446a2722c4bc5985296edcd53807fd1
                addContainerConverter(typeResolver, docket, cell.getRowKey(), cell.getColumnKey());
            }
        });
        docket.directModelSubstitute(Instant.class, Date.class);
        docket.directModelSubstitute(LocalDate.class, Date.class);
        docket.directModelSubstitute(LocalDateTime.class, Date.class);
        docket.directModelSubstitute(LocalTime.class, Date.class);
        addContainerConverter(typeResolver, docket, String.class);
        Primitives.allWrapperTypes()
                .forEach(wc -> addContainerConverter(typeResolver, docket, wc));
        return docket;
    }

    private void addContainerConverter(TypeResolver typeResolver, Docket docket, Class<?> typeClass) {
        addContainerConverter(typeResolver, docket, typeClass, typeClass);
    }

    private void addContainerConverter(TypeResolver typeResolver, Docket docket, Class<?> fromClass, Class<?> toClass) {
        if (fromClass != toClass) {
            docket.alternateTypeRules(AlternateTypeRules.newRule(
                    typeResolver.resolve(Set.class, fromClass),
                    typeResolver.resolve(Set.class, toClass)
            ));
            docket.directModelSubstitute(fromClass, toClass);
            docket.directModelSubstitute(
                    Array.newInstance(fromClass, 0).getClass(),
                    Array.newInstance(toClass, 0).getClass()
            );
        }
        docket.alternateTypeRules(AlternateTypeRules.newRule(
                typeResolver.resolve(List.class, fromClass),
                typeResolver.resolve(PageableResponse.class, toClass)
        ));
    }


    private ApiInfo apiInfo(String description) {
        return new ApiInfoBuilder()
                .title("Sage APIs")
                .description(description)
                .contact(new Contact("Daniel YU", "https://icymint.me", "i@icymint.me"))
                .version("2.0")
                .build();
    }
}
