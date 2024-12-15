package org.zjubs.pricecomwebbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zjubs.pricecomwebbackend.mapper.HomeMapper;
import org.zjubs.pricecomwebbackend.query.ApiResult;
import org.zjubs.pricecomwebbackend.query.UpdateUserInfo;
import org.zjubs.pricecomwebbackend.query.UserInfo;
import org.zjubs.pricecomwebbackend.utils.JWTUtil;

@Service
public class HomeService {
    @Autowired
    HomeMapper homeMapper;

    public ApiResult queryUserInfoByToken(String token){
        try {
            try {
                JWTUtil.verify(token);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return ApiResult.notLogin();
            }
            String username = JWTUtil.getUsernameByToken(token);
            UserInfo userInfo = homeMapper.queryUserInfoByUsername(username);
            return ApiResult.success(userInfo);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ApiResult.fail(e.getMessage());
        }
    }

    public ApiResult updateUserInfo(String token, UpdateUserInfo updateUserInfo) {
        try {
            try {
                JWTUtil.verify(token);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return ApiResult.notLogin();
            }
            String address = updateUserInfo.getAddress();
            String phone = updateUserInfo.getPhone();
            String username = JWTUtil.getUsernameByToken(token);
            homeMapper.updateUserInfo(phone, address, username);
            return ApiResult.success();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ApiResult.fail(e.getMessage());
        }
    }
}
