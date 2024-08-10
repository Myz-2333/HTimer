package com.zdf.worker.lock;

import lombok.Data;

@Data
public class LockParam {
    private static Long HOLD_LOCK_TIME = 5L;
    private static Long TRY_LOCK_TIME = 10L;

    private String lockKey;
    private String lockValue;
    private Long holdLockTime;
    // 最大尝试时间，防止无限期抢锁
    private Long tryLockTime;


    public LockParam(String lockKey) {
        this(lockKey, 5 * 1000L, 5L);
    }

    public LockParam(String lockKey, Long tryLockTime) {
        this(lockKey, tryLockTime, 3000L);
    }

    public LockParam(String lockKey, Long tryLockTime, Long holdLockTime) {
        this.lockKey = lockKey;
        this.tryLockTime = tryLockTime;
        this.holdLockTime = holdLockTime;
    }
}
