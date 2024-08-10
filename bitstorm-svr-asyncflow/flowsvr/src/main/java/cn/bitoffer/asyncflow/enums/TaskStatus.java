package cn.bitoffer.asyncflow.enums;

public enum TaskStatus {
    TASK_STATUS_PENDING(1),
    TASK_STATUS_PROCESSING(2),
    TASK_STATUS_SUCCESS(3),
    TASK_STATUS_FAILED(4);

    private TaskStatus(int status) {
        this.status = status;
    }

    private final int status;

    public int getStatus() {
        return this.status;
    }
}
