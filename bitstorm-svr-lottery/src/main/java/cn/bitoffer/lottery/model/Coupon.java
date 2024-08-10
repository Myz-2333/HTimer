package cn.bitoffer.lottery.model;

import java.util.Date;

public class Coupon {
    private Long id;
    private Long prizeId;
    private String code;
    private Date sysCreated;
    private Date sysUpdated;
    private int sysStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(Long prizeId) {
        this.prizeId = prizeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getSysCreated() {
        return sysCreated;
    }

    public void setSysCreated(Date sysCreated) {
        this.sysCreated = sysCreated;
    }

    public Date getSysUpdated() {
        return sysUpdated;
    }

    public void setSysUpdated(Date sysUpdated) {
        this.sysUpdated = sysUpdated;
    }

    public int getSysStatus() {
        return sysStatus;
    }

    public void setSysStatus(int sysStatus) {
        this.sysStatus = sysStatus;
    }
}
