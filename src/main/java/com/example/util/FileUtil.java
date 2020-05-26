package com.example.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

@Slf4j
public class FileUtil {

    /**
     * MultipartFile 转 File
     *
     * @param multipartFile 上传文件
     * @return 转换文件
     */
    public static File multipartFileToFile(@RequestParam MultipartFile multipartFile) {

        try {
            File file = null;
            if (multipartFile == null || multipartFile.getSize() <= 0) {
                throw new RuntimeException("转换文件异常");
            } else {
                InputStream ins = null;
                ins = multipartFile.getInputStream();
                file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
                inputStreamToFile(ins, file);
                ins.close();
                return file;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }


    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
