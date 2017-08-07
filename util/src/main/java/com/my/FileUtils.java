package com.my;

import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class FileUtils
{
	public static byte[] readURLFile(String strUrl) throws Exception
	{
		int byteread=0;
		URL url=new URL(strUrl);
		ByteArrayOutputStream out=new ByteArrayOutputStream(1024);
		InputStream in=null;
		try
		{
			URLConnection conn=url.openConnection();
			in=conn.getInputStream();
			byte[] buffer=new byte[1024];
			while((byteread=in.read(buffer))!=-1)
			{
				out.write(buffer,0,byteread);
			}
			return out.toByteArray();
		}
		finally
		{
			if(out!=null)
			{
				try
				{
					out.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
			if(in!=null)
			{
				try
				{
					in.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public static InputStream readURLFileInput(String strUrl) throws Exception{
		URL url=new URL(strUrl);
		InputStream in=null;
		URLConnection conn=url.openConnection();
		in=conn.getInputStream();
		return in;
	}

	public static byte[] readLocalFile(String strFilePath) throws Exception
	{
		byte[] data=new byte[0];
		File file=new File(strFilePath);
		if(file.exists())
		{
			FileInputStream in=new FileInputStream(file);
			ByteArrayOutputStream out=new ByteArrayOutputStream(1024);
			byte[] cache=new byte[1024];
			int nRead=0;
			while((nRead=in.read(cache))!=-1)
			{
				out.write(cache,0,nRead);
				out.flush();
			}
			out.close();
			in.close();
			data=out.toByteArray();
		}
		return data;
	}
	public static byte[] readLocalFile(File file) throws Exception
	{
		byte[] data=new byte[0];
		if(file.exists())
		{
			FileInputStream in=new FileInputStream(file);
			ByteArrayOutputStream out=new ByteArrayOutputStream(1024);
			byte[] cache=new byte[1024];
			int nRead=0;
			while((nRead=in.read(cache))!=-1)
			{
				out.write(cache,0,nRead);
				out.flush();
			}
			out.close();
			in.close();
			data=out.toByteArray();
		}
		return data;
	}
	public static String upload2FileServer(String urlStr,byte[] fileData,String fileName) throws IOException
	{
		String result="";
		OutputStream out=null;
		BufferedReader reader=null;
		HttpURLConnection conn=null;
		// boundary is the separator between request header and the uploaded file content
		String BOUNDARY="---------------------------";
		try
		{
			URL url=new URL(urlStr);
			conn=(HttpURLConnection)url.openConnection();
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection","Keep-Alive");
			conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type","multipart/form-data; boundary="+BOUNDARY);
			out=new DataOutputStream(conn.getOutputStream());

			String contentType="application/octet-stream";
			if(fileName.endsWith(".csv")||fileName.endsWith(".txt"))
			{

			}
			else if(fileName.endsWith(".png"))
			{
				contentType="image/png";
			}
			else if(fileName.endsWith(".jpg")||fileName.endsWith(".jpeg")||fileName.endsWith(".jpe"))
			{
				contentType="image/jpeg";
			}
			else if(fileName.endsWith(".gif"))
			{
				contentType="image/gif";
			}
			else if(fileName.endsWith(".ico"))
			{
				contentType="image/image/x-icon";
			}

			StringBuffer strBuf=new StringBuffer();
			strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
			strBuf.append("Content-Disposition: form-data; name=\""+fileName+"\"; filename=\""+fileName+"\"\r\n");
			strBuf.append("Content-Type:"+contentType+"\r\n\r\n");

			out.write(strBuf.toString().getBytes());
			out.write(fileData,0,fileData.length);

			byte[] endData=("\r\n--"+BOUNDARY+"--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();
			// read response data
			StringBuffer strRet=new StringBuffer();
			reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line=null;
			while((line=reader.readLine())!=null)
			{
				strRet.append(line).append("\n");
			}
			result=strRet.toString();
			reader.close();
			reader=null;

			JSONObject json=JSONObject.parseObject(result);
			if(json.getString("error")==null)
			{
				if(json.get("url")!=null)
				{
					result=json.getString("url");
				}
				else
				{
					String strFilePath=json.getString("strFilePath");
					String strFileName=json.getString("strFileName");
					result=strFilePath+strFileName;
				}
			}
		}
		finally
		{
			if(out!=null)
			{
				try
				{
					out.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
			if(reader!=null)
			{
				try
				{
					reader.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
			if(conn!=null)
			{
				conn.disconnect();
				conn=null;
			}
		}
		return result;
	}

	//将byte数组写入文件
	public static void createFile(String file, byte[] content) throws IOException {
		File target = new File(file);
		if (!target.getParentFile().exists()) {
			target.getParentFile().mkdirs();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(target);
			fos.write(content);
		}finally {
			if(fos != null)
			{
				try
				{
					fos.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
