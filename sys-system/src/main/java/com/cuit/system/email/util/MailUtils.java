package com.cuit.system.email.util;

import com.cuit.system.email.domain.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Component
public class MailUtils {

    private static String from;

    private static String nickname;

    private static MailUtils utils;

    @Value("${spring.mail.username}")
    public void setFrom(String from){
        MailUtils.from = from;
    }

    @Value("${spring.mail.nickname}")
    public void setNickname(String nickname){
        MailUtils.nickname = nickname;
    }

    @Resource
    private JavaMailSenderImpl mailSender;

    @PostConstruct
    public void init() {
        utils = this;
    }



    /**
     * @author xym
     * @since 2023-11-15 10:54
     * @description //发送信息工具类
     */
    public static void sendMail(Email email) throws MessagingException {

        SimpleMailMessage mimeMessage = new SimpleMailMessage();

        //正文
        mimeMessage.setSubject(email.getSubject());
        mimeMessage.setText(email.getContent());


        mimeMessage.setTo(email.getTo());
        mimeMessage.setFrom(nickname+'<'+from+'>');

        utils.mailSender.send(mimeMessage);
    }

    /**
     * 发送信息带图片附件
     */
    public static void sendMailByFile(Email email, String filePath) throws MessagingException {
        MimeMessage mimeMessage = utils.mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true); // `true` 表示支持多媒体类型

        // 设置邮件主题和内容
        helper.setSubject(email.getSubject());

        // 使用HTML格式设置邮件正文并引用图片
        String contentWithImg = "<html><body><img src=\"cid:image\">" + email.getContent() + "</body></html>";
        helper.setText(contentWithImg, true); // 第二个参数为true表示文本是HTML格式

        // 设置收件人和发件人
        helper.setTo(email.getTo());
        helper.setFrom(nickname + '<' + from + '>');

        // 添加图片附件并指定ID以便在HTML中引用
        File imageFile = new File(filePath);
        helper.addInline("image", imageFile); // "image" 是图片的引用ID，应与HTML中的`cid:image`匹配

        // 发送邮件
        utils.mailSender.send(mimeMessage);
    }

    public static void main(String[] args) {
        Email email = new Email();
    }

}
