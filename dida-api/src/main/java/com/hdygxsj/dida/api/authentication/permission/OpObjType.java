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

package com.hdygxsj.dida.api.authentication.permission;

import lombok.Getter;

@Getter
public enum OpObjType {
    SYSTEM("system", "系统","cluster"),
    CLUSTER("cluster", "集群","cluster"),
    ROLE("role", "角色","cluster"),
    GROUP("group", "组","cluster"),
    NAMESPACE("namespace", "命名空间","group"),
    SWITCH("switch","开关","namespace"),
    USER_ROLE("user_role","用户权限","cluster"),
    USER("user","用户","cluster");

    private String type;

    private String descp;

    private String belong;

    OpObjType(String type, String descp,String belong) {
        this.type = type;
        this.descp = descp;
        this.belong = belong;
    }
}
