package cn.bitoffer.xtimer.manager;

import cn.bitoffer.xtimer.model.TimerModel;

public interface MigratorManager{
    public void migrateTimer(TimerModel timerModel);
}
