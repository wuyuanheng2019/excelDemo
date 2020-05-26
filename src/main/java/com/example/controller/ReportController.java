package com.example.controller;


import com.example.query.ReportQuery;
import com.example.service.IReportService;
import com.example.util.IDGeneratorUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wuyuanheng
 * @since 2019-07-09
 */
@Slf4j
@RestController
@Api(value = "ReportController",tags = {"报表接口文档"})
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private IReportService reportService;

    @ApiOperation(value = "报表生成",notes = "报表生成")
    @PostMapping
    public void creatReport(ReportQuery reportQuery){
        List<String> list = new ArrayList<>();
        //生成请求标识
        String identify = IDGeneratorUtil.getRedisNo("DC", 6, true, true);
        reportService.creatReport(reportQuery,identify);
    }

}
