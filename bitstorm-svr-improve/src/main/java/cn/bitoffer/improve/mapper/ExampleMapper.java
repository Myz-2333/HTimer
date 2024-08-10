package cn.bitoffer.improve.mapper;


import cn.bitoffer.improve.model.Example;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * mybatis mapper 接口模版
 * 操作数据库的接口
 **/
@Mapper
public interface ExampleMapper {

    /**
     * 保存Example
     *
     * @param example
     */
    void save(@Param("example") Example example);

    /**
     * 根据ExampleId删除Example
     *
     * @param exampleId
     */
    void deleteById(@Param("exampleId") Long exampleId);

    /**
     * 更新Example
     *
     * @param example
     */
    void update(@Param("example") Example example);

    /**
     * 根据ExampleId查询Example
     *
     * @param exampleId
     * @return Example
     */
    Example getExampleById(@Param("exampleId") Long exampleId);

}
