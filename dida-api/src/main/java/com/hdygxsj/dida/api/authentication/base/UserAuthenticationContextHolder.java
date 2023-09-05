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

package com.hdygxsj.dida.api.authentication.base;

import com.hdygxsj.dida.api.service.entity.RoleDO;
import com.hdygxsj.dida.api.service.entity.UserDO;
import com.hdygxsj.dida.enums.ApiStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UserAuthenticationContextHolder {

    private UserAuthenticationContextHolder() {

    }

    private ThreadLocal<UserDO> opUser = new ThreadLocal<>();
    private Map<String, Set<RoleDO>> map = new ConcurrentHashMap<>();

    private static volatile UserAuthenticationContextHolder context = null;

    public static UserAuthenticationContextHolder getContext() {
        if (context == null) {
            synchronized (UserAuthenticationContextHolder.class) {
                if (context == null) {
                    context = new UserAuthenticationContextHolder();
                }
            }
        }
        return context;
    }

    public Set<RoleDO> getRoles() {
        UserDO opUser = getOpUser();
        if (opUser == null) {
            throw new CheckPermissionException(ApiStatus.NO_AUTH);
        }
        return getRoles(opUser.getUsername());
    }

    public Set<RoleDO> getRoles(String username) {
        return map.get(username);
    }

    public void addRole(String username, RoleDO roleDO) {
        Set<RoleDO> roleDOS = map.computeIfAbsent(username, (e) -> new HashSet<>());
        roleDOS.add(roleDO);
    }

    public void addRole(String username, List<RoleDO> roles) {
        Set<RoleDO> roleDOS = map.computeIfAbsent(username, (e) -> new HashSet<>());
        roleDOS.addAll(roles);
    }

    public void setOpUser(UserDO userDO) {
        opUser.set(userDO);
    }

    public UserDO getOpUser() {
        return opUser.get();
    }

    public void removeOpUser() {
        opUser.remove();
    }

}
