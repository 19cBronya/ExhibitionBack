package com.cuit.business.product.controller;

import com.cuit.business.product.domain.Product;
import com.cuit.business.product.domain.dto.ProductDTO;
import com.cuit.business.product.service.IProductService;
import com.cuit.common.common.CommonConstants;
import com.cuit.common.common.R;
import com.cuit.common.common.page.TableDataInfo;
import com.cuit.system.base.BaseController;
import com.cuit.system.log.anno.Log;
import com.cuit.system.sysUser.domain.SysUser;
import com.cuit.system.sysUserRoles.domain.SysUserRoles;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Api(tags = "展会商品信息控制类")
@RestController
@RequestMapping("/product")
public class ProductController extends BaseController {

    @Autowired
    private IProductService productService;

    /*图片路径补充*/
    @Value("${Image.path}")
    private String basePath;

    /**
     * 根据参展商id,分页查询商品信息
     */
    @Log
    @ApiOperation("商品信息分页查询")
    @RequiresAuthentication
    @PostMapping("/page")
    public R<TableDataInfo> page(@RequestBody ProductDTO productDTO) {
        SysUserRoles sysUserRoles = getSysUserRoles();
        if(CommonConstants.UserRole.普通观众.getValue().equals(sysUserRoles.getRid().toString())
                || CommonConstants.UserRole.专业观众.getValue().equals(sysUserRoles.getRid().toString())
        ){
            productDTO.setStatus(CommonConstants.Status.启用.getValue());
        }
        startPage(productDTO);
        List<ProductDTO> list = productService.selectByPage(sysUserRoles.getUid(),productDTO);
        if(list.size()>0){
            for (ProductDTO dto : list) {
                String picUrl = dto.getProductUrl();
                if (picUrl !=null && !picUrl.contains(basePath)) {
                    picUrl = basePath + picUrl;
                }
                dto.setProductUrl(picUrl);
            }
        }
        return R.success(getDataTable(list));
    }

    /**
     * 查询参展商下商品介绍
     */
    @Log
    @ApiOperation("查询参展商下商品介绍")
    @RequiresAuthentication
    @GetMapping("/infor")
    public R<TableDataInfo> getInfor(@RequestParam("id") String id,Integer pageNum,Integer pageSize) {
        startPage(pageNum,pageSize);
        List<ProductDTO> list = productService.selectByPageByEid(Long.valueOf(id));
        if(list.size()>0){
            for (ProductDTO dto : list) {
                String picUrl = dto.getProductUrl();
                if (picUrl !=null && !picUrl.contains(basePath)) {
                    picUrl = basePath + picUrl;
                }
                dto.setProductUrl(picUrl);
            }
        }
        return R.success(getDataTable(list));
    }

    /**
     * 新增商品信息
     */
    @Log
    @ApiOperation("新增商品信息")
    @RequiresAuthentication
    @PostMapping("/add")
    public R add(@RequestBody Product product) {
        SysUserRoles sysUserRoles = getSysUserRoles();
        if(CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())||
                CommonConstants.UserRole.参展商.getValue().equals(sysUserRoles.getRid().toString())){
            product.setCreateTime(LocalDateTime.now());
            product.setCreateId(product.getEtorid());
            product.setUpdateId(product.getEtorid());
            productService.save(product);
            return R.success("新增成功");
        }else
            return R.error("权限不足，请联系管理员");
    }

    /**
     * 修改商品信息
     */
    @Log
    @ApiOperation("修改商品信息")
    @RequiresAuthentication
    @PostMapping("/update")
    public R update(@RequestBody Product product) {
        SysUserRoles sysUserRoles = getSysUserRoles();
        if(CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())||
                CommonConstants.UserRole.参展商.getValue().equals(sysUserRoles.getRid().toString())){
            product.setUpdateId(product.getEtorid());
            productService.updateById(product);
            return R.success("修改成功");
        }else
            return R.error("权限不足，请联系管理员");
    }


    /**
     * 删除商品信息
     */
    @Log
    @ApiOperation("删除商品信息")
    @RequiresAuthentication
    @DeleteMapping
    public R userDeleteByIds(@RequestParam("id") String[] id) {
        SysUserRoles sysUserRoles = getSysUserRoles();
        if(CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())||
                CommonConstants.UserRole.参展商.getValue().equals(sysUserRoles.getRid().toString())){
            Long[] longIds = Arrays.stream(id)
                    .mapToLong(Long::parseLong)
                    .boxed()
                    .toArray(Long[]::new);
            return productService.deleteById(longIds);
        }else
            return R.error("权限不足，请联系管理员");
    }

    /**
     * 禁用/启用商品信息
     */
    @Log
    @ApiOperation("禁用/启用商品信息")
    @RequiresAuthentication
    @PostMapping("/changeStatus/{status}")
    public R forbidden(@PathVariable String status, @RequestParam("id") String[] id) {
        SysUserRoles sysUserRoles = getSysUserRoles();
        if(CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())||
                CommonConstants.UserRole.参展商.getValue().equals(sysUserRoles.getRid().toString())){
            Long[] longIds = Arrays.stream(id)
                    .mapToLong(Long::parseLong)
                    .boxed()
                    .toArray(Long[]::new);
            return productService.forbiddenStatus(status, longIds);
        }else
            return R.error("权限不足，请联系管理员");
    }

}
