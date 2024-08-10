package cn.bitoffer.seckill.model;

import cn.bitoffer.seckill.common.BaseModel;
import lombok.Getter;

import java.io.Serializable;

public class Goods extends BaseModel implements Serializable {
    /**
     * Id
     */
    private Long ID;

    private String goodsNum;
    private String goodsName;
    private Float price;

    private String picUrl;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(String goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Long getSeller() {
        return seller;
    }

    public void setSeller(Long seller) {
        this.seller = seller;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "ID=" + ID +
                ", goodsNum='" + goodsNum + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", price=" + price +
                ", picUrl='" + picUrl + '\'' +
                ", seller=" + seller +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                '}';
    }

    private Long seller;


}
