package cn.bitoffer.shorturlx.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Post请求
 *
 */
@Data
public class ExamplePostRequest implements Serializable {

    /**
     * 名称
     */
    private String name;

    /**
     * 年龄
     */
    private int age;

    /**
     * 标签列表
     */
    private List<String> tags;

    private static final long serialVersionUID = 1L;
}