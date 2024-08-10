package cn.bitoffer.seckill.mapper;


import cn.bitoffer.seckill.model.Quota;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface QuotaMapper {

    /**
     * 保存Quota
     *
     * @param quota
     */
    void save(@Param("quota") Quota quota);

    /**
     * 更新Quota
     *
     * @param quota
     */
    void update(@Param("quota") Quota quota);

    /**
     * 根据QuotaId查询Quota
     *
     * @param quotaId
     * @return Quota
     */
    Quota getQuotaById(@Param("quotaId") Long quotaId);

    /**
     * 根据QuotaId查询Quota
     *
     * @param goodsID
     * @return Quota
     */
    Quota getGoodsQuota( @Param("goodsID") Long goodsID);
}
