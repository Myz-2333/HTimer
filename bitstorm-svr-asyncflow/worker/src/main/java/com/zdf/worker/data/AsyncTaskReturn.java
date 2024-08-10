package com.zdf.worker.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsyncTaskReturn {
    private String user_id; //NOT NULL DEFAULT '',

    private String task_id; // NOT NULL DEFAULT '',

    private String task_type; //NOT NULL DEFAULT '',

    private String task_stage; //NOT NULL DEFAULT '',

    private long order_time;

    private int priority;

    private int status; //tinyint(3) unsigned NOT NULL DEFAULT '0',

    private int crt_retry_num; //NOT NULL DEFAULT '0' COMMENT '已经重试几次了',

    private int max_retry_num; //NOT NULL DEFAULT '0' COMMENT '最大能重试几次',

    private int max_retry_interval;// int(11) NOT NULL DEFAULT '0' COMMENT '最大重试间隔',

    private String schedule_log;// varchar(4096) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '调度信息记录',

    private String task_context;

    private Long create_time;

    private Long modify_time;

}
