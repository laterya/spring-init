package cn.yp.springinit;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.yp.springinit.core.PhoneHelper;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yp
 * @date: 2023/10/9
 */

public class PhoneUtilTest {
    @Test
    void useTest() {
        String host = "https://gyytz.market.alicloudapi.com";
        String path = "/sms/smsSend";
        String method = "POST";
        String appcode = "a747d6b20d76422591de9093a0940449";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, Object> querys = new HashMap<>();
        querys.put("mobile", "17673027237");
        querys.put("param", "**code**:12345,**minute**:5");

//smsSignId（短信前缀）和templateId（短信模板），可登录国阳云控制台自助申请。参考文档：http://help.guoyangyun.com/Problem/Qm.html

        querys.put("smsSignId", "2e65b1bb3d054466b82f0c9d125465e2");
        querys.put("templateId", "908e94ccf08b4476ba6c876d13f084ad");
        Map<String, String> bodys = new HashMap<String, String>();


        try {
            HttpResponse response = HttpUtil.createPost(host + path).header("Authorization", "APPCODE " + appcode).form(querys).execute();
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void checkTest() {
        PhoneHelper.sendCode("17673027237", "1234567");
    }
}
