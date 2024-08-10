package cn.bitoffer.lottery.common;

public enum ErrorCode {

    SUCCESS(0, "success"),
    ERR_INTERNAL_SERCER(500, "internal server error"),
    ERR_INPUT_INVALID(8020, "input invalid"),
    ERR_SHOULD_BIND(8021, "should bind failed"),
    ERR_JSON_PARSE(8022, "json marshal failed"),
    ERR_JWT_PARSE(8023, "jwt marshal failed"),
    ERR_LOGIN(10000, "login fail"),
    ERR_IP_LIMIT_INVALID(10001, "ip day num limited"),
    ERR_USER_LIMIT_INVALID(10002, "user day num limited"),
    ERR_BLACK_IP(10003, "blacked ip"),
    ERR_BLACK_USER(10004, "blacked user"),
    ERR_NOT_WON(10005, "not won,please try again!"),
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
