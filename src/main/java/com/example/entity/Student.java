package com.example.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author wuyuanheng
 * @since 2019-07-07
 */
@Data
@Accessors(chain = true)
@TableName("student")
@EqualsAndHashCode(callSuper = false)
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 钱
     */
    private BigDecimal mony;

    /**
     * 创建时间
     * 设置时间为自动更新，现实为false
     */
    @TableField(fill = FieldFill.INSERT,select = false)
    private LocalDateTime creatTime;
}
