package com.zdf.worker.task;

import com.zdf.worker.data.AsyncTaskSetStage;
import com.zdf.worker.data.NftTaskContext;
import com.zdf.worker.enums.TaskStatus;

import java.lang.reflect.Method;

public interface AsyncExecutable<T> {
    TaskRet<T> handleProcess();
    TaskRet<T> handleFinish();
    TaskRet<T> handleError();
    TaskRet<T> contextLoad(String context);

    default AsyncTaskSetStage setStage(Class<?> clazz, String methodName, Object[] params, Class<?>[] parameterTypes, Object... envs) {
        return build(clazz, methodName, params, parameterTypes, envs);
    }


    // 利用类信息创建任务
    default AsyncTaskSetStage build(Class<?> clazz, String methodName, Object[] params, Class<?>[] parameterTypes, Object... envs) {
        TaskBuilder.checkParamsNum(params, parameterTypes);
        Method method = TaskBuilder.getMethod(clazz, methodName, params, parameterTypes);

        // get 方法名
        String taskStage = method.getName();

        // 上下文信息
        NftTaskContext nftTaskContext = new NftTaskContext(params, envs, parameterTypes);
        return AsyncTaskSetStage.builder()
                .status(TaskStatus.PENDING.getStatus())
                .task_context(nftTaskContext)
                .task_stage(taskStage)
                .build();
    }

    default boolean judgeParamsTypes(Method clazzMethod, Class<?>[] parameterTypes) {
        Class<?>[] types = clazzMethod.getParameterTypes();
        for (int i = 0; i < types.length; i++) {
            if (types[i] != parameterTypes[i]) {
                return false;
            }
        }
        return true;
    }
}
