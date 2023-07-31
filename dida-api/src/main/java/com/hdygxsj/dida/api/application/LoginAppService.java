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

import com.hdygxsj.dida.api.domain.entity.TokenDO;
import com.hdygxsj.dida.api.domain.service.TokenDomainService;
import com.hdygxsj.dida.api.domain.service.UserDomainService;
import com.hdygxsj.dida.api.enums.ApiStatus;
import com.hdygxsj.dida.api.tools.Result;
import com.hdygxsj.dida.security.Sm4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RequestMapping("api/v1/login")
@RestController
public class LoginAppService {

    @Autowired
    private UserDomainService userDomainService;

    @Autowired
    private TokenDomainService tokenDomainService;

    @PostMapping
    public Result<TokenDO> login(@RequestParam(required = false) String username,
                                @RequestParam(required = false) String password,
                                @RequestAttribute(value = "ip",required = false) String ip) {
        boolean validUser = userDomainService.checkUser(username, Sm4.execute(password,Sm4.ENCRYPT));
        if (!validUser) {
            return Result.error(ApiStatus.LOGIN_FAILED);
        }
        TokenDO tokenDO = tokenDomainService.get(username, ip);
        if(tokenDO!=null && tokenDomainService.refresh(tokenDO.getToken())){
            return Result.success(tokenDO);
        }
        TokenDO genTokenDO = tokenDomainService.genToken(username);
        tokenDomainService.add(genTokenDO);
        return Result.success(genTokenDO);
    }


}
