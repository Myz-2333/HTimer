package cn.bitoffer.shorturlx.service.impl;

import cn.bitoffer.shorturlx.mapper.UrlMapMapper;
import cn.bitoffer.shorturlx.model.UrlMap;
import cn.bitoffer.shorturlx.redis.RedisUtil;
import cn.bitoffer.shorturlx.service.ShortUrlXService;
import cn.bitoffer.shorturlx.utils.Base62;
import cn.bitoffer.shorturlx.utils.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShortUrlXServiceImpl implements ShortUrlXService {

    @Autowired
    private UrlMapMapper urlMapMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private Base62 base62;
    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public String getV1LongUrl(String shortUrl) {
        // 直接从数据库中获取
        return urlMapMapper.dbGetLongUrl(shortUrl);
    }

    @Override
    public String getV2LongUrl(String shortUrl) {
        // 先从布隆过滤器中获取
        List<String> keys = new ArrayList<>();
        keys.add(shortUrlBloomFilterKey);
        List<Object> values = new ArrayList<>();
        values.add(shortUrl);
        List<Object> result = redisUtil.executeLua(findShortUrlFormBloomFilterLua, keys, values);
        assert result != null;
        long isExist = (long) result.get(0);
        if (isExist == 1) {
            // 如果存在，还得从数据库中获取
            return urlMapMapper.dbGetLongUrl(shortUrl);
        }
        // 如果不存在，直接返回null
        return null;
    }

    @Override
    public String getV3LongUrl(String shortUrl) {
        // 从布隆过滤器中查询以及缓存中查询
        List<String> keys = new ArrayList<>();
        keys.add(shortUrlBloomFilterKey);
        keys.add(shortUrlPrefix + shortUrl);
        List<Object> values = new ArrayList<>();
        values.add(shortUrl);
        List<Object> result = redisUtil.executeLua(findShortUrlFormBloomFilterAndCacheLua, keys, values);
        assert result != null;

        long need = (long) result.get(0);
        if (need == 1) {
            return urlMapMapper.dbGetLongUrl(shortUrl);
        }
        return (String) result.get(1);
    }

    @Override
    public String createV1ShortUrl(String longUrl) {
        // 先从数据库中获取
        String shortUrl = urlMapMapper.dbGetShortUrl(longUrl);
        if (shortUrl != null && !shortUrl.isEmpty()) {
            return shortUrl;
        }
        // 如果没有，在数据库里面创建一条记录
        UrlMap urlMap = new UrlMap(longUrl);
        urlMapMapper.dbCreate(urlMap);
        // 利用base62算法 生成短链
        shortUrl = base62.generateShortUrl(urlMap.getId());

        //  更新数据库中的记录
        urlMapMapper.dbUpdate(shortUrl, urlMap.getLongUrl());

        return shortUrl;
    }

    @Override
    public String createV2ShortUrl(String longUrl) {
        // 先从数据库中查询
        String shortUrl = urlMapMapper.dbGetShortUrl(longUrl);
        if (shortUrl != null && !shortUrl.isEmpty()) {
            return shortUrl;
        }

        Long id = redisUtil.incr(cacheIdKey, 1);
        shortUrl = base62.generateShortUrl(id);
        // 保存到布隆过滤器中
        addShortUrlToBloomFilterLua(shortUrl);
        // 保存到数据库中
        urlMapMapper.dbCreate(new UrlMap(longUrl, shortUrl));
        return shortUrl;
    }

    @Override
    public String createV3ShortUrl(String longUrl) {
        // 先查询缓存里面是否对应的短链
        String shortUrl = (String) redisUtil.get(longUrlPrefix + longUrl);
        // 缓存中有这个短链，直接返回
        if (shortUrl != null && !shortUrl.isEmpty()) {
            return shortUrl;
        }

        // 再查询数据库里面是否有长链 对应的短链
        shortUrl = urlMapMapper.dbGetShortUrl(longUrl);
        // 数据库中有这个短链, 顺便保存到缓存中
        if (shortUrl != null && !shortUrl.isEmpty()) {
            redisUtil.set(longUrlPrefix + longUrl, shortUrl);
            return shortUrl;
        }

        // 还是没找到, 那就利用雪花算法生成ID
        Long id = snowflakeIdWorker.nextId();
        // 利用base62算法，生成短链
        shortUrl = base62.generateShortUrl(id);
        // 将短链保存到布隆过滤器中
        addShortUrlToBloomFilterLua(shortUrl);
        // 保存到缓存中, 以便下次查询
        redisUtil.set(longUrlPrefix + longUrl, shortUrl);

        // 保存到数据库
        urlMapMapper.dbCreate(new UrlMap(longUrl, shortUrl));
        return shortUrl;
    }

    private void addShortUrlToBloomFilterLua(String shortUrl) {
        List<String> keys = new ArrayList<>();
        keys.add(shortUrlBloomFilterKey);
        List<Object> values = new ArrayList<>();
        values.add(shortUrl);
        redisUtil.executeLua(addShortUrlToBloomFilterLua, keys, values);
    }


    private final String projectPrefix = "shortUrlX-";
    private final String shortUrlPrefix = projectPrefix + "ShortUrl:";
    private final String longUrlPrefix = projectPrefix + "LongUrl:";
    private final String shortUrlBloomFilterKey = projectPrefix + "BloomFilter-ShortUrl";
    private final String cacheIdKey = projectPrefix + "IncrId";

    private final String findShortUrlFormBloomFilterLua = "local exist = redis.call('bf.exists', KEYS[1], ARGV[1])\n" +
            "return exist\n";

    private final String addShortUrlToBloomFilterLua = "redis.call('bf.add', KEYS[1], ARGV[1])";
    private final String findShortUrlFormBloomFilterAndCacheLua = "local bloomKey = KEYS[1]\nlocal cacheKey = KEYS[2]\nlocal bloomVal = ARGV[1]\n\n-- 检查val是否存在于布隆过滤器对应的bloomKey中\nlocal exists = redis.call('BF.EXISTS', bloomKey, bloomVal)\n\n-- 如果bloomVal不存在于布隆过滤器中，直接返回空字符串, 返回0代表不需要查db了\nif exists == 0 then\n    return {0, ''}\nend\n\n-- 如果bloomVal存在于布隆过滤器中，查询cacheKey\nlocal value = redis.call('GET', cacheKey)\n\n-- 如果cacheKey存在，返回对应的值，否则返回空字符串\nif value then\n    return {0, value}\nelse\n    return {1, ''}\nend\n";
}
