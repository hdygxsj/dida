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

package com.hdygxsj.dida.plugin.engine;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.hdygxsj.dida.exceptions.DidaRuntimeException;
import com.hdygxsj.dida.spi.engine.SwitchClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@ConditionalOnProperty(prefix = "engine", name = "type", havingValue = "nacos")
@Slf4j

public class NacosSwitchClient implements SwitchClient {


    private final Executor executor;

    private final Properties properties;


    @Autowired
    public NacosSwitchClient(NacosSwitchClientProperties nacosSwitchClientProperties) {
        this(nacosSwitchClientProperties.toProperties());

    }


    private static final Map<String, JSONObject> switchConfigCache = new ConcurrentHashMap<>();

    private final String type = "nacos";

    private final ConfigService configService;

    private String nacosGroup = "DEFAULT_GROUP";

    public NacosSwitchClient(Properties properties) {
        this.properties = properties;
        int executorCoreThreads = Integer.parseInt(properties.getProperty("executorCoreThreads", "20"));
        int executorMaxThreads = Integer.parseInt(properties.getProperty("executorMaxThreads", "100"));
        int executorQueueSize = Integer.parseInt(properties.getProperty("executorQueueSize", "200"));
        executor = new ThreadPoolExecutor(executorCoreThreads, executorMaxThreads, 60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue(executorQueueSize));
        try {
            this.configService = NacosFactory.createConfigService(properties);
        } catch (NacosException e) {
            throw new RuntimeException("nacos客户端初始化失败", e);
        }
    }


    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public void setValue(String group, String namespace, String key, String value) {
        try {
            String dataId = getDataId(group, namespace);
            JSONObject object = switchConfigCache.get(dataId);
            if (object == null) {
                object = new JSONObject();
            }
            switchConfigCache.put(dataId, object);
            object.put(key, value);
            boolean result = configService.publishConfig(dataId, nacosGroup
                    , JSONObject.toJSONString(object, true));
            if (!result) {
                throw new DidaRuntimeException("发布配置失败");
            }
        } catch (NacosException e) {
            throw new RuntimeException("无法创建nacos配置", e);
        }
    }

    private String getDataId(String group, String namespace) {
        return group + "." + namespace;
    }

    @Override
    public String getValue(String group, String namespace, String key) {
        String dataId = getDataId(group, namespace);
        JSONObject data = switchConfigCache.get(dataId);
        if (data == null) {
            try {
                String config = configService.getConfig(dataId, nacosGroup, 5000);
                if (StrUtil.isBlank(config)) {
                    return null;
                }
                String configAndSignListener = configService.getConfigAndSignListener(dataId, nacosGroup, 5000, new Listener() {
                    @Override
                    public Executor getExecutor() {
                        return executor;
                    }

                    @Override
                    public void receiveConfigInfo(String s) {
                        log.info("{} 配置更新", dataId);
                        switchConfigCache.put(dataId, JSONObject.parseObject(s));
                    }
                });
                switchConfigCache.put(dataId, JSONObject.parseObject(configAndSignListener));
            } catch (NacosException e) {
                throw new RuntimeException(e);
            }

        }


        return switchConfigCache.get(dataId).getString(key);
    }
}
