package org.zjubs.pricecomwebbackend.query;

import com.auth0.jwt.interfaces.Payload;

public class ApiResult {

    /* whether the operation is successfully completed */
    public boolean ok;
    /* information message returned by Api */
    public String message;
    /* other information returned by the interface */
    public Object payload;

    public ApiResult(boolean ok, Object payload) {
        this.ok = ok;
        this.payload = payload;
    }

    public ApiResult(boolean ok, String message) {
        this.ok = ok;
        this.message = message;
    }

    public ApiResult(boolean ok, String message, Object payload) {
        this.ok = ok;
        this.message = message;
        this.payload = payload;
    }

    public static ApiResult success() {
        return new ApiResult(true, null, null);
    }

    public static ApiResult success(Object payload) {
        return new ApiResult(true, null, payload);
    }

    public static ApiResult fail(String err) {
        return new ApiResult(false, err, null);
    }

    public static ApiResult notLogin() {
        return ApiResult.fail("JWTerror");
    }
}

