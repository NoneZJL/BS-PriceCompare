package org.zjubs.pricecomwebbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zjubs.pricecomwebbackend.query.ApiResult;
import org.zjubs.pricecomwebbackend.query.RespResult;
import org.zjubs.pricecomwebbackend.service.JdService;
import org.zjubs.pricecomwebbackend.utils.JWTUtil;

@RestController
@RequestMapping("/jd")
public class JdController {
    @Autowired
    private JdService jdService;

    @GetMapping("/getGoodsBySearchingName")
    public RespResult getGoodsBySearchingName(@RequestParam String name, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        System.out.println("jd-token:"+token);
        ApiResult apiResult = jdService.getGoodsBySearchingName(name, token);
        if (apiResult.ok) {
            return RespResult.success(apiResult.payload);
        } else {
            if (JWTUtil.checkNotLogin(apiResult)) {
                return RespResult.notLogin();
            }
            return RespResult.fail(apiResult.message);
        }
    }
}
