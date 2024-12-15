package org.zjubs.pricecomwebbackend.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.zjubs.pricecomwebbackend.query.RespResult;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public RespResult ex(Exception ex) {
        ex.printStackTrace();
        return RespResult.fail("有异常");
    }
}
