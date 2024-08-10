package cn.bitoffer.xtimer.common.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MigratorAppConf {

    @Value("${migrator.workersNum}")
    private int workersNum;
    @Value("${migrator.migrateStepMinutes}")
    private int migrateStepMinutes;
    @Value("${migrator.migrateSuccessExpireMinutes}")
    private int migrateSuccessExpireMinutes;
    @Value("${migrator.migrateTryLockMinutes}")
    private int migrateTryLockMinutes;
    @Value("${migrator.timerDetailCacheMinutes}")
    private int timerDetailCacheMinutes;

    public int getWorkersNum() {
        return workersNum;
    }

    public void setWorkersNum(int workersNum) {
        this.workersNum = workersNum;
    }

    public int getMigrateStepMinutes() {
        return migrateStepMinutes;
    }

    public void setMigrateStepMinutes(int migrateStepMinutes) {
        this.migrateStepMinutes = migrateStepMinutes;
    }

    public int getMigrateSuccessExpireMinutes() {
        return migrateSuccessExpireMinutes;
    }

    public void setMigrateSuccessExpireMinutes(int migrateSuccessExpireMinutes) {
        this.migrateSuccessExpireMinutes = migrateSuccessExpireMinutes;
    }

    public int getMigrateTryLockMinutes() {
        return migrateTryLockMinutes;
    }

    public void setMigrateTryLockMinutes(int migrateTryLockMinutes) {
        this.migrateTryLockMinutes = migrateTryLockMinutes;
    }

    public int getTimerDetailCacheMinutes() {
        return timerDetailCacheMinutes;
    }

    public void setTimerDetailCacheMinutes(int timerDetailCacheMinutes) {
        this.timerDetailCacheMinutes = timerDetailCacheMinutes;
    }
}
