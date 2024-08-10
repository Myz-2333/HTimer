package com.zdf.worker.Client;

import com.zdf.worker.data.ScheduleConfig;

public class TaskCfgBuilder {
    // 构建任务配置
    public ScheduleConfig build(Class<?> clazz, int schedule_limit, int schedule_interval, int max_processing_time, int max_retry_num, int retry_interval) {
        return new ScheduleConfig(clazz.getSimpleName(), schedule_limit, schedule_interval, max_processing_time, max_retry_num, retry_interval, 0L, 0L);
    }
}
