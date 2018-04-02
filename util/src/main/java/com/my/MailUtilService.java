package com.my;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Properties;

/**
 * Created by wlb on 2016/8/1.
 */

public class MailUtilService {
    private static Logger logger = LoggerFactory
            .getLogger(MailUtilService.class);

    public void sendSimpleMessage(String name, String toAddress) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        //smtp server
        mailSender.setHost(PropertyUtil.getValue("mail.sender.host"));
        // 邮件消息
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        //  收/发人 用数组发送多个邮件
        // String[] array = new String[] {"sun@163.com","sun@sohu.com"};
        // mailMessage.setTo(array);
        mailMessage.setTo(toAddress);
        mailMessage.setFrom(PropertyUtil.getValue("mail.sender.user"));
        mailMessage.setSubject(name);

        StringBuilder stringBuilder = new StringBuilder("lBorrowIntentId");
        stringBuilder.append("strLoanAcctNo");
        mailMessage.setText(stringBuilder.toString());
        //发送用户/密码
        mailSender.setUsername(PropertyUtil.getValue("mail.sender.user"));
        mailSender.setPassword(PropertyUtil.getValue("mail.sender.password"));

        Properties prop = new Properties();
        // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.timeout", "25000");
        mailSender.setJavaMailProperties(prop);
        // 发送邮件
        mailSender.send(mailMessage);
    }

    public void sendAttachment(String filename,String address) throws Exception
    {
        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
        // smtp server
        senderImpl.setHost((String)PropertyUtil.getValue("mail.sender.host"));
        // 建立邮件消息,发送简单邮件和html邮件的区别
        MimeMessage mailMessage = senderImpl.createMimeMessage();
        // multipart 为true时发送附件
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,
                true, "utf-8");
        //  收/发人
        messageHelper.setTo(address);
        messageHelper.setFrom((String)PropertyUtil.getValue("mail.sender.user"));
        messageHelper.setSubject("测试邮件中上传附件!！");
        // true 使用html格式的邮件
        messageHelper.setText(
                "<html><head></head><body><h1>你好：附件中有资料</h1></body></html>",
                true);

        FileSystemResource file = new FileSystemResource(
                new File(filename));
        //附件
        messageHelper.addAttachment(filename, file);

        senderImpl.setUsername(PropertyUtil.getValue("mail.sender.user"));
        senderImpl.setPassword(PropertyUtil.getValue("mail.sender.password"));
        Properties prop = new Properties();
        // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.timeout", "25000");
        senderImpl.setJavaMailProperties(prop);
        // 发送邮件
        senderImpl.send(mailMessage);

        System.out.println("邮件发送成功..");
    }

    public static void main(String[] args) {
        MailUtilService mailUtilService = new MailUtilService();
        mailUtilService.sendSimpleMessage("隔日通知未成功数据","wanglibin@dafy.com");
    }
}
