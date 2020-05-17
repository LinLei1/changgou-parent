package com.changgou.dao;

import com.changgou.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

public interface SkuEsMapper extends ElasticsearchCrudRepository<SkuInfo,Long> {
}
