package cn.bitoffer.asyncflow.common;

public enum ResponseEnum {
    OK(0, "ok"), FAIL(1, "fail"), CREATE_TASK_FAIL(2, "create task fail"), GET_TASK_FAIL(3, "get task fail"), HOLD_TASKS_FAIL(4, "hold tasks fail"), GET_TASK_LIST_FAIL(5, "get task list fail"), Set_Task_Fail(6, "set task fail");
    private final int code;
    private final String message;

    ResponseEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }
}
