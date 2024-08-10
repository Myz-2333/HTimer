package cn.bitoffer.improve.mapper;


import cn.bitoffer.improve.model.Count;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface CountMapper {

    /**
     * 保存Count
     *
     * @param count
     */
    void save(@Param("count") Count count);

    /**
     * 更新Count
     *
     * @param count
     */
    void update(@Param("count") Count count);

    /**
     * 根据CountId查询Count
     *
     * @return Count
     */
    Count getCount();

    /**
     * 增加Count计数
     *
     * @param num
     * @return Count
     */
    Integer incrCount(@Param("num") Integer num);
}