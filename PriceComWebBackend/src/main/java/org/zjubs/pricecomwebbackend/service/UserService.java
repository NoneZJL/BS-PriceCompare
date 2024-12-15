package org.zjubs.pricecomwebbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zjubs.pricecomwebbackend.entity.User;
import org.zjubs.pricecomwebbackend.query.ApiResult;
import org.zjubs.pricecomwebbackend.query.LoginByEmailAnswer;
import org.zjubs.pricecomwebbackend.query.ModifyPassword;
import org.zjubs.pricecomwebbackend.utils.EmailUtil;
import org.zjubs.pricecomwebbackend.utils.EncryptSha256Util;
import org.zjubs.pricecomwebbackend.utils.JWTUtil;
import org.zjubs.pricecomwebbackend.utils.RandomUtil;

@Service
public class UserService {
    @Autowired
    private org.zjubs.pricecomwebbackend.mapper.UserMapper userMapper;
    @Autowired
    private EmailUtil emailUtils;

    public ApiResult queryUserByUsername(String username) {
        User user = userMapper.queryUserByUsername(username);
        return new ApiResult(true, user);
    }

    public ApiResult login(String username, String password) {
        try {
            User user1 = userMapper.queryUserByUsername(username);
            if (user1 == null) {
                return ApiResult.fail("该用户名不存在");
            }
            String encodePassword = EncryptSha256Util.getSha256Str(password);
            User user = userMapper.queryUserByUsernameAndPassword(username, encodePassword);
            if (user != null) {
                String token = JWTUtil.getTokenByIdAndUsername(user.getId(), user.getUsername());
                return ApiResult.success(token);
            } else {
                return new ApiResult(false, "用户名与密码不匹配");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ApiResult.fail(e.getMessage());
        }
    }

    public ApiResult loginByEmail(String email, String password) {
        try {
            User findUser = userMapper.queryUserByEmail(email);
            if (findUser == null) {
                return ApiResult.fail("改邮箱不存在对应的用户");
            }
            String encodePassword = EncryptSha256Util.getSha256Str(password);
            User user = userMapper.queryUserByEmailAndPassword(email, encodePassword);
            if (user == null) {
                return ApiResult.fail("邮箱与密码不匹配");
            } else {
                String token = JWTUtil.getTokenByIdAndUsername(user.getId(), user.getUsername());
                return ApiResult.success(new LoginByEmailAnswer(token, user.getUsername()));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ApiResult.fail(e.getMessage());
        }
    }


    public ApiResult sendEmailJustifyCode(String email) {
        try {
            String verificationCode = RandomUtil.generateVerificationCode();
            emailUtils.sendAuthCodeEmail(email, verificationCode);
            String sha256Str = EncryptSha256Util.getSha256Str(verificationCode);
            String tokenByEmailAndJustifyCode = JWTUtil.getTokenByEmailAndJustifyCode(email, sha256Str);
            return ApiResult.success(tokenByEmailAndJustifyCode);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ApiResult.fail(e.getMessage());
        }
    }

    public ApiResult userRegister(String username, String password, String email, String emailCode, String lastEmailCode) {
        try {
            try {
                JWTUtil.verify(lastEmailCode);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return ApiResult.fail("未获取验证码或者验证码到期，请重新获取");
            }
            String newEmailCode = EncryptSha256Util.getSha256Str(emailCode);
            String justifyCodeByToken = JWTUtil.getJustifyCodeByToken(lastEmailCode);
            if (!newEmailCode.equals(justifyCodeByToken)) {
                return ApiResult.fail("验证码错误");
            }
            User user1 = userMapper.queryUserByUsername(username);
            if (user1 != null) {
                return ApiResult.fail("用户名已存在");
            }
            User user2 = userMapper.queryUserByEmail(email);
            if (user2 != null) {
                return ApiResult.fail("该邮箱已被注册");
            }
            String emailByToken = JWTUtil.getEmailByToken(lastEmailCode);
            if (!emailByToken.equals(email)) {
                return ApiResult.fail("请确保注册邮箱和获取验证码的邮箱是同一个邮箱");
            }
            String encodePassword = EncryptSha256Util.getSha256Str(password);
            userMapper.userRegister(username, encodePassword, email);
            return ApiResult.success();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ApiResult.fail(e.getMessage());
        }
    }

    public ApiResult modifyPasswordByEmail(ModifyPassword modifyPassword) {
        try {
            String email = modifyPassword.getEmail();
            String password = modifyPassword.getPassword();
            String newEmailCode = modifyPassword.getNewEmailCode();
            String lastEmailCode = modifyPassword.getLastEmailCode();
            try {
                JWTUtil.verify(lastEmailCode);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return ApiResult.fail("未获取验证码或者验证码到期，请重新获取");
            }
            String emailCode = EncryptSha256Util.getSha256Str(newEmailCode);
            String justifyCodeByToken = JWTUtil.getJustifyCodeByToken(lastEmailCode);
            if (!emailCode.equals(justifyCodeByToken)) {
                return ApiResult.fail("验证码错误");
            }
            String emailByToken = JWTUtil.getEmailByToken(lastEmailCode);
            if (!emailByToken.equals(email)) {
                return ApiResult.fail("请确保注册邮箱和获取验证码的邮箱是同一个邮箱");
            }
            String encodePassword = EncryptSha256Util.getSha256Str(password);
            userMapper.modifyPasswordByEmail(encodePassword, email);
            return ApiResult.success();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ApiResult.fail(e.getMessage());
        }
    }

    public ApiResult sendEmailCodeInForgetPassword(String email) {
        try {
            User user = userMapper.queryUserByEmail(email);
            if (user == null) {
                return ApiResult.fail("输入的邮箱不存在对应的用户");
            }
            String verificationCode = RandomUtil.generateVerificationCode();
            emailUtils.sendAuthCodeEmail(email, verificationCode);
            String sha256Str = EncryptSha256Util.getSha256Str(verificationCode);
            String tokenByEmailAndJustifyCode = JWTUtil.getTokenByEmailAndJustifyCode(email, sha256Str);
            return ApiResult.success(tokenByEmailAndJustifyCode);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ApiResult.fail(e.getMessage());
        }
    }
}
