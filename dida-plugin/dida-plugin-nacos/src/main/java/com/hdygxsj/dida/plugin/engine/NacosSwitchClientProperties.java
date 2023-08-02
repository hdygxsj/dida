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
import com.alibaba.nacos.api.PropertyKeyConst;
import com.hdygxsj.dida.spi.engine.SwitchClientProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Properties;


@Configuration
@ConditionalOnProperty(prefix = "engine", name = "type", havingValue = "nacos")
@ConfigurationProperties(prefix = "engine")
public class NacosSwitchClientProperties implements SwitchClientProperties {

    private ClientNacosProperties nacos = new ClientNacosProperties();


    public ClientNacosProperties getNacos() {
        return nacos;
    }

    public void setNacos(ClientNacosProperties nacos) {
        this.nacos = nacos;
    }

    @Getter
    @Setter
    public static final class ClientNacosProperties {
        private String host;

        private String username;

        private String password;

        private String group = "DEFAULT_GROUP";

        private String namespace = "public";
    }


    @Override
    public Properties toProperties() {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.SERVER_ADDR, nacos.host);
        if (StrUtil.isNotBlank(nacos.username)) {
            properties.setProperty(PropertyKeyConst.ACCESS_KEY, nacos.username);
        }
        if (StrUtil.isNotBlank(nacos.username)) {
            properties.setProperty(PropertyKeyConst.SECRET_KEY, nacos.password);
        }
        properties.setProperty(PropertyKeyConst.NAMESPACE, nacos.namespace);
        return properties;
    }
}
