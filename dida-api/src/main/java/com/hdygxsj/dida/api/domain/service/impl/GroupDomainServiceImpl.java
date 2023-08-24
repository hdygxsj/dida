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

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdygxsj.dida.api.authentication.permission.OpObjType;
import com.hdygxsj.dida.api.authentication.permission.OpRight;
import com.hdygxsj.dida.api.authentication.permission.Permission;
import com.hdygxsj.dida.api.domain.entity.GroupDO;
import com.hdygxsj.dida.api.domain.entity.UserDO;
import com.hdygxsj.dida.api.domain.entity.UserGroupRelDO;
import com.hdygxsj.dida.api.domain.service.GroupDomainService;
import com.hdygxsj.dida.api.mapper.GroupMapper;
import com.hdygxsj.dida.api.mapper.UserGroupRelMapper;
import com.hdygxsj.dida.exceptions.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupDomainServiceImpl implements GroupDomainService {


    @Autowired
    private GroupMapper groupMapper;


    @Autowired
    private UserGroupRelMapper userGroupRelMapper;

    @Override
    public void add(GroupDO groupDO) {
        groupMapper.insert(groupDO);
    }

    @Override
    public GroupDO get(String code) {
        return groupMapper.selectById(code);
    }

    @Override
    public void addUser(String groupCode, String username) {
        UserGroupRelDO userGroupRelDO = new UserGroupRelDO();
        userGroupRelDO.setGroupCode(groupCode);
        userGroupRelDO.setUsername(username);
        userGroupRelMapper.insert(userGroupRelDO);
    }

    @Override
    public Page<GroupDO> page(int pageNum, int pageSize, String name, String code) {
        Page<GroupDO> page = new Page<>(pageNum, pageSize);
        QueryWrapper<GroupDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        queryWrapper.like(StrUtil.isNotBlank(code), "code", code);
        queryWrapper.orderByDesc("descp");
        return groupMapper.selectPage(page, queryWrapper);
    }

    @Override
    public List<GroupDO> listByUser(UserDO userDO) {
        if (userDO.isSuperUser()) {
            return groupMapper.selectList(null);
        }
        QueryWrapper<UserGroupRelDO> relQuery = new QueryWrapper<>();
        List<UserGroupRelDO> userGroupRelDOS = userGroupRelMapper.selectList(relQuery);
        QueryWrapper<GroupDO> groupQuery = new QueryWrapper<>();
        List<String> groupCode = userGroupRelDOS.stream().map(UserGroupRelDO::getGroupCode).collect(Collectors.toList());
        groupQuery.in("group_code", groupCode);
        return groupMapper.selectList(groupQuery);
    }

    @Override
    public boolean hasGroup(UserDO userDO, String groupCode) {
        QueryWrapper<GroupDO> groupQuery = new QueryWrapper<>();
        groupQuery.eq("code", groupCode);
        Assert.isTrue(groupMapper.exists(groupQuery), "组不存在");
        if (userDO.isSuperUser()) {
            return true;
        }
        QueryWrapper<UserGroupRelDO> relQuery = new QueryWrapper<>();
        relQuery.eq("group_code", groupCode);
        relQuery.eq("username", userDO.getUsername());
        return userGroupRelMapper.exists(relQuery);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Permission(objType = OpObjType.GROUP, opRight = OpRight.DELETE)
    public void deleteByCode(String code) {
        groupMapper.deleteById(code);
        QueryWrapper<UserGroupRelDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", code);
        userGroupRelMapper.delete(queryWrapper);
    }

    @Override
    public Page<UserGroupRelDO> pageGroupMember(String code, int pageNum, int pageSize) {
        Page<UserGroupRelDO> page = new Page<>(pageNum, pageSize);
        QueryWrapper<UserGroupRelDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_code", code);
        return userGroupRelMapper.selectPage(page, queryWrapper);
    }

}
