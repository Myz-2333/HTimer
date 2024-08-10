package cn.bitoffer.lottery.model;


import java.util.Date;

public class Result {
    private Long id;
    private  Long prizeId;
    private String prizeName;
    private  int prizeType;
    private  Long userId;
    private String userName;
    private  int prizeCode;
    private String prizeData;
    private Date sysCreated;
    private String sysIp;
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

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public int getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(int prizeType) {
        this.prizeType = prizeType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPrizeCode() {
        return prizeCode;
    }

    public void setPrizeCode(int prizeCode) {
        this.prizeCode = prizeCode;
    }

    public String getPrizeData() {
        return prizeData;
    }

    public void setPrizeData(String prizeData) {
        this.prizeData = prizeData;
    }

    public Date getSysCreated() {
        return sysCreated;
    }

    public void setSysCreated(Date sysCreated) {
        this.sysCreated = sysCreated;
    }

    public String getSysIp() {
        return sysIp;
    }

    public void setSysIp(String sysIp) {
        this.sysIp = sysIp;
    }

    public int getSysStatus() {
        return sysStatus;
    }

    public void setSysStatus(int sysStatus) {
        this.sysStatus = sysStatus;
    }
}
