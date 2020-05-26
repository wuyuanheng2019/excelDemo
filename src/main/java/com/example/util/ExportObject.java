package com.example.util;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 导出中转对象
 */
@Data
@Slf4j
@Accessors(chain = true)
public class ExportObject {

    /**
     * 导出excel对象
     */
    SXSSFWorkbook workbook;

    /**
     * 样式map
     */
    HashMap<Integer, CellStyle> cellMap;

    /**
     * 序号
     */
    AtomicInteger no;

    /**
     * 缓存路径
     */
    String path;

    /**
     * 样式头部
     */
    HashMap<Integer, CellStyle> cellStyleHashMap;

    /**
     * 头部文字
     */
    ArrayList<String> cellHead;


    public static ExportObject saveExportObject(String path, HashMap<Integer, CellStyle> cellMap,
                                                HashMap<Integer, CellStyle> cellStyleHashMap, ArrayList<String> cellHead) {

        try {

            //获取输入流
            FileInputStream fileInputStream = new FileInputStream(new File(path));
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            SXSSFWorkbook sheets = new SXSSFWorkbook(workbook);

            return new ExportObject().setWorkbook(sheets)
                    .setCellMap(cellMap)
                    .setPath(path)
                    .setCellStyleHashMap(cellStyleHashMap)
                    .setNo(new AtomicInteger())
                    .setCellHead(cellHead);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("保存本地 threadLocal 失败！");
        }
    }
}
