package com.example.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@ApiModel(value = "报表生成查询对象", description = "报表生成查询对象")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ReportQuery {

    /**
     * 报表类型
     */
    @ApiModelProperty(value = "1 学生", example = "1", required = true)
    private String code;

    /**
     * 学生名称
     * 各种查询条件
     */
    @ApiModelProperty(value = "名称", example = "姓名")
    private String name;

    /**
     * 学生年龄
     */
    @ApiModelProperty(value = "年龄", example = "11")
    private Integer age;
}
