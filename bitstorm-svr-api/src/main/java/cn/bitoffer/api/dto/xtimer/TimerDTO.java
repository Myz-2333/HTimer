package cn.bitoffer.api.dto.xtimer;

public class TimerDTO {
    /**
     * 定时任务ID
     */
    private Long timerId;

    /**
     * APP名称（所属业务）
     */
    private String app;

    /**
     * 定时任务-名称
     */
    private String name;

    /**
     * 定时任务-状态
     */
    private int status;

    /**
     *  定时任务-定时配置
     */
    private String cron;

    /**
     * Name 定时任务-回调参数配置
     */
    private NotifyHTTPParam notifyHTTPParam;

    public Long getTimerId() {
        return timerId;
    }

    public void setTimerId(Long timerId) {
        this.timerId = timerId;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public NotifyHTTPParam getNotifyHTTPParam() {
        return notifyHTTPParam;
    }

    public void setNotifyHTTPParam(NotifyHTTPParam notifyHTTPParam) {
        this.notifyHTTPParam = notifyHTTPParam;
    }
}
