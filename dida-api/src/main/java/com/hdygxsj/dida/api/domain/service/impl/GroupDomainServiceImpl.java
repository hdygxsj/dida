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

import com.hdygxsj.dida.api.domain.entity.GroupDO;
import com.hdygxsj.dida.api.domain.entity.UserGroupRelDO;
import com.hdygxsj.dida.api.domain.service.GroupDomainService;
import com.hdygxsj.dida.api.mapper.GroupMapper;
import com.hdygxsj.dida.api.mapper.UserGroupRelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void addUser(String groupCode, String username) {
        UserGroupRelDO userGroupRelDO = new UserGroupRelDO();
        userGroupRelDO.setGroupCode(groupCode);
        userGroupRelDO.setUsername(username);
        userGroupRelMapper.insert(userGroupRelDO);
    }
}