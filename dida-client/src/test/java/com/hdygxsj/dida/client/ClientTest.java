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


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ClientTest {

    @Test
    public void nacosPlugin() {
        long epochMilli = Instant.now().toEpochMilli();

            DidaClient didaClient = DidaClientBuilder.builder()
                    .host("127.0.0.1:8080")
                    .username("admin")
                    .password("admin")
                    .build();
            String engineType = didaClient.getEngineType();
            Properties engineProperties = didaClient.getEngineProperties();
            log.info(engineType);
            log.info(String.valueOf(engineProperties));
        while (Instant.now().toEpochMilli() - epochMilli < 60000) {
            String value = didaClient.getValue("otsp", "yd01", "key1");
            log.info("value is {}", value);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
