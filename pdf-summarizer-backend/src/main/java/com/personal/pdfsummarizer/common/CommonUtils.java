package com.personal.pdfsummarizer.common;

import com.personal.pdfsummarizer.common.constants.CommonError;
import com.personal.pdfsummarizer.common.models.BaseException;

public class CommonUtils {
    // * Helper function that is used to handle errors
    public static Throwable baseExceptionHandler(Throwable e) {
        if (e instanceof BaseException) {
            return e;
        }
        return new BaseException(CommonError.INTERNAL_SERVER_ERROR.getCode(), CommonError.INTERNAL_SERVER_ERROR.getDescription());
    }

    // * Helper function that is used to hash passwords
    public static String pwHash(String password) {
        return password;
    }
}
