package cn.bitoffer.xtimer.service.impl;

import cn.bitoffer.api.dto.xtimer.TimerDTO;
import cn.bitoffer.xtimer.exception.ErrorCode;
import cn.bitoffer.xtimer.enums.TimerStatus;
import cn.bitoffer.xtimer.exception.BusinessException;
import cn.bitoffer.xtimer.manager.MigratorManager;
import cn.bitoffer.xtimer.mapper.TimerMapper;
import cn.bitoffer.xtimer.model.TimerModel;
import cn.bitoffer.common.redis.ReentrantDistributeLock;
import cn.bitoffer.xtimer.service.XTimerService;
import cn.bitoffer.xtimer.utils.TimerUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class XTimerServiceImpl implements XTimerService {

    @Autowired
    private TimerMapper timerMapper;

    @Autowired
    ReentrantDistributeLock reentrantDistributeLock;

    @Autowired
    MigratorManager migratorManager;

    private static final int  defaultGapSeconds= 3;

    @Override
    public Long CreateTimer(TimerDTO timerDTO) {
//        String lockToken = TimerUtils.GetTokenStr();
////        // 只加锁不解锁，只有超时解锁；超时时间控制频率；
////        boolean ok = reentrantDistributeLock.lock(
////                TimerUtils.GetCreateLockKey(timerDTO.getApp()),
////                lockToken,
////                defaultGapSeconds);
////        if(!ok){
////            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"创建/删除操作过于频繁，请稍后再试！");
////        }

        boolean isValidCron = CronExpression.isValidExpression(timerDTO.getCron());
        if(!isValidCron){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"invalid cron");
        }

        TimerModel timerModel = TimerModel.voToObj(timerDTO);
        if (timerModel == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        timerMapper.save(timerModel);
        return timerModel.getTimerId();
    }

    @Override
    public void DeleteTimer(String app, long id) {
        String lockToken = TimerUtils.GetTokenStr();
        boolean ok = reentrantDistributeLock.lock(
                TimerUtils.GetCreateLockKey(app),
                lockToken,
                defaultGapSeconds);
        if(!ok){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"创建/删除操作过于频繁，请稍后再试！");
        }

        timerMapper.deleteById(id);
    }

    @Override
    public void Update(TimerDTO timerDTO) {
        TimerModel timerModel = TimerModel.voToObj(timerDTO);
        if (timerModel == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        timerMapper.update(timerModel);
    }

    @Override
    public TimerDTO GetTimer(String app, long id) {
        TimerModel timerModel  = timerMapper.getTimerById(id);
        TimerDTO timerDTO = TimerModel.objToVo(timerModel);
        return timerDTO;
    }

    @Override
    public void EnableTimer(String app, long id) {
        String lockToken = TimerUtils.GetTokenStr();
        boolean ok = reentrantDistributeLock.lock(
                TimerUtils.GetEnableLockKey(app),
                lockToken,
                defaultGapSeconds);
        if(!ok){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"激活/去激活操作过于频繁，请稍后再试！");
        }

        // 激活逻辑
        doEnableTimer(id);
    }

    @Transactional
    public void doEnableTimer(long id){
        // 1. 数据库获取Timer
        TimerModel timerModel = timerMapper.getTimerById(id);
        if(timerModel == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"激活失败，timer不存在：timerId"+id);
        }
        // 2. 校验状态
        if(timerModel.getStatus() == TimerStatus.Enable.getStatus()){
            log.warn("Timer非Unable状态，激活失败，timerId:"+timerModel.getTimerId());
        }
        // 修改 timer 状态为激活态
        timerModel.setStatus(TimerStatus.Enable.getStatus());
        timerMapper.update(timerModel);
        //迁移数据
        migratorManager.migrateTimer(timerModel);

    }


    @Override
    public void UnEnableTimer(String app, long id) {
        String lockToken = TimerUtils.GetTokenStr();
        boolean ok = reentrantDistributeLock.lock(
                TimerUtils.GetEnableLockKey(app),
                lockToken,
                defaultGapSeconds);
        if(!ok){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"激活/去激活操作过于频繁，请稍后再试！");
        }

        // 去激活逻辑
        doUnEnableTimer(id);
    }

    @Transactional
    public void doUnEnableTimer(Long id){
        // 1. 数据库获取Timer
        TimerModel timerModel = timerMapper.getTimerById(id);
        // 2. 校验状态
        if(timerModel.getStatus() != TimerStatus.Unable.getStatus()){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"Timer非Unable状态，去激活失败，id:"+id);
        }
        timerModel.setStatus(TimerStatus.Unable.getStatus());
        timerMapper.update(timerModel);
    }


    @Override
    public List<TimerDTO> GetAppTimers(String app) {
        return null;
    }
}
