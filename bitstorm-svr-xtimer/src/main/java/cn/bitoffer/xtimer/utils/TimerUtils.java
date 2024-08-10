package cn.bitoffer.xtimer.utils;

import org.quartz.CronExpression;

import java.text.SimpleDateFormat;
import java.util.*;

public class TimerUtils {
    public static String GetCreateLockKey(String app){
        return "create_timer_lock_"+app;
    }

    public static String GetEnableLockKey(String app){
        return "enable_timer_lock_"+app;
    }

    public static String GetMigratorLockKey(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
        String dateStr = sdf.format(date);
        return "migrator_lock_"+dateStr;
    }

    public static String GetTimeBucketLockKey(Date time , int bucketId){
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String timeStr = sdf.format(time);
        return sb.append("time_bucket_lock_").append(timeStr).append("_").append(bucketId).toString();
    }

    public static String GetSliceMsgKey(Date time , int bucketId){
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String timeStr = sdf.format(time);
        return sb.append(timeStr).append("_").append(bucketId).toString();
    }

     public static String GetTokenStr() {
        long timestamp = System.currentTimeMillis();
        String thread = Thread.currentThread().getName();
        return thread+timestamp;
    }

    public static Date GetForwardTwoMigrateStepEnd(Date start, int diffMinutes){
        Date end = new Date(start.getTime() + 2L *diffMinutes * 60000);
        return end;
    }

    public static List<Long> GetCronNextsBetween(CronExpression cronExpression, Date now, Date end){
        List<Long> times = new ArrayList<>();
        if( end.before(now)){
            return times;
        }

        for (Date start =now;start.before(end);){
            Date next = cronExpression.getNextValidTimeAfter(start);
            times.add(next.getTime());
            start = next;
        }
        return times;
    }

    public static String UnionTimerIDUnix(long timerId, long unix){
        return new StringBuilder().append(timerId).append("_").append(unix).toString();
    }

    public static List<Long> SplitTimerIDUnix(String timerIDUnix){
        List<Long> longSet = new ArrayList<>();
        String[] strList = timerIDUnix.split("_");
        if(strList.length != 2){
            return longSet;
        }
        longSet.add(Long.parseLong(strList[0]));
        longSet.add(Long.parseLong(strList[1]));
        return longSet;
    }
}
