use dida;

create table if not exists dida.dida_group
(
    name        varchar(20)                        not null,
    code        varchar(20)                        not null
        primary key,
    create_time datetime default CURRENT_TIMESTAMP null,
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    descp       varchar(500)                       null
);

create table if not exists dida.dida_namespace
(
    code        varchar(10)                        not null
        primary key,
    name        varchar(50)                        null,
    descp       varchar(200)                       null,
    create_time datetime default CURRENT_TIMESTAMP null,
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);

create table if not exists dida.dida_role
(
    code         varchar(10)                        not null
        primary key,
    name         varchar(20)                        null,
    cluster_role tinyint                            null,
    create_time  datetime default CURRENT_TIMESTAMP null
);

create table if not exists dida.dida_role_object_rel
(
    obejct_type varchar(10)                        not null,
    object_name varchar(50)                        not null,
    role_code   varchar(10)                        not null,
    permission  bigint                             null,
    create_time datetime default CURRENT_TIMESTAMP null,
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    primary key (role_code, obejct_type, object_name)
);

create table if not exists dida.dida_swith
(
    namespace_code varchar(10)                        not null,
    swith_key      varchar(20)                        not null,
    type           text                               not null,
    create_time    datetime default CURRENT_TIMESTAMP null,
    update_time    datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    primary key (namespace_code, swith_key)
);

create table if not exists dida.dida_token
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
    on dida.dida_token (username);

create table if not exists dida.dida_user
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

create table if not exists dida.dida_user_group_rel
(
    username    varchar(20)                        not null,
    group_code  varchar(20)                        not null,
    create_time datetime default CURRENT_TIMESTAMP null,
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    primary key (group_code, username)
);

create table if not exists dida.dida_user_role_rel
(
    role_code   varchar(10)                        not null,
    username    varchar(20)                        not null,
    create_time datetime default CURRENT_TIMESTAMP null,
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    primary key (username, role_code)
);

