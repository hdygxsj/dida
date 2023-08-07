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

use dida;

create table dida_group
(
    name        varchar(20)                        not null,
    code        varchar(20)                        not null
        primary key,
    create_time datetime default CURRENT_TIMESTAMP null,
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    descp       varchar(500)                       null
);

create table dida_namespace
(
    code        varchar(20)                        not null
        primary key,
    name        varchar(50)                        null,
    descp       varchar(200)                       null,
    create_time datetime default CURRENT_TIMESTAMP null,
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    group_code  varchar(20)                        null
);

create index dida_namespace_group_index
    on dida_namespace (group_code);

create table dida_role
(
    code         varchar(10)                        not null
        primary key,
    name         varchar(20)                        null,
    cluster_role tinyint                            null,
    create_time  datetime default CURRENT_TIMESTAMP null
);

create table dida_role_object_rel
(
    obejct_type varchar(10)                        not null,
    role_code   varchar(10)                        not null,
    permission  bigint                             null,
    create_time datetime default CURRENT_TIMESTAMP null,
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    primary key (role_code, obejct_type)
);

create table dida_swith
(
    namespace_code varchar(10)                        not null,
    swith_key      varchar(20)                        not null,
    type           text                               not null,
    create_time    datetime default CURRENT_TIMESTAMP null,
    update_time    datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    primary key (namespace_code, swith_key)
);

create table dida_token
(
    id          int auto_increment
        primary key,
    username    varchar(20)                        not null,
    login_ip    varchar(20)                        null,
    exp_time    int      default 180000            null,
    create_time datetime default CURRENT_TIMESTAMP null,
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    token       varchar(500)                       null,
    constraint dida_token_token_uindex
        unique (token)
);

create index dida_token_username_index
    on dida_token (username);

create table dida_user
(
    username    varchar(20)                        not null
        primary key,
    password    varchar(50)                        null,
    create_time datetime default CURRENT_TIMESTAMP null,
    type        tinyint  default 1                 not null comment '0-停用；1-启用',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    super       tinyint  default 0                 null
)
    comment '用户';

create table dida_user_group_rel
(
    username    varchar(20)                        not null,
    group_code  varchar(20)                        not null,
    create_time datetime default CURRENT_TIMESTAMP null,
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    primary key (group_code, username)
);

create table dida_user_role_rel
(
    role_code   varchar(10)                        not null,
    username    varchar(20)                        not null,
    create_time datetime default CURRENT_TIMESTAMP null,
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    primary key (username, role_code)
);


INSERT INTO dida.dida_user (username, password, create_time, type, update_time, super) VALUES ('admin', '0f94644c67a7ee820f769bafd7e527ec', '2023-08-01 00:09:59', 1, '2023-08-01 00:09:59', 1);
