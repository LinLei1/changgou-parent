package com.changgou.dao;
import com.changgou.goods.pojo.Sku;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/****
 * @Author:传智播客
 * @Description:Sku的Dao
 * @Date 2019/6/14 0:12
 *****/
public interface SkuMapper extends Mapper<Sku> {
    /**
     * 商品库存递减
     * @param id
     * @param num
     * @return
     */
    @Update("update tb_sku set num=num-#{num} where id =#{id} and num >=#{num}")
    int decrCount(@Param(value = "id") Long id, @Param(value = "num") Integer num);
}
