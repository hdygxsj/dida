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

import com.hdygxsj.dida.api.authentication.permission.OpObjType;
import com.hdygxsj.dida.api.service.entity.RoleDO;
import com.hdygxsj.dida.api.service.entity.RoleObjectRelDO;
import com.hdygxsj.dida.api.service.RoleService;
import com.hdygxsj.dida.api.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public void addPermission(String roleCode, OpObjType objType, Long permission) {

    }

    @Override
    public List<RoleObjectRelDO> listPermission(String roleCode) {
        return null;
    }

    @Override
    public List<RoleDO> list(String roleCode) {
        return null;
    }
}
