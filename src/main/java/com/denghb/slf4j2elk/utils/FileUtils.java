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
            SimpleDateFormat format = new SimpleDateFormat("yyyyMM/ddHH");
            String fileName = format.format(new Date());
            filePath = filePath + "/" + fileName + ".log";

            checkExist(filePath);

            // 追加字符串
            FileWriter writer = new FileWriter(filePath, true);
            writer.write("\n\t" + log);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查文件是否存在
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static void checkExist(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        if (!file.exists())
            file.createNewFile();
    }
}
