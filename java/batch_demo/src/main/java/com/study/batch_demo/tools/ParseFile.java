package com.study.batch_demo.tools;

import com.alibaba.fastjson.JSON;
import com.study.batch_demo.param.InputTemplate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author zhudan <zhudan@ebchinatech.com>
 * @since 2021/1/26 15:00
 */
public class ParseFile {
    public static String readJsonFile(String filePath){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            StringBuilder line = new StringBuilder();
            int ch = 0;
            while ((ch = reader.read()) != -1) {
                line.append((char) ch);
            }
            reader.close();
            return line.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
    public static <T> T parseJson(String jsonPath, Class<T> clazz){
        T configFile = JSON.parseObject(readJsonFile(jsonPath), clazz);
        return configFile;
    }


}
