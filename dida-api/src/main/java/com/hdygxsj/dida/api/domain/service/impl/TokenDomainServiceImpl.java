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
import com.hdygxsj.dida.api.domain.service.TokenDomainService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenDomainServiceImpl implements TokenDomainService {
    @Override
    public String genToken(String username) {
        LocalDateTime now = LocalDateTime.now();
        long epochMilli = now.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        StrFormatter.format("{}-{}", username, epochMilli);

        return null;
    }

    @Override
    public void refresh(String token){

    }


}
