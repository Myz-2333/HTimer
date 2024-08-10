package cn.bitoffer.seckill.model;

import cn.bitoffer.seckill.common.BaseModel;
import lombok.Getter;

import java.io.Serializable;

public class UserQuota extends BaseModel implements Serializable {
    /**
     * Id
     */
    private Long ID;

    private Integer num;
    private Integer killedNum;
    private Long userID;
    private Long goodsID;

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getKilledNum() {
        return killedNum;
    }

    public void setKilledNum(Integer killedNum) {
        this.killedNum = killedNum;
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

    @Override
    public String toString() {
        return "UserQuota{" +
                "ID=" + ID +
                ", num=" + num +
                ", killedNum=" + killedNum +
                ", userID=" + userID +
                ", goodsID=" + goodsID +
                '}';
    }
}
