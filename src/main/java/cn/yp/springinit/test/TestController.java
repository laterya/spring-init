package cn.yp.springinit.test;

import cn.yp.springinit.common.BaseRes;
import cn.yp.springinit.core.PhoneHelper;
import cn.yp.springinit.utils.ResUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "首页模块")
@RestController("/test")
public class TestController {

    @ApiImplicitParam(name = "name", value = "姓名", required = true)
    @ApiOperation(value = "向客人问好")
    @GetMapping("/sayHi")
    public BaseRes<String> sayHi(@RequestParam(value = "name") String name) {
        return ResUtil.buildSuccessRes("hello " + name);
    }

    @GetMapping("/sendCode")
    public BaseRes<Boolean> sendCode(@RequestParam(value = "name") String phone) {
        PhoneHelper.sendCode(phone, "123456");
        return ResUtil.buildSuccessRes(true);
    }
}