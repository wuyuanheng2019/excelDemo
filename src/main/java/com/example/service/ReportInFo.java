package com.example.service;

import com.example.query.ReportQuery;

/**
 * 包装接口
 */
public interface ReportInFo {

    /**
     * 根据不同条件生成不同报表
     * 使用异步方法调用（spring @async）
     *
     * @param reportQuery 条件
     */
    void creatReport(ReportQuery reportQuery);
}
