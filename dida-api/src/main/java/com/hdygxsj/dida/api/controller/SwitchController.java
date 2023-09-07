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

package com.hdygxsj.dida.api.controller;

import com.hdygxsj.dida.api.service.SwitchService;
import com.hdygxsj.dida.api.service.entity.SwitchDO;
import com.hdygxsj.dida.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/groups/{groupCode}/namespaces/{namespace}/switches")
@RestController
public class SwitchController {

    @Autowired
    private SwitchService switchService;

    @GetMapping("{key}")
    public Result<String> getValue(@PathVariable("namespace") String namespace,
                                   @PathVariable("groupCode") String group,
                                   @PathVariable("key") String key) {

        return Result.success(switchService.getValue(group, namespace, key));
    }

    @PutMapping("/{key}/set-value")
    public void setSwitch(@PathVariable("namespace") String namespace,
                         @PathVariable("groupCode") String group,
                         @RequestParam("key") String key,
                         @RequestParam("value") String value) {
        switchService.setValue(group, namespace, key, value);
    }

    @PostMapping
    public void addSwitch(@PathVariable String groupCode, @PathVariable String namespace,
                          @RequestBody String key, @RequestParam String type){
        SwitchDO switchDO = new SwitchDO();
        switchDO.setNamespaceCode(namespace);
        switchDO.setType(type);
        switchDO.setSwitchKey(key);
        switchService.add(switchDO);
    }
}
