package cn.bitoffer.lottery.model;

public class CheckResult {
    BlackUser blackUser;
    BlackIp blackIp;
    boolean ok;

    public BlackUser getBlackUser() {
        return blackUser;
    }

    public void setBlackUser(BlackUser blackUser) {
        this.blackUser = blackUser;
    }

    public BlackIp getBlackIp() {
        return blackIp;
    }

    public void setBlackIp(BlackIp blackIp) {
        this.blackIp = blackIp;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }
}
