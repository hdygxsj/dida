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
import com.hdygxsj.dida.spi.engine.Engine;
import com.hdygxsj.dida.spi.engine.EngineFactory;

import java.util.Iterator;
import java.util.Properties;
import java.util.ServiceLoader;

public class DidaClient {

    private String host;

    private String username;

    private String password;

    private String token;

    DidaClient(){

    }

    /**
     * 重连
     */
    public void reconnect(){

    }

    public void check(){

    }

    public String getEngineType(){
        //todo
        return null;
    }

    public Properties getEngineProperties(){
        //todo
        return null;
    }

    public Engine getEngine(){
        String engineType = getEngineType();
        Asset.isTrue(StrUtil.isNotBlank(engineType),"dida平台异常，无法获取引擎类型");
        ServiceLoader<EngineFactory> serviceLoader = ServiceLoader.load(EngineFactory.class);
        Iterator<EngineFactory> services = serviceLoader.iterator();
        Engine engine = null;
        while(services.hasNext()){
            EngineFactory engineFactory = services.next();
            String name = engineFactory.name();
            if(!engineType.equals(name)){
                continue;
            }
            Properties engineProperties = getEngineProperties();
            engine = engineFactory.getEngine(engineProperties);
        }
        Asset.notNull(engine,"未装载引擎类型插件："+engineType);
        return engine;
    }

     void setHost(String host) {
        this.host = host;
    }

     void setUsername(String username) {
        this.username = username;
    }

     void setPassword(String password) {
        this.password = password;
    }

     void setToken(String token) {
        this.token = token;
    }
}