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

package com.hdygxsj.dida.api.authentication.permission;


import com.hdygxsj.dida.api.authentication.base.CheckPermissionException;
import com.hdygxsj.dida.enums.ApiStatus;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class PermissionAspect {

    @Autowired
    private RolePermissionCache rolePermissionCache;

    @Around("@annotation(com.hdygxsj.dida.api.authentication.permission.Permission)")
    public Object permissionCheck(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Map<String, Object> params = getParams(proceedingJoinPoint);
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Permission checkPermission = signature.getMethod().getAnnotation(Permission.class);
        doCheck(checkPermission, params);
        return proceedingJoinPoint.proceed();
    }

    private void doCheck(Permission checkPermission, Map<String, Object> params) {
        OpObjType opObjType = checkPermission.objType();
        OpRight[] opRights = checkPermission.opRight();
        boolean hasPermission = rolePermissionCache.checkPermission(opObjType, opRights);
        if (!hasPermission) {
            throw new CheckPermissionException(ApiStatus.INSUFFICIENT_PERMISSION);
        }
    }

    @Around("@annotation(com.hdygxsj.dida.api.authentication.permission.Permissions)")
    public Object permissionsCheck(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Map<String, Object> params = getParams(proceedingJoinPoint);
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Permissions permissions = signature.getMethod().getAnnotation(Permissions.class);
        if(permissions !=null){
            for (Permission permission : permissions.value()) {
                doCheck(permission,params);
            }
        }
        return proceedingJoinPoint.proceed();
    }

    private static Map<String, Object> getParams(ProceedingJoinPoint proceedingJoinPoint) {
        Map<String, Object> map = new HashMap<>();
        Object[] values = proceedingJoinPoint.getArgs();
        String[] names = ((CodeSignature) proceedingJoinPoint.getSignature()).getParameterNames();
        for (int i = 0; i < names.length; i++) {
            map.put(names[i], values[i]);
        }
        return map;
    }


    public boolean checkBelong(String belong, Object objName) {
        //TODO
        return true;
    }

}
