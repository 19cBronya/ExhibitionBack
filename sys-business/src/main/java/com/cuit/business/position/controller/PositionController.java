package com.cuit.business.position.controller;

import com.cuit.business.position.domain.dto.PositionDTO;
import com.cuit.business.position.service.ICityService;
import com.cuit.business.position.service.ICityProperService;
import com.cuit.business.position.service.IProvinceService;
import com.cuit.common.common.R;
import com.cuit.system.log.anno.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(tags = "省城区信息控制类")
@RestController
@RequestMapping("/position")
public class PositionController {

    @Autowired
    private IProvinceService provinceService;

    @Autowired
    private ICityService cityService;

    @Autowired
    private ICityProperService cityProperService;

    /**
     * 查询省份信息
     */
    @Log
    @RequiresAuthentication
    @ApiOperation(value = "查询省份信息")
    @PostMapping("/province")
    public R selectProcinceList()
    {
        List<PositionDTO> list = provinceService.selectProcinceList();
        return R.success(list);
    }

    /**
     * 查询省对应市信息
     */
    @Log
    @RequiresAuthentication
    @ApiOperation(value = "查询省对应市信息")
    @PostMapping("/city")
    public R selectCityList(@RequestParam("id") Integer id)
    {
        List<PositionDTO> list = cityService.selectCityList(id);
        return R.success(list);
    }

    /**
     * 查询市对应区县信息
     */
    @Log
    @RequiresAuthentication
    @ApiOperation(value = "查询市对应区信息")
    @PostMapping("/cityProper")
    public R selectCityProperList(@RequestParam("id") Integer id)
    {
        List<PositionDTO> list = cityProperService.selectCityProperList(id);
        return R.success(list);
    }
}
