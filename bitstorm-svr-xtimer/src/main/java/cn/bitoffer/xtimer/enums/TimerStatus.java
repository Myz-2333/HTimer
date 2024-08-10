package cn.bitoffer.xtimer.enums;

public enum TimerStatus {
    Unable(1),
    Enable(2),;

    private TimerStatus(int status) {
        this.status = status;
    }
    private int status;

    public int getStatus() {
        return this.status;
    }

    public static TimerStatus getTimerStatus(int status){
        for (TimerStatus value:TimerStatus.values()) {
            if(value.status == status){
                return value;
            }
        }
        return null;
    }
}
