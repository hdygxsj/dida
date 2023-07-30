package com.hdygxsj.dida.api.enums;

import lombok.Getter;

@Getter
public enum ApiStatus {
    SUCCESS(0,"成功"),
    INTERNAL_SERVER_ERROR_ARGS(10000,  "服务端异常: {0}");

    ApiStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;

    private String message;
}
