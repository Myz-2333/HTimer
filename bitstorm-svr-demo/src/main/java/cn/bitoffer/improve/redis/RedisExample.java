package cn.bitoffer.improve.redis;

import cn.bitoffer.common.redis.RedisBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Redis 模版
 *
 **/
@Component
@Slf4j
public class RedisExample {

    @Autowired
    private RedisBase redisBase;

    public void luaExample(){
        List<String> res= redisBase.executeLuaReturnString(getTestLuaScript(), null);
        System.out.println(res);
    }

    private String getTestLuaScript() {
        String script = "local retAry = {0, \"\"}\n" +
                "retAry[1] = \"bbb\"\n" +
                "retAry[2] = \"aaa\"\n" +
                "return retAry\n";
        return script;
    }

    private DefaultRedisScript<Long> get() {
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
