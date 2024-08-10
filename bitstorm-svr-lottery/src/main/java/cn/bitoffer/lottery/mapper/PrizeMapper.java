package cn.bitoffer.lottery.mapper;

import cn.bitoffer.lottery.model.Prize;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Date;

@Mapper
public interface PrizeMapper {
    Prize get(@Param("id") int id);

    ArrayList<Prize> getAll();

    void sava(@Param("prize") Prize prize);

    void saveInBatches(@Param("prizeList") ArrayList<Prize> prizeList);

    void deleteAll();

    void updatePrizePlan(@Param("id") Long id, @Param("prizePlan") String prizePlan);

    void updatePrizePlanWithTime(@Param("id") Long id, @Param("prizePlan") String prizePlan, @Param("prizeBegin") Date prizeBegin, @Param("prizeEnd") Date prizeEnd);

    ArrayList<Prize> getAllUsefulPrizeList(@Param("now") Date now);

    int decrLeftNum(@Param("id") Long id,@Param("num")int num);

}
