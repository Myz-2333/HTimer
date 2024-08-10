package com.zdf.worker.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleConfig {
    private String task_type;
    private Integer schedule_limit;
    private Integer schedule_interval;
    private Integer max_processing_time;
    private Integer max_retry_num;
    private Integer retry_interval;
    private Long create_time;
    private Long modify_time;
}
