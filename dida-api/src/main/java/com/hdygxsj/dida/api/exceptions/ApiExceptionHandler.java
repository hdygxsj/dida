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

package com.hdygxsj.dida.api.exceptions;

import com.hdygxsj.dida.api.enums.ApiStatus;
import com.hdygxsj.dida.api.tools.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

@Slf4j
@ResponseBody
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public Result exception(ServiceException e, HandlerMethod hm){
        log.error("serviceException: ",e);
        return Result.error(e.getCode(),e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(Exception e,HandlerMethod hm){
        ApiException ma = hm.getMethodAnnotation(ApiException.class);
        if(ma == null){
            log.error(e.getMessage(),e);
            return Result.error(ApiStatus.INTERNAL_SERVER_ERROR_ARGS);
        }
        ApiStatus apiStatus = ma.value();
        log.error(apiStatus.getMessage(),e);
        return Result.error(apiStatus);
    }
}
