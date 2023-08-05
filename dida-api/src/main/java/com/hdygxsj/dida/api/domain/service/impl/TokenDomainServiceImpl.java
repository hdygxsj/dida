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
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hdygxsj.dida.api.domain.entity.TokenDO;
import com.hdygxsj.dida.api.domain.service.TokenDomainService;
import com.hdygxsj.dida.api.mapper.TokenMapper;
import com.hdygxsj.dida.exceptions.Assert;
import com.hdygxsj.dida.security.Sm4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Optional;
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
        char checkCode = genCheckCode(token);
        token = token + checkCode;
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
        if (tokenDO.getExpTime() == null) {
            return true;
        }
        if ((updateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() + tokenDO.getExpTime())
                < Instant.now().toEpochMilli()) {
            tokenMapper.deleteById(tokenDO.getId());
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
        queryWrapper.eq(realIp != null, "login_ip", realIp);
        queryWrapper.isNull(realIp == null, "login_ip");
        queryWrapper.orderByDesc("update_time");
        return tokenMapper.selectOne(queryWrapper);
    }

    /**
     * 从token中获取username
     *
     * @param tokenDO
     * @return
     */
    @Override
    public String getUsernameByToken(TokenDO tokenDO) {
        Assert.isTrue(checkToken(tokenDO), "无效的token");
        String usernameFormat = Sm4.execute(getRealToken(tokenDO.getToken()), Sm4.DECRYPT);
        String[] split = usernameFormat.split("-");
        return split[0];
    }

    /**
     * token校验
     *
     * @param tokenDO
     * @return
     */
    @Override
    public boolean checkToken(TokenDO tokenDO) {
        String token = Optional.ofNullable(tokenDO).map(TokenDO::getToken).filter(StrUtil::isNotBlank).orElse(null);
        if (token == null || token.length() < 2) {
            return false;
        }
        String realToken = getRealToken(token);
        String checkCode = token.substring(token.length() - 1);
        return checkCode.equals(genCheckCode(realToken)+"");
    }

    /**
     * 获取原token
     *
     * @param token
     * @return
     */
    private static String getRealToken(String token) {
        return token.substring(0, token.length() - 1);
    }

    /**
     * 生成token校验位
     *
     * @param token
     * @return
     */
    @Override
    public char genCheckCode(String token) {
        char[] charArray = token.toCharArray();
        int xor = charArray[0];
        for (int i = 1; i < charArray.length; i++) {
            xor = (int) charArray[i] ^ xor;
        }
        return (char) xor;
    }

}
