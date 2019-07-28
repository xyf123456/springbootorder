package com.tt.springbootorder.controller;

import com.tt.springbootorder.pojo.SpringJSONResult;
import com.tt.springbootorder.pojo.User;
import com.tt.springbootorder.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: com.tt.springbootorder.controller.TestController
 * @Description: 测试控制器
 * @Author:      Administrator
 * @CreateDate: 2019/7/28 0028 下午 10:17
 * @UpdateUser:   Administrator
 * @Version:        1.0
 **/
@RestController
@RequestMapping("/test")
@Api("测试控制器")
public class TestController {

    @Autowired
    private UserService userService;

        @ApiOperation(value = "使用用户编码查找用户",notes = "查询用户")
    @ApiImplicitParam(name = "userCode",value = "用户编码",required = false, paramType = "query",dataType = "String")
    @RequestMapping("/findUserCode/{userCode}")
    public SpringJSONResult findUserCode(@PathVariable("userCode") String userCode) throws Exception {
            User userList=userService.findByUserCode(userCode);
        if (userList == null) {//通过userCode查询到用户，则存在此用户
            return SpringJSONResult.errorException("null");
        }
      return SpringJSONResult.ok(userList);
    }

}
