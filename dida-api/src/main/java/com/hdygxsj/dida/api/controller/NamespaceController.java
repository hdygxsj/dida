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

import com.hdygxsj.dida.api.service.entity.NamespaceDO;
import com.hdygxsj.dida.api.service.NamespaceService;
import com.hdygxsj.dida.exceptions.Assert;
import com.hdygxsj.dida.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("api/v1/groups/{groupCode}/namespaces")
@RestController
public class NamespaceController {

    @Autowired
    private NamespaceService namespaceService;

    @GetMapping
    public Result<List<NamespaceDO>> listNamespace(@PathVariable String groupCode) {
        return Result.success(namespaceService.list(groupCode));
    }

    @PostMapping
    public void addNamespace(@PathVariable String groupCode,
                             @RequestParam(required = false) String code,
                             @RequestParam(required = false) String name,
                             @RequestParam(required = false) String descp) {
        NamespaceDO namespaceDO = new NamespaceDO();
        Assert.notBlank(code,"命名空间编码不能为空");
        Assert.notBlank(name,"命名空间名称不能为空");
        namespaceDO.setGroupCode(groupCode);
        namespaceDO.setName(name);
        namespaceDO.setCode(code);
        namespaceDO.setDescp(descp);
        namespaceService.add(namespaceDO);
    }

    @DeleteMapping("{code}")
    public void delete(@PathVariable String groupCode,
                       @PathVariable String code){
        NamespaceDO namespaceDO = new NamespaceDO();
        namespaceDO.setCode(code);
        namespaceService.delete(namespaceDO);
    }
}
