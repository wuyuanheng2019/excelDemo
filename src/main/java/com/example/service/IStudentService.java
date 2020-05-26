package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Student;
import com.example.query.ReportQuery;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wuyuanheng
 * @since 2019-07-07
 */
public interface IStudentService extends IService<Student> {

    /**
     * 文件导入
     *
     * @param file 文件
     */
    void doImportStudentExcel(MultipartFile file);

    /**
     * 根据条件查询数据
     *
     * @param reportQuery 报表query
     * @return 集合
     */
    List<Student> selectByReportQuery(ReportQuery reportQuery);

    /**
     * 生成文件并写入响应
     *
     * @param response 响应
     */
    void exprot(HttpServletResponse response);
}
