package com.zdf.worker.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AsyncTaskSetStage {

    private String task_stage; //NOT NULL DEFAULT '', 存储任务阶段信息

    private int status; //tinyint(3) unsigned NOT NULL DEFAULT '0',

    private NftTaskContext task_context;// varchar(8192) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '任务上下文，用户自定义',

}
