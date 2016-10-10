package me.icymint.sage;

import me.icymint.sage.base.core.service.EventServiceImpl;
import me.icymint.sage.base.rest.controller.DevController;
import me.icymint.sage.base.rest.request.LoginRequest;
import me.icymint.sage.base.rest.request.PasswordRequest;
import me.icymint.sage.base.rest.resource.HmacResponse;
import me.icymint.sage.base.spec.def.Magics;
import me.icymint.sage.base.util.HMacs;
import me.icymint.sage.user.core.service.IdentityServiceImpl;
import me.icymint.sage.user.data.mapper.EventMapper;
import me.icymint.sage.user.spec.api.TokenService;
import me.icymint.sage.user.spec.def.EventStatus;
import me.icymint.sage.user.spec.def.IdentityType;
import me.icymint.sage.user.spec.entity.Identity;
import me.icymint.sage.user.spec.entity.Token;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.annotation.Timed;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {
        Magics.PROP_ENABLE_API_ + "ROLE_DEV=true",
        Magics.PROP_ENABLE_API_ + "ROLE_ADMIN=true",
        Magics.PROP_DEV_MODE + "=true",
        Magics.PROP_ALWAYS_SAVE_LOG + "=true",
        Magics.PROP_JOB_EVENT_CRON + "=0/1 * * * * *"
})
public class ApplicationTests {
    private final Logger logger = LoggerFactory.getLogger(ApplicationTests.class);
    @Autowired
    DataSource dataSource;
    @Autowired
    EventMapper eventMapper;
    @Autowired
    EventServiceImpl eventService;
    @Autowired
    IdentityServiceImpl identityService;
    @Autowired
    TokenService tokenService;
    @Autowired
    TestService testService;
    @Autowired
    DevController hmacController;

    @Test
    public void contextLoads() {
        logger.info("HelloResource");
        System.out.println("hello");
        Identity client = identityService.findOne(1000L, IdentityType.CLIENT);
        assertNotNull(client);
        System.out.println(client);
        assertEquals(IdentityType.CLIENT, client.getType());
        HmacResponse resource = hmacController.hash(new PasswordRequest().setPassword("12345678"));

        Identity id = identityService.register(client.getId(), "daniel", resource.getSalt(), resource.getPassword());

        assertEquals(client.getId(), id.getCreateId());
        assertEquals(client.getId(), id.getOwnerId());
        assertEquals(IdentityType.MEMBER, id.getType());

        Token token = hmacController.login(new LoginRequest()
                .setClientId(client.getId())
                .setIdentityId(id.getId())
                .setPassword(resource.getPassword()));

        assertEquals(client.getId(), token.getClientId());
        assertEquals(id.getId(), token.getOwnerId());

        System.out.println(id);
    }

    @Test
    @Repeat(10)
    public void test() {
        String hmac = HMacs.encodeToHex("key", "The quick brown fox jumps over the lazy dog");
        String hmac_64 = HMacs.encodeToBase64("key", "The quick brown fox jumps over the lazy dog");
        assertEquals("f7bc83f430538424b13298e6aa6fb143ef4d59a14946175997479dbc2d1a3cd8", hmac);
        assertEquals("97yD9DBThCSxMpjmqm+xQ+9NWaFJRhdZl0edvC0aPNg=", hmac_64);
    }

    @Test
    @Timed(millis = 120 * 1000L)
    public void testEvent() {
        TestEvent event = new TestEvent().setMessage("Hello");
        eventService.post(event);

        identityService.register(1000L, "hello", "123", "679");

        while (eventMapper.findByEventId(event.getEventId()).getStatus() == EventStatus.CREATED) {
            try {
                TimeUnit.MILLISECONDS.sleep(1000L);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    @Test
    public void testEventAnnotation() {
        testService.sendMessageHolder("Hello , --------> world");
    }
}
