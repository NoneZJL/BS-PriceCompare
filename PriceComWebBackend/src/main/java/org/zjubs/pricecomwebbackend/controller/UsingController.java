package org.zjubs.pricecomwebbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zjubs.pricecomwebbackend.query.ApiResult;
import org.zjubs.pricecomwebbackend.query.GoodDetail;
import org.zjubs.pricecomwebbackend.query.Remainder;
import org.zjubs.pricecomwebbackend.query.RespResult;
import org.zjubs.pricecomwebbackend.service.UsingService;
import org.zjubs.pricecomwebbackend.utils.JWTUtil;

@RestController
public class UsingController {
    @Autowired
    private UsingService usingService;

    @GetMapping("/getAllHistory")
    public RespResult getAllHistoryByUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        ApiResult apiResult = usingService.getAllHistoryByUserId(token);
        if (apiResult.ok) {
            return RespResult.success(apiResult.payload);
        } else {
            if (JWTUtil.checkNotLogin(apiResult)) {
                return RespResult.notLogin();
            }
            return RespResult.fail(apiResult.message);
        }
    }

    @GetMapping("/queryGood")
    public RespResult queryGood(@RequestParam String name, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        ApiResult apiResult = usingService.updateHistoryByUserIdAndContent(token, name);
        if (apiResult.ok) {
            return RespResult.success();
        } else {
            if (JWTUtil.checkNotLogin(apiResult)) {
                return RespResult.notLogin();
            }
            return RespResult.fail(apiResult.message);
        }
    }

    @GetMapping("/deleteSingleHistory")
    public RespResult deleteSingleHistory(@RequestParam String name, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        ApiResult apiResult = usingService.deleteSingleHistoryByUserIdAndContent(token, name);
        if (apiResult.ok) {
            return RespResult.success();
        } else {
            if (JWTUtil.checkNotLogin(apiResult)) {
                return RespResult.notLogin();
            }
            return RespResult.fail(apiResult.message);
        }
    }

    @GetMapping("/deleteAllHistory")
    public RespResult deleteAllHistory(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        ApiResult apiResult = usingService.deleteAllHistoryByUserId(token);
        if (apiResult.ok) {
            return RespResult.success();
        } else {
            if (JWTUtil.checkNotLogin(apiResult)) {
                return RespResult.notLogin();
            }
            return RespResult.fail(apiResult.message);
        }
    }

    @PostMapping("/getDetail")
    public RespResult getGoodDetail(@RequestBody GoodDetail detail, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String from = detail.getFrom();
        String url = detail.getUrl();
        ApiResult apiResult = usingService.queryGoodsByDetailUrlAndFrom(from, url, token);
        if (apiResult.ok) {
            return RespResult.success(apiResult.payload);
        } else {
            if (JWTUtil.checkNotLogin(apiResult)) {
                return RespResult.notLogin();
            }
            return RespResult.fail(apiResult.message);
        }
    }

    @PostMapping("/insertRemainder")
    public RespResult insertRemainder(@RequestBody Remainder remainder, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        ApiResult apiResult = usingService.insertRemainder(remainder, token);
        if (apiResult.ok) {
            return RespResult.success();
        } else {
            if (JWTUtil.checkNotLogin(apiResult)) {
                return RespResult.notLogin();
            }
            return RespResult.fail(apiResult.message);
        }
    }

    @GetMapping("/getRemainsers")
    public RespResult getRemainders(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        ApiResult apiResult = usingService.getRemaindersByToken(token);
        if (apiResult.ok) {
            return RespResult.success(apiResult.payload);
        } else {
            if (JWTUtil.checkNotLogin(apiResult)) {
                return RespResult.notLogin();
            }
            return RespResult.fail(apiResult.message);
        }
    }

    @GetMapping("/deleteRemainder")
    public RespResult deleteRemainder(@RequestParam Integer id, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        ApiResult apiResult = usingService.deleteRemainderById(id, token);
        if (apiResult.ok) {
            return RespResult.success();
        } else {
            if (JWTUtil.checkNotLogin(apiResult)) {
                return RespResult.notLogin();
            }
            return RespResult.fail(apiResult.message);
        }
    }

    @GetMapping("/sendRemindEmail")
    public RespResult sendRemindEmail(@RequestParam Integer id, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        ApiResult apiResult = usingService.sendRemindEmail(token, id);
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
