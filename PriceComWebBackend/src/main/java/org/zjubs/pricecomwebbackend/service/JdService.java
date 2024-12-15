package org.zjubs.pricecomwebbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zjubs.pricecomwebbackend.entity.Good;
import org.zjubs.pricecomwebbackend.mapper.JdMapper;
import org.zjubs.pricecomwebbackend.query.ApiResult;
import org.zjubs.pricecomwebbackend.utils.JDCrawlerUtil;
import org.zjubs.pricecomwebbackend.utils.JWTUtil;

import java.util.List;
import java.util.Objects;

@Service
public class JdService {
    @Autowired
    private JdMapper jdMapper;

    public ApiResult searchAndInsert(String name) {
        try {
            List<Good> goodList = JDCrawlerUtil.searchAndInsert(name);
            for (Good good: Objects.requireNonNull(goodList)) {
                jdMapper.insetItems(good);
            }
            return ApiResult.success();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ApiResult.fail(e.getMessage());
        }
    }

    public ApiResult getGoodsBySearchingName(String name, String token) {
        try {
            try {
                JWTUtil.verify(token);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return ApiResult.notLogin();
            }
            //List<Good> goodList = jdMapper.queryGoodsBySearchingName(name);
            List<Good> goodList = JDCrawlerUtil.searchAndInsert(name);
            for (Good good: Objects.requireNonNull(goodList)) {
                jdMapper.insetItems(good);
            }
            return ApiResult.success(goodList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ApiResult.fail(e.getMessage());
        }
    }
}
