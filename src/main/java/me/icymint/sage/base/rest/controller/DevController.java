package me.icymint.sage.base.rest.controller;

import io.swagger.annotations.Api;
import me.icymint.sage.base.rest.request.LoginHashRequest;
import me.icymint.sage.base.rest.request.LoginRequest;
import me.icymint.sage.base.rest.request.PasswordRequest;
import me.icymint.sage.base.rest.resource.HmacResponse;
import me.icymint.sage.base.spec.api.Clock;
import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.base.util.HMacs;
import me.icymint.sage.user.core.service.TokenServiceImpl;
import me.icymint.sage.user.rest.authorization.HmacAuthorizationMethod;
import me.icymint.sage.user.spec.entity.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * Created by daniel on 16/9/6.
 */
@Api(tags = Magics.API_DEV)
@RestController
@RequestMapping("/dev")
@ConditionalOnProperty(name = Magics.PROP_DEV_MODE, havingValue = "true")
public class DevController {

    @Autowired
    Environment environment;
    @Autowired
    TokenServiceImpl tokenService;
    @Autowired
    Clock clock;
    @Autowired
    HmacAuthorizationMethod authorizationMethod;

    @GetMapping(value = "/salt", produces = MediaType.TEXT_PLAIN_VALUE)
    public String salt() {
        return environment.getRequiredProperty("random.value").substring(0, 16);
    }

    @PostMapping(value = "/password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HmacResponse hash(@RequestBody PasswordRequest request) {
        String salt = salt();
        return new HmacResponse()
                .setSalt(salt)
                .setPassword(HMacs.encodeToHex(salt, request.getPassword()));
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Token login(@RequestBody LoginRequest request) {
        String nonce = salt();
        Long timestamp = clock.timestamp();
        String hash = tokenService.calculateLoginHash(request.getIdentityId(), request.getClientId(), nonce, timestamp, request.getPassword());
        return tokenService.login(request.getIdentityId(), request.getClientId(), nonce, timestamp, hash);
    }

    @PostMapping(value = "/hash", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String loginHash(@RequestBody LoginHashRequest request) {
        String nonce = salt();
        Long timestamp = clock.timestamp();
        String hash = tokenService.calculateTokenHash(request.getClientId(), nonce, timestamp, request.getId(), request.getAccessSecret());
        return Magics.TOKEN_AUTHORIZATION_HEAD
                + authorizationMethod.methodHeader()
                + " "
                + hash
                + Stream.of(String.valueOf(request.getClientId()),
                String.valueOf(request.getId()),
                String.valueOf(timestamp),
                nonce).collect(joining("|"));
    }

    @GetMapping(value = "/locale", produces = MediaType.TEXT_PLAIN_VALUE)
    public String locale() {
        return LocaleContextHolder.getLocale().toString();
    }
}
