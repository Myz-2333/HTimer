package com.zdf.worker.lock;

import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.UUID;

public class RedisLock {
    private Long tryLockEndTime;
    private String lockValue;
    private LockParam lockParam;
    private final String LOCK_SUCCESS = "OK";
    private final Long UNLOCK_SUCCESS = 1L;
    private final String passwd = "password";
    // KEY[1] = lockKey
    // ARGC[1] = lockValue
    private final static  String unLockScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    private Jedis jedis;

    public RedisLock(LockParam lockParam) {
        if (lockParam == null) {
            throw new RuntimeException("LockParam is null.");
        }
        this.lockParam = lockParam;
        this.lockValue = UUID.randomUUID().toString();
        this.tryLockEndTime = System.currentTimeMillis() + lockParam.getTryLockTime();
        this.jedis = new Jedis("localhost", 6379);
        this.jedis.auth(passwd);
    }

    public void close() {
        this.jedis.close();
    }

    public boolean lock() {
        while (true) {
            if (System.currentTimeMillis() > tryLockEndTime) {
                return false;
            }
            if (tryLock()) {
                return true;
            } else {
                // 加锁失败后重试
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 加锁
    public boolean tryLock() {
        String flag;
        flag = jedis.set(this.lockParam.getLockKey(), this.lockValue, "NX", "EX", this.lockParam.getHoldLockTime());
        if (LOCK_SUCCESS.equals(flag)) {
            return true;
        }
        return false;
    }
    public boolean unlock() {
        Object eval ;
        try {
            // 执行lua脚本释放锁
            eval = jedis.eval(unLockScript, Collections.singletonList(this.lockParam.getLockKey()), Collections.singletonList(this.lockValue));
            if (UNLOCK_SUCCESS.equals(eval)) {
                return true;
            }
        } finally {
            this.close();
        }
        return false;
    }
}
