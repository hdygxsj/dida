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

package com.hdygxsj.dida.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdygxsj.dida.api.authentication.permission.OpObjType;
import com.hdygxsj.dida.api.authentication.permission.OpRight;
import com.hdygxsj.dida.api.authentication.permission.Permission;
import com.hdygxsj.dida.api.service.entity.RoleDO;
import com.hdygxsj.dida.api.service.entity.UserDO;
import com.hdygxsj.dida.api.service.entity.UserRoleRelDO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    List<UserDO> listAll(String username);

    boolean checkUser(String username,String password);

    UserDO get(String username);

    boolean exist(String username);

    void create(UserDO userDO);

    UserDO create(UserDO userDO, List<UserRoleRelDO> userRoleRelList);

    @Permission(objType = OpObjType.ROLE, opRight = {OpRight.WRITE})
    @Transactional(rollbackFor = Exception.class)
    void updateRoles(UserDO userDO, List<UserRoleRelDO> userRoleRelList);

    void createBySystem(UserDO userDO);

    void addRoles(String username, List<String> roles);

    List<RoleDO> getRoles(String username);

    Page<UserDO> page(String username, int pageNum, int pageSize);

    void deleteUser(String username);

    void update(UserDO userDO);
}
