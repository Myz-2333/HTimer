package cn.bitoffer.lottery.constant;

public class Constants {
    public static final int userPrizeMax = 20;    // 用户每天最多抽奖次数
    public static final int ipPrizeMax   = 30000; // 同一个IP每天最多抽奖次数
    public static final int ipLimitMax   = 3000;  // 同一个IP每天最多抽奖次数
    public static final int ipFrameSize   = 2;
    public static final int userFrameSize = 2;
    public static final int prizeCodeMax = 10000;
    public static final int prizeTypeVirtualCoin  = 0; // 虚拟币
    public static final int prizeTypeCouponSame   = 1; // 虚拟券，相同的码
    public static final int prizeTypeCouponDiff   = 2; // 虚拟券，不同的码
    public static final int prizeTypeEntitySmall  = 3; // 实物小奖
    public static final int prizeTypeEntityMiddle = 4; // 实物中等将
    public static final int prizeTypeEntityLarge  = 5; // 实物大奖
    public static final int defaultBlackTime    = 7;   // 默认1周
    public static final int allPrizeCacheTime   = 30 * 86400; // 默认1周
    public static final int couponDiffLockLimit = 10000000;

    public static final String allPrizeCacheKey  = "all_prize";
    public static final String userCacheKeyPrefix      = "black_user_info_";
    public static final String ipCacheKeyPrefix        = "black_ip_info_";
    public static final String userLotteryDayNumPrefix = "user_lottery_day_num_";
    public static final String ipLotteryDayNumPrefix = "ip_lottery_day_num_";
    public static final String prizePoolCacheKey       = "prize_pool";
    public static final String prizeCouponCacheKey     = "prize_coupon_";
    public static final String lotteryLockKeyPrefix = "lottery_lock";

}
