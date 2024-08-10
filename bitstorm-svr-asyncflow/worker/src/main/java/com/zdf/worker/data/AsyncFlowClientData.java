package com.zdf.worker.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsyncFlowClientData {

    private String user_id;

    private String task_type;
    
    private String task_stage;

    private String schedule_log;

    private String task_context;




}
