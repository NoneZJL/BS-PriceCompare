package org.zjubs.pricecomwebbackend.filter;

import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.zjubs.pricecomwebbackend.query.RespResult;
import org.zjubs.pricecomwebbackend.utils.JWTUtil;

import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String url = request.getRequestURL().toString();
        String method = request.getMethod();

        // 如果是预检请求（OPTIONS），直接放行
        if ("OPTIONS".equalsIgnoreCase(method)) {
            response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Max-Age", "3600");
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (url.contains("login") || url.contains("register")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String jwt = request.getHeader("Authorization");
        if (!StringUtils.hasLength(jwt)) {
            log.info("token为空");
            RespResult err = RespResult.fail("NOT_LOGIN");
            String notLogin = JSONObject.toJSONString(err);
            response.getWriter().write(notLogin);
            return;
        }

        try {
            JWTUtil.verify(jwt);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("解析令牌失败");
            RespResult err = RespResult.fail("NOT_LOGIN");
            String notLogin = JSONObject.toJSONString(err);
            response.getWriter().write(notLogin);
            return;
        }

        log.info("令牌合法，放行");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}