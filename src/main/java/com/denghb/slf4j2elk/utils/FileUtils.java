package com.denghb.slf4j2elk.utils;

import com.denghb.slf4j2elk.domain.LoggerObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by denghb on 2017/3/28.
 */
public class FileUtils {

    /**
     * 一个小时写一个文件
     *
     * @param filePath
     * @param log
     */
    public static void write(String filePath, String log) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHH");
            String fileName = format.format(new Date());

            filePath = filePath + "/" + fileName + ".log";
            checkExist(filePath);

            // 追加字符串
            FileWriter writer = new FileWriter(filePath, true);
            writer.write("\n\t" + log);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File checkExist(String filePath) throws Exception {
        System.out.println(filePath);

        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("文件夹不存在！");
            File file2 = new File(file.getParent());
            file2.mkdirs();
            System.out.println("创建文件夹成功！");
            if (file.isDirectory()) {
                System.out.println("文件存在！");
            } else {
                file.createNewFile();//创建文件
                System.out.println("文件不存在，创建文件成功！");
            }
        }
        return file;
    }
}
