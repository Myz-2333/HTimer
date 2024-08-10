package cn.bitoffer.seckill.mapper;


import cn.bitoffer.seckill.model.Example;
import cn.bitoffer.seckill.model.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExampleMapper {

    /**
     * 保存Example
     *
     * @param example
     */
    void save(@Param("example") Example example);

    /**
     * 根据ExampleId删除Example
     *
     * @param exampleId
     */
    void deleteById(@Param("exampleId") Long exampleId);

    /**
     * 更新Example
     *
     * @param example
     */
    void update(@Param("example") Example example);

    /**
     * 根据ExampleId查询Example
     *
     * @param exampleId
     * @return Example
     */
    Example getExampleById(@Param("exampleId") Long exampleId);


    /**
     * 根据goodsNum查询goods
     *
     * @param goodsNum
     * @return Goods
     */
    Goods getGoodsByNum(@Param("goodsNum") String goodsNum);

}
