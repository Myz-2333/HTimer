package com.zdf.worker.task;

import com.alibaba.fastjson.JSON;
import com.zdf.worker.data.AsyncTaskSetStage;
import com.zdf.worker.data.NftTaskContext;

import java.lang.reflect.Method;

// 测试任务
// 此处可以定义自己的任务
public class Lark implements AsyncExecutable {
    public TaskRet printMsg(String msg) {
        System.out.println("The printed msg is: " + msg);
        AsyncTaskSetStage asyncTaskSetStage = null;
        try {
            Method method = this.getClass().getMethod("printMsg2", String.class);
            asyncTaskSetStage = setStage(this.getClass(), method.getName(), new Object[]{"我要开花！"}, method.getParameterTypes());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return new TaskRet("SUCCESS", asyncTaskSetStage);
    }

    public TaskRet printMsg2(String msg) {
        System.out.println("第二阶段开启中文打印: " + msg);
        return new TaskRet("执行成功");
    }

    @Override
    public TaskRet handleProcess() {
        return printMsg("I did it!");
    }

    @Override
    public TaskRet handleFinish() {
        System.out.println("任务后置处理，可以自定义做点任务执行成功后的后置处理，例如回收资源等");
        return new TaskRet("全部任务阶段执行完毕~");
    }

    @Override
    public TaskRet handleError() {
        System.out.println("任务最终执行失败干点事，可以自定义一些操作");
        return new TaskRet("任务实在是执行不了了，还是人工检查一下吧~");
    }

    @Override
    public TaskRet contextLoad(String context) {
        System.out.println("上下文加载，用户可以根据自己定义的协议格式对上下文进行解析");
        NftTaskContext nftTaskContext = JSON.parseObject(context, NftTaskContext.class);
        return new TaskRet<>(nftTaskContext);
    }
}
