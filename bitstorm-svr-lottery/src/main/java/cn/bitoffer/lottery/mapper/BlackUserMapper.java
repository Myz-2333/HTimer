package cn.bitoffer.lottery.mapper;

import cn.bitoffer.lottery.model.BlackUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface BlackUserMapper {
    /**
     * 保存BlackUser
     *
     * @param blackUser
     */
    void save(@Param("blackUser") BlackUser blackUser);

    /**
     通过ip获取user_id黑名单信息
     @param uid
     **/
    BlackUser getByUserId(@Param("uid") Long uid);

    /**
     * BlackUser
     *
     * @param uid
     * @param blackTime
     */
    void updateBlackTimeByUserId(@Param("uid") Long uid,@Param("userId") Date blackTime);
}
