package com.github.client.api.network;

/**
 * Created by Sam on 4/18/17.
 */
public enum ErrorCode {

    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NO_CONNECTION(0),
    UNKNOWN(999);

    int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public static ErrorCode parse(int code) {
        for (int i = 0; i < ErrorCode.values().length; i++) {
            if (ErrorCode.values()[i].code == code) {
                return ErrorCode.values()[i];
            }
        }
        return UNKNOWN;
    }

}
