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

import com.hdygxsj.dida.api.domain.entity.UserDO;
import com.hdygxsj.dida.api.domain.service.UserDomainService;
import com.hdygxsj.dida.exceptions.Assert;
import com.hdygxsj.dida.security.Sm4;
import com.hdygxsj.dida.tools.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "user")
@RequestMapping("api/v1/users")
@Slf4j
public class UserAppService {

    @Autowired
    private UserDomainService userDomainService;

    @GetMapping
    @Operation
    public Result<List<UserDO>> listAll() {
        return Result.success(userDomainService.listAll());
    }

    @PostMapping
    public Result<Boolean> addUser(String username, String password) {
        UserDO userDO = new UserDO();
        userDO.setUsername(username);
        userDO.setPassword(Sm4.execute(password, Sm4.ENCRYPT));
        userDomainService.create(userDO);
        return Result.success();
    }

    @PutMapping("{username}/reset-password")
    public Result<String> resetPassword(@PathVariable String username, @RequestAttribute UserDO opUser) {
        Assert.isTrue(opUser.isSuperUser(), "没有操作权限");
        String words = "0123456789abcdefghijklmmopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()_+{}:<>?/.,";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(words.charAt((int) (Math.random() * words.length())));
        }
        String newPassword = sb.toString();
        UserDO userDO = new UserDO();
        userDO.setPassword(Sm4.execute(newPassword, Sm4.ENCRYPT));
        userDO.setUsername(username);
        return Result.success(newPassword);
    }

    @PostMapping("{username}/roles")
    public Result<String> addRole(@RequestParam String username,
                                  @RequestParam List<String> roleCodes,
                                  @RequestAttribute UserDO opUser){
        log.info("add role op user {}",opUser.getUsername());
        userDomainService.addRoles(username,roleCodes);
        return Result.success();
    }
}
