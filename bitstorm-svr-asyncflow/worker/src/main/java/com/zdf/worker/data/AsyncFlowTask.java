package com.zdf.worker.data;

import lombok.Data;

@Data
public class AsyncFlowTask {
    
    private Long id;
    
    private String user_id; //NOT NULL DEFAULT '',
    
    private String task_id; // NOT NULL DEFAULT '',
    
    private String task_type; //NOT NULL DEFAULT '',  存储任务的全类名
    
    private String task_stage; //NOT NULL DEFAULT '', 存储任务阶段信息
    
    private int status; //tinyint(3) unsigned NOT NULL DEFAULT '0',

    private long order_time;

    private int priority;
    
    private int crt_retry_num; //NOT NULL DEFAULT '0' COMMENT '已经重试几次了',
    
    private int max_retry_num; //NOT NULL DEFAULT '0' COMMENT '最大能重试几次',
    
    private int max_retry_interval;// int(11) NOT NULL DEFAULT '0' COMMENT '最大重试间隔',
    
    private String schedule_log;// varchar(4096) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '调度信息记录',
    
    private String task_context;// varchar(8192) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '任务上下文，用户自定义',
    
    private Long create_time;// datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    private Long modify_time;// datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,


}
