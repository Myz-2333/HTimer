package cn.bitoffer.seckill.mapper;


import cn.bitoffer.seckill.model.SecKillStock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SecKillStockMapper {

    /**
     * 保存SecKillStock
     *
     * @param secKillStock
     */
    void save(@Param("stock") SecKillStock secKillStock);

    /**
     * 更新SecKillStock
     *
     * @param secKillStock
     */
    void update(@Param("stock") SecKillStock secKillStock);

    /**
     * 根据SecKillStockId查询SecKillStock
     *
     * @param stockId
     * @return SecKillStock
     */
    SecKillStock getSecKillStockById(@Param("stockId") Long stockId);

    /**
     * DescStock
     *
     * @param goodsID
     * @param num
     * @return void
     */
    Integer descStock(@Param("goodsID") Long goodsID, @Param("num") Integer num);
}
