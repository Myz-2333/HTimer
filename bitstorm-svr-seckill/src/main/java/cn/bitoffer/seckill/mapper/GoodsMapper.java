package cn.bitoffer.seckill.mapper;


import cn.bitoffer.seckill.model.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface GoodsMapper {

    /**
     * 保存Goods
     *
     * @param goods
     */
    void save(@Param("goods") Goods goods);

    /**
     * 根据GoodsId删除Goods
     *
     * @param goodsId
     */
    void deleteById(@Param("goodsId") Long goodsId);

    /**
     * 更新Goods
     *
     * @param goods
     */
    void update(@Param("goods") Goods goods);

    /**
     * 根据GoodsId查询Goods
     *
     * @param goodsId
     * @return Goods
     */
    Goods getGoodsById(@Param("goodsId") Long goodsId);

    /**
     * 根据GoodsId查询Goods
     *
     * @param goodsNum
     * @return Goods
     */
    Goods getGoodsByNum(@Param("goodsNum") String goodsNum);

    ArrayList<Goods> getGoodsList(@Param("offset") Integer offset, @Param("limit") Integer limit);
}
