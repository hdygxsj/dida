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

import com.hdygxsj.dida.api.authentication.base.UserAuthenticationContextHolder;
import com.hdygxsj.dida.api.domain.entity.RoleDO;
import com.hdygxsj.dida.api.domain.entity.RoleObjectRelDO;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class RolePermissionCache {

    public Map<String, RoleObjectRelDO> roleCache = new ConcurrentHashMap<>();


    public boolean checkPermission(OpObjType objType, OpRight[] permission) {
        if (permission == null || objType == null) {
            return true;
        }
        Set<RoleDO> roles = UserAuthenticationContextHolder.getContext().getRoles();
        Set<String> roleStrs = roles.stream().map(RoleDO::getCode).collect(Collectors.toSet());
        if (roleStrs.contains("super admin")) {
            return true;
        }
        Long hasPermission = roleCache.values().stream().filter(e -> {
            return roleStrs.contains(e.getRoleCode());
        }).map(RoleObjectRelDO::getPermission).reduce((i1, i2) -> i1 | i2).get();
        for (OpRight opRight : permission) {
            int bitInt = opRight.bitInt;
            if ((bitInt & hasPermission) == 0) {
                return false;
            }
        }
        return true;
    }
}
