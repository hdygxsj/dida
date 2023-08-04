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

package com.hdygxsj.dida.api.interceptor;

import cn.hutool.core.util.StrUtil;
import com.hdygxsj.dida.api.domain.entity.TokenDO;
import com.hdygxsj.dida.api.domain.entity.UserDO;
import com.hdygxsj.dida.api.domain.service.TokenDomainService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenDomainService tokenDomainService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        UserDO user;
        if (StrUtil.isBlank(token)) {
            log.error("未获得授权的访问{}", request.getRequestURI());
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            return false;
        } else {
            TokenDO tokenDO = new TokenDO();
            tokenDO.setToken(token);
            boolean validToken = tokenDomainService.checkToken(tokenDO);
            if(!validToken){
                log.error("token已过期{},用户{}", token,tokenDomainService.getUsernameByToken(tokenDO));
                response.setStatus(HttpStatus.SC_UNAUTHORIZED);
                return false;
            }
            boolean refresh = tokenDomainService.refresh(token);
            if (!refresh) {
                log.error("token已过期{},用户{}", token,tokenDomainService.getUsernameByToken(tokenDO));
                response.setStatus(HttpStatus.SC_UNAUTHORIZED);
                return false;
            }
        }
        return true;
    }
}
