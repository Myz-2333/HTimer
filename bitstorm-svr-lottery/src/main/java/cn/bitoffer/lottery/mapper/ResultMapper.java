package cn.bitoffer.lottery.mapper;

import cn.bitoffer.lottery.model.Result;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ResultMapper {
    void save(@Param("result") Result result);

    void deleteAll();
}
