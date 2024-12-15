package org.zjubs.pricecomwebbackend.utils;

import java.util.Random;

public class RandomUtil {
    public static String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 生成6位随机数
        return String.valueOf(code);
    }
}
