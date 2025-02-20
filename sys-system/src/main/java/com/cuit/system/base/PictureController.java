package com.cuit.system.base;

import com.cuit.common.common.CommonConstants;
import com.cuit.common.common.R;
import com.cuit.common.util.OssManagerUtil;
import com.cuit.system.log.anno.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Api(tags = "图片上传控制类")
@RestController
@RequestMapping("/picture")
public class PictureController extends BaseController {

    @Value("${Image.path}")
    private String basePath;


    /**
     * 图片上传OSS
     *
     * @param file
     * @return
     * @throws IOException
     */
    @Log
    @RequiresAuthentication
    @ApiOperation(value = "图片上传到阿里oss")
    @PostMapping("/upload")
    public R uploadFiletoOss(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        if(fileName == null)
            return R.error(CommonConstants.ConstantsCode.错误.getValue(), "文件上传失败");
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        fileName = UUID.randomUUID().toString().replace("-", "") + "." + suffix;
        String url = OssManagerUtil.uploadImage(fileName, file.getInputStream().available(), file.getInputStream());
        String returnUrl = url.replace("https://xympic.oss-cn-beijing.aliyuncs.com/img/","");
        return R.success(returnUrl);
    }
}
