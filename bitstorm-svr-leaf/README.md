# leaf service model

分布式 ID 生成器

## 部署

目前的版本是基于 MySQL 实现的，首先创建 leaf database，定义表结构

```sql
create schema leaf;

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
```

定义业务服务 key (test)
```sql
# key 用来区分不同业务线，全局唯一
INSERT INTO `leaf_alloc`
VALUES ('test', 1, 100, '2023-09-01 08:00:00', '测试', 100);
```

修改 leaf 服务 application.yml 配置

```yml
spring:
  application:
    name: @artifactId@
  datasource:
    url: jdbc:mysql://${MYSQL_HOST:192.168.5.13}:${MYSQL_PORT:3306}/${MYSQL_DATABASE:leaf}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true&useAffectedRows=true
    username: ${MYSQL_USERNAME:witt}
    password: ${MYSQL_PASSWORD:Witt@2023}
  cloud:
    nacos:
      discovery:
        server-addr: ${NACOS_HOST:192.168.5.11}:${NACOS_PORT:8848}
        username: nacos
        password: nacos
```

启动 & 测试

```bash
curl http://localhost:8080/insider/segment?key=test
```

## 参考
<https://tech.meituan.com/2019/03/07/open-source-project-leaf.html>  
<https://tech.meituan.com/2017/04/21/mt-leaf.html>  
<https://github.com/Meituan-Dianping/Leaf>