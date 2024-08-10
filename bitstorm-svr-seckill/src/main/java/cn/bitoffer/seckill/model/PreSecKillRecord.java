package cn.bitoffer.seckill.model;

import cn.bitoffer.seckill.common.BaseModel;

import java.io.Serializable;

public class PreSecKillRecord extends BaseModel implements Serializable {
    private String secNum;
    private Long userID;
    private Long goodsID;
    private String orderNum;
    private Float price;

    private Integer status;

    public String getSecNum() {
        return secNum;
    }

    public void setSecNum(String secNum) {
        this.secNum = secNum;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getGoodsID() {
        return goodsID;
    }

    public void setGoodsID(Long goodsID) {
        this.goodsID = goodsID;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
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
