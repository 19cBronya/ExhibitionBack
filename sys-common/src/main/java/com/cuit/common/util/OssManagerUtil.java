package com.cuit.common.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * 阿里Oss对象存储工具类
 */
public class OssManagerUtil {

    private static String endpoint = "oss-cn-beijing.aliyuncs.com";
     private static String accessKeyId = "";
     private static String accessKeySecret = "";
    private static String bucket = "xympic";


    private static OSS client;


    static {
        client = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }


    /**
     * 上传图片
     *
     * @param fileName 图片名称，图片名称包括文件夹名称和“/”
     * @param length   图片大小
     * @param content  输入流
     */
    public static String uploadImage(String fileName, long length, InputStream content) {
        uploadBucketImage(bucket, fileName, length, content);
        return "https://" + bucket + "." + endpoint + "/img/" + fileName;
    }


    /**
     * 上传文件
     *
     * @param bucket   存储空间名
     * @param fileName 文件名(包括文件夹名称和“/”)
     * @param length   流的长度
     * @param content  输入流
     */
    public static void uploadBucketImage(String bucket, String fileName, long length, InputStream content) {
        // 创建上传Object的Metadata
        ObjectMetadata meta = new ObjectMetadata();
        // 必须设置ContentLength
        meta.setContentLength(length);
        // 上传Object.
        client.putObject(bucket, fileName, content, meta);
    }


    /**
     * 删除文件
     *
     * @param fileName 文件名称，图片名称包括文件夹名称和“/”
     */
    public static boolean delShopImage(String fileName) {
        //判断文件是否存在
        boolean exist = client.doesObjectExist(bucket, fileName);
        //文件不存在删除失败
        if (!exist) {
            return false;
        }
        //执行删除
        client.deleteObject(bucket, fileName);
        return true;
    }


    /**
     * 获得上传文件后url链接
     *
     * @param fileName 文件名(包括文件夹名称和“/”)
     * @return
     */
    public static String getUrl(String fileName) {
        // 设置URL过期时间为10年  3600l* 1000*24*365*10
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = client.generatePresignedUrl(bucket, fileName, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }

    /**
     * 创建存储空间
     *
     * @param bucketName 新建存储空间默认为标准存储类型，私有权限。
     * @return
     */
    public static void crateBucket(String bucketName) {
        // 新建存储空间默认为标准存储类型，私有权限。
        client.createBucket(bucketName);
    }
}
