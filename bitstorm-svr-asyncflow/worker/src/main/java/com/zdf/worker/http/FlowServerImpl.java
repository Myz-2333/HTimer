package com.zdf.worker.http;

import com.alibaba.fastjson.JSON;
import com.zdf.worker.constant.TaskUrl;
import com.zdf.worker.data.AsyncTaskRequest;
import com.zdf.worker.data.AsyncTaskSetRequest;
import com.zdf.worker.data.ReturnStatus;
import com.zdf.worker.data.ScheduleConfig;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// 利用OKhttp发起端口请求
public class FlowServerImpl implements FlowServer {
    OkHttpClient client = new OkHttpClient();

    // get方法
    public ReturnStatus get(String url) {

        Request request = new Request.Builder().url(url)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            String result = response.body().string();
            return JSON.parseObject(result, ReturnStatus.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 拼接url参数
    private String getParamStr(Map<String, String> params) {
        StringBuffer sb = new StringBuffer();
        sb.append("?");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    // post请求
    public <E> ReturnStatus post(String url, E body) {
        Request request = new Request.Builder()
                .addHeader("content-type", "application/json")
                .url(TaskUrl.IPORT + url)
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(body)))
                .build();

        String result;
        try {
            result = client.newCall(request).execute().body().string();
            return JSON.parseObject(result, ReturnStatus.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    // 获取任务列表url
    @Override
    public ReturnStatus getTaskList(String taskType, int status, int limit) {
        Map<String, String> params = new HashMap<String, String>() {{
            put("task_type", taskType);
            put("status", status + "");
            put("limit", limit + "");
        }};
        String url = TaskUrl.IPORT + TaskUrl.HOLD_TASK + getParamStr(params);
        return get(url);
    }

    // 调用创建任务接口
    @Override
    public ReturnStatus createTask(AsyncTaskRequest asyncTaskRequest) {
        return post(TaskUrl.CREATE_TASK, asyncTaskRequest);
    }

    // 调用更改任务信息接口
    @Override
    public ReturnStatus setTask(AsyncTaskSetRequest asyncTaskSetRequest) {
        return post(TaskUrl.SET_TASK, asyncTaskSetRequest);
    }

    // 通过task_id获取任务
    @Override
    public ReturnStatus getTask(String taskId) {
        Map<String, String> params = new HashMap<>();
        params.put("task_id", taskId);
        String url = TaskUrl.IPORT + TaskUrl.GET_TASK + getParamStr(params);
        return get(url);
    }

    // 获取任务配置信息
    @Override
    public ReturnStatus getTaskTypeCfgList() {
       return get(TaskUrl.IPORT + TaskUrl.GET_CFG_LIST);
    }

    // 根据任务状态获取用户对应的任务列表
    @Override
    public ReturnStatus getUserTaskList(String user_id, int statusList) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("statusList", statusList + "");
        return get(TaskUrl.IPORT + TaskUrl.GET_USER_TASK_LIST + getParamStr(params));
    }

    // 创建任务配置信息接口
    @Override
    public ReturnStatus createTaskCFG(ScheduleConfig scheduleConfig) {
        return post(TaskUrl.IPORT + TaskUrl.CREATE_TASK_CFG, scheduleConfig);
    }


}
