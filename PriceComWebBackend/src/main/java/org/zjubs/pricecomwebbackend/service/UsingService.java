package org.zjubs.pricecomwebbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zjubs.pricecomwebbackend.entity.Good;
import org.zjubs.pricecomwebbackend.entity.History;
import org.zjubs.pricecomwebbackend.entity.User;
import org.zjubs.pricecomwebbackend.mapper.*;
import org.zjubs.pricecomwebbackend.query.ApiResult;
import org.zjubs.pricecomwebbackend.query.Remainder;
import org.zjubs.pricecomwebbackend.utils.EmailUtil;
import org.zjubs.pricecomwebbackend.utils.JWTUtil;

import java.util.List;

@Service
public class UsingService {
    @Autowired
    private UsingMapper usingMapper;
    @Autowired
    private JdMapper jdMapper;
    @Autowired
    private SnMapper snMapper;
    @Autowired
    private WphMapper wphMapper;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private UserMapper userMapper;

    public ApiResult getAllHistoryByUserId(String token) {
        try {
            try {
                JWTUtil.verify(token);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return ApiResult.notLogin();
            }
            Integer id = JWTUtil.getIdByToken(token);
            List<History> history = usingMapper.getAllHistoryByUserId(id);
            return ApiResult.success(history);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ApiResult.fail(e.getMessage());
        }
    }

    public ApiResult updateHistoryByUserIdAndContent(String token, String content) {
        try {
            try {
                JWTUtil.verify(token);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return ApiResult.notLogin();
            }
            Integer id = JWTUtil.getIdByToken(token);
            History history = usingMapper.queryHistoryByUserIdAndContent(id, content);
            if (history != null) {
                usingMapper.deleteHistoryByUserIdAndContent(id, content);
            }
            usingMapper.insertQueryHistory(id, content);
            return ApiResult.success();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ApiResult.fail(e.getMessage());
        }
    }

    public ApiResult deleteSingleHistoryByUserIdAndContent(String token, String content) {
        try {
            try {
                JWTUtil.verify(token);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return ApiResult.notLogin();
            }
            Integer id = JWTUtil.getIdByToken(token);
            usingMapper.deleteHistoryByUserIdAndContent(id, content);
            return ApiResult.success();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ApiResult.fail(e.getMessage());
        }
    }

    public ApiResult deleteAllHistoryByUserId(String token) {
        try {
            try {
                JWTUtil.verify(token);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return ApiResult.notLogin();
            }
            Integer id = JWTUtil.getIdByToken(token);
            usingMapper.deleteAllHistoryByUserId(id);
            return ApiResult.success();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ApiResult.fail(e.getMessage());
        }
    }

    public ApiResult queryGoodsByDetailUrlAndFrom(String from, String url, String token) {
        try {
            try {
                JWTUtil.verify(token);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return ApiResult.notLogin();
            }
            if ("jd".equals(from)) {
                List<Good> goodList = jdMapper.queryFoodsByDetailUrl(url);
                return ApiResult.success(goodList);
            } else if ("sn".equals(from)) {
                List<Good> goodList = snMapper.queryFoodsByDetailUrl(url);
                return ApiResult.success(goodList);
            } else if ("wph".equals(from)) {
                List<Good> goodList = wphMapper.queryFoodsByDetailUrl(url);
                return ApiResult.success(goodList);
            } else {
                return ApiResult.fail("查询失败");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ApiResult.fail(e.getMessage());
        }
    }

    public ApiResult insertRemainder(Remainder remainder, String token) {
        try {
            try {
                JWTUtil.verify(token);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return ApiResult.notLogin();
            }
            Integer id = JWTUtil.getIdByToken(token);
            String description = remainder.getDescription();
            String img = remainder.getImg();
            String from = remainder.getFrom();
            Double price = remainder.getPrice();
            String detailUrl = remainder.getDetailUrl();
            usingMapper.insertRemainder(id, description, price, img, detailUrl, from);
            return ApiResult.success();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ApiResult.fail(e.getMessage());
        }
    }

    public ApiResult getRemaindersByToken(String token) {
        try {
            try {
                JWTUtil.verify(token);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return ApiResult.notLogin();
            }
            Integer id = JWTUtil.getIdByToken(token);
            List<Remainder> remaindersByUserId = usingMapper.getRemaindersByUserId(id);
            return ApiResult.success(remaindersByUserId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ApiResult.fail(e.getMessage());
        }
    }

    public ApiResult deleteRemainderById(Integer id, String token) {
        try {
            try {
                JWTUtil.verify(token);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return ApiResult.notLogin();
            }
            usingMapper.deleteRemainderById(id);
            return ApiResult.success();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ApiResult.fail(e.getMessage());
        }
    }

    public ApiResult sendRemindEmail(String token, Integer id) {
        try {
            try {
                JWTUtil.verify(token);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return ApiResult.notLogin();
            }
            String username = JWTUtil.getUsernameByToken(token);
            User user = userMapper.queryUserByUsername(username);
            if (user == null) {
                return ApiResult.fail("用户不存在");
            }
            String email = user.getEmail();
            Remainder remind = usingMapper.getReminderById(id);
            String description = remind.getDescription();
            Double oldPrice = remind.getPrice();
            Double newPrice = oldPrice * 0.9;
            String url = remind.getDetailUrl();
            emailUtil.sendRemainderEmail(email, description, oldPrice, newPrice, url);
            return ApiResult.success();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ApiResult.fail(e.getMessage());
        }
    }
}
