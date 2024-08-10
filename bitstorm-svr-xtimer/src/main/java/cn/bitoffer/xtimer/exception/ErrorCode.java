package cn.bitoffer.xtimer.exception;

/**
 * 自定义错误码
 */
public enum ErrorCode {

    SUCCESS(0, "ok"),
    UNKNOWN_ERROR(90001, "未知异常"),
    SYSTEM_ERROR(90002, "系统内部异常"),
    PARAMS_ERROR(90003, "请求参数错误"),

    ;


    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
