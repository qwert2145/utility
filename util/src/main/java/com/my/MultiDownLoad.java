package com.my;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MultiDownLoad {

    private ThreadPoolExecutor executor;
    private int corePoolSize;
    private int maxPoolSize;

    public MultiDownLoad(int corSize,int maxSize) {
        this.corePoolSize = corSize;
        this.maxPoolSize = maxSize;
        executor = new ThreadPoolExecutor(corePoolSize,maxPoolSize,60, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());
    }

    void start(){
        System.out.println("start-----");
        for(int i= 1;i<=corePoolSize;i++){
            executor.execute(new TaskProcessor(i));
        }
    }

    void stop() throws InterruptedException {
        executor.shutdown();//只是不能再提交新任务，等待执行的任务不受影响
        try {
            boolean loop = true;
            int i =0 ;
            do {
                //等待所有任务完成
                loop = !executor.awaitTermination(2, TimeUnit.MINUTES);  //阻塞，直到线程池里所有任务结束
                i++;
                System.out.println(i + "times");
            } while(loop);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("stop-----");
    }

    private class TaskProcessor implements Runnable{
        int fileNum;
        TaskProcessor(int fileNum){
            this.fileNum = fileNum;
        }
        public void run() {
            String file = "D:\\ftptest\\contract" +fileNum + ".txt";
            System.out.println("contract file:---" + file);
//            readFileByLines(file,"contract");
        }
    }

    public boolean downloadFile(String strUrlPath, String strLocalPath, String strFolderPath) {
        boolean result = true;
        GetMethod httpGet = new GetMethod(strUrlPath);
        if (!strFolderPath.equals("")) {
            File Folder = new File(strFolderPath);
            if (!Folder.exists()) {
                Folder.mkdirs();
            }
        }

        InputStream in = null;
        FileOutputStream out = null;
        try {
            HttpClient httpClient = new HttpClient();
            httpClient.executeMethod(httpGet);
            in = httpGet.getResponseBodyAsStream();
            out = new FileOutputStream(new File(strLocalPath));
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception e) {
//            log.error("httpClient下载文件失败", e);
            result = false;
        } finally {
            httpGet.releaseConnection();
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e1) {
//                log.error("文件流信息关闭失败", e1);
                result = false;
            }

        }
        return result;
    }

    public void readFileByLines(String fileName,String type) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                String temp[] = tempString.split(",");
                String intentId = temp[0];
                String urls[] = temp[1].split("\\|");
                String directory = "D:\\ftptest\\" +type + "\\" + intentId;
                for(int i = 0;i<urls.length;i++){
                    String name = directory + "\\"+ i +".pdf";
                    if(new File(name).exists()){
                        continue;
                    }
                    System.out.println("thread:---" + Thread.currentThread().getName()+",file:---" + name);

                    downloadFile(urls[i],name,directory);
                }
            }
            reader.close();
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

    }

    public static void main(String[] args) throws InterruptedException {
        MultiDownLoad multiDownLoad = new MultiDownLoad(4,4);
        multiDownLoad.start();
        multiDownLoad.stop();
    }
}
