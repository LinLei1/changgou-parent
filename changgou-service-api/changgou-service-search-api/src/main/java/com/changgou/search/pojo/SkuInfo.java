package com.changgou.search.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.Map;

@Document(indexName = "skuinfo",type = "docs")
public class SkuInfo {

    @Id
    private Long id;//商品ID

    /**
     * type = FieldType.Text:类型,Text支持分词
     * analyzer = "ik_smart":创建索引的分词器
     * index = true:添加数据的时候是否分词
     * store = false: 是否存储
     * searchAnalyzer = "smart": 搜索时使用的分词器
     */
    @Field(type = FieldType.Text,analyzer = "ik_smart",index = true,store = false,searchAnalyzer = "ik_smart")
    private String name;//SKU名称

    @Field(type = FieldType.Double)
    private Integer price;//价格(分)

    private Integer num;//库存数量

    private String image;//商品图片

    private Integer weight;//重量(克)

    private Date cteateTime;//创建时间

    private Date updateTime;//更新时间

    private Long spuId;//SPUID

    private Integer categoryId;//类目ID

    /**
     * type = FieldType.Keyword: 表示不分词,作为整体查询
     */
    @Field(type = FieldType.Keyword)
    private String categoryName;//类目名称

    @Field(type = FieldType.Keyword)
    private String brandName;//品牌名称

    private String spec;//规格

    private String status;//商品状态 1-正常 2-下架 3-删除

    private Map<String,Object> specMap;  //规格参数

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Date getCteateTime() {
        return cteateTime;
    }

    public void setCteateTime(Date cteateTime) {
        this.cteateTime = cteateTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Object> getSpecMap() {
        return specMap;
    }

    public void setSpecMap(Map<String, Object> specMap) {
        this.specMap = specMap;
    }
}
