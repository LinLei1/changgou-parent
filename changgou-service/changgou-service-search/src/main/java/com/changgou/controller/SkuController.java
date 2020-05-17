package com.changgou.controller;

import com.changgou.service.SkuService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/search")
@CrossOrigin
public class SkuController {
    @Autowired
    SkuService skuService;

    /**
     * 调用搜索实现
     * @return
     */
    @GetMapping
    public Map search(@RequestParam(required = false) Map<String,String> searchMap){
        return skuService.search(searchMap);
    }

    /**
     * 数据导入
     * @return
     */
    @GetMapping("/import")
    public Result importData(){
        skuService.importData();
        return new Result(true, StatusCode.OK,"导入数据成功!");
    }
}
