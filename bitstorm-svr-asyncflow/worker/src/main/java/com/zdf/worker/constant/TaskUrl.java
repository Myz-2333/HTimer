package com.zdf.worker.constant;

/**
 * 请求URL常量
 */
public class TaskUrl {
    public final static String IPORT = "http://localhost:8081";
    public final static String CREATE_TASK = "/task/create_task";
    public final static String SET_TASK = "/task/set_task";
    public final static String GET_TASK = "/task/get_task";
    public final static String GET_TASK_LIST = "/task/task_list";
    public final static String HOLD_TASK = "/task/hold_task";
    public final static String GET_CFG_LIST = "/task_schedule_cfg/list";
    public final static String GET_USER_TASK_LIST = "/task/user_task_list";
    public final static String CREATE_TASK_CFG = "/task_schedule_cfg/task_configuration";

}
