package cn.bitoffer.lottery.mapper;
import cn.bitoffer.lottery.model.BlackIp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface BlackIpMapper {
    /**
     * 保存BlackIp
     *
     * @param blackIp
     */
    void save(@Param("blackIp") BlackIp blackIp);

    /**
    通过ip获取ip黑名单信息
     @param ip
     **/
    BlackIp getByIP(@Param("ip") String ip);


    /**
     * BlackIp
     *
     * @param ip
     * @param blackTime
     */
    void updateBlackTimeByIP(@Param("ip") String ip, @Param("blackTime") Date blackTime);

}
