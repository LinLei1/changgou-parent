package com.changgou.goods.feign;

import com.changgou.goods.pojo.Sku;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "goods")  //调用的微服务名称
@RequestMapping("/sku")
public interface SkuFeign {

    /**
     * 商品库存递减
     */
    @GetMapping(value = "/decr/count")
    Result decrCount(@RequestParam Map<String,String> decrMap);

    /**
     * 查询Sku全部数据,数据量如果太大,建议分页查询
     * @return
     */
    @GetMapping
    Result<List<Sku>> findAll();

    /***
     * 根据ID查询Sku数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<Sku> findById(@PathVariable Long id);
}
