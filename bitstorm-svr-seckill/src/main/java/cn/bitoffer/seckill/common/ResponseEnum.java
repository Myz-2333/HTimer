package cn.bitoffer.seckill.common;

public enum ResponseEnum {
    OK(0, "success"),
    FAIL(1, "failed"),
    ERR_INPUT_INVALID                 ( 8020, "input invalid"),
    ERR_SHOULD_BIND                   ( 8021, "should bind failed"),
    ERR_JSON_MARSHAL                  ( 8022, "json marshal failed"),
    ERR_FIND_GOODS_FAILED             ( 8101, "商品查询失败"),
    ERR_GOODS_STOCK_NOT_ENOUGH        ( 8102, "商品库存不足"),
    ERR_CREATE_ORDER_FAILED           ( 8103, "订单创建失败"),
    ERR_CREATE_SECKILL_RECORD_FAILED  ( 8104, "秒杀记录创建失败"),
    ERR_RECORD_USER_KILLED_NUM_FAILED ( 8105, "记录用户已经秒杀到的名额数失败"),
    ERR_DESC_STOCK_FAILED             ( 8106, "库存扣减失败"),
    ERR_CREATER_USER_QUOTA_FAILED     ( 8107, "插入用户限额记录失败"),
    ERR_USER_QUOTA_NOT_ENOUGH         ( 8108, "用户额度不足"),
    ERR_FIND_USER_QUOTA_FAILED        ( 8109, "查询用户额度失败"),


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
