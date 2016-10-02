package me.icymint.sage.base.rest.config;

import com.google.common.collect.Lists;
import me.icymint.sage.base.spec.def.MagicConstants;
import me.icymint.sage.user.spec.annotation.CheckToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by daniel on 16/9/3.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket sageApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("sage-api")
                .apiInfo(apiInfo("Backend APIs for Sage"))
                .select()
                .apis(this::sageApi)
                .build();
    }

    @Bean
    public Docket sageAuthApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("sage-auth-api")
                .apiInfo(apiInfo("Backend Auth APIs for Sage"))
                .globalOperationParameters(Lists.newArrayList(new ParameterBuilder()
                        .name(MagicConstants.HEADER_AUTHORIZATION)
                        .description("Token Auth")
                        .parameterType("header")
                        .modelRef(new ModelRef("string"))
                        .required(true)
                        .build()))
                .select()
                .apis(this::needToken)
                .build();
    }

    @Bean
    public Docket systemApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("sage-system-api")
                .apiInfo(apiInfo("System APIs for Sage"))
                .select()
                .apis(this::systemApi)
                .build();
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

    private ApiInfo apiInfo(String description) {
        return new ApiInfoBuilder()
                .title("Sage APIs")
                .description(description)
                .contact(new Contact("Daniel YU", "https://icymint.me", "i@icymint.me"))
                .version("2.0")
                .build();
    }
}
