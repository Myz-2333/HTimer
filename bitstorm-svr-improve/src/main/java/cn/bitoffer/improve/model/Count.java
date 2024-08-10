package cn.bitoffer.improve.model;


import cn.bitoffer.common.model.BaseModel;

import java.io.Serializable;

/**
 * Example 数据库模型
 *
 **/
public class Count extends BaseModel implements Serializable {
    /**
     * Id
     */
    private Long ID;

    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Count{" +
                "count=" + count +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                '}';
    }
}
