package cn.bitoffer.improve.service.impl;

import cn.bitoffer.common.redis.RedisBase;
import cn.bitoffer.improve.mapper.CountMapper;
import cn.bitoffer.improve.model.Count;
import cn.bitoffer.improve.service.CountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class CountServiceImpl implements CountService {
    public static Integer tmpCount = 0;
    public static final Lock lock = new ReentrantLock();

    @Autowired
    private CountMapper countMapper;

    @Autowired
    private RedisBase redisBase;



    @Override
    public void save(Count example) {
        countMapper.save(example);
    }

    @Override
    public void update(Count example) {
        countMapper.update(example);
    }

    @Override
    public void incrCount(Integer num) {
        countMapper.incrCount(num);
    }

    @Override
    public void incrMemCountAndReset() {
        lock.lock();
        try {
            if (tmpCount != 0) {
                countMapper.incrCount(tmpCount);
                clearMemCount();
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void incrCountAsync(Integer num) {
        incrCountAtomic(num);
    }

    private  void clearMemCount() {
        tmpCount = 0;
    }

    public void incrCountAtomic(Integer num) {
        lock.lock();
        tmpCount++;
        lock.unlock();
    }

}
