package com.zdf.worker.task;

import com.alibaba.fastjson.JSON;
import com.zdf.worker.constant.UserConfig;
import com.zdf.worker.data.AsyncFlowClientData;
import com.zdf.worker.data.NftTaskContext;
import com.zdf.worker.data.ScheduleLog;

import java.lang.reflect.Method;

/**
 * @author zhangdafeng
 */
public class TaskBuilder {

    public static AsyncFlowClientData build(AsyncExecutable executable) throws NoSuchMethodException {
        Class<? extends AsyncExecutable> aClass = executable.getClass();
        Method handProcess = aClass.getMethod("handleProcess");
        return TaskBuilder.build(aClass, handProcess.getName(), new Object[0], new Class[0]);
    }

    // 利用类信息创建任务
    public static AsyncFlowClientData build(Class<?> clazz, String methodName, Object[] params, Class<?>[] parameterTypes, Object... envs) {
        if (!AsyncExecutable.class.isAssignableFrom(clazz)) {
            throw new RuntimeException("The task must be implemented TaskDefinition!");
        }
        checkParamsNum(params, parameterTypes);
        Method method = getMethod(clazz, methodName, params, parameterTypes);

        // 获取类名
        String taskType = method.getDeclaringClass().getSimpleName();
        // get 方法名
        String taskStage = method.getName();
        // 调度日志
        ScheduleLog sl = new ScheduleLog();
        String scheduleLog = JSON.toJSONString(sl);

        // 上下文信息
        NftTaskContext nftTaskContext = new NftTaskContext(params, envs, parameterTypes);
        String taskContext = JSON.toJSONString(nftTaskContext);
        return new AsyncFlowClientData(
                UserConfig.USERID,
                taskType,
                taskStage,
                scheduleLog,
                taskContext
        );
    }

    public static void checkParamsNum(Object[] params, Class<?>[] parameterTypes) {
        // 参数个数检验
        if (params.length != parameterTypes.length) {
            throw new RuntimeException("Parameters are invalid!");
        }
    }

    public static Method getMethod(Class<?> clazz, String methodName, Object[] params, Class<?>[] parameterTypes) {
        Method method = null;
        for (Method clazzMethod : clazz.getMethods()) {
            // 获取对应要执行的方法
            if (clazzMethod.getName().equals(methodName) && clazzMethod.getParameterCount() == params.length && judgeParamsTypes(clazzMethod, parameterTypes)) {
                method = clazzMethod;
            }
        }

        return method;
    }


    private static boolean judgeParamsTypes(Method clazzMethod, Class<?>[] parameterTypes) {
        Class<?>[] types = clazzMethod.getParameterTypes();
        for (int i = 0; i < types.length; i++) {
            if (types[i] != parameterTypes[i]) {
                return false;
            }
        }
        return true;
    }
}
