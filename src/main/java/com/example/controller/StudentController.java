package com.example.controller;


import com.example.service.IStudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wuyuanheng
 * @since 2019-07-07
 */
@RestController
@RequestMapping("/student")
@Api(value = "StudentController", tags = {"学生接口"})
public class StudentController {

    @Autowired
    private IStudentService studentService;

    @ApiOperation(value = "导入文件", notes = "导入文件")
    @PostMapping("/upload-file")
    public void doImportExcel(@RequestParam("file") MultipartFile file) {
        studentService.doImportStudentExcel(file);
    }

    @ApiOperation(value = "测试导出", notes = "测试导出")
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        studentService.exprot(response);
    }

}
