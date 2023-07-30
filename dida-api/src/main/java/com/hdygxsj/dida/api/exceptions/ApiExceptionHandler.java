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
            return Result.errorWithArgs(ApiStatus.INTERNAL_SERVER_ERROR_ARGS,e.getMessage());
        }
        ApiStatus apiStatus = ma.value();
        log.error(apiStatus.getMessage(),e);
        return Result.error(apiStatus);
    }
}
