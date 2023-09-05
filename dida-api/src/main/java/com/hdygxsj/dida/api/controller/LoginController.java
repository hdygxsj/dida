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


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.hdygxsj.dida.api.configuration.OAuth2Configuration;
import com.hdygxsj.dida.api.service.entity.TokenDO;
import com.hdygxsj.dida.api.service.entity.UserDO;
import com.hdygxsj.dida.api.service.TokenService;
import com.hdygxsj.dida.api.service.UserService;
import com.hdygxsj.dida.enums.ApiStatus;
import com.hdygxsj.dida.exceptions.Assert;
import com.hdygxsj.dida.exceptions.ServiceException;
import com.hdygxsj.dida.http.RequestClient;
import com.hdygxsj.dida.security.Sm4;
import com.hdygxsj.dida.tools.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("api/v1")
@RestController
@Tag(name = "login")
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired(required = false)
    private OAuth2Configuration oauth2Configuration;

    @Autowired
    private RequestClient requestClient;

    @PostMapping("login")
    public Result<TokenDO> login(@RequestParam(required = false) String username,
                                @RequestParam(required = false) String password,
                                @RequestAttribute(value = "ip",required = false) String ip) {
        Assert.notBlank(username,"用户名不能为空");
        Assert.notBlank(password,"密码不能为空");
        boolean validUser = userService.checkUser(username, Sm4.execute(password,Sm4.ENCRYPT));
        if (!validUser) {
            return Result.error(ApiStatus.LOGIN_FAILED);
        }
        TokenDO tokenDO = tokenService.get(username, ip);
        if(tokenDO!=null && tokenService.refresh(tokenDO.getToken())){
            return Result.success(tokenDO);
        }
        TokenDO genTokenDO = tokenService.genToken(username);
        tokenService.add(genTokenDO);
        return Result.success(genTokenDO);
    }

    @GetMapping("oauth2-providers")
    public Result<List<OAuth2Configuration.OAuth2ClientProperties>> getOAuth2Providers(){
        if (oauth2Configuration == null) {
            return Result.success(new ArrayList<>());
        }

        Collection<OAuth2Configuration.OAuth2ClientProperties> values = oauth2Configuration.getProvider().values();
        List<OAuth2Configuration.OAuth2ClientProperties> providers = values.stream().map(e -> {
            OAuth2Configuration.OAuth2ClientProperties oauth2ClientProperties =
                    new OAuth2Configuration.OAuth2ClientProperties();
            oauth2ClientProperties.setAuthorizationUri(e.getAuthorizationUri());
            oauth2ClientProperties.setRedirectUri(e.getRedirectUri());
            oauth2ClientProperties.setClientId(e.getClientId());
            oauth2ClientProperties.setProvider(e.getProvider());
            oauth2ClientProperties.setIconUri(e.getIconUri());
            return oauth2ClientProperties;
        }).collect(Collectors.toList());
        return Result.success(providers);
    }

    @GetMapping("redirect/login/oauth2")
    public void oauthLogin(@RequestParam String code, @RequestParam String provider,
                           HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, OAuth2Configuration.OAuth2ClientProperties> providers = oauth2Configuration.getProvider();
        OAuth2Configuration.OAuth2ClientProperties oauth2ClientProperties = providers.get(provider);
        Assert.isTrue(oauth2ClientProperties!=null,"未配置provider");
        try {
            log.info("login by oauth2 {}" ,code);
            Map<String, String> tokenRequestHeader = new HashMap<>();
            tokenRequestHeader.put("Accept", "application/json");
            Map<String, Object> requestBody = new HashMap<>(16);
            requestBody.put("client_secret", oauth2ClientProperties.getClientSecret());
            HashMap<String, Object> requestParamsMap = new HashMap<>();
            requestParamsMap.put("client_id", oauth2ClientProperties.getClientId());
            requestParamsMap.put("code", code);
            requestParamsMap.put("grant_type", "authorization_code");
            requestParamsMap.put("redirect_uri",
                    String.format("%s?provider=%s", oauth2ClientProperties.getRedirectUri(), provider));
            String tokenJsonStr = requestClient.post(oauth2ClientProperties.getTokenUri(), tokenRequestHeader,
                    requestParamsMap, requestBody);
            String accessToken = JSONObject.parseObject(tokenJsonStr).getString("access_token");
            Map<String, String> userInfoRequestHeaders = new HashMap<>();
            userInfoRequestHeaders.put("Accept", "application/json");
            Map<String, Object> userInfoQueryMap = new HashMap<>();
            userInfoQueryMap.put("access_token", accessToken);
            userInfoRequestHeaders.put("Authorization", "Bearer " + accessToken);
            String userInfoJsonStr =
                    requestClient.get(oauth2ClientProperties.getUserInfoUri(),
                            userInfoRequestHeaders, userInfoQueryMap).toString();
            String username = JSONObject.parseObject(userInfoJsonStr).getString("login");
            if(StrUtil.isBlank(username)){
                throw new ServiceException(ApiStatus.AUTH2_ERROR);
            }
            UserDO userDO = new UserDO();
            userDO.setUsername(username);
            boolean exist = userService.exist(username);
            if(!exist){
                userService.createBySystem(userDO);
            }
            TokenDO tokenDO = tokenService.get(username, null);
            if(tokenDO!=null && tokenService.refresh(tokenDO.getToken())){
                String redirectUrl = String.format("http://127.0.0.1:5173/login?status=success&token=%s",tokenDO.getToken());
                response.sendRedirect(redirectUrl);
                response.setStatus(HttpStatus.SC_MOVED_PERMANENTLY);
                return;
            }
            TokenDO genTokenDO = tokenService.genToken(username);
            tokenService.add(genTokenDO);
            String redirectUrl = String.format("http://127.0.0.1:5173/login?status=success&token=%s",genTokenDO.getToken());
            response.sendRedirect(redirectUrl);
        }catch (Exception ex){
            response.setStatus(HttpStatus.SC_MOVED_TEMPORARILY);
            response.sendRedirect(String.format("%s?authType=%s&error=%s", oauth2ClientProperties.getCallbackUrl(),
                    "oauth2", "oauth2 auth error"));
        }

//        Result<Object> result = request
    }


}
