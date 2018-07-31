package com.my;

import org.apache.commons.lang3.StringUtils;

import java.io.*;

/**
 * Created by dell on 2018/7/31.
 */
public class FileSplit {


    /**
     * 按行分割文件
     * @param rows 为多少行一个文件
     * @param sourceFilePath 为源文件路径
     * @param targetDirectoryPath 文件分割后存放的目标目录
     */
    public void splitDataToFile(int rows, String sourceFilePath,
                                    String targetDirectoryPath) {
        File sourceFile = new File(sourceFilePath);
        File targetFile = new File(targetDirectoryPath);
        if (!sourceFile.exists() || rows <= 0 || sourceFile.isDirectory()) {
            System.out.println("parameters are not valid");
            return;
        }
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }

        try {
            InputStreamReader in = new InputStreamReader(new FileInputStream(sourceFilePath),"UTF-8");
            BufferedReader br=new BufferedReader(in);

            BufferedWriter bw;
            String str = "";
            String tempData = br.readLine();
            String sourceName = sourceFile.getName().substring(0, sourceFile.getName().indexOf("."));
            String fileName = targetFile.getAbsolutePath() + "/" + sourceName + "_";
            int i = 1, s = 0;
            while (tempData != null) {
                if(i % rows != 0){
                    str += tempData + "\r\n";
                }else if (i % rows == 0) {
                    str += tempData;
                    String tempName = fileName + (s+1) +".txt";
                    System.out.println("generate file " + tempName);
                    bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempName), "UTF-8"),1024);
                    bw.write(str);
                    bw.close();
                    str = "";
                    s += 1;
                }
                i++;
                tempData = br.readLine();
            }
            if (StringUtils.isNotEmpty(str)) {
                str = str.substring(0, str.lastIndexOf("\r\n"));
                String tempName = fileName + (s+1) +".txt";
                System.out.println("generate file " + tempName);
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempName), "UTF-8"),1024);
                bw.write(str);
                bw.close();
                br.close();
            }
            in.close();
        } catch (Exception e) {

        }
    }

    public static void main(String[] args) {
        FileSplit fs = new FileSplit();
        fs.splitDataToFile(2000, "D:\\ftptest\\contract.txt", "D:\\ftptest\\split");
    }
}
