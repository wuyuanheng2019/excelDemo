package com.example.service;

import com.example.entity.Report;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.query.ReportQuery;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wuyuanheng
 * @since 2019-07-09
 */
public interface IReportService extends IService<Report> {

    /**
     * 根据条件生成报表
     *
     * @param reportQuery 查询条件
     * @param identify    请求标识
     */
    void creatReport(ReportQuery reportQuery, String identify);


}
