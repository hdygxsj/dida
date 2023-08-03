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

package com.hdygxsj.dida.client;

import cn.hutool.core.util.StrUtil;
import com.hdygxsj.dida.exceptions.Assert;
import okhttp3.OkHttpClient;

public class DidaClientBuilder {

    private String host;

    private String username;

    private String password;

    private String token;

    public DidaClient build() {
        Assert.isTrue(StrUtil.isNotBlank(host), "host为空");
        if (token != null) {
            return new DidaClient(token, new OkHttpClient());
        }
        Assert.isTrue(StrUtil.isAllNotBlank(host, username, password), "username 、password不能为空");
        return new DidaClient(host, username, password, new OkHttpClient());
    }

    private DidaClientBuilder() {
    }

    public DidaClientBuilder host(String host){
        this.host = host;
        return this;
    }
    public DidaClientBuilder username(String username){
        this.username = username;
        return this;
    }
    public DidaClientBuilder password(String password){
        this.password = password;
        return this;
    }
    public DidaClientBuilder token(String token){
        this.token = token;
        return this;
    }



    public static DidaClientBuilder builder() {
        return new DidaClientBuilder();
    }


}
