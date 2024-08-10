package cn.bitoffer.seckill.mapper;


import cn.bitoffer.seckill.model.UserQuota;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface UserQuotaMapper {

    /**
     * 保存UserQuota
     *
     * @param userQuota
     */
    void save(@Param("userQuota") UserQuota userQuota);

    /**
     * 更新UserQuota
     *
     * @param userQuota
     */
    void update(@Param("userQuota") UserQuota userQuota);

    /**
     * 根据UserQuotaId查询UserQuota
     *
     * @param userQuotaId
     * @return UserQuota
     */
    UserQuota getUserQuotaById(@Param("userQuotaId") Long userQuotaId);

    /**
     * 根据UserQuotaId查询UserQuota
     *
     * @param userID
     * @param goodsID
     * @return UserQuota
     */
    UserQuota getUserGoodsUserQuota(@Param("userID") Long userID, @Param("goodsID") Long goodsID);

    /**
     * 根据UserQuotaId查询UserQuota
     *
     * @param userID
     * @param goodsID
     * @param num
     * @return UserQuota
     */
    Integer incrKilledNum(@Param("userID") Long userID, @Param("goodsID") Long goodsID, @Param("num") Integer num);
}
