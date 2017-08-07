package com.my;

/**
 * Created by dell on 2017/4/26.
 */

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.*;
import java.util.Vector;

public class SftpUtil {

    public static void main(String[] args) throws Exception {
        SftpUtil.sshSftp("127.0.0.1", "Administrator", "cyg_server", 22);
        SftpUtil.sshSftp2("127.0.0.1", "Administrator", 22, "C:/Users/Administrator/rsa_my", "");
    }

    /**
     * 利用JSch包实现SFTP下载、上传文件(用户名密码方式登陆)
     * @param ip 主机IP
     * @param user 主机登陆用户名
     * @param psw  主机登陆密码
     * @param port 主机ssh2登陆端口，如果取默认值(默认值22)，传-1
     *
     */
    public static void sshSftp(String ip, String user, String psw
            ,int port) throws Exception{
        System.out.println("开始用户名密码方式登陆");
        Session session = null;

        JSch jsch = new JSch();

        if(port <=0){
            //连接服务器，采用默认端口
            session = jsch.getSession(user, ip);
        }else{
            //采用指定的端口连接服务器
            session = jsch.getSession(user, ip ,port);
        }

        //如果服务器连接不上，则抛出异常
        if (session == null) {
            throw new Exception("session is null");
        }

        //设置登陆主机的密码
        session.setPassword(psw);//设置密码
        //设置第一次登陆的时候提示，可选值：(ask | yes | no)
        session.setConfig("StrictHostKeyChecking", "no");
        //设置登陆超时时间
        session.connect(30000);

        sftp(session, "aa.log");
        System.out.println("sftp成功");
    }

    /**
     * 利用JSch包实现SFTP下载、上传文件(秘钥方式登陆)
     * @param ip 主机IP
     * @param user 主机登陆用户名
     * @param port 主机ssh2登陆端口，如果取默认值(默认值22)，传-1
     * @param privateKey 密钥文件路径
     * @param passphrase 密钥的密码
     *
     */
    public static void sshSftp2(String ip, String user
            ,int port ,String privateKey ,String passphrase) throws Exception{
        System.out.println("开始秘钥方式登陆");
        Session session = null;

        JSch jsch = new JSch();

        //设置密钥和密码
        //支持密钥的方式登陆，只需在jsch.getSession之前设置一下密钥的相关信息就可以了
        if (privateKey != null && !"".equals(privateKey)) {
            if (passphrase != null && !"".equals(passphrase)) {
                //设置带口令的密钥
                jsch.addIdentity(privateKey, passphrase);
            } else {
                //设置不带口令的密钥
                jsch.addIdentity(privateKey);
            }
        }


        if(port <=0){
            //连接服务器，采用默认端口
            session = jsch.getSession(user, ip);
        }else{
            //采用指定的端口连接服务器
            session = jsch.getSession(user, ip ,port);
        }

        //如果服务器连接不上，则抛出异常
        if (session == null) {
            throw new Exception("session is null");
        }

        //设置第一次登陆的时候提示，可选值：(ask | yes | no)
        session.setConfig("StrictHostKeyChecking", "no");
        //设置登陆超时时间
        session.connect(30000);

        sftp(session, "bb.log");
        System.out.println("sftp成功");
    }

    private static void sftp(Session session, String uploadFileName) throws Exception {
        Channel channel = null;
        try {
            //创建sftp通信通道
            channel = (Channel) session.openChannel("sftp");
            channel.connect(1000);
            ChannelSftp sftp = (ChannelSftp) channel;


            //进入服务器指定的文件夹
            sftp.cd("testsftp");

            //列出服务器指定的文件列表
            Vector v = sftp.ls("*.txt");
            for(int i=0;i<v.size();i++){
                System.out.println(v.get(i));
            }

            //以下代码实现从本地上传一个文件到服务器，如果要实现下载，对换以下流就可以了
            OutputStream outstream = sftp.put(uploadFileName);
            OutputStreamWriter writer = new OutputStreamWriter(outstream, "utf-8");
            writer.write("借款人dfd");
            writer.write("\r\n");
            writer.close();
            InputStream instream = new FileInputStream(new File("C:/ftptest/aa.txt"));

//            byte b[] = new byte[1024];
//            int n;
//            while ((n = instream.read(b)) != -1) {
//                outstream.write(b, 0, n);
//            }
            outstream.flush();
            outstream.close();
            instream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.disconnect();
            channel.disconnect();
        }
    }
}
