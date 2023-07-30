package com.hdygxsj.dida.api.exceptions;

import com.hdygxsj.dida.api.enums.ApiStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiException {

    ApiStatus value();
}
