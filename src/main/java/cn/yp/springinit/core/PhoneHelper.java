package cn.yp.springinit.core;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.HttpUtil;
import cn.yp.springinit.common.ResCode;
import cn.yp.springinit.exception.CustomException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yp
 * @date: 2023/10/9
 */
@Slf4j
public class PhoneHelper {

    private static final String url = "https://api.guoyangyun.com/api/sms/smsoto.htm";

    private static final String SMS_SIGN_ID = "2e65b1bb3d054466b82f0c9d125465e2";

    private static final String TEMPLATE_ID = "908e94ccf08b4476ba6c876d13f084ad";

    private static final String APP_CODE = "a747d6b20d76422591de9093a0940449";

    public static Boolean sendCode(String phone, String code) {

        String host = "https://gyytz.market.alicloudapi.com";
        String path = "/sms/smsSend";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + APP_CODE);
        Map<String, Object> querys = new HashMap<>();
        querys.put("mobile", phone);
        querys.put("param", "**code**:" + code + ",**minute**:5");
        querys.put("smsSignId", SMS_SIGN_ID);
        querys.put("templateId", TEMPLATE_ID);

        try (HttpResponse response = HttpUtil.createPost(host + path).header("Authorization", "APPCODE " + APP_CODE).form(querys).execute()) {
            if (response.getStatus() != HttpStatus.HTTP_OK) {
                log.error("send code error: {}", response.body());
                return false;
            }
        } catch (Exception e) {
            throw new CustomException(ResCode.SEND_CODE_ERROR);
        }
        return true;
    }
}
