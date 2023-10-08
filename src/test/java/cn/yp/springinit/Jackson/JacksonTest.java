package cn.yp.springinit.Jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yp
 * @date: 2023/10/7
 */
@Slf4j
public class JacksonTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void toObjectTest() {
        // 转义字符\
        String carJson = "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";

        try {
            Car car = objectMapper.readValue(carJson, Car.class);

            System.out.println("car brand = " + car.getBrand());
            System.out.println("car doors = " + car.getDoors());
        } catch (IOException e) {
            log.info("error is " + e);
        }
    }

    @Test
    void toJsonTest() throws JsonProcessingException {
        Car car = new Car();
        car.setBrand("bc");
        car.setDoors(5);
        String json = objectMapper.writeValueAsString(car);
        System.out.println(json);

        // ========== 时间类型序列化
        Transaction transaction = new Transaction("transfer", new Date());
        System.out.println(new Date());
        ObjectMapper objectMapper = new ObjectMapper();
        String output = objectMapper.writeValueAsString(transaction);
        System.out.println(output);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        objectMapper.setDateFormat(simpleDateFormat);
        String s = objectMapper.writeValueAsString(transaction);
        System.out.println(s);
    }
}
