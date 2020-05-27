package com.example.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author wuyuanheng
 * @since 2019-07-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("report")
@Accessors(chain = true)
public class Report {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 请求标识
     */
    private String identify;

    /**
     * 文件上传地址
     */
    private String url;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 状态 1 运行完成 2运行中 3 运行失败
     */
    private String state;

    /**
     * 报表类型
     */
    private String reportType;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT,select = false)
    private LocalDateTime creatTime;


}
