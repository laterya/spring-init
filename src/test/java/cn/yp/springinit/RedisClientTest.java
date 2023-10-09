package cn.yp.springinit;

import cn.yp.springinit.cache.RedisClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author yp
 * @date: 2023/10/9
 */
@SpringBootTest
public class RedisClientTest {

    @Test
    void setTest() {
        RedisClient.setStrWithExpire("xbb", "ybz", 9999L);
        RedisClient.setStrWithExpire("1", "ybz", 9999L);
        RedisClient.setStrWithExpire("2", "ybz", 9999L);
    }

}
