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

package com.hdygxsj.dida.api.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdygxsj.dida.api.service.entity.GroupDO;
import com.hdygxsj.dida.api.service.entity.UserGroupRelDO;
import com.hdygxsj.dida.api.service.GroupService;
import com.hdygxsj.dida.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("groups")
    public Result<Boolean> add(@RequestParam(required = false) String name,
                               @RequestParam(required = false) String code,
                               @RequestParam(required = false) String descp) {
        GroupDO groupDO = new GroupDO();
        groupDO.setCode(code);
        groupDO.setName(name);
        groupDO.setDescp(descp);
        groupService.add(groupDO);
        return Result.success();
    }

    @PostMapping("groups/{code}/members")
    public Result<Boolean> addUser(@PathVariable String code, @RequestParam String username) {
        groupService.addUser(code, username);
        return Result.success();
    }


    @GetMapping("groups/page")
    public Result<Page<GroupDO>> page(@RequestParam Integer pageNum,
                                      @RequestParam Integer pageSize,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) String code) {
        return Result.success(groupService.page(pageNum, pageSize, name, code));
    }

    @DeleteMapping("groups/{code}")
    public void deleteGroup(@PathVariable String code) {
        groupService.deleteByCode(code);
    }

    @GetMapping("groups/{code}/members")
    public Result<Page<UserGroupRelDO>> getMembers(@PathVariable String code,
                                                   @RequestParam String username,
                                                   @RequestParam Integer pageNum,
                                                   @RequestParam Integer pageSize) {
        return Result.success(groupService.pageGroupMember(code, username, pageNum, pageSize));
    }

    @DeleteMapping("groups/{code}/members/{username}")
    public void deleteMember(@PathVariable String code,
                             @PathVariable String username) {
        groupService.deleteMember(code, username);
    }

    @GetMapping("groups/{code}/info")
    public Result<GroupDO> info(@PathVariable String code) {
        return Result.success(groupService.get(code));
    }


}
