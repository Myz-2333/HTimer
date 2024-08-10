package com.zdf.worker.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ScheduleLog {
    ScheduleData lastData;
    List<ScheduleData> historyDatas;
    public ScheduleLog() {
        lastData = new ScheduleData();
        historyDatas = new ArrayList<>();
    }
}
