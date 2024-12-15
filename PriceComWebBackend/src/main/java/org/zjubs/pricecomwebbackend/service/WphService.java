package org.zjubs.pricecomwebbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zjubs.pricecomwebbackend.entity.Good;
import org.zjubs.pricecomwebbackend.mapper.WphMapper;
import org.zjubs.pricecomwebbackend.query.ApiResult;
import org.zjubs.pricecomwebbackend.utils.JWTUtil;
import org.zjubs.pricecomwebbackend.utils.WphCrawlerUtil;

import java.util.List;
import java.util.Objects;

@Service
public class WphService {
    @Autowired
    private WphMapper wphMapper;

    public ApiResult searchAndInsert(String name, String token) {
        try {
            try {
                JWTUtil.verify(token);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return ApiResult.notLogin();
            }
            List<Good> goodList = WphCrawlerUtil.search(name);
            for (Good good: Objects.requireNonNull(goodList)) {
                wphMapper.insetItems(good);
            }
            return ApiResult.success(goodList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ApiResult.fail(e.getMessage());
        }
    }
}
