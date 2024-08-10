package cn.bitoffer.lottery.model;

import cn.bitoffer.lottery.common.ErrorCode;

public class LotteryResult {
    private ErrorCode errcode;
    private  Long userId;
    LotteryPrizeInfo lotteryPrize;

    public ErrorCode getErrcode() {
        return errcode;
    }

    public void setErrcode(ErrorCode errcode) {
        this.errcode = errcode;
    }

    public LotteryPrizeInfo getLotteryPrize() {
        return lotteryPrize;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setLotteryPrize(LotteryPrizeInfo lotteryPrize) {
        this.lotteryPrize = lotteryPrize;
    }
}
