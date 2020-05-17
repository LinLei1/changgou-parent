package com.changgou.goods.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 商品组合对象
 */
public class Goods implements Serializable {
    //spu信息
    private Spu spu;

    //Sku信息
    private List<Sku> skuList;


    public Spu getSpu() {
        return spu;
    }

    public void setSpu(Spu spu) {
        this.spu = spu;
    }

    public List<Sku> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<Sku> skuList) {
        this.skuList = skuList;
    }
}
