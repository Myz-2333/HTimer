package cn.bitoffer.lottery.common;

public enum ResponseEnum {
    OK(0, "ok"),
    FAIL(1, "fail"),
    ;
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
