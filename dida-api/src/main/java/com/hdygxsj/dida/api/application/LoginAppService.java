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


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.hdygxsj.dida.api.application.entity.Oauth2AccessTokenDTO;
import com.hdygxsj.dida.api.domain.entity.TokenDO;
import com.hdygxsj.dida.api.domain.entity.UserDO;
import com.hdygxsj.dida.api.domain.service.TokenDomainService;
import com.hdygxsj.dida.api.domain.service.UserDomainService;
import com.hdygxsj.dida.enums.ApiStatus;
import com.hdygxsj.dida.exceptions.Assert;
import com.hdygxsj.dida.exceptions.ServiceException;
import com.hdygxsj.dida.http.RequestClient;
import com.hdygxsj.dida.security.Sm4;
import com.hdygxsj.dida.tools.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import okhttp3.internal.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("api/v1/login")
@RestController
@Tag(name = "login")
@Slf4j
public class LoginAppService {

    @Autowired
    private UserDomainService userDomainService;

    @Autowired
    private TokenDomainService tokenDomainService;

    @Autowired
    private RequestClient requestClient;

    @PostMapping
    public Result<TokenDO> login(@RequestParam(required = false) String username,
                                @RequestParam(required = false) String password,
                                @RequestAttribute(value = "ip",required = false) String ip) {
        Assert.notBlank(username,"用户名不能为空");
        Assert.notBlank(password,"密码不能为空");
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

    @GetMapping("oauth-info")
    public Result oauthInfo(){
        return null;
    }

    @GetMapping("oauth")
    public void oauthLogin(@RequestParam String code, HttpServletResponse httpServletResponse) throws IOException {
        log.info("login by oauth 2 {}" ,code);
        Map<String,String> accTokenRequestHeaders = new HashMap<>();
        accTokenRequestHeaders.put("Accept","application/json");
        String getAccessTokenUrl = "https://github.com/login/oauth/access_token";
        Map<String, Object> requestBody = new HashMap<>(16);
        requestBody.put("client_id", "8e9e1b8fcf23ec1ca7b5");
        requestBody.put("client_secret","75f447847542ae9687837bff66235095339f51cc");
        requestBody.put("code", code);
        String accessTokenResult = requestClient.post(getAccessTokenUrl, accTokenRequestHeaders, requestBody,String.class);


        log.info("login by oauth2 accessToken {}" ,accessTokenResult);
        JSONObject jsonObject = JSON.parseObject(accessTokenResult);
        String accessToken = jsonObject.getString("access_token");
        String getUserInfoUrl = "https://api.github.com/user";
        Map<String,String> userInfoRequestHeaders = new HashMap<>();
//
        userInfoRequestHeaders.put("Accept","application/json");
        userInfoRequestHeaders.put("Authorization",  "token "+accessToken);
        String userInfoResult = requestClient.get(getUserInfoUrl, userInfoRequestHeaders, new HashMap<>(),String.class);
        log.info("login by oauth2 userInfoResult {}" ,userInfoResult);
        JSONObject userInfo = JSON.parseObject(userInfoResult);
        String username = userInfo.getString("login");
        if(StrUtil.isBlank(username)){
            throw new ServiceException(ApiStatus.AUTH2_ERROR);
        }
        UserDO userDO = new UserDO();
        userDO.setUsername(username);
        boolean exist = userDomainService.exist(username);
        if(!exist){
            userDomainService.createBySystem(userDO);
        }
        TokenDO tokenDO = tokenDomainService.get(username, null);
        if(tokenDO!=null && tokenDomainService.refresh(tokenDO.getToken())){
            String redirectUrl = String.format("http://127.0.0.1:5173/login?status=success&token=%s",tokenDO.getToken());
            httpServletResponse.sendRedirect(redirectUrl);
            httpServletResponse.setStatus(HttpStatus.SC_MOVED_PERMANENTLY);
            return;
        }
        TokenDO genTokenDO = tokenDomainService.genToken(username);
        tokenDomainService.add(genTokenDO);
        String redirectUrl = String.format("http://127.0.0.1:5173/login?status=success&token=%s",genTokenDO.getToken());
        httpServletResponse.sendRedirect(redirectUrl);
        httpServletResponse.setStatus(HttpStatus.SC_MOVED_PERMANENTLY);
//        Result<Object> result = request
    }


}
