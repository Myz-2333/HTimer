package cn.bitoffer.seckill.mapper;


import cn.bitoffer.seckill.model.SecKillRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SecKillRecordMapper {

    /**
     * 保存SecKillRecord
     *
     * @param secKillRecord
     */
    void save(@Param("secKillRecord") SecKillRecord secKillRecord);

    /**
     * 更新SecKillRecord
     *
     * @param secKillRecord
     */
    void update(@Param("secKillRecord") SecKillRecord secKillRecord);

}
