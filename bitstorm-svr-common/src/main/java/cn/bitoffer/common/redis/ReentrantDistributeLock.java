package cn.bitoffer.common.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
public class ReentrantDistributeLock {

    @Autowired
    private RedisBase redisBase;

    public boolean lock(String key, String token, long expireSeconds){
        // 首先查询锁是否属于自己
        Object res = redisBase.get(key);
        if(res != null && StringUtils.equals(res.toString(),token)){
            return true;
        }

        // 不属于自己，尝试获取锁
        boolean ok = redisBase.setnx(key,token,expireSeconds);
        if(!ok){
            log.info("lock is acquired by others");
        }
        return ok;
    }

    public void unlock(String key,String token){
        Long execute = redisBase.executeLua(getUnlockScript(), Arrays.asList(key), token, null);
        if (execute.longValue() == 0) {
            log.info("释放锁{}失败:{}", key, execute);
        } else if (execute.longValue() == 1) {
            log.info("释放锁{}成功:{}", key,execute);
        }
    }

    private DefaultRedisScript<Long> getUnlockScript() {
        String script = "if redis.call('get',KEYS[1]) == ARGV[1]\n" +
                "then\n" +
                "return redis.call('del',KEYS[1])\n" +
                "else\n" +
                "   return 0\n" +
                "end";
        DefaultRedisScript<Long> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setResultType(Long.class);
        defaultRedisScript.setScriptText(script);
        return defaultRedisScript;
    }

    public void expireLock(String key, String token, long expireSeconds){
        Long execute = redisBase.executeLua(getExpireLockScript(), Arrays.asList(key), token, expireSeconds);
        if (execute.longValue() == 0) {
            log.info("延期{}失败:{}", key, execute);
        } else if (execute.longValue() == 1) {
            log.info("延期{}成功:{}", key,execute);
        }
    }

    private DefaultRedisScript<Long> getExpireLockScript() {
        String script = "local lockerKey = KEYS[1]\n" +
                "  local targetToken = ARGV[1]\n" +
                "  local duration = ARGV[2]\n" +
                "  local getToken = redis.call('get',lockerKey)\n" +
                "  if (not getToken or getToken ~= targetToken) then\n" +
                "    return 0\n" +
                "\telse\n" +
                "\t\treturn redis.call('expire',lockerKey,duration)\n" +
                "  end";
        DefaultRedisScript<Long> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setResultType(Long.class);
        defaultRedisScript.setScriptText(script);
        return defaultRedisScript;
    }
}
