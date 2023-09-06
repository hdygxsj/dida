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

package com.hdygxsj.dida.api.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hdygxsj.dida.api.authentication.base.UserAuthenticationContextHolder;
import com.hdygxsj.dida.api.service.entity.NamespaceDO;
import com.hdygxsj.dida.api.service.entity.UserDO;
import com.hdygxsj.dida.api.service.GroupService;
import com.hdygxsj.dida.api.service.NamespaceService;
import com.hdygxsj.dida.api.mapper.NamespaceMapper;
import com.hdygxsj.dida.exceptions.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NamespaceServiceImpl implements NamespaceService {

    @Autowired
    private NamespaceMapper namespaceMapper;

    @Autowired
    private GroupService groupService;

    @Override
    public void add(NamespaceDO namespaceDO) {
        UserDO opUser = UserAuthenticationContextHolder.getContext().getOpUser();
        Assert.isTrue(groupService.hasGroup(opUser, namespaceDO.getGroupCode()), "没有权限操作");
        Assert.isTrue(exist(namespaceDO.getCode()),"命名空间已被占用");
        namespaceMapper.insert(namespaceDO);
    }

    @Override
    public boolean exist(String code){
        return get(code) == null;
    }

    @Override
    public NamespaceDO get(String code){
        return namespaceMapper.selectById(code);
    }

    @Override
    public List<NamespaceDO> list(String groupCode) {
        NamespaceDO namespaceDO = new NamespaceDO();
        namespaceDO.setGroupCode(groupCode);
        QueryWrapper<NamespaceDO> namespaceDOQueryWrapper = new QueryWrapper<>(namespaceDO);
        return namespaceMapper.selectList(namespaceDOQueryWrapper);
    }

    @Override
    public void delete(NamespaceDO namespaceDO) {
        namespaceMapper.deleteById(namespaceDO);
    }
}
