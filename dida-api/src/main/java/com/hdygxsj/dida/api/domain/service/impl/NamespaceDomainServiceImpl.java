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
import com.hdygxsj.dida.api.authentication.base.UserAuthenticationContextHolder;
import com.hdygxsj.dida.api.domain.entity.NamespaceDO;
import com.hdygxsj.dida.api.domain.entity.UserDO;
import com.hdygxsj.dida.api.domain.service.GroupDomainService;
import com.hdygxsj.dida.api.domain.service.NamespaceDomainService;
import com.hdygxsj.dida.api.mapper.NamespaceMapper;
import com.hdygxsj.dida.exceptions.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NamespaceDomainServiceImpl implements NamespaceDomainService {

    @Autowired
    private NamespaceMapper namespaceMapper;

    @Autowired
    private GroupDomainService groupDomainService;

    @Override
    public void add(NamespaceDO namespaceDO) {
        UserDO opUser = UserAuthenticationContextHolder.getContext().getOpUser();
        Assert.isTrue(groupDomainService.hasGroup(opUser, namespaceDO.getGroupCode()), "没有权限操作");
        namespaceMapper.insert(namespaceDO);
    }

    @Override
    public List<NamespaceDO> list(String groupCode) {
        NamespaceDO namespaceDO = new NamespaceDO();
        namespaceDO.setGroupCode(groupCode);
        QueryWrapper<NamespaceDO> namespaceDOQueryWrapper = new QueryWrapper<>(namespaceDO);
        return namespaceMapper.selectList(namespaceDOQueryWrapper);
    }
}
