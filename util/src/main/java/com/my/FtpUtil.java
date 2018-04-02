package com.my;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;

/**
 */
public class FtpUtil {

	private static Logger log= Logger.getLogger(FtpUtil.class);

	/**
	 * 获取FTPClient对象
	 * @param ftpHost FTP主机服务器
	 * @param ftpPassword FTP 登录密码
	 * @param ftpUserName FTP登录用户名
	 * @param ftpPort FTP端口 默认为21
	 * @param ftpHost FTP服务器hostname
	 * @param ftpPort FTP服务器端口
	 * @param ftpUserName FTP登录账号
	 * @param ftpPassword FTP登录密码
	 * @return
	 */
	public static FTPClient getFTPClient(String ftpHost, String ftpPassword,String ftpUserName, int ftpPort){
		FTPClient ftpClient = null;
		try{
			ftpClient = new FTPClient();
			ftpClient.connect(ftpHost, ftpPort);// 连接FTP服务器
			ftpClient.login(ftpUserName, ftpPassword);// 登陆FTP服务器
			if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				log.info("未连接到FTP，用户名或密码错误。");
				ftpClient.disconnect();
			} else {
				log.info("FTP连接成功。");
			}
		} catch (SocketException e) {
			log.info("FTP的IP地址可能错误，请正确配置。",e);
			throw new RuntimeException("FTP的IP地址可能错误，请正确配置。");

		} catch (IOException e) {
			log.info("FTP的端口错误,请正确配置",e);
			throw new RuntimeException("FTP的端口错误,请正确配置");
		}
		return ftpClient;
	}

	/** 
	 * Description: 向FTP服务器上传文件
	 * @param basePath FTP服务器基础目录
	 * @param filename 上传到FTP服务器上的文件名 
	 * @param input 输入流 
	 * @return 成功返回true，否则返回false 
	 */
	public static boolean uploadFile( FTPClient ftp,String basePath, String filename, InputStream input) throws Exception {
		boolean result = false;
		try {
			//切换到上传目录
			if (!ftp.changeWorkingDirectory(basePath)) {
				boolean flag = createMultiDirectory(ftp,basePath);
				if(!flag){
					return false;
				}
			}
			//设置上传文件的类型为二进制类型
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			//上传文件
			if (!ftp.storeFile(filename, input)) {
				return result;
			}
			result = true;
		} catch (IOException e) {
			log.info("===影像资料上传ftp服务器异常==="+e);
			result = false;
		} finally {
			if(input != null){
				input.close();
			}
		}
		return result;
	}


	private static boolean createMultiDirectory(FTPClient ftpClient, String multiDirectory) {
		try {
			String[] dirs = multiDirectory.split("/");
			ftpClient.changeWorkingDirectory("/");
			//按顺序检查目录是否存在，不存在则创建目录
			for(int i=1; dirs!=null&&i<dirs.length; i++) {
				dirs[i]= new String(dirs[i].getBytes("UTF-8"),"iso-8859-1");
				if(!ftpClient.changeWorkingDirectory(dirs[i])) {//切换目录
					if(ftpClient.makeDirectory(dirs[i])) {
						if(!ftpClient.changeWorkingDirectory(dirs[i])) {
							return false;
						}
					}else {
						return false;
					}
				}
			}
		} catch (IOException e) {
			log.error("创建目录失败异常",e);
			return false;
		}
		return true;
	}




	/** 
	 * Description: 从FTP服务器下载文件 
	 * @param host FTP服务器hostname 
	 * @param port FTP服务器端口 
	 * @param username FTP登录账号 
	 * @param password FTP登录密码 
	 * @param remotePath FTP服务器上的相对路径 
	 * @param fileName 要下载的文件名 
	 * @param localPath 下载后保存到本地的路径 
	 * @return 
	 */  
	public static boolean downloadFile(String host, int port, String username, String password, String remotePath,
			String fileName, String localPath) {
		boolean result = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(host, port);
			// 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return result;
			}
			ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName)) {
					File localFile = new File(localPath + "/" + ff.getName());

					OutputStream is = new FileOutputStream(localFile);
					ftp.retrieveFile(ff.getName(), is);
					is.close();
				}
			}

			ftp.logout();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return result;
	}


	public static InputStream readURLFileInput(String strUrl) throws Exception{
		URL url=new URL(strUrl);
		InputStream in=null;
		URLConnection conn=url.openConnection();
		in=conn.getInputStream();
		return in;
	}
}
