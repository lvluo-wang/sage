package me.icymint.sage;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import me.icymint.sage.base.core.util.HMacs;
import me.icymint.sage.user.data.mapper.IdentityMapper;
import me.icymint.sage.user.spec.def.IdentityType;
import me.icymint.sage.user.spec.entity.Identity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.annotation.Timed;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
    private Logger logger = LoggerFactory.getLogger(DemoApplicationTests.class);

    @Autowired
    DataSource dataSource;
    @Autowired
    IdentityMapper identityMapper;

    @Test
    public void contextLoads() {
        logger.info("HelloResource");
        System.out.println("hello");
        Identity id = new Identity()
                .setCreateBy(0L)
                .setOwnerId(0L)
                .setSalt("-")
                .setPassword("-")
                .setType(IdentityType.USER);
        assertEquals(1, identityMapper.save(id));
        id = identityMapper.findOne(id.getId());
        System.out.println(id);
        System.out.println(identityMapper.findAll(new PageBounds(1, 10)));
    }

    @Test
    @Repeat(10)
    @Timed(millis = 500L)
    public void test() {
        String hmac = HMacs.encodeToHex("key", "The quick brown fox jumps over the lazy dog");
        String hmac_64 = HMacs.encodeToBase64("key", "The quick brown fox jumps over the lazy dog");
        assertEquals("f7bc83f430538424b13298e6aa6fb143ef4d59a14946175997479dbc2d1a3cd8", hmac);
        assertEquals("97yD9DBThCSxMpjmqm+xQ+9NWaFJRhdZl0edvC0aPNg=", hmac_64);
    }

}