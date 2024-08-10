package cn.bitoffer.seckill.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONUtil {

    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        mapper.setSerializationInclusion(Include.NON_NULL);
    }

    /**
     * 序列化为JSON字符串
     */
    public static String toJsonString(Object obj) {
        if (obj == null) return null;
        String result = null;
        try {
            result = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 解析对象
     * 反序列化为Object
     */
    public static <T> T parseObject(String jsonStr, Class<T> clazz) {
        if (StringUtils.isBlank(jsonStr) || clazz == null) return null;
        T t = null;
        try {
            t = mapper.readValue(jsonStr, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return t;
    }


    /**
     * 解析List
     * 反序列化为List集合
     */
    public static <T> List<T> parseList(String listJsonStr, Class<T> clazz) {
        if (StringUtils.isBlank(listJsonStr) || clazz == null) return Collections.emptyList();
        List<T> list = Collections.emptyList();
        try {
            list = mapper.readValue(listJsonStr, List.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 解析Map
     * 反序列化为Map集合
     */
    public static <K, V> Map<K, V> parseMap(String mapJsonStr, Class<K> kClazz, Class<V> vClazz) {
        if (StringUtils.isBlank(mapJsonStr) || kClazz == null || vClazz == null) return Collections.emptyMap();
        Map<K, V> map = new HashMap<>();
        try {
            map = mapper.readValue(mapJsonStr, mapper.getTypeFactory().constructParametricType(Map.class, kClazz, vClazz));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return map;
    }


}