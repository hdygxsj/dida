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

package com.hdygxsj.dida.api.authentication.filter;

import cn.hutool.core.util.StrUtil;
import com.hdygxsj.dida.api.authentication.base.DidaUser;
import com.hdygxsj.dida.api.authentication.base.UserAuthenticationContextHolder;
import com.hdygxsj.dida.api.authentication.service.TokenUserDetailsService;
import com.hdygxsj.dida.api.service.entity.TokenDO;
import com.hdygxsj.dida.api.service.entity.UserDO;
import com.hdygxsj.dida.api.service.TokenService;
import com.hdygxsj.dida.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TokenUserDetailsService tokenUserDetailsService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        UserAuthenticationContextHolder context = UserAuthenticationContextHolder.getContext();
        String token = request.getHeader("token");
        if (StrUtil.isNotBlank(token)) {
            TokenDO tokenDO = new TokenDO();
            tokenDO.setToken(token);
            if (tokenService.checkToken(tokenDO) && tokenService.refresh(token)) {
                String username = tokenService.getUsernameByToken(tokenDO);
                DidaUser userDetails = (DidaUser) tokenUserDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken
                        (userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                UserDO userDO = userDetails.getUserDO();
                userDO.setPassword(null);
                request.setAttribute("opUser", userDO);
            }
        }
        filterChain.doFilter(request, response);
        context.removeOpUser();
    }
}
