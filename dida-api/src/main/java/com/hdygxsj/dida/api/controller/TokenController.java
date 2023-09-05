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

import com.hdygxsj.dida.api.service.entity.TokenDO;
import com.hdygxsj.dida.api.service.TokenService;
import com.hdygxsj.dida.tools.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/token")
@RestController
@Tag(name = "token")
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @PutMapping("refresh")
    public Result<Boolean> refreshToken(@PathVariable("token") String token) {
        return Result.success(tokenService.refresh(token));
    }

    @PostMapping("create")
    public Result<TokenDO> genToken(@RequestParam(required = false) String username) {
        TokenDO token = tokenService.genToken(username);
        token.setExpTime(null);
        tokenService.add(token);
        return Result.success(token);
    }
}
