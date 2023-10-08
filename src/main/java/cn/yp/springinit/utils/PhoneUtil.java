package cn.yp.springinit.utils;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.yp.springinit.common.ResCode;
import cn.yp.springinit.exception.CustomException;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author yp
 * @date: 2023/10/8
 */
public class PhoneUtil {

    private static final String PHONE_REGEX = "^1[3-9]\\d{9}$";

    private static final String SMS_SIGN_ID = "2e65b1bb3d054466b82f0c9d125465e2";

    private static final String TEMPLATE_ID = "908e94ccf08b4476ba6c876d13f084ad";

    private static final String APP_CODE = "a747d6b20d76422591de9093a0940449";

    /**
     * 校验手机号格式
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (!StringUtils.isBlank(phoneNumber)) {
            return Pattern.matches(PHONE_REGEX, phoneNumber);
        }
        return false;
    }

    /**
     * @param mobile 手机号
     * @param code   发送的验证码
     */
    // todo 发送验证码返回问题：当发送额度没了的时候的返回处理
    public static boolean sendMessage(String mobile, String code) {
        HttpResponse response = null;
        String host = "https://gyytz.market.alicloudapi.com/sms/smsSend";
        Map<String, Object> body = new HashMap<>();
        body.put("mobile", mobile);
        body.put("param", "**code**:" + code + ",**minute**:" + 3);
        body.put("smsSignId", SMS_SIGN_ID);
        body.put("templateId", TEMPLATE_ID);
        boolean res = false;
        try {
            response = HttpUtil.createPost(host)
                    .header("Authorization", "APPCODE " + APP_CODE)
                    .form(body)
                    .execute();
        } catch (Exception e) {
            throw new CustomException(ResCode.SYSTEM_ERROR, e.getMessage());
        } finally {
            System.out.println(response.body());
            if (response != null) {
                response.close();
                res = true;
            }
        }
        return res;
    }

}
