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

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdygxsj.dida.api.mapper.SwitchMapper;
import com.hdygxsj.dida.api.service.SwitchService;
import com.hdygxsj.dida.api.service.entity.SwitchDO;
import com.hdygxsj.dida.api.service.entity.SwitchValueDO;
import com.hdygxsj.dida.exceptions.Assert;
import com.hdygxsj.dida.exceptions.DidaRuntimeException;
import com.hdygxsj.dida.spi.engine.SwitchClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SwitchServiceImpl implements SwitchService {

    @Autowired(required = false)
    private SwitchClient switchClient;

    @Autowired
    private SwitchMapper switchMapper;

    @Override
    public void setValue(String group, String namespace, String key, String value) {
        Assert.notNull(switchClient, "开关引擎未装载");
        switchClient.beforeSetValue(group, namespace, key);
        switchClient.setValue(group, namespace, key, value);
        switchClient.afterSetValue(group, namespace, value);
    }

    @Override
    public String getValue(String group, String namespace, String key) {
        Assert.notNull(switchClient, "开关引擎未装载");
        return switchClient.getValue(group, namespace, key);
    }

    @Override
    public void add(SwitchDO switchDO, String group) {
        Assert.isTrue(!exsit(switchDO), "开关已存在，无法添加");
        String type = switchDO.getType();
        JSONObject jsonObject = JSONObject.parseObject(type);
        String defaultValue = jsonObject.getString("defaultValue");
        jsonObject.getString(defaultValue);
        String typeValue = jsonObject.getString("switchType");
        if ("0".equals(typeValue)) {
            if (StrUtil.isBlank(defaultValue)) {
                defaultValue = "false";
            }
        } else if ("1".equals(typeValue)) {
            JSONArray options = jsonObject.getJSONArray("options");
            Assert.isTrue(CollectionUtil.isNotEmpty(options), "多选开关的选项为空");
            for (int i = 0; i < options.size(); i++) {
                JSONObject temp = options.getJSONObject(i);
                String key = temp.getString("key");
                String value = temp.getString("value");
                Assert.isTrue(StrUtil.isAllNotBlank(key, value), "选项参数存在空值");
            }
            if (StrUtil.isBlank(defaultValue)) {
                defaultValue = options.getJSONObject(0).getString("value");
            }
        } else {
            throw new DidaRuntimeException("非法的开关类型");
        }
        switchClient.setValue(group, switchDO.getNamespaceCode(), switchDO.getSwitchKey(), defaultValue);
        switchMapper.insert(switchDO);
    }

    @Override
    public boolean exsit(SwitchDO switchDO) {
        return get(switchDO) != null;
    }

    @Override
    public SwitchDO get(SwitchDO switchDO) {
        QueryWrapper<SwitchDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("namespace_code", switchDO.getNamespaceCode());
        queryWrapper.eq("switch_key", switchDO.getSwitchKey());
        return switchMapper.selectOne(queryWrapper);
    }

    @Override
    public Page<SwitchDO> page(int pageNum, int pageSize, String group, String namespaceCode, String searchText) {
        Page<SwitchDO> switchDOPage = new Page<>(pageNum, pageSize);
        QueryWrapper<SwitchDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("namespace_code", namespaceCode);
        queryWrapper.and(StrUtil.isNotBlank(searchText),
                q -> q.eq("switch_key", searchText)
                        .or().eq("type", searchText));
        queryWrapper.orderByDesc("update_time");
        Page<SwitchDO> newPage = switchMapper.selectPage(switchDOPage, queryWrapper);
        List<SwitchDO> records = newPage.getRecords();
        for (int i = 0; i < records.size(); i++) {
            SwitchDO switchDO = records.get(i);
            SwitchValueDO switchValueDO = new SwitchValueDO(switchDO, switchClient.getValue(group, namespaceCode, switchDO.getSwitchKey()),group);
            records.set(i,switchValueDO);
        }
        return newPage;
    }
}
