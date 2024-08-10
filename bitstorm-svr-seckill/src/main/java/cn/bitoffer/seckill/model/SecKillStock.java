package cn.bitoffer.seckill.model;

import cn.bitoffer.seckill.common.BaseModel;
import lombok.Getter;

import java.io.Serializable;

public class SecKillStock extends BaseModel implements Serializable {
    /**
     * Id
     */
    private Long ID;

    @Getter
    private String secNum;
    /**
     * Name
     */
    @Getter
    private Long userID;
    private Long goodsID;
    private Float price;
    private Integer status;


    @Override
    public String toString() {
       return secNum;
    }
}
