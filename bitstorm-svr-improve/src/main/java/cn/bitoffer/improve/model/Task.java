package cn.bitoffer.improve.model;


import cn.bitoffer.common.model.BaseModel;

import java.io.Serializable;

/**
 * Example 数据库模型
 *
 **/
public class Task extends BaseModel implements Serializable {
    /**
     * Id
     */
    private Long ID;

    private String taskID;

    public String getTaskID() {
        return taskID;
    }

    public void setTaskId(String taskID) {
        this.taskID = taskID;
    }

    @Override
    public String toString() {
        return "Task{" +
                "ID=" + ID +
                ", taskID='" + taskID + '\'' +
                '}';
    }

}
