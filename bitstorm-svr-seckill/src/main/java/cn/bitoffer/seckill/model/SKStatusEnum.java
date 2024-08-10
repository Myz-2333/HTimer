package cn.bitoffer.seckill.model;

public enum SKStatusEnum {
    SK_STATUS_BEFORE_ORDER(1),
    SK_STATUS_BEFORE_PAY(2),
    SK_STATUS_PAYED(3),
    SK_STATUS_OOT(4),
    SK_STATUS_CANCEL(5);

    private int value;

    SKStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
