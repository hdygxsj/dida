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
import com.hdygxsj.dida.exceptions.Asset;

public class DidaClientBuilder {

    private String host;

    private String username;

    private String password;

    private String token;
    public DidaClient build(){
        DidaClient didaClient = new DidaClient();
        Asset.isTrue( StrUtil.isNotBlank(host),"host为空");
        didaClient.setHost(host);
        if(token!=null){
            didaClient.setToken(token);
            return didaClient;
        }
        Asset.isTrue(StrUtil.isAllNotBlank(host,username,password),"username 、password不能为空");
        didaClient.setUsername(username);
        didaClient.setPassword(password);

        return didaClient;
    }

    private DidaClientBuilder(){}

    public DidaClientBuilder builder(){
        return new DidaClientBuilder();
    }



}
