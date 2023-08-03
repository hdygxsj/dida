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
import com.alibaba.fastjson.JSON;
import com.hdygxsj.dida.constants.Constants;
import com.hdygxsj.dida.exceptions.Assert;
import com.hdygxsj.dida.exceptions.DidaRuntimeException;
import com.hdygxsj.dida.http.RequestClient;
import com.hdygxsj.dida.spi.engine.SwitchClient;
import com.hdygxsj.dida.spi.engine.SwitchClientFactory;
import com.hdygxsj.dida.tools.Result;
import okhttp3.OkHttpClient;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;

public class DidaClient {

    private String host;

    private String username;

    private String password;

    private String token;

    /**
     * 授权类型 0-用户名授权，1-token授权
     */
    private int authType;


    private final RequestClient httpClient;

    private String type;

    private Properties properties;

    /**
     * 重连
     */
    public void reconnect() {
        connect();
    }

    public void check() {

    }

    public String getEngineType() {
        return this.type;
    }

    public Properties getEngineProperties() {
        return this.properties;
    }

    DidaClient(String host, String username, String password, OkHttpClient okHttpClient) {
        this.httpClient = new RequestClient(okHttpClient);
        this.host = host;
        this.username = username;
        this.password = password;
        authType = 0;
        connect();
    }

    SwitchClient switchClient;

    DidaClient(String token, OkHttpClient okHttpClient) {
        this.httpClient = new RequestClient(okHttpClient);
        this.token = token;
        authType = 1;
        connect();
    }

    public void connect() {
        if (authType == 0) {
            Map<String, Object> requests = new HashMap<>();
            try {
                requests.put("username",username);
                requests.put("password",password);
                Result<Object> tokenResult = httpClient.post(createRequestUrl("api/v1/login"), null, requests);
                if (tokenResult.isFailed()) {
                    throw new DidaRuntimeException("客户端创建失败，无法与服务端创建连接" + tokenResult.getMessage());
                }
                this.token = String.valueOf( tokenResult.getData());

            } catch (Exception ex) {
                throw new DidaRuntimeException("客户端创建失败，无法与服务端创建连接" + ex.getMessage());
            }
        }
        try{
            Map<String, String> headers = getHeaders();
            Result<Object> clusterInfoDTOResult = httpClient.get(createRequestUrl("api/v1/cluster/info/"), headers, null);
            ClusterInfoDTO clusterInfoDTO = JSON.parseObject(String.valueOf(clusterInfoDTOResult.getData()),ClusterInfoDTO.class);
            this.type = clusterInfoDTO.getEngineType();
            this.properties = clusterInfoDTO.getEnginProperties();
            this.switchClient = getEngine();
        }catch (Exception ex) {
            throw new DidaRuntimeException("客户端创建失败，无法与服务端创建连接" + ex.getMessage());
        }
    }

    private String createRequestUrl(String path){
        return String.format("http://%s/%s/%s",host, Constants.API_CONTEXT,path);
    }

    public SwitchClient getEngine() {
        String engineType = getEngineType();
        Assert.isTrue(StrUtil.isNotBlank(engineType), "dida平台异常，无法获取引擎类型");
        ServiceLoader<SwitchClientFactory> serviceLoader = ServiceLoader.load(SwitchClientFactory.class);
        Iterator<SwitchClientFactory> services = serviceLoader.iterator();
        SwitchClient switchClient = null;
        while (services.hasNext()) {
            SwitchClientFactory engineFactory = services.next();
            String name = engineFactory.name();
            if (!engineType.equals(name)) {
                continue;
            }
            Properties engineProperties = getEngineProperties();
            switchClient = engineFactory.getSwitchClient(engineProperties);
        }
        Assert.notNull(switchClient, "未装载引擎类型插件：" + engineType);
        return switchClient;
    }

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("token", token);
        return headers;
    }


    public String getValue(String group, String namespace, String key) {
        return switchClient.getValue(group, namespace, key);
    }


}
