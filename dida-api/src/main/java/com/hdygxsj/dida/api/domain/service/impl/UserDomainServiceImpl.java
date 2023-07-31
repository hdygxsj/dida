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
import com.hdygxsj.dida.api.domain.entity.UserDO;
import com.hdygxsj.dida.api.domain.service.UserDomainService;
import com.hdygxsj.dida.api.mapper.UserMapper;
import com.hdygxsj.dida.exceptions.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDomainServiceImpl implements UserDomainService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<UserDO> listAll() {
        return userMapper.selectList(null);
    }

    @Override
    public boolean checkUser(String username, String password) {
        UserDO userDO = new UserDO();
        userDO.setUsername(username);
        userDO.setPassword(password);
        return userMapper.selectOne(new QueryWrapper<>(userDO)) != null;
    }

    @Override
    public UserDO get(String username){
        return userMapper.selectById(username);
    }

    @Override
    public boolean exist(String username){
        return get(username)!=null;
    }


    @Override
    public void create(UserDO userDO) {
        Assert.isTrue(exist(userDO.getUsername()),"用户已存在");
        userMapper.insert(userDO);
    }
}

