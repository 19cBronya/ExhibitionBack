package com.cuit.common.common;

import cn.hutool.core.io.IoUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class HutoolQRCodeGenerator {
    public static String generateQRCodeWithHutool(String content, String filePath) {
        // 使用Hutool生成二维码
        BufferedImage qrCodeImage = QrCodeUtil.generate(content, 400, 400);

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(qrCodeImage, "PNG", os);
            byte[] qrCodeBytes = os.toByteArray();

            // 将字节数组保存为文件
            IoUtil.write(new FileOutputStream(filePath), false, qrCodeBytes);
            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }

    public static void main(String[] args) {
        generateQRCodeWithHutool("待开发中", "E:\\Exhibition\\sys-common\\src\\main\\resources\\file\\22.png");
    }
}
