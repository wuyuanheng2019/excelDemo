package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Report;
import com.example.mapper.ReportMapper;
import com.example.query.ReportQuery;
import com.example.service.IReportService;
import com.example.service.ReportInFo;
import com.example.util.SpringUtil;
import com.example.util.enums.ReportStatus;
import com.example.util.enums.ReportType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wuyuanheng
 * @since 2019-07-09
 */
@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements IReportService {

    @Override
    @Async("taskExecutor")
    public void creatReport(ReportQuery reportQuery, String identify) {
        try {

            //生成请求信息
            save(new Report()
                    .setReportType(reportQuery.getCode())
                    .setState(ReportStatus.BEING.getCode())
                    .setIdentify(identify));

            //通过编码找到beanName，通过beanName找到bean，并生成对应的报表
            String beanName = ReportType.getBeanNameByCode(reportQuery.getCode());
            ReportInFo reportInFo = (ReportInFo) SpringUtil.getBean(beanName);
            reportInFo.creatReport(reportQuery);

            //修改状态为成功
            lambdaUpdate().set(Report::getFileName, "fileName")
                    .set(Report::getUrl, "rul")
                    .set(Report::getState, ReportStatus.COMPLETE.getCode())
                    .eq(Report::getIdentify, identify);
        } catch (Exception e) {

            log.error(e.getMessage(), e);

            //修改报表状态为生成失败
            lambdaUpdate().set(Report::getState, ReportStatus.FAIL.getCode())
                    .eq(Report::getIdentify, identify);
        }
    }


}
