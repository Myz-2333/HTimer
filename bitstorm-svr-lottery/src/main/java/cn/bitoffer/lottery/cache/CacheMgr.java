package cn.bitoffer.lottery.cache;

import cn.bitoffer.lottery.constant.Constants;
import cn.bitoffer.lottery.model.BlackIp;
import cn.bitoffer.lottery.model.BlackUser;
import cn.bitoffer.lottery.model.Coupon;
import cn.bitoffer.lottery.model.Prize;
import cn.bitoffer.lottery.redis.RedisUtil;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class CacheMgr {
    @Autowired
    private RedisUtil redisUtil;

    // 缓存中增加用户的抽奖次数
    public int incrUserDayLotteryNum(Long userId) {
        int i = (int) (userId % Constants.userFrameSize);
        String key = String.format(Constants.userLotteryDayNumPrefix+"%d", i);
        double ret = redisUtil.hincr(key,Long.toString(userId),1);
        return (int)ret;
    }

    // initUserLuckyNum 从给定的数据直接初始化用户的参与抽奖次数到缓存中
    public boolean initUserLuckyNum(Long userId, int num) {
        int i = (int) (userId % Constants.userFrameSize);
        String key = String.format(Constants.userLotteryDayNumPrefix+"%d", i);
        boolean ok = redisUtil.hset(key,Long.toString(userId),num);
        if (!ok) {
            log.error("initUserLuckyNum err");
            return false;
        }
        return true;
    }

    public BlackIp getBlackIpByCache(String ip) throws ParseException {
        BlackIp blackIp = new BlackIp();
        blackIp.setIp(ip);

        String key = String.format(Constants.ipCacheKeyPrefix+"%s", ip);
        Map<Object, Object> valueMap = redisUtil.hmget(key);
        if (valueMap.isEmpty()) {
            return null;
        }
        Long id = Long.parseLong(valueMap.get("Id").toString());
        blackIp.setId(id);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String blackTimeStr = valueMap.get("BlackTime").toString();
        blackIp.setBlackTime(sdf.parse(blackTimeStr));

        String sysCreatedStr = valueMap.get("SysCreated").toString();
        blackIp.setSysCreated(sdf.parse(sysCreatedStr));

        String sysUpdatedStr = valueMap.get("SysUpdated").toString();
        blackIp.setSysUpdated(sdf.parse(sysUpdatedStr));
        return blackIp;
    }

    public void setBlackIpByCache(BlackIp blackIp) throws ParseException {
        if (blackIp == null){
            return;
        }
        String key = String.format(Constants.ipCacheKeyPrefix+"%s", blackIp.getIp());

        Map<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put("Id",Integer.toString(blackIp.getId().intValue()));
        valueMap.put("Ip",blackIp.getIp());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        valueMap.put("BlackTime",sdf.format(blackIp.getBlackTime()));
        valueMap.put("SysCreated",sdf.format(blackIp.getSysCreated()));
        valueMap.put("SysUpdated",sdf.format(blackIp.getSysUpdated()));

        boolean ok = redisUtil.hmset(key,valueMap);
        if (!ok) {
            log.error("setBlackIpByCache err: {}",ok);
        }
    }

    public BlackUser getBlackUserByCache(Long userId) throws ParseException {
        String key = String.format(Constants.userCacheKeyPrefix+"%d", userId);
        Map<Object, Object> valueMap = redisUtil.hmget(key);
        if (valueMap.isEmpty()) {
            return null;
        }
        BlackUser blackUser = new BlackUser();
        blackUser.setUserId(userId);
        blackUser.setId(Long.parseLong(valueMap.get("Id").toString()));
        blackUser.setUserName(valueMap.get("UserName").toString());
        blackUser.setRealName(valueMap.get("RealName").toString());
        blackUser.setAddress(valueMap.get("Address").toString());
        blackUser.setMobile(valueMap.get("Mobile").toString());
        blackUser.setSysIp(valueMap.get("SysIp").toString());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        blackUser.setBlackTime(sdf.parse(valueMap.get("BlackTime").toString()));
        blackUser.setSysCreated(sdf.parse(valueMap.get("SysCreated").toString()));
        blackUser.setSysUpdated(sdf.parse(valueMap.get("SysUpdated").toString()));
        return blackUser;
    }

    public void setBlackUserByCache(BlackUser blackUser) {
        if (blackUser == null) {
            return;
        }
        String key = String.format(Constants.userCacheKeyPrefix+"%s", blackUser.getUserId());
        Map<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put("Id",blackUser.getId().toString());
        valueMap.put("UserId",blackUser.getUserId().toString());
        valueMap.put("UserName",blackUser.getUserName());
        valueMap.put("RealName",blackUser.getRealName());
        valueMap.put("Mobile",blackUser.getMobile());
        valueMap.put("Address",blackUser.getAddress());
        valueMap.put("SysIp",blackUser.getSysIp());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        valueMap.put("BlackTime",sdf.format(blackUser.getBlackTime()));
        valueMap.put("SysCreated",sdf.format(blackUser.getSysCreated()));
        valueMap.put("SysUpdated",sdf.format(blackUser.getSysUpdated()));
        boolean ok = redisUtil.hmset(key,valueMap);
        if (!ok){
            log.error("setBlackUserByCache err: {}",ok);
        }
    }

    public ArrayList<Prize> getAllPrizesByCache() throws ParseException {
        Object obj = redisUtil.get(Constants.allPrizeCacheKey);
        if (obj == null) {
            return null;
        }
        String valueStr = obj.toString();
        ArrayList<Prize> prizeList = new ArrayList<Prize>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<Map<String,String>> valueMapLsit = (List<Map<String,String>>) JSONArray.parse(valueStr);
        for(Map<String,String> valueMap : valueMapLsit) {
            Prize prize = new Prize();
            prize.setId(Long.parseLong(valueMap.get("Id")));
            prize.setTitle(valueMap.get("Title").toString());
            prize.setPrizeNum(Integer.parseInt(valueMap.get("PrizeNum")));
            prize.setLeftNum(Integer.parseInt(valueMap.get("LeftNum")));
            prize.setPrizeCode(valueMap.get("PrizeCode"));
            prize.setPrizeTime(Integer.parseInt(valueMap.get("PrizeTime")));
            prize.setImg(valueMap.get("Img"));
            prize.setDisplayOrder(Integer.parseInt(valueMap.get("DisplayOrder")));
            prize.setPrizeType(Integer.parseInt(valueMap.get("PrizeType")));
            prize.setPrizeProfile(valueMap.get("PrizeProfile"));
            prize.setSysStatus(Integer.parseInt(valueMap.get("SysStatus")));
            prize.setSysIp(valueMap.get("SysIp"));
            prize.setBeginTime(sdf.parse(valueMap.get("BeginTime")));
            prize.setEndTime(sdf.parse(valueMap.get("EndTime")));
            prize.setPrizeBegin(sdf.parse(valueMap.get("PrizeBegin")));
            prize.setPrizeEnd(sdf.parse(valueMap.get("PrizeEnd")));
            prize.setSysCreated(sdf.parse(valueMap.get("SysCreated")));
            prize.setSysUpdated(sdf.parse(valueMap.get("SysUpdated")));
            prizeList.add(prize);
        }
        return prizeList;
    }

    public void setAllPrizesByCache(ArrayList<Prize> prizeList) {
        if (prizeList == null || prizeList.isEmpty()) {
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ArrayList<Map<String,String>> prizeMapList = new ArrayList<Map<String,String>>();
        for (Prize prize : prizeList) {
            Map<String,String> prizeMap = new HashMap<String,String>();
            prizeMap.put("Id",prize.getId().toString());
            prizeMap.put("Title",prize.getTitle());
            prizeMap.put("PrizeNum",String.valueOf(prize.getPrizeNum()));
            prizeMap.put("LeftNum",String.valueOf(prize.getLeftNum()));
            prizeMap.put("PrizeCode",prize.getPrizeCode());
            prizeMap.put("PrizeTime",String.valueOf(prize.getPrizeTime()));
            prizeMap.put("Img",prize.getImg());
            prizeMap.put("DisplayOrder",String.valueOf(prize.getDisplayOrder()));
            prizeMap.put("PrizeType",String.valueOf(prize.getPrizeType()));
            prizeMap.put("PrizeProfile",prize.getPrizeProfile());
            prizeMap.put("SysIp",prize.getSysIp());
            prizeMap.put("SysStatus",String.valueOf(prize.getSysStatus()));
            prizeMap.put("PrizePlan",prize.getPrizePlan());
            prizeMap.put("BeginTime",sdf.format(prize.getBeginTime()));
            prizeMap.put("EndTime",sdf.format(prize.getEndTime()));
            prizeMap.put("PrizeBegin",sdf.format(prize.getPrizeBegin()));
            prizeMap.put("PrizeEnd",sdf.format(prize.getPrizeEnd()));
            prizeMap.put("SysCreated",sdf.format(prize.getSysCreated()));
            prizeMap.put("SysUpdated",sdf.format(prize.getSysUpdated()));
            prizeMapList.add(prizeMap);
        }
        String valueStr = JSONArray.toJSONString(prizeMapList);
        boolean ok = redisUtil.set(Constants.allPrizeCacheKey,valueStr);
        if (!ok) {
            log.error("setAllPrizesByCache err: {}",ok);
        }
    }

    public void updatePrizeByCache(Long prizeId){
        if (prizeId <= 0) {
            return;
        }
        // 旁路缓存模式，更新即删除缓存
        redisUtil.del(Constants.allPrizeCacheKey);
    }

    public String getNextUsefulCouponByCache(Long prizeId){
        String key = String.format(Constants.prizeCouponCacheKey+"%s", prizeId);
        return  redisUtil.popMember(key).toString();
    }

    // 从奖品池中获取奖品的数量
    public int getPrizeNumByPool(Long prizeId) {
        Object obj = redisUtil.hget(Constants.prizePoolCacheKey,prizeId.toString());
        if (obj == null) {
            return 0;
        }
        int num = Integer.parseInt(obj.toString());
        return num;
    }

    public int decrPrizeLeftNumByPool(Long prizeId) {
        double ret = redisUtil.hdecr(Constants.prizePoolCacheKey,prizeId.toString(),1);
        return (int)ret;
    }

    public void setPrizePool(Long prizeId,int num){
        boolean ok = redisUtil.hset(Constants.prizePoolCacheKey,prizeId.toString(),num);
        if (!ok) {
            log.error("setPrizePool err");
        }
    }

    public int addPrizeInPrizePool(String key,String field, int num) {
        double ret = redisUtil.hincr(key,field,num);
        return (int)ret;
    }

    public boolean importCouponByCache(Long prizeId,String code){
        String key = String.format(Constants.prizeCouponCacheKey+"%d", prizeId);
        long ret = redisUtil.sSet(key,code);
        if (ret == 0) {
            return false;
        }
        return true;
    }
}
