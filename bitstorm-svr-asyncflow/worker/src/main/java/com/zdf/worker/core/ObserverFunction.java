package com.zdf.worker.core;

import com.zdf.worker.data.AsyncTaskBase;
import com.zdf.worker.data.AsyncTaskReturn;
import com.zdf.worker.data.AsyncTaskSetStage;
import com.zdf.worker.data.ScheduleConfig;

import java.util.List;

public interface ObserverFunction {
    void onBoot();
    void onObtain(List<AsyncTaskReturn> taskList, List<AsyncTaskBase> asyncTaskBaseList);
    void onExecute(AsyncTaskBase asyncTaskReturn);
    void onFinish(AsyncTaskBase asyncTaskReturn, AsyncTaskSetStage asyncTaskSetStage, Class<?> aClass);
    void onStop(AsyncTaskBase asyncTaskReturn);
    void onError(AsyncTaskBase asyncTaskReturn, ScheduleConfig scheduleConfig, List<AsyncTaskBase> asyncTaskBaseList, Class<?> aClass, Exception e);

}
