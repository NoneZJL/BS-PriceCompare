package org.zjubs.pricecomwebbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zjubs.pricecomwebbackend.query.ApiResult;
import org.zjubs.pricecomwebbackend.query.RespResult;
import org.zjubs.pricecomwebbackend.service.SnService;
import org.zjubs.pricecomwebbackend.utils.JWTUtil;

@RestController
@RequestMapping("/sn")
public class SnController {
    @Autowired
    private SnService snService;

    @GetMapping("/getGoodsBySearchingName")
    public RespResult getGoodsBySearchingName(@RequestParam String name, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        System.out.println("sn-token:"+token);
        ApiResult apiResult = snService.searchAndInsert(name, token);
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
