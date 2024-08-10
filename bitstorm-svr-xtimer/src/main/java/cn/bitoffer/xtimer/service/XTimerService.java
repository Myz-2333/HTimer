package cn.bitoffer.xtimer.service;

import cn.bitoffer.api.dto.xtimer.TimerDTO;

import java.util.List;

public interface XTimerService {

    Long CreateTimer(TimerDTO timerDTO);

    void DeleteTimer(String app, long id);

    void Update(TimerDTO timerDTO);

    TimerDTO GetTimer(String app, long id);

    void EnableTimer(String app, long id);

    void UnEnableTimer(String app, long id);

    List<TimerDTO> GetAppTimers(String app);
}
