package cn.bitoffer.lottery.service.impl;

import cn.bitoffer.lottery.common.ErrorCode;
import cn.bitoffer.lottery.controller.ViewPrize;
import cn.bitoffer.lottery.mapper.*;
import cn.bitoffer.lottery.model.*;
import cn.bitoffer.lottery.service.LotteryService;
import cn.bitoffer.lottery.constant.Constants;
import cn.bitoffer.lottery.redis.RedisUtil;
import cn.bitoffer.lottery.utils.UtilTools;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class LotteryServiceImpl1 implements LotteryService {
    @Autowired
    protected BlackIpMapper blackIpMapper;

    @Autowired
    protected BlackUserMapper blackUserMapper;

    @Autowired
    protected LotteryTimesMapper lotteryTimesMapper;

    @Autowired
    protected PrizeMapper prizeMapper;

    @Autowired
    protected CouponMapper couponMapper;

    @Autowired
    protected ResultMapper resultMapper;

    @Autowired
    protected RedisUtil redisUtil;

    @Autowired
    protected StringRedisTemplate redisTemplate;

    @Autowired
    RedissonClient redissonClient;


    @Override
    public LotteryResult lottery(Long userID, String userName, String ip) throws ParseException {
        LotteryResult lotteryResult = new LotteryResult();
        lotteryResult.setUserId(userID);
        String lockKey = String.format(Constants.lotteryLockKeyPrefix+"%d", userID);
        RLock lock = redissonClient.getLock(lockKey);
        /*
        ******** 抽奖逻辑*******
        */
        if (lock.tryLock()) {
            log.info("get lock success!!!!!!!!");
            try {
                getLuckyV1(lotteryResult, userID, userName,ip);
            }finally {
                lock.unlock();
            }
        }
        return lotteryResult;
    }

    public boolean checkUserDayLotteryTimes(Long userId){
        LotteryTimes userLotteryTimes = getUserCurrentLotteryTimes(userId);
        if (userLotteryTimes != null) {
            if (userLotteryTimes.getNum() > Constants.userPrizeMax){
                log.info("checkUserDayLotteryTimes|user_id: {}, lottery times: {}",userId,userLotteryTimes.getNum());
                return false;
            }
            lotteryTimesMapper.update(userLotteryTimes);
            return true;
        }
        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);  // 获取当前年
        int m  = calendar.get(Calendar.MONTH) + 1; // 获取当前月
        int d = calendar.get(Calendar.DATE); // 获取当前日
        String strDay = String.format("%d%02d%02d", y, m, d);
        int day = Integer.parseInt(strDay);
        LotteryTimes lotteryTimesInfo = new LotteryTimes();
        lotteryTimesInfo.setUserId(userId);
        lotteryTimesInfo.setDay(new Long(day));
        lotteryTimesInfo.setNum(1);
        lotteryTimesInfo.setSysCreated(calendar.getTime());
        lotteryTimesInfo.setSysUpdated(calendar.getTime());
        lotteryTimesMapper.save(lotteryTimesInfo);
        return true;
    }

    public LotteryTimes getUserCurrentLotteryTimes(Long userId) {
        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);  // 获取当前年
        int m  = calendar.get(Calendar.MONTH) + 1; // 获取当前月
        int d = calendar.get(Calendar.DATE); // 获取当前日
        String strDay = String.format("%d%02d%02d", y, m, d);
        int day = Integer.parseInt(strDay);
        LotteryTimes lotteryTimes = lotteryTimesMapper.getByUserIDAndDay(userId, new Long((long)day));
        return lotteryTimes;
    }

    public boolean checkIpLimit(String ipStr) {
        long ip = UtilTools.ipToLong(ipStr);
        int i = new Long(ip % Constants.ipFrameSize).intValue();
        String key = String.format(Constants.ipLotteryDayNumPrefix+"%d", i);
        double ret = redisUtil.hincr(key,ipStr,1);
        if ((int)ret > Constants.ipLimitMax) {
            log.info("ip Limit exceeded, ret: {}",(int)ret);
            return false;
        }
        return true;
    }

    public CheckResult checkBlackIp(Date now,String ipStr) {
        CheckResult checkResult = new CheckResult();
        BlackIp blackIp = blackIpMapper.getByIP(ipStr);
        checkResult.setBlackIp(blackIp);
        if (blackIp == null || blackIp.getIp().isEmpty()){
            checkResult.setOk(true);
            return checkResult;
        }
        if (now.before(blackIp.getBlackTime())) {
            checkResult.setOk(false);
            return checkResult;
        }
        checkResult.setOk(true);
        return checkResult;
    }

    public CheckResult checkBlackUser(Date now,Long userId) {
        CheckResult checkResult = new CheckResult();
        BlackUser blackUser = blackUserMapper.getByUserId(userId);
        checkResult.setBlackUser(blackUser);
        if (blackUser != null && now.before(blackUser.getBlackTime())) {
            checkResult.setOk(false);
            return checkResult;
        }
        checkResult.setOk(true);
        return checkResult;
    }

    public ArrayList<LotteryPrizeInfo> getAllUsefulPrizes(Date now) {
        ArrayList<LotteryPrizeInfo> lotteryPrizeInfoList = new ArrayList<LotteryPrizeInfo>();
        ArrayList<Prize> prizeList = prizeMapper.getAllUsefulPrizeList(now);
        for (Prize prize : prizeList) {
            String[] codes = prize.getPrizeCode().split("-");
            if (codes.length == 2) {
                String codeA = codes[0];
                String codeB = codes[1];
                int low = Integer.parseInt(codeA);
                int high = Integer.parseInt(codeB);
                if (high >= low && low >= 0 && high < Constants.prizeCodeMax){
                    LotteryPrizeInfo lotteryPrizeInfo = new LotteryPrizeInfo();
                    lotteryPrizeInfo.setId(prize.getId());
                    lotteryPrizeInfo.setTitle(prize.getTitle());
                    lotteryPrizeInfo.setPrizeNum(prize.getPrizeNum());
                    lotteryPrizeInfo.setLeftNum(prize.getLeftNum());
                    lotteryPrizeInfo.setPrizeCodeLow(low);
                    lotteryPrizeInfo.setPrizeCodeHigh(high);
                    lotteryPrizeInfo.setImg(prize.getImg());
                    lotteryPrizeInfo.setDisplayOrder(prize.getDisplayOrder());
                    lotteryPrizeInfo.setPrizeType(prize.getPrizeType());
                    lotteryPrizeInfo.setPrizeProfile(prize.getPrizeProfile());
                    lotteryPrizeInfoList.add(lotteryPrizeInfo);
                }
            }
        }
        return lotteryPrizeInfoList;
    }

    public LotteryPrizeInfo getPrize(Date now,int prizeCode){
        ArrayList<LotteryPrizeInfo> lotteryPrizeInfoList = getAllUsefulPrizes(now);
        for (LotteryPrizeInfo lotteryPrizeInfo : lotteryPrizeInfoList) {
            if (lotteryPrizeInfo.getPrizeCodeLow() <= prizeCode && lotteryPrizeInfo.getPrizeCodeHigh() >= prizeCode) {
                if (lotteryPrizeInfo.getPrizeType() < Constants.prizeTypeEntitySmall){
                    return lotteryPrizeInfo;
                }
            }
        }
        return null;
    }

    // 发放奖品
    public boolean giveOutPrize(Long prizeId) {
        int ret = prizeMapper.decrLeftNum(prizeId,1);
        if (ret <= 0){
            return false;
        }
        return true;
    }

    public String prizeCodeDiff(Long prizeId) {
        String key =  String.format("%d",-prizeId - Constants.couponDiffLockLimit);
        RLock lock = redissonClient.getLock(key);
        Long couponID = 0L;
        lock.lock();
        Coupon coupon = couponMapper.getGetNextUsefulCoupon(prizeId,couponID);
        if(coupon == null) {
            lock.unlock();
            return "";
        }
        couponMapper.updateByCode(coupon.getCode());
        lock.unlock();
        return coupon.getCode();
    }

    public void getLuckyV1(LotteryResult lotteryResult,Long userID, String userName, String ip){
        /*
         ******** 抽奖逻辑*******
         */
        CheckResult checkResult = new CheckResult();
        // 1. 验证用户今日抽奖次数
        if (!checkUserDayLotteryTimes(userID)) {
            log.info("lotteryV1|CheckUserDayLotteryTimes failed，user_id：{}", userID);
            lotteryResult.setErrcode(ErrorCode.ERR_USER_LIMIT_INVALID);
            return;
        }
        // 2. 验证当天IP参与的抽奖次数
        if (!checkIpLimit(ip)) {
            log.info("lotteryV1|checkIpLimit failed，ip：{}", ip);
            lotteryResult.setErrcode(ErrorCode.ERR_IP_LIMIT_INVALID);
            return;
        }

        Date now = new Date();
        // 3. 验证IP是否在ip黑名单
        CheckResult ipCheckResult = checkBlackIp(now,ip);
        checkResult.setBlackIp(ipCheckResult.getBlackIp());
        if (!ipCheckResult.isOk()) {
            log.info("lotteryV1|checkBlackIp failed，ip：{}", ip);
            lotteryResult.setErrcode(ErrorCode.ERR_BLACK_IP);
            return;
        }

        // 4. 验证用户是否在用户黑名单
        CheckResult userCheckResult = checkBlackUser(now,userID);
        checkResult.setBlackUser(userCheckResult.getBlackUser());
        if (!userCheckResult.isOk()) {
            log.info("lotteryV1|checkBlackUser failed，user_id：{}", userID);
            lotteryResult.setErrcode(ErrorCode.ERR_BLACK_USER);
            return;
        }
        checkResult.setOk(true);

        // 5. 奖品匹配
        int prizeCode = UtilTools.getRandom(Constants.prizeCodeMax);
        log.info("lotteryV1|prizeCode===={}", prizeCode);
        LotteryPrizeInfo prize = getPrize(now,prizeCode);
        if (prize == null)  {
            log.info("lotteryV1|getPrize null");
            lotteryResult.setErrcode(ErrorCode.ERR_NOT_WON);
            return;
        }

        if (prize.getPrizeNum() <=0) {
            log.info("lotteryV1|prize_num invalid,prize_id: {}", prize.getId());
            lotteryResult.setErrcode(ErrorCode.ERR_NOT_WON);
            return;
        }

        // 6. 剩余奖品发放
        if (!giveOutPrize(prize.getId())) {
            log.info("lotteryV1|prize not enough,prize_id: {}", prize.getId());
            lotteryResult.setErrcode(ErrorCode.ERR_NOT_WON);
            return;
        }

        // 7. 发放优惠券
        if (prize.getPrizeType() == Constants.prizeTypeCouponDiff) {
            String code = prizeCodeDiff(prize.getId());
            if (code.isEmpty()) {
                log.info("lotteryV1|coupon code is empty: prize_id: {}", prize.getId());
                lotteryResult.setErrcode(ErrorCode.ERR_NOT_WON);
                return;
            }
            // 填充优惠券编码
            prize.setCouponCode(code);
        }
        lotteryResult.setErrcode(ErrorCode.SUCCESS);
        lotteryResult.setLotteryPrize(prize);
        // 8.记录中奖记录
        logLotteryResult(prize,now,userID,ip,userName,prizeCode);
        // 9. 大奖黑名单处理
        if (prize.getPrizeType() == Constants.prizeTypeEntityLarge) {
            LotteryUserInfo lotteryUserInfo = new LotteryUserInfo();
            lotteryUserInfo.setUserId(userID);
            lotteryUserInfo.setUserName(userName);
            lotteryUserInfo.setIp(ip);
            prizeLargeBlackLimit(now,checkResult.getBlackUser(),checkResult.getBlackIp(),lotteryUserInfo);
        }
    }

    public void logLotteryResult(LotteryPrizeInfo prize,Date now,Long userId,String ip,String userName,int prizeCode) {
        Result result = new Result();
        result.setPrizeId(prize.getId());
        result.setPrizeName(prize.getTitle());
        result.setPrizeType(prize.getPrizeType());
        result.setUserId(userId);
        result.setUserName(userName);
        result.setPrizeCode(prizeCode);
        result.setPrizeData(prize.getPrizeProfile());
        result.setSysCreated(now);
        result.setSysIp(ip);
        result.setSysStatus(1);
        resultMapper.save(result);
    }

    public void prizeLargeBlackLimit(Date now,BlackUser blackUser,BlackIp blackIp,LotteryUserInfo lotteryUserInfo){
        Calendar calendar = Calendar.getInstance(); // 当前时间
        calendar.setTime(now);
        calendar.add(Calendar.DATE,Constants.defaultBlackTime); // 当前时间黑名单限制之后的截止时间
        Date blackTime = calendar.getTime();
        if (blackUser == null || blackUser.getUserId() <= 0){
            // 黑名单不存在，新加入黑名单
            BlackUser blackUserInfo = new BlackUser();
            blackUserInfo.setUserId(lotteryUserInfo.getUserId());
            blackUserInfo.setUserName(lotteryUserInfo.getUserName());
            blackUserInfo.setBlackTime(blackTime);
            blackUserInfo.setSysCreated(now);
            blackUserInfo.setSysUpdated(now);
            blackUserMapper.save(blackUserInfo);
        }else{
            // 黑名单存在，更新很名单截止时间
            blackUserMapper.updateBlackTimeByUserId(lotteryUserInfo.getUserId(),blackTime);
        }

        if (blackIp == null || blackIp.getIp().isEmpty()) {
            BlackIp blackIpInfo = new BlackIp();
            blackIpInfo.setIp(lotteryUserInfo.getIp());
            blackIpInfo.setBlackTime(blackTime);
            blackIpInfo.setSysCreated(now);
            blackIpInfo.setSysUpdated(now);
            blackIpMapper.save(blackIpInfo);
        }else{
            blackIpMapper.updateBlackTimeByIP(lotteryUserInfo.getIp(),blackTime);
        }
    }

    public void addPrizeList(List<ViewPrize> viewPrizeList){
        Date now = new Date();
        ArrayList<Prize> prizeList = new ArrayList<>();
        for(ViewPrize viewPrize : viewPrizeList) {
            Prize prize = new Prize();
            prize.setTitle(viewPrize.getTitle());
            prize.setPrizeNum(viewPrize.getPrizeNum());
            prize.setLeftNum(viewPrize.getPrizeNum());
            prize.setPrizeCode(viewPrize.getPrizeCode());
            prize.setPrizeTime(viewPrize.getPrizeTime());
            prize.setImg(viewPrize.getImg());
            prize.setDisplayOrder(viewPrize.getDisplayOrder());
            prize.setPrizeType(viewPrize.getPrizeType());
            prize.setBeginTime(viewPrize.getBeginTime());
            prize.setEndTime(viewPrize.getEndTime());
            prize.setPrizePlan(viewPrize.getPrizePlan());
            prize.setSysStatus(viewPrize.getSysStatus());
            prize.setSysCreated(now);
            prize.setSysUpdated(now);
            prizeList.add(prize);
        }
        prizeMapper.saveInBatches(prizeList);
    }

 }