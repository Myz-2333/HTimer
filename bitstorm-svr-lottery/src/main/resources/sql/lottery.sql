use lottery_system;

DROP TABLE IF EXISTS `t_prize`;
CREATE TABLE `t_prize`
(
    `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
    `title` varchar(255) NOT NULL DEFAULT '' COMMENT '奖品名称',
    `prize_num` int(11) NOT NULL DEFAULT '-1' COMMENT '奖品数量，0 无限量，>0限量，<0无奖品',
    `left_num` int(11) NOT NULL DEFAULT '0' COMMENT '剩余数量',
    `prize_code` varchar(50) NOT NULL DEFAULT '' COMMENT '0-9999表示100%，0-0表示万分之一的中奖概率',
    `prize_time` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '发奖周期，多少天，以天为单位',
    `img` varchar(255) NOT NULL DEFAULT '' COMMENT '奖品图片',
    `display_order` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '位置序号，小的排在前面',
    `prize_type` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '奖品类型，1-虚拟币，2-虚拟券，3-实物小奖，4-实物大奖',
    `prize_profile` varchar(255) NOT NULL DEFAULT '' COMMENT '奖品扩展数据，如：虚拟币数量',
    `begin_time` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '奖品有效周期：开始时间',
    `end_time` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '奖品有效周期：结束时间',
    `prize_plan` mediumtext COMMENT '发奖计划，[[时间1,数量1],[时间2,数量2]]',
    `prize_begin` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '发奖计划周期的开始',
    `prize_end` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '发奖计划周期的结束',
    `sys_status` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '状态，1-正常，2-删除',
    `sys_created` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '创建时间',
    `sys_updated` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT'修改时间',
    `sys_ip` varchar(50) NOT NULL DEFAULT '' COMMENT '操作人IP',
    PRIMARY KEY (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='奖品表';


DROP TABLE IF EXISTS `t_coupon`;
CREATE TABLE `t_coupon` (
                            `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                            `prize_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '奖品ID，关联lt_prize表',
                            `code` varchar(255) NOT NULL DEFAULT '' COMMENT '虚拟券编码',
                            `sys_created` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '创建时间',
                            `sys_updated` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '更新时间',
                            `sys_status` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '状态，1-正常，2-作废，3-已发放',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `uk_code` (`code`),
                            KEY `idx_prize_id` (`prize_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='优惠券表';


DROP TABLE IF EXISTS `t_result`;
CREATE TABLE `t_result` (
                            `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                            `prize_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '奖品ID，关联lt_prize表',
                            `prize_name` varchar(255) NOT NULL DEFAULT '' COMMENT '奖品名称',
                            `prize_type` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '奖品类型，同lt_prize. gtype',
                            `user_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '用户ID',
                            `user_name` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名',
                            `prize_code` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '抽奖编号（4位的随机数）',
                            `prize_data` varchar(255) NOT NULL DEFAULT '' COMMENT '获奖信息',
                            `sys_created` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '创建时间',
                            `sys_ip` varchar(50) NOT NULL DEFAULT '' COMMENT '用户抽奖的IP',
                            `sys_status` smallint(5) unsigned NOT NULL DEFAULT '1' COMMENT '状态，1-正常，2-删除，3-作弊',
                            PRIMARY KEY (`id`),
                            KEY `idx_user_id` (`user_id`),
                            KEY `idx_prize_id` (`prize_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='抽奖记录表';


DROP TABLE IF EXISTS `t_black_user`;
CREATE TABLE `t_black_user` (
                                `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                `user_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '用户ID',
                                `user_name` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名',
                                `black_time` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '黑名单限制到期时间',
                                `real_name` varchar(50) NOT NULL DEFAULT '' COMMENT '真是姓名',
                                `mobile` varchar(50) NOT NULL DEFAULT '' COMMENT '手机号',
                                `address` varchar(255) NOT NULL DEFAULT '' COMMENT '联系地址',
                                `sys_created` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '创建时间',
                                `sys_updated` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '修改时间',
                                `sys_ip` varchar(50) NOT NULL DEFAULT '' COMMENT 'IP地址',
                                PRIMARY KEY (`id`),
                                KEY `idx_user_name` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户黑明单表';


DROP TABLE IF EXISTS `t_black_ip`;
CREATE TABLE `t_black_ip` (
                              `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                              `ip` varchar(50) NOT NULL DEFAULT '' COMMENT 'IP地址',
                              `black_time` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '黑名单限制到期时间',
                              `sys_created` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '创建时间',
                              `sys_updated` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '修改时间',
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `uk_ip` (`ip`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 comment='ip黑明单表';


DROP TABLE IF EXISTS `t_lottery_times`;
CREATE TABLE `t_lottery_times` (
                                   `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                   `user_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '用户ID',
                                   `day` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '日期，如：20220625',
                                   `num` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '次数',
                                   `sys_created` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '创建时间',
                                   `sys_updated` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '修改时间',
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `idx_user_id_day` (`user_id`,`day`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 comment='用户每日抽奖次数表';


DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
                          `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                          `user_name` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名',
                          `pass_word` varchar(255) NOT NULL DEFAULT '' COMMENT '用户密码',
                          `signature`  varchar(255) NOT NULL DEFAULT '' COMMENT '登录用户签名',
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 comment='用户表';