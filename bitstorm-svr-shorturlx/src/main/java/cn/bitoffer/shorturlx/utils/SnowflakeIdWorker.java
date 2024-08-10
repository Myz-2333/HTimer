package cn.bitoffer.shorturlx.utils;

import cn.bitoffer.shorturlx.common.conf.AppConf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SnowflakeIdWorker {
    @Autowired
    private AppConf appConf;

    // private int workId;
    // 工作机器ID(默认0~1023)
    private long workerId;
    // 序列号(1ms内默认0~4095)
    private long sequence = 0L;

    // 开始时间戳
    private long twepoch = System.currentTimeMillis();

    // workerId位数
    private long workerIdBits = 10L;
    // 最大值
    private long maxWorkerId;

    private long sequenceBits = 12L;

    // workerId左移12位
    private long workerIdShift = sequenceBits;
    // 时间戳左移22位(10+12)
    private long timestampLeftShift;
    // 生成序列的掩码
    private long sequenceMask = ~(-1L << sequenceBits);

    private long lastTimestamp = -1L;

    @PostConstruct
    public void init(){
        // 获取work_id
        workerId = appConf.getWorkId();
        // 获取workerIdBits
        workerIdBits = appConf.getWorkerIdBits();
        maxWorkerId  = ~(-1L << workerIdBits);
        timestampLeftShift = sequenceBits + workerIdBits;
        sequence = 0L;
        // 做校验
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        } else {
            System.out.println("workerId: " + workerId);
        }

        if  (workerIdBits > 12 || workerIdBits < 0) {
            throw new IllegalArgumentException("worker Id bits can't be greater than 12 or less than 0");
        } else {
            System.out.println("workerIdBits: " + workerIdBits);
        }

        System.out.println("InitByPostConstructAnnotation do something");
    }

    public synchronized long nextId() {
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        // 如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            // 毫秒内序列溢出
            if (sequence == 0) {
                // 阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else { // 时间戳改变，毫秒内序列重置
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        // 移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - twepoch) << timestampLeftShift) | (workerId << workerIdShift) | sequence;
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }

    // public static void main(String[] args) {
    //     SnowflakeIdWorker idWorker = new SnowflakeIdWorker();
    //     for (int i = 0; i < 1000; i++) {
    //         long id = idWorker.nextId();
    //     }
    // }
}



