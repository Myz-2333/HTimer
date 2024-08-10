package cn.bitoffer.improve.model;


import cn.bitoffer.common.model.BaseModel;

import java.io.Serializable;

/**
 * Example 数据库模型
 *
 **/
public class Example extends BaseModel implements Serializable {
    /**
     * Id
     */
    private Long exampleId;

    /**
     * Name
     */
    private String exampleName;

    /**
     * Status
     */
    private Integer status;

    public Long getExampleId() {
        return exampleId;
    }

    public void setExampleId(Long exampleId) {
        this.exampleId = exampleId;
    }

    public String getExampleName() {
        return exampleName;
    }

    public void setExampleName(String exampleName) {
        this.exampleName = exampleName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Example{" +
                "exampleId=" + exampleId +
                ", exampleName='" + exampleName + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                '}';
    }
}
