package com.example.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.servlet.multipart.UploadFile;
import com.example.entity.Student;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.FileUpload;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ExportUtil {

    private static ThreadLocal<ExportObject> exportObject = new ThreadLocal<>();

    public static HashMap<Integer, Object> swapStudent(Student student) {

        HashMap<Integer, Object> map = new HashMap<>();

        //姓名
        map.put(0, student.getName());

        //年龄
        map.put(1, student.getAge());

        //钱
        map.put(2, student.getMony());

        //返回
        return map;
    }

    /**
     * 读取文件模版
     *
     * @param template 模版
     * @return 文本
     */
    private static XSSFWorkbook init(String template) {
        InputStream inputStream = ExportUtil.class.getClassLoader().getResourceAsStream(template);

        if (inputStream == null) {
            throw new RuntimeException("加载文件失败！");
        }

        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(inputStream);

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return workbook;

    }

    /**
     * 导出excel（生成初始化文件）
     *
     * @param template 模版
     * @param name     文件名称
     */
    public static void saveLocal(String template, String name) {
        //生成文件名称，系统缓存文件夹下生成文件
        String title = name + "导出" + IDGeneratorUtil.getRedisNo("", 4, true, true);
        String path = FileUtils.getTempDirectoryPath() + File.separator + title + ".xlsx";

        //读取模版
        XSSFWorkbook workbook = init(template);
        if (workbook == null) {
            throw new RuntimeException("init 方法失败！");
        }

        //读取并设置模版第一行数据的样式
        XSSFRow row = workbook.getSheetAt(0).getRow(1);
        HashMap<Integer, CellStyle> cellMap = new HashMap<>(32);
        row.iterator().forEachRemaining(cell -> {
            //获取并设置样式
            cellMap.put(cell.getColumnIndex(), cell.getCellStyle());
        });

        //读取模版头并保存样式
        XSSFRow rowHead = workbook.getSheetAt(0).getRow(0);
        HashMap<Integer, CellStyle> cellMapHead = new HashMap<>(32);

        //保存头部文字
        ArrayList<String> headStr = new ArrayList<>();
        rowHead.iterator().forEachRemaining(cell -> {
            cellMapHead.put(cell.getColumnIndex(), cell.getCellStyle());
            headStr.add(cell.getStringCellValue());
        });

        //生成初始化文件，删除样式，保留表头
        workbook.getSheetAt(0).removeRow(row);
        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(path))) {
            workbook.write(fileOutputStream);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("初始化文件生成异常！");

        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }

        //把导出需要的数据存放到threadLocal中
        exportObject.remove();
        exportObject.set(ExportObject.saveExportObject(path, cellMap, cellMapHead, headStr));
    }


    /**
     * 数据转换
     *
     * @param list 查询数据
     */
    public static void export(List<HashMap<Integer, Object>> list) {
        if (CollectionUtil.isEmpty(list)) {
            throw new RuntimeException("传入数据不能为null ！");
        }


        ExportObject object = exportObject.get();
        SXSSFWorkbook workbook = object.getWorkbook();
        HashMap<Integer, CellStyle> cellMap = object.getCellMap();

        //获取excel中的sheet页
        AtomicInteger no = object.getNo();
        SXSSFSheet sheet = workbook.getSheetAt(0);

        for (Map<Integer, Object> map : list) {
            int index = no.getAndIncrement() + 1;
            SXSSFRow row = sheet.createRow(index);

            /*//是否生成序号（根据业务需求场景来定）
            SXSSFCell noCell = row.createCell(0);
            noCell.setCellValue(index);
            noCell.setCellStyle(cellMap.get(0));*/

            //写入数据
            map.forEach((integer, o) -> {
                        SXSSFCell cell = row.createCell(integer);
                        cell.setCellStyle(cellMap.get(integer));

                        //数据转换
                        if (o != null) {
                            if (o instanceof BigDecimal) {
                                cell.setCellValue(((BigDecimal) o).doubleValue());
                            } else if (o instanceof LocalDateTime) {
                                cell.setCellValue(new XSSFRichTextString(
                                        ((LocalDateTime) o).format(DateTimeFormatter.ISO_DATE)
                                ));
                            } else if (o instanceof Date) {
                                cell.setCellValue((Date) o);
                            } else {
                                cell.setCellValue(new XSSFRichTextString(o.toString()));
                            }
                        }

                    }
            );

        }

        log.info("导出数据量：[{}]", list.size());

    }

    /**
     * 上传文件，请求管理等功能
     */
    public static void stop() {
        ExportObject object = exportObject.get();
        String uploadPath;

        //获取文件并写入 当大批量导出的时候，多个合并为一个
        try (SXSSFWorkbook workbook = object.getWorkbook();
             FileOutputStream out = new FileOutputStream(new File(object.getPath()))) {

            workbook.write(out);
            workbook.close();
            out.close();
            workbook.dispose();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        System.out.println(object.getPath()+"!!!!!!!!!!!!!!!!!!!!!");

        File file = new File(object.getPath());
        //上传到文件服务器 TODO


        //--------------
        FileUtils.deleteQuietly(file);


    }

    /**
     * 将文件写入响应
     */
    public static void writeResponse(HttpServletResponse response, String identify) {
        try {

            ExportObject object = exportObject.get();
            @Cleanup SXSSFWorkbook workbook = object.getWorkbook();

            //写入响应
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + identify + ".xlsx");
            response.setContentType("application/ms-excel");
            @Cleanup ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();

            //关流
            workbook.close();
            outputStream.close();
            workbook.dispose();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
