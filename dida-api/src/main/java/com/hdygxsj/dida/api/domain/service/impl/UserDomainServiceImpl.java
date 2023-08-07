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

package com.hdygxsj.dida.api.domain.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hdygxsj.dida.api.authentication.permission.OpObjType;
import com.hdygxsj.dida.api.authentication.permission.OpRight;
import com.hdygxsj.dida.api.authentication.permission.Permission;
import com.hdygxsj.dida.api.domain.entity.GroupDO;
import com.hdygxsj.dida.api.domain.entity.RoleDO;
import com.hdygxsj.dida.api.domain.entity.UserDO;
import com.hdygxsj.dida.api.domain.entity.UserRoleRelDO;
import com.hdygxsj.dida.api.domain.service.GroupDomainService;
import com.hdygxsj.dida.api.domain.service.UserDomainService;
import com.hdygxsj.dida.api.mapper.GroupMapper;
import com.hdygxsj.dida.api.mapper.UserMapper;
import com.hdygxsj.dida.api.mapper.UserRoleRelMapper;
import com.hdygxsj.dida.exceptions.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDomainServiceImpl implements UserDomainService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleRelMapper userRoleRelMapper;



    @Override
    public List<UserDO> listAll() {
        return userMapper.selectList(null);
    }

    @Override
    public boolean checkUser(String username, String password) {
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        queryWrapper.eq("password", password);
        queryWrapper.eq("type", 1);
        return userMapper.selectOne(queryWrapper) != null;
    }

    @Override
    public UserDO get(String username) {
        return userMapper.selectById(username);
    }

    @Override
    public boolean exist(String username) {
        return get(username) != null;
    }


    @Override
    @Permission(objType = OpObjType.USER, opRight = {OpRight.WRITE})
    public void create(UserDO userDO) {
        Assert.isTrue(!exist(userDO.getUsername()), "用户已存在");
        userMapper.insert(userDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Permission(objType = OpObjType.USER_ROLE, opRight = OpRight.WRITE)
    public void addRoles(String username, List<String> roles) {
        QueryWrapper<UserRoleRelDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        userRoleRelMapper.delete(queryWrapper);
        roles.forEach(e -> {
            UserRoleRelDO userRoleRelDO = new UserRoleRelDO();
            userRoleRelDO.setUsername(username);
            userRoleRelDO.setRoleCode(e);
            userRoleRelMapper.insert(userRoleRelDO);
        });
    }

    @Override
    public List<RoleDO> getRoles(String username) {
        QueryWrapper<UserRoleRelDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        List<UserRoleRelDO> userRoleRelList = userRoleRelMapper.selectList(queryWrapper);
        UserDO userDO = get(username);
        List<RoleDO> roleList = new ArrayList<>();

        for (UserRoleRelDO userRoleRelDO : userRoleRelList) {
            RoleDO roleDO = new RoleDO();
            roleDO.setCode(userRoleRelDO.getRoleCode());
        }
        boolean superUser = userDO.isSuperUser();
        if (superUser) {
            RoleDO roleDO = new RoleDO();
            roleDO.setCode("super admin");
            roleList.add(roleDO);
        }
        return roleList;
    }


}

