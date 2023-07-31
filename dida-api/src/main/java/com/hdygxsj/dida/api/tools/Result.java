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

package com.hdygxsj.dida.api.tools;


import com.hdygxsj.dida.api.enums.ApiStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.text.MessageFormat;


@Getter
@Setter
@ToString
public class Result<T> {

    /**
     * status
     */
    private Integer code;

    private String message;


    private T data;

    public Result() {
    }

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(ApiStatus status) {
        if (status != null) {
            this.code = status.getCode();
            this.message = status.getMessage();
        }
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ApiStatus.SUCCESS.getCode(), ApiStatus.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static Result error(int code, String message) {
        Result result = new Result();
        result.code = code;
        result.message = message;
        return result;
    }

    public boolean isSuccess() {
        return this.isStatus(ApiStatus.SUCCESS);
    }

    public boolean isFailed() {
        return !this.isSuccess();
    }

    public boolean isStatus(ApiStatus status) {
        return this.code != null && this.code.equals(status.getCode());
    }


    public static <T> Result<T> error(ApiStatus status) {
        return new Result<>(status);
    }


    public static <T> Result<T> errorWithArgs(ApiStatus status, Object... args) {
        return new Result<>(status.getCode(), MessageFormat.format(status.getMessage(), args));
    }

    public Boolean checkResult() {
        return this.code == ApiStatus.SUCCESS.getCode();
    }
}
