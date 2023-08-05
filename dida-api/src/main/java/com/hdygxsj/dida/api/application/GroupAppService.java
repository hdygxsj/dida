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

package com.hdygxsj.dida.api.application;


import com.hdygxsj.dida.api.domain.entity.GroupDO;
import com.hdygxsj.dida.api.domain.service.GroupDomainService;
import com.hdygxsj.dida.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/")
public class GroupAppService {

    @Autowired
    private GroupDomainService groupDomainService;

    @PostMapping("groups")
    public Result<Boolean> add(@RequestParam(required = false) String name,
                               @RequestParam(required = false) String code,
                               @RequestParam(required = false) String descp) {
        GroupDO groupDO = new GroupDO();
        groupDO.setCode(code);
        groupDO.setName(name);
        groupDO.setDescp(descp);
        groupDomainService.add(groupDO);
        return Result.success();
    }

    @PostMapping("{groupCode}/users")
    public Result<Boolean> addUser(@PathVariable String groupCode, @RequestParam String username){
        groupDomainService.addUser(groupCode,username);
        return Result.success();
    }
}
