/*
SQLyog Ultimate v13.1.1 (64 bit)
MySQL - 8.0.25 : Database - mall4cloud_order
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`bitstorm-svr` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 */;

USE `bitstorm-svr`;

/*Table structure for table `xtimer` */

DROP TABLE IF EXISTS `xtimer`;

CREATE TABLE `xtimer` (
                           `timer_id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'TimerId',
                           `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `modify_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           `app` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'app',
                           `name` varchar(256) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'name',
                           `status` tinyint NOT NULL DEFAULT '0' COMMENT '0新建，1激活，2未激活',
                           `cron` varchar(256) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'cron表达式',
                           `notify_http_param` varchar(8192)  COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '回调上下文',
                           PRIMARY KEY (`timer_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Timer 信息';


/*Table structure for table `timer_task` */
DROP TABLE IF EXISTS `timer_task`;

CREATE TABLE `timer_task` (
                         `task_id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'taskId',
                         `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `modify_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         `timer_id` bigint unsigned NOT NULL COMMENT 'TimerId',
                         `app` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'app',
                         `output` varchar(1028) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'output',
                         `status` tinyint NOT NULL DEFAULT '0' COMMENT '0新建，1激活，2未激活',
                         `run_timer` bigint COMMENT '运行时间',
                         `cost_time` bigint unsigned NOT NULL COMMENT '执行耗时',
                         PRIMARY KEY (`task_id`) USING BTREE,
                         UNIQUE KEY `idx_timer_id_run_timer` (`timer_id`,`run_timer`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Timer Task任务信息';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
