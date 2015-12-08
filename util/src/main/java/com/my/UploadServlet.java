package com.my;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wlb on 2015/12/8.
 */
@SuppressWarnings("restriction")
public class UploadServlet extends HttpServlet {

    private static final long serialVersionUID = 6250247322092847556L;
    protected static SimpleDateFormat _format = new SimpleDateFormat("yyyyMMdd");
    private static final String ENCODING = "utf-8";
    private static String BASEDIR = "";
    public static final String UPLOAD_BASE_DIR = "upload";
    public static final String DOWNLOAD_BASE_DIR = "download";
    public static final String UPLOAD_JT_DIR = "jt";
    public static final String UPLOAD_DIR = "repository";
    public static final String TEMP_DIR = "temp";

    public static final String DOC_TYPE = "1";
    public static final String IMAGE_TYPE = "2";


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<String, Object> data = new HashMap<String, Object>();

        String title = "";
        String originalName = "";
        String saveFileName = "";
        String state = "SUCCESS";

        long size = 0;
        String suffix = "";

        UploadType uploadType = UploadType.IMAGE;

        req.setCharacterEncoding(ENCODING);

        if (!ServletFileUpload.isMultipartContent(req)){
            state = "数据类型不正确！";
        } else {
            File repositoryDir = new File(getBaseDir() + UPLOAD_BASE_DIR + File.separator + TEMP_DIR);
            if (!repositoryDir.exists())
                repositoryDir.mkdirs();
            // create factory and file cleanup tracker
            FileCleaningTracker tracker = FileCleanerCleanup.getFileCleaningTracker(getServletContext());
            DiskFileItemFactory factory = new DiskFileItemFactory(
                    DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repositoryDir);

            factory.setFileCleaningTracker(tracker);
            // save upload file to disk
            ServletFileUpload upload = new ServletFileUpload(factory);
            try {
                @SuppressWarnings("unchecked")
                List<FileItem> items = upload.parseRequest(req);
                long time = System.currentTimeMillis();
                File tmpDir = new File("");
                if (!tmpDir.exists())
                    tmpDir.mkdirs();
                Map<String,String> map = new HashMap<String, String>();
                for (FileItem item : items){
                    if (!item.isFormField()){
                        // 确定是文件而不是一个普通的表单字段
                        originalName = item.getName();
                        Pattern reg = uploadType.getPattern();
                        Matcher matcher= reg.matcher(originalName);
                        if(!matcher.find()) {
                            state = "文件类型不允许！";
                            break;
                        }
                        title = originalName.substring(0, originalName.indexOf(matcher.group())-1);
                        saveFileName = UUID.randomUUID() + matcher.group();
                        suffix = matcher.group().substring(matcher.group().indexOf(".") + 1);
                        size = item.getSize();
                        File saveFile = new File(tmpDir, saveFileName);
                        item.write(saveFile);
                    }else if(item.isFormField()){
                        //form text field
                        // 中文乱码
                        if(StringUtils.isBlank(item.getString(ENCODING))){
                            continue;
                        }
                        BASE64Decoder decoder = new BASE64Decoder();
                        byte[] decoderBytes = decoder.decodeBuffer(item.getString(ENCODING));
                        String str2 = new String(decoderBytes,ENCODING);
                        map.put(item.getFieldName(), str2);
//						//form text field
//						String jsonStr = item.getString("utf-8");
//						// 中文乱码
//						BASE64Decoder decoder = new BASE64Decoder();
//						byte[] decoderBytes = decoder.decodeBuffer(jsonStr);
//						String str2 = new String(decoderBytes,"UTF-8");
//						BusinessOfficeRequest request = new BusinessOfficeRequest();
//						assemble(request, str2);
//						logger.info("jsonStr11111:" + str2);
//						logger.info("jsonStr:" + request.toJsonString());
//
//						String fileName = tmpDir + "/detail.txt";
//				    	File file = new File(fileName);
//						file.createNewFile();
//
//						FileOutputStream out = new FileOutputStream(file, false);
//						out.write(request.toJsonString().getBytes("utf-8"));
//						out.close();
                    }
                }
                String fileName = tmpDir + "/detail.txt";
                File file = new File(fileName);
                file.createNewFile();
                FileOutputStream out = new FileOutputStream(file, false);
                out.write("".getBytes(ENCODING));
                out.close();
                FileUtils.deleteDirectory(tmpDir);
            } catch (Exception e) {
                state = "IO错误！请稍后重试";
            }
        }

        if(!"SUCCESS".equals(state)){
        }
        Gson gson = new GsonBuilder().serializeNulls().create();
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.print(gson.toJson(null));
        out.flush();
        out.close();
    }

    /**
     * @return
     */
    public String getBaseDir(){

        String basePath = this.getServletContext().getRealPath("/");
        if(basePath!=null){
            BASEDIR = basePath;
        }
        return BASEDIR;
    }

    public static String getBASEDIR() {
        return BASEDIR;
    }

    public static void setBASEDIR(String bASEDIR) {
        BASEDIR = bASEDIR;
    }

}

enum UploadType {
    ATTACHMENT(1, Pattern.compile("[\\.](.*)$")),
    IMAGE(2, Pattern.compile("[\\.](jpg|png|jpeg|gif)$"));

    private int id;
    private Pattern pattern;

    UploadType (int id, Pattern pattern) {
        this.id = id;
        this.pattern = pattern;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
}