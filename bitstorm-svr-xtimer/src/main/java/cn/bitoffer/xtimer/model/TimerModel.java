package cn.bitoffer.xtimer.model;

import cn.bitoffer.api.dto.xtimer.NotifyHTTPParam;
import cn.bitoffer.api.dto.xtimer.TimerDTO;
import cn.bitoffer.common.model.BaseModel;
import cn.bitoffer.xtimer.utils.JSONUtil;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

public class TimerModel extends BaseModel implements Serializable {

    private Long timerId;

    private String app;

    private String name;

    private int status;

    private String cron;

    private String notifyHTTPParam;

    /**
     * 包装类转对象
     *
     * @param timerDTO
     * @return
     */
    public static TimerModel voToObj(TimerDTO timerDTO) {
        if (timerDTO == null) {
            return null;
        }
        TimerModel timerModel = new TimerModel();
        timerModel.setApp(timerDTO.getApp());
        timerModel.setTimerId(timerDTO.getTimerId());
        timerModel.setName(timerDTO.getName());
        timerModel.setStatus(timerDTO.getStatus());
        timerModel.setCron(timerDTO.getCron());
        timerModel.setNotifyHTTPParam(JSONUtil.toJsonString(timerDTO.getNotifyHTTPParam()));
        return timerModel;
    }

    /**
     * 对象转包装类
     *
     * @param timerModel
     * @return
     */
    public static TimerDTO objToVo(TimerModel timerModel) {
        if (timerModel == null) {
            return null;
        }
        TimerDTO timerDTO = new TimerDTO();
        timerDTO.setApp(timerModel.getApp());
        timerDTO.setTimerId(timerModel.getTimerId());
        timerDTO.setName(timerModel.getName());
        timerDTO.setStatus(timerModel.getStatus());
        timerDTO.setCron(timerModel.getCron());

        NotifyHTTPParam httpParam = JSONUtil.parseObject(timerModel.getNotifyHTTPParam(),NotifyHTTPParam.class);
        timerDTO.setNotifyHTTPParam(httpParam);

        BeanUtils.copyProperties(timerModel, timerDTO);
        return timerDTO;
    }

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

    public String getNotifyHTTPParam() {
        return notifyHTTPParam;
    }

    public void setNotifyHTTPParam(String notifyHTTPParam) {
        this.notifyHTTPParam = notifyHTTPParam;
    }
}
