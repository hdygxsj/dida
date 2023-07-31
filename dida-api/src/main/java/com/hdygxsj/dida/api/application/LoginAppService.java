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

import com.hdygxsj.dida.api.domain.service.TokenDomainService;
import com.hdygxsj.dida.api.domain.service.UserDomainService;
import com.hdygxsj.dida.api.enums.ApiStatus;
import com.hdygxsj.dida.api.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("api/v1/login")
public class LoginAppService {

    @Autowired
    private UserDomainService userDomainService;

    @Autowired
    private TokenDomainService tokenDomainService;

    public Result<String> login(@RequestParam(required = false) String username,
                                @RequestParam(required = false) String password) {
        boolean validUser = userDomainService.checkUser(username, password);
        if (!validUser) {
            return Result.error(ApiStatus.LOGIN_FAILED);
        }
        return Result.success(tokenDomainService.genToken(username));
    }
}
