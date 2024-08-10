package cn.bitoffer.seckill.mapper;


import cn.bitoffer.seckill.model.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper {

    /**
     * 保存Order
     *
     * @param order
     */
    void save(@Param("order") Order order);

    /**
     * 更新Order
     *
     * @param order
     */
    void update(@Param("order") Order order);

}
