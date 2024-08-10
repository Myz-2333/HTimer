package cn.bitoffer.xtimer.enums;

public enum TaskStatus {
    NotRun(0),
    Running(1),
    Succeed(2),
    Failed(3);

    private TaskStatus(int status) {
        this.status = status;
    }
    private int status;

    public int getStatus() {
        return this.status;
    }
}
