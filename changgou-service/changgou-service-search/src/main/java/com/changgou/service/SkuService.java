package com.changgou.service;

import java.util.Map;

public interface SkuService {
    /**
     * 条件搜索
     * @param :searchMap
     * @return :Map
     */
    Map<String,Object> search(Map<String,String> searchMap);

    /**
     * 导入数据到索引库中
     */
    void importData();
}
