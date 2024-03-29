package com.personal.pdfsummarizer.common;

import com.personal.pdfsummarizer.common.constants.CommonError;
import com.personal.pdfsummarizer.common.models.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommonUtils {
    // Random value added to password before hashing
    private static final int SALT_LENGTH = 16;
    // Length of the generated hash output in bytes
    private static final int HASH_LENGTH = 32;
    // Controls the number of threads and compute lanes used for hashing
    private static final int PARALLELISM = 1;
    // Amount of memory to use in kb during the hashing process
    private static final int MEMORY = 65536;
    // Number of iterations to perform
    private static final int ITERATIONS = 4;


    // * Helper function that is used to handle errors
    public static Throwable baseExceptionHandler(Throwable e) {
        if (e instanceof BaseException) {
            return e;
        }
        return new BaseException(CommonError.INTERNAL_SERVER_ERROR.getCode(), CommonError.INTERNAL_SERVER_ERROR.getBusinessCode(), CommonError.INTERNAL_SERVER_ERROR.getDescription());
    }

    // * Helper function that is used to hash passwords
    public static String pwHash(String password) {
        Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(
                // * Hashing parameters for basic development purposes
                SALT_LENGTH, HASH_LENGTH, PARALLELISM, MEMORY, ITERATIONS);
        return passwordEncoder.encode(password);
    }

    // * Helper function that is used to verify passwords
    public static boolean pwVerify(String password, String hash) {
        Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(
                // * Hashing parameters for basic development purposes
                SALT_LENGTH, HASH_LENGTH, PARALLELISM, MEMORY, ITERATIONS);
        return passwordEncoder.matches(password, hash);
    }
}
