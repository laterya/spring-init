CREATE DATABASE IF NOT EXISTS spring_init;

USE spring_init;

-- 用户表
create table if not exists spring_init.`user`
(
    `id`          bigint                                not null auto_increment comment '主键' primary key,
    `user_name`   varchar(32)                           null comment '用户名',
    `password`    varchar(256)                          null comment '密码',
    `phone`       varchar(32)                           not null comment '手机号',
    `user_role`   varchar(16) default 'user'            not null comment '用户角色',
    `create_time` datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time` datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `is_deleted`  tinyint     default 0                 not null comment '是否删除(0-未删, 1-已删)',
    UNIQUE KEY `idx_phone` (`phone`)
) comment '用户表';

-- 文章表
create table if not exists spring_init.`article`
(
    `id`               bigint                             not null auto_increment comment '主键' primary key,
    `user_id`          bigint                             not null comment '作者id',
    `title`            varchar(32)                        null comment '标题',
    `content`          text                               null comment '文章内容',
    `article_category` tinyint                            null comment '文章分类',
    `tags`             varchar(128)                       null comment '标签，可自定义',
    `commend_nums`     int      default 0                 not null comment '点赞数',
    `collection_nums`  int      default 0                 not null comment '收藏数',
    `create_time`      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time`      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `is_deleted`       tinyint  default 0                 not null comment '是否删除(0-未删, 1-已删)'
) comment '文章表';

-- 文章分类表
create table if not exists spring_init.`category`
(
    `id`            tinyint                            not null auto_increment comment '主键' primary key,
    `category_name` varchar(32)                        not null comment '类别名',
    `create_time`   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time`   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `is_deleted`    tinyint  default 0                 not null comment '是否删除(0-未删, 1-已删)'
) comment '文章分类表';

-- 文章点赞表
create table if not exists spring_init.`article_commend`
(
    `id`          bigint                             not null auto_increment comment '主键' primary key,
    `article_id`  bigint                             not null comment '点赞文章id',
    `user_id`     bigint                             not null comment '点赞人id',
    `create_time` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `is_deleted`  tinyint  default 0                 not null comment '是否删除(0-未删, 1-已删)'
) comment '文章点赞表';

-- 文章收藏表
create table if not exists spring_init.`article_collection`
(
    `id`          bigint                             not null auto_increment comment '主键' primary key,
    `article_id`  bigint                             not null comment '收藏文章id',
    `user_id`     bigint                             not null comment '收藏人id',
    `create_time` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `is_deleted`  tinyint  default 0                 not null comment '是否删除(0-未删, 1-已删)'
) comment '文章收藏表';