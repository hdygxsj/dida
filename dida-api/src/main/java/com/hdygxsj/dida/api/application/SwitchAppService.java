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

package com.hdygxsj.dida.api.application;

import com.hdygxsj.dida.api.domain.service.SwitchDomainService;
import com.hdygxsj.dida.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/{group}/{namespace}/switchs")
@RestController
public class SwitchAppService {

    @Autowired
    private SwitchDomainService switchDomainService;

    @GetMapping("{key}")
    public Result<String> getValue(@PathVariable("namespace") String namespace,
                                   @PathVariable("group") String group,
                                   @PathVariable("key") String key) {

        return Result.success(switchDomainService.getValue(group, namespace, key));
    }

    @PostMapping("/")
    public void getValue(@PathVariable("namespace") String namespace,
                         @PathVariable("group") String group,
                         @RequestParam("key") String key,
                         @RequestParam("value") String value) {
        switchDomainService.setValue(group, namespace, key, value);
    }
}
