package cn.bitoffer.api.dto.xtimer;

public class LotteryParam {
    private int userId;  // 用户id
    private String userName; // 用户名
    private String ip; // ip

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }


    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public LotteryParam(int userId, String userName, String ip) {
        this.userId = userId;
        this.userName = userName;
        this.ip = ip;
    }
}
