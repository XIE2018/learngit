package com.gemei.dao;

import com.gemei.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

   List<Product> selectList();
   
   List<Product> selectByNameAndProductId(@Param("productName") String productName,@Param("productId") Integer productId);
   
   List<Product> selectByNameAndCategoryIds(@Param("productName") String productName,@Param("categoryIdList") List<Integer> categoryIdList);
   
   //使用Integer,int无法为Null，可能商品删除的情况
   Integer selectStockByProductId(Integer id);

}