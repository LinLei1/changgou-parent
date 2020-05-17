package com.changgou.goods.feign;

import com.changgou.goods.pojo.Spu;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(value = "goods")  //调用的微服务名称
@RequestMapping("/spu")
public interface SpuFeign {

    /**
     * 查询Sku全部数据,数据量如果太大,建议分页查询
     * @return
     */
    @GetMapping
    Result<List<Spu>> findAll();

    /***
     * 根据ID查询Spu数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<Spu> findById(Long id);
}
