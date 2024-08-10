package cn.bitoffer.lottery.scheduler;

import cn.bitoffer.lottery.cache.CacheMgr;
import cn.bitoffer.lottery.constant.Constants;
import cn.bitoffer.lottery.mapper.PrizeMapper;
import cn.bitoffer.lottery.model.Prize;
import cn.bitoffer.lottery.service.impl.LotteryServiceImpl2;
import cn.bitoffer.lottery.service.impl.LotteryServiceImpl3;
import cn.bitoffer.lottery.utils.UtilTools;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Async;
import com.alibaba.fastjson.serializer.SerializerFeature;


import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
public class ShedulerPrizePlanTask {

    @Autowired
    private CacheMgr cacheMgr;
    @Autowired
    private PrizeMapper prizeMapper;
    @Autowired
    private LotteryServiceImpl3 lotteryServiceImpl3;

    private final int[] dayPrizeWeights = {
        0, 0, 0,
        1, 1, 1,
        2, 2, 2,
        3, 3, 3,
        4, 4, 4,
        5, 5, 5,
        6, 6, 6,
        7, 7, 7,
        8, 8, 8, 8, 8, 8, 8,
        9, 9, 9,
        10, 10, 10,
        11, 11, 11,
        12, 12, 12,
        13, 13, 13,
        14, 14, 14, 14, 14, 14, 14,
        15, 15, 15, 15, 15, 15, 15,
        16, 16, 16, 16, 16, 16, 16,
        17, 17, 17, 17, 17, 17, 17,
        18, 18, 18,
        19, 19, 19,
        20, 20, 20, 20, 20, 20, 20,
        21, 21, 21, 21, 21, 21, 21,
        22, 22, 22,
        23, 23, 23,
    };

    @Async
    @Scheduled(fixedDelay = 300000)
    public void resetAllPrizePlan() throws ParseException {
        log.info("Resetting all prizes!!!!!");
        ArrayList<Prize> prizeList = prizeMapper.getAll();
        Date now = new Date();
        for(Prize prize : prizeList) {
            if(prize.getPrizeTime() > 0 && (prize.getPrizePlan().isEmpty() || prize.getPrizeEnd().before(now))) {
                // ResetPrizePlan只会更新db的数据
                resetPrizePlan(prize);
                // 因为上面resetPrizePlan会把缓存中的奖品数据清空，所以这里用一个缓存读取，将数据加载到缓存
                lotteryServiceImpl3.getAllUsefulPrizesWithCache(now);
            }
        }
    }

    public void resetPrizePlan(Prize prize) {
        if (prize == null || prize.getId() < 1) {
            return;
        }
        Date now = new Date();
        // 奖品状态不对，不能发奖
        if (prize.getSysStatus() == 2 || prize.getBeginTime().after(now) ||
        prize.getEndTime().before(now) || prize.getPrizeNum() <=0 || prize.getLeftNum() <= 0) {
            // 在重置的时候，如果发现原来奖品的发奖计划不为空，需要清空发奖计划
            if (prize.getPrizePlan().isEmpty()) {
                clearPrizePlan(prize.getId());
            }
            log.debug("prize can not be given out,prize_id: {}", prize.getId());
            return;
        }
        // PrizeTime, 发奖周期，这类奖品需要在多少天内发完
        int prizePlanDays = prize.getPrizeTime();
        if (prizePlanDays <= 0) {
            cacheMgr.setPrizePool(prize.getId(), prize.getLeftNum());
            log.debug("adminService|ResetGiftPrizePlan|prizePlanDays <= 0,prize_id: {}",prize.getId());
            return;
        }
        // 对于设置发奖周期的奖品重新计算出来合适的奖品发放节奏
        // 奖品池的剩余数先设置为空
        cacheMgr.setPrizePool(prize.getId(), 0);
        // 发奖周期中的每天的发奖概率一样，一天内24小时，每个小时的概率是不一样的，每个小时内的每一分钟的概率一样
        int prizeNum = prize.getLeftNum();   // 对剩余奖品做均匀分布
        // 先计算每天至少发多少奖
        int avgPrizeNum = prizeNum / prizePlanDays;
        // 每天可以分配到的奖品数量
        Map<Integer, Integer> dayPrizeNumMap = new HashMap<Integer, Integer>();
        // 发奖周期大雨1天，并且平均每天发的奖品书大于等于1
        if (prizePlanDays > 0 && avgPrizeNum >= 1) {
            for(int day = 0; day < prizePlanDays; day++) {
                dayPrizeNumMap.put(day,avgPrizeNum);
            }
        }
        // 剩下的奖品一个一个的随机分配到任意哪天
        prizeNum -= prizePlanDays * avgPrizeNum;
        while (prizeNum > 0) {
            prizeNum--;
            int day = UtilTools.getRandom(prizePlanDays);
            dayPrizeNumMap.put(day,dayPrizeNumMap.get(day)+1);
        }
        // 发奖map：map[int]map[int][60]int
        //map[天]map[小时][60]奖品数量：后一个map表示value是一个60大小的数组，表示一个小时中每分钟要发的奖品数量
        Map<Integer, HashMap<Integer, int[]>> prizePlanMap = new  HashMap<Integer, HashMap<Integer, int[]>>();
        for (Map.Entry<Integer, Integer> entry : dayPrizeNumMap.entrySet()) {
            int day = entry.getKey();
            int num = entry.getValue();
            //计算一天的发奖计划
            HashMap<Integer, int[]> dayPrizePlan = prizePlanOneDay(num);
            prizePlanMap.put(day, dayPrizePlan);
        }
        log.debug("prize_id = {}\nprizePlanMap = {}", prize.getId(), prizePlanMap);
        System.out.println("prize_id="+prize.getId());
        System.out.println("prizePlanMap="+ JSON.toJSONString(prizePlanMap,SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,SerializerFeature.WriteDateUseDateFormat));

        // 格式化 dayPrizePlan数据，序列化成为一个[时间:数量]二元组的数组
        ArrayList<TimePrizeInfo>  planList = formatPrizePlan(now, prizePlanDays, prizePlanMap);
        String planListStr = JSONArray.toJSONString(planList);

        Calendar calendar = Calendar.getInstance(); // 当前时间
        calendar.setTime(now);
        calendar.add(Calendar.DATE, prizePlanDays); // 当前时间黑名单限制之后的截止时间
        // 更新发奖计划，以及计划的开始时间和结束时间，注意这里会把缓存中的奖品数据清空
        lotteryServiceImpl3.updatePrizePlanAndTimeWithCache(prize.getId(),planListStr,now,calendar.getTime());
    }


    public void clearPrizePlan(Long prizeId) {
        lotteryServiceImpl3.updatePrizePlanWithCache(prizeId,"") ;
        // 奖品池设置为0
        cacheMgr.setPrizePool(prizeId,0);
    }

    public HashMap<Integer, int[]> prizePlanOneDay(int num) {
        HashMap<Integer, int[]> resultMap = new HashMap<>();
        int[] hourPrizeNumList = new int[24]; // 长度为24的数组表示1天中每个小时对应的奖品数
        // 计算一天中的24个小时，每个小时应该发出的奖品数，为什么是100，100表示每一天的权重百分比
        if (num > 100) {
            for (int h : dayPrizeWeights) {
                hourPrizeNumList[h]++;
            }
            for (int h = 0; h < 24; h++) {
                int d = hourPrizeNumList[h];
                int n = num * d / 100;  // d / 100 每个小时所占的奖品数量概率
                hourPrizeNumList[h] = n;
                num -= n;
            }
        }
        log.info("num = {}", num);
        while (num > 0){
            num--;
            // 随机将这个奖品分配到某一个小时上
            int hourIndex = UtilTools.getRandom(100);
            int h = dayPrizeWeights[hourIndex];
            hourPrizeNumList[h]++;
        }

        // 将每个小时内的奖品数量分配到60分钟
        for(int h=0;h<hourPrizeNumList.length;h++) {
            if (hourPrizeNumList[h] <= 0) {
                continue;
            }
            int[] minutePrizeNumList = new int[60];
            if (hourPrizeNumList[h] >= 60) {
                int avgMinutePrizeNum = hourPrizeNumList[h] / 60;
                for (int i=0;i<60;i++) {
                    minutePrizeNumList[i] = avgMinutePrizeNum;
                }
                hourPrizeNumList[h] -= avgMinutePrizeNum*60;
            }
            while (hourPrizeNumList[h] > 0) {
                hourPrizeNumList[h]--;
                int m = UtilTools.getRandom(60);
                minutePrizeNumList[m]++;
            }
            resultMap.put(h, minutePrizeNumList);
        }
        return resultMap;
    }

    // 将prizeData格式化成具体到一个时间（分钟）的奖品数量
    // 结构为： [day][hour][minute]num
    // result: [][时间, 数量]
    public ArrayList<TimePrizeInfo> formatPrizePlan(Date now, int prizePlanDays, Map<Integer, HashMap<Integer, int[]>> prizePlan) {
        //result: [][时间, 数量]
        ArrayList<TimePrizeInfo> result = new ArrayList<TimePrizeInfo>();
        int nowHour = now.getHours();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (int i = 0; i < prizePlanDays; i++) {
            HashMap<Integer, int[]> dayPrizePlanMap = prizePlan.get(i);
            long dayTimeStamp = now.getTime() + i*86400*1000; // dayTimeStamp 为发奖周期中的每一天对应当前时间的时刻，一天有86400秒
            for(int h = 0;h < 24; h++){
                int[] hourPrizePlanMap = dayPrizePlanMap.get((h+nowHour)%24);
                if (hourPrizePlanMap == null || hourPrizePlanMap.length == 0) {
                    continue;
                }
                long hourTimeStamp = dayTimeStamp + h * 3600*1000; // hourTimeStamp 为发奖周期中的每一天中每个小时对应的时刻，1小时有3600秒
                for(int m = 0; m < 60; m++) {
                    int num = hourPrizePlanMap[m];
                    if (num <=0) {
                        continue;
                    }
                    // 找到特定一个时间的计划数据
                    long minuteTimeStamp = hourTimeStamp + m*60*1000; // minuteTimeStamp 为发奖周期中的每一分钟对应的时刻，1分钟有60秒
                    TimePrizeInfo timePrizeInfo = new TimePrizeInfo();
//                    System.out.println("i ===" + i);
//                    System.out.println("nowTimeStamp ===" + now.getTime());
//                    System.out.println("minuteTimeStamp ===" + minuteTimeStamp);
//                    System.out.println("difference ===" + (minuteTimeStamp-now.getTime()));
//                    System.out.println("nowDate ===" + new Date(now.getTime()));
//                    System.out.println("minuteTimeDate===" + new Date(minuteTimeStamp));
//                    System.out.println("now ===" + sdf.format(now.getTime()));
//                    System.out.println("minuteTime===" + sdf.format(minuteTimeStamp));
//                    System.out.println("--------------------------------------------------------------------------");
                    timePrizeInfo.setTime(sdf.format(minuteTimeStamp));
                    timePrizeInfo.setNum(num);
                    result.add(timePrizeInfo);
                }
            }
        }
        return result;
    }

}


