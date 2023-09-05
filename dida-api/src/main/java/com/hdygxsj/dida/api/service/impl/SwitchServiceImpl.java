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

package com.hdygxsj.dida.api.service.impl;

import com.hdygxsj.dida.api.service.SwitchService;
import com.hdygxsj.dida.exceptions.Assert;
import com.hdygxsj.dida.spi.engine.SwitchClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SwitchServiceImpl implements SwitchService {

    @Autowired(required = false)
    private SwitchClient switchClient;


    @Override
    public void setValue(String group, String namespace, String key, String value) {
        Assert.notNull(switchClient,"开关引擎未装载");
        switchClient.beforeSetValue(group,namespace,key);
        switchClient.setValue(group,namespace,key,value);
        switchClient.afterSetValue(group,namespace,value);
    }

    @Override
    public String getValue(String group, String namespace, String key) {
        Assert.notNull(switchClient,"开关引擎未装载");
        return switchClient.getValue(group,namespace,key);
    }
}
