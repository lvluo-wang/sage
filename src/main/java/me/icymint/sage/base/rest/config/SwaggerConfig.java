package me.icymint.sage.base.rest.config;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.primitives.Primitives;
import me.icymint.sage.base.rest.entity.PaginatorResponse;
import me.icymint.sage.base.rest.support.RestConverter;
import me.icymint.sage.base.spec.def.MagicConstants;
import me.icymint.sage.user.spec.annotation.CheckToken;
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

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import static me.icymint.sage.base.util.Classes.isValueClass;

/**
 * Created by daniel on 16/9/3.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Autowired
    RestConverter restConverter;

    @Bean
    public Docket sageApi() {
        return build("sage-api", "Backend APIs for Sage", this::sageApi, null);
    }

    @Bean
    public Docket sageAuthApi() {
        return build("sage-auth-api", "Backend Auth APIs for Sage", this::needToken, a -> a.globalOperationParameters(Lists.newArrayList(new ParameterBuilder()
                .name(MagicConstants.HEADER_AUTHORIZATION)
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
        return !needToken(requestHandler) && !systemApi(requestHandler);
    }

    private boolean systemApi(RequestHandler requestHandler) {
        HandlerMethod method = requestHandler.getHandlerMethod();
        return method != null && !method.getBeanType().getPackage().getName().startsWith("me.icymint.sage");
    }

    private boolean needToken(RequestHandler requestHandler) {
        HandlerMethod method = requestHandler.getHandlerMethod();
        return method != null && method.hasMethodAnnotation(CheckToken.class);
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
        restConverter.getConverterCell().forEach(cell -> {
            if (cell.getRowKey() != null
                    && !isValueClass(cell.getRowKey())
                    && !cell.getRowKey().isPrimitive()
                    && cell.getRowKey() != String.class) {
                docket.directModelSubstitute(cell.getRowKey(), cell.getColumnKey());
                //See https://github.com/springfox/springfox/commit/c8d4f251d446a2722c4bc5985296edcd53807fd1
                addContainerConverter(typeResolver, docket, cell.getRowKey(), cell.getColumnKey());
            }
        });
        docket.directModelSubstitute(Instant.class, Date.class);
        addContainerConverter(typeResolver, docket, String.class);
        Primitives.allWrapperTypes()
                .forEach(wc -> addContainerConverter(typeResolver, docket, wc));
        return docket;
    }

    private void addContainerConverter(TypeResolver typeResolver, Docket docket, Class<?> typeClass) {
        addContainerConverter(typeResolver, docket, typeClass, typeClass);
    }

    private void addContainerConverter(TypeResolver typeResolver, Docket docket, Class<?> fromClass, Class<?> toClass) {
        docket.alternateTypeRules(AlternateTypeRules.newRule(
                typeResolver.resolve(List.class, fromClass),
                typeResolver.resolve(PaginatorResponse.class, toClass)
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
