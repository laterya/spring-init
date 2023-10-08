package cn.yp.springinit.cache;

import cn.yp.springinit.utils.JsonUtil;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author yp
 * @date: 2023/10/4
 */
public class RedisClient {

    private static RedisTemplate<String, String> template;

    private static final String KEY_PRIMARY = "spring_init_";

    private static final Charset CODE = StandardCharsets.UTF_8;

    public static void nullCheck(Object... args) {
        for (Object obj : args) {
            if (obj == null) {
                throw new IllegalArgumentException("redis argument can not be null!");
            }
        }
    }

    /**
     * 缓存键序列化处理
     *
     * @param key key
     * @return 字节数组
     */
    public static byte[] keyBytes(String key) {
        key = KEY_PRIMARY + key;
        return key.getBytes(CODE);
    }

    /**
     * 缓存值序列化处理
     *
     * @param val
     * @param <T>
     * @return
     */
    public static <T> byte[] valBytes(T val) {
        if (val instanceof String) {
            return ((String) val).getBytes(CODE);
        } else {
            return JsonUtil.toStr(val).getBytes(CODE);
        }
    }

    /**
     * 带过期时间的缓存写入
     *
     * @param key
     * @param value
     * @param expire s为单位
     * @return
     */
    public static Boolean setStrWithExpire(String key, String value, Long expire) {
        return template.execute((RedisConnection redisConnection) -> {
            return redisConnection.setEx(keyBytes(key), expire, valBytes(value));
        });
    }

    public static void del(String key) {
        template.execute((RedisCallback<Long>) connect -> connect.del(keyBytes(key)));
    }


    public static String getStr(String key) {
        return template.execute((RedisCallback<String>) con -> {
            byte[] bytes = con.get(keyBytes(key));
            return bytes == null ? null : new String(bytes);
        });
    }
}
