package cn.bitoffer.seckill.model;

import cn.bitoffer.seckill.common.BaseModel;
import lombok.Getter;

import java.io.Serializable;

public class Order extends BaseModel implements Serializable {
    public Order(Long seller, Long buyer, String orderNum, Long goodsID, String goodsNum, Float price, Integer status) {
        this.seller = seller;
        this.buyer = buyer;
        this.orderNum = orderNum;
        this.goodsID = goodsID;
        this.goodsNum = goodsNum;
        this.price = price;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "ID=" + ID +
                ", seller=" + seller +
                ", buyer=" + buyer +
                ", orderNum='" + orderNum + '\'' +
                ", goodsID=" + goodsID +
                ", goodsNum='" + goodsNum + '\'' +
                ", price=" + price +
                ", status=" + status +
                '}';
    }

    public Order() {
    }

    /**
     * Id
     */
    private Long ID;
    private Long seller;
    private Long buyer;
    private String orderNum;
    private Long goodsID;
    private String goodsNum;
    private Float price;
    private Integer status;

    public Long getSeller() {
        return seller;
    }

    public void setSeller(Long seller) {
        this.seller = seller;
    }

    public Long getBuyer() {
        return buyer;
    }

    public void setBuyer(Long buyer) {
        this.buyer = buyer;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Long getGoodsID() {
        return goodsID;
    }

    public void setGoodsID(Long goodsID) {
        this.goodsID = goodsID;
    }

    public String getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(String goodsNum) {
        this.goodsNum = goodsNum;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


}
