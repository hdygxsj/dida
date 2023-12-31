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

package com.hdygxsj.dida.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdygxsj.dida.api.service.entity.SwitchDO;

public interface SwitchService {


    void setValue(String group, String namespace, String key, String value);

    String getValue(String group, String namespace, String key);

    void add(SwitchDO switchDO,String group);

    boolean exsit(SwitchDO switchDO);

    SwitchDO get(SwitchDO switchDO);

    Page<SwitchDO> page(int pageNum, int pageSize,String group, String namespaceCode, String searchText);
}
