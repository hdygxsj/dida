/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hdygxsj.dida.enums;

import lombok.Getter;

@Getter
public enum ApiStatus {
    SUCCESS(0,"成功"),
    INTERNAL_SERVER_ERROR_ARGS(10000,  "服务端异常"),
    LOGIN_FAILED(10001,"账号或密码错误"),
    INSUFFICIENT_PERMISSION(10002,"权限不足"),
    NO_AUTH(10003,"未授权的用户"),
    NO_REQUEST_RIGHT(10004,"访问权限不足")
    ;

    ApiStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;

    private String message;
}
