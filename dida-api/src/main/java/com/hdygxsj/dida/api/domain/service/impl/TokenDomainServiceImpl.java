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

package com.hdygxsj.dida.api.domain.service.impl;

import cn.hutool.core.text.StrFormatter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hdygxsj.dida.api.domain.entity.TokenDO;
import com.hdygxsj.dida.api.domain.service.TokenDomainService;
import com.hdygxsj.dida.api.mapper.TokenMapper;
import com.hdygxsj.dida.security.Sm4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

@Service
public class TokenDomainServiceImpl implements TokenDomainService {

    @Autowired
    private TokenMapper tokenMapper;


    @Override
    public TokenDO genToken(String username) {

        LocalDateTime now = LocalDateTime.now();
        long epochMilli = now.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        String temp = StrFormatter.format("{}-{}", username, epochMilli);
        String token = Sm4.execute(temp, Sm4.ENCRYPT);
        TokenDO tokenDO = new TokenDO();
        tokenDO.setUsername(username);
        tokenDO.setToken(token);
        tokenDO.setExpTime(TimeUnit.MINUTES.toMicros(30));
        return tokenDO;
    }

    @Override
    public void add(TokenDO tokenDO) {
        tokenMapper.insert(tokenDO);
    }

    @Override
    public boolean refresh(String token) {
        QueryWrapper<TokenDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("token", token);
        TokenDO tokenDO = tokenMapper.selectOne(queryWrapper);
        if (tokenDO == null) {
            return false;
        }
        LocalDateTime updateTime = tokenDO.getUpdateTime();
        if ((updateTime.toInstant(ZoneOffset.UTC).toEpochMilli() + tokenDO.getExpTime())
                < Instant.now().atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()) {
            return false;
        }
        tokenDO.setToken(token);
        tokenDO.setUpdateTime(LocalDateTime.now());
        tokenMapper.update(tokenDO, queryWrapper);
        return true;
    }

    @Override
    public TokenDO get(String username, String realIp) {
        QueryWrapper<TokenDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        queryWrapper.eq("login_ip", realIp);
        return tokenMapper.selectOne(queryWrapper);
    }


}
