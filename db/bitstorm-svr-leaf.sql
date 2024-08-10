USE leaf;

CREATE TABLE `leaf_alloc`
(
    `biz_tag`     varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '区分业务',
    `max_id`      bigint unsigned                                               NOT NULL DEFAULT '1' COMMENT '该biz_tag目前所被分配的ID号段的最大值',
    `step`        int                                                           NOT NULL COMMENT '每次分配的号段长度',
    `update_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `description` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '描述',
    `random_step` int unsigned                                                  NOT NULL DEFAULT '1' COMMENT '每次getid时随机增加的长度，这样就不会有连续的id了',
    PRIMARY KEY (`biz_tag`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

# 测试 key
INSERT INTO `leaf_alloc`
VALUES ('test', 1, 100, '2023-09-01 08:00:00', '测试', 100);
