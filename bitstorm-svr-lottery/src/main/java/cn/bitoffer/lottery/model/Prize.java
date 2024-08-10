package cn.bitoffer.lottery.model;

import java.util.Date;

public class Prize {
    private Long id;
    private String title;
    private int prizeNum;
    private int leftNum;
    private  String prizeCode;
    private int prizeTime;
    private String img;
    private int displayOrder;
    private int prizeType;
    private String prizeProfile;
    private Date beginTime;
    private Date endTime;
    private String prizePlan;
    private Date prizeBegin;
    private Date prizeEnd;
    private int sysStatus;
    private Date sysCreated;
    private Date sysUpdated;
    private String sysIp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrizeNum() {
        return prizeNum;
    }

    public void setPrizeNum(int prizeNum) {
        this.prizeNum = prizeNum;
    }

    public int getLeftNum() {
        return leftNum;
    }

    public void setLeftNum(int leftNum) {
        this.leftNum = leftNum;
    }

    public String getPrizeCode() {
        return prizeCode;
    }

    public void setPrizeCode(String prizeCode) {
        this.prizeCode = prizeCode;
    }

    public int getPrizeTime() {
        return prizeTime;
    }

    public void setPrizeTime(int prizeTime) {
        this.prizeTime = prizeTime;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public int getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(int prizeType) {
        this.prizeType = prizeType;
    }

    public String getPrizeProfile() {
        return prizeProfile;
    }

    public void setPrizeProfile(String prizeProfile) {
        this.prizeProfile = prizeProfile;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getPrizePlan() {
        return prizePlan;
    }

    public void setPrizePlan(String prizePlan) {
        this.prizePlan = prizePlan;
    }

    public Date getPrizeBegin() {
        return prizeBegin;
    }

    public void setPrizeBegin(Date prizeBegin) {
        this.prizeBegin = prizeBegin;
    }

    public Date getPrizeEnd() {
        return prizeEnd;
    }

    public void setPrizeEnd(Date prizeEnd) {
        this.prizeEnd = prizeEnd;
    }

    public int getSysStatus() {
        return sysStatus;
    }

    public void setSysStatus(int sysStatus) {
        this.sysStatus = sysStatus;
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

    public String getSysIp() {
        return sysIp;
    }

    public void setSysIp(String sysIp) {
        this.sysIp = sysIp;
    }
}
