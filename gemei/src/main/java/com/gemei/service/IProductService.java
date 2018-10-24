package com.gemei.service;

import com.gemei.common.ServerResponse;
import com.gemei.pojo.Product;
import com.gemei.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;

public interface IProductService {
	
	ServerResponse saveOrUpdateProduct(Product product);
	
	ServerResponse<String> setSaleStatus(Integer productId,Integer status);
	
	ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);
	
	ServerResponse getProductList(int pageNum,int pageSize);
	
	ServerResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize);
	
	ServerResponse<ProductDetailVo> getProductDetail(Integer productId);
	
	ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);
}
