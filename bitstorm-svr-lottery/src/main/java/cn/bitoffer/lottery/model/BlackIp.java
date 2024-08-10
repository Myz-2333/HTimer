package cn.bitoffer.lottery.model;

import java.util.Date;

// BlackIp ip黑明单表
public class BlackIp {
    private Long id;
    private String ip;
    private Date blackTime;
    private Date sysCreated;
    private Date sysUpdated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getBlackTime() {
        return blackTime;
    }

    public void setBlackTime(Date blackTime) {
        this.blackTime = blackTime;
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
}
