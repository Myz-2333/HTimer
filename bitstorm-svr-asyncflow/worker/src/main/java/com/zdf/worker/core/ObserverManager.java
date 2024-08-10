package com.zdf.worker.core;

import com.zdf.worker.boot.AppLaunch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ObserverManager {
    List<ObserverFunction> observers;

    public ObserverManager() {
        observers = new ArrayList<>();
    }
    // 添加观察者
    public void registerEventObserver(ObserverFunction observerFunction) {
        observers.add(observerFunction);
    }

    // 通过发射找到对应的方法执行
    public void wakeupObserver(AppLaunch.ObserverType observerType, Object... params) throws InvocationTargetException, IllegalAccessException {
        for (ObserverFunction observer : observers) {
            for (Method method : observer.getClass().getMethods()) {
                if (method.getName().equals(observerType.name())) {
                    method.invoke(observer, params);
                }
            }
        }
    }
}
