package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Student;
import com.example.entity.vo.StudentVo;
import com.example.mapper.StudentMapper;
import com.example.query.ReportQuery;
import com.example.service.IStudentService;
import com.example.service.ReportInFo;
import com.example.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wuyuanheng
 * @since 2019-07-07
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService, ReportInFo {

    @Value("${template.studentTemplate}")
    private String studentTemplate;

    @Override
    public void doImportStudentExcel(MultipartFile multipartFile) {

        //需要转换的字段
        String[] fileds = {"name", "age", "mony"};
        File file = FileUtil.multipartFileToFile(multipartFile);
        List<StudentVo> studentVos = ExcelImportUtil.doImportEntity(StudentVo.class,
                file, fileds);

        //校验是否符合数据塞选规则，得到失败队列
        List<StudentVo> vos = StudentVo.check(studentVos);
        vos.forEach(
                //输出失败队列中的数据
                System.out::println
        );

        //删除零时文件
        cn.hutool.core.io.FileUtil.del(file);
    }

    @Override
    public List<Student> selectByReportQuery(ReportQuery reportQuery) {
        return lambdaQuery()
                .eq(StringUtils.isNotEmpty(reportQuery.getName()), Student::getName, reportQuery.getName())
                .eq(reportQuery.getAge() != null, Student::getAge, reportQuery.getAge())
                .list();
    }

    @Override
    public void exprot(HttpServletResponse response) {
        List<Student> students = selectByReportQuery(new ReportQuery());
        List<HashMap<Integer, Object>> mapList = new ArrayList<>();
        String identify = IDGeneratorUtil.getRedisNo("", 4, true, true);

        //数据转换,生成excel，并写入响应
        students.forEach(student -> mapList.add(ExportUtil.swapStudent(student)));
        ExportUtil.saveLocal(studentTemplate, "学生表");
        ExportUtil.export(mapList);
        ExportUtil.writeResponse(response, identify);

    }

    @Override
    public void creatReport(ReportQuery reportQuery) {

        List<Student> students = selectByReportQuery(reportQuery);
        List<HashMap<Integer, Object>> maps = new ArrayList<>();

        //数据转换
        students.forEach(student -> maps.add(ExportUtil.swapStudent(student)));

        //导出并生成excel
        ExportUtil.saveLocal(studentTemplate, "学生表");
        ExportUtil.export(maps);
        ExportUtil.stop();
    }


}
