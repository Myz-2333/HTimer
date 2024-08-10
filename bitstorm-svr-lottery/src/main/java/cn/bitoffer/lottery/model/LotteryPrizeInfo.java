package cn.bitoffer.lottery.model;
public class LotteryPrizeInfo
{
    Long id;
    String title;
    int prizeNum;
    int leftNum;
    int prizeCodeLow;
    int prizeCodeHigh;
    String img;
    int displayOrder;
    int prizeType;
    String prizeProfile;
    String couponCode;

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

    public int getPrizeCodeLow() {
        return prizeCodeLow;
    }

    public void setPrizeCodeLow(int prizeCodeLow) {
        this.prizeCodeLow = prizeCodeLow;
    }

    public int getPrizeCodeHigh() {
        return prizeCodeHigh;
    }

    public void setPrizeCodeHigh(int prizeCodeHigh) {
        this.prizeCodeHigh = prizeCodeHigh;
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

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
}
