package com.my;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dell on 2017/4/26.
 */
public class FileWriteRead {

    public static Map readFileByLines(String fileName) {
        Map<String, String> map = new HashMap<String,String>();
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                String temp[] = tempString.split(",");
                map.put(temp[0],temp[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return map;
    }

    void readFile() throws IOException {
        File f = new File("d:/ftptest/a.txt");
        FileInputStream fip = new FileInputStream(f);
        // 构建FileInputStream对象
        InputStreamReader reader = new InputStreamReader(fip, "UTF-8");
        // 构建InputStreamReader对象,编码与写入相同
        StringBuffer sb = new StringBuffer();
        while (reader.ready()) {
            sb.append((char) reader.read());
            // 转成char加到StringBuffer对象中
        }
        System.out.println(sb.toString());
        // 关闭读取流
        reader.close();
        // 关闭输入流,释放系统资源
        fip.close();
    }

    void writeFile() throws IOException {
        File f = new File("d:/ftptest/a.csv");
        FileOutputStream fop = new FileOutputStream(f);
        // 构建FileOutputStream对象,文件不存在会自动新建
        OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");
        // 写入到缓冲区
        writer.append("中文输入dfdfd");
        //换行
        writer.append("\r\n");
        writer.append("English");
        //关闭写入流,同时会把缓冲区内容写入文件
        writer.close();
        // 关闭输出流,释放系统资源
        fop.close();
    }


    public static void main(String[] args) throws IOException {

    }
}
