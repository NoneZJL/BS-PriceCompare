package org.zjubs.pricecomwebbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zjubs.pricecomwebbackend.query.ApiResult;
import org.zjubs.pricecomwebbackend.query.RespResult;
import org.zjubs.pricecomwebbackend.query.UpdateUserInfo;
import org.zjubs.pricecomwebbackend.query.UserInfo;
import org.zjubs.pricecomwebbackend.service.HomeService;
import org.zjubs.pricecomwebbackend.utils.JWTUtil;

@RestController
public class HomeController {
    @Autowired
    HomeService homeService;

    @GetMapping("/getUserInfo")
    public RespResult getUserInformation(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        ApiResult apiResult = homeService.queryUserInfoByToken(token);
        if(apiResult.ok) {
            return RespResult.success(apiResult.payload);
        } else {
            if (JWTUtil.checkNotLogin(apiResult)) {
                return RespResult.notLogin();
            }
            return RespResult.fail(apiResult.message);
        }
    }

    @PostMapping("/updateUserInfo")
    public RespResult updateUserInformation(@RequestBody UpdateUserInfo userInfo, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        System.out.println("Received request body: " + userInfo); // 添加调试日志
        ApiResult apiResult = homeService.updateUserInfo(token, userInfo);
        if (apiResult.ok) {
            return RespResult.success();
        } else {
            if (JWTUtil.checkNotLogin(apiResult)) {
                return RespResult.notLogin();
            }
            return RespResult.fail(apiResult.message);
        }
    }
}
