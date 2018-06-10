package com.feng.core.service.product;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.feng.core.bean.product.Color;
import com.feng.core.bean.product.ColorQuery;
import com.feng.core.bean.product.Product;
import com.feng.core.bean.product.ProductQuery;
import com.feng.core.bean.product.ProductQuery.Criteria;
import com.feng.core.bean.product.Sku;
import com.feng.core.dao.product.ColorDao;
import com.feng.core.dao.product.ProductDao;
import com.feng.core.dao.product.SkuDao;

import cn.itcast.common.page.Pagination;

@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductDao productDao;
	@Autowired
	private ColorDao colorDao;
	@Autowired
	private SkuDao skuDao;
	
	//分页
	@Override
	public Pagination selectPaginationByQuery(Integer pageNo, String name, Long brandId, Boolean isShow) {
		ProductQuery productQuery = new ProductQuery();
		//分页
		productQuery.setPageNo(Pagination.cpn(pageNo));
		//参数绑定
		Criteria criteria = productQuery.createCriteria();
		//分页URL帮参
		StringBuilder params = new StringBuilder();
		if (null != name) {
			criteria.andNameLike("%"+name+"%");
			params.append("name=").append(name);
		}
		if (null != brandId) {
			criteria.andBrandIdEqualTo(brandId);
			params.append("&brandId=").append(brandId);
		}
		if (null != isShow) {
			criteria.andIsShowEqualTo(isShow);
			params.append("&isShow=").append(isShow);
		}else {
			criteria.andIsShowEqualTo(false);
			params.append("&isShow=").append(false);
		}
		//倒序
		productQuery.setOrderByClause("id DESC");
		
		List<Product> products = productDao.selectByExample(productQuery);
		int count = productDao.countByExample(productQuery);
		Pagination pagination = new Pagination(
				productQuery.getPageNo(),
				productQuery.getPageSize(), 
				count,
				products);
		//分页展示
		String url = "/product/list.do";
		
		pagination.pageView(url, params.toString());
		
		return pagination;
	}
	
	//加载颜色集合
	public List<Color> selectColorList() {
		ColorQuery colorQuery = new ColorQuery();
		colorQuery.createCriteria().andParentIdNotEqualTo(0L);		
		return colorDao.selectByExample(colorQuery);
	}

	
	//商品保存
	public void insertProduct(Product product){
		//保存商品
		//下架状态 后台程序写的
		product.setIsShow(false);
		//删除  后台程序写的不删除
		product.setIsDel(true);
		//时间
		product.setCreateTime(new Date());
		
		productDao.insertSelective(product);
		//返回ID
		//保存SKU
		String[] colors = product.getColors().split(",");
		String[] sizes = product.getSizes().split(",");
		//颜色
		for (String color : colors) {
			for (String size : sizes) {
				//保存SKU
				Sku sku = new Sku();
				//商品ＩＤ
				sku.setProductId(product.getId());
				//颜色
				sku.setColorId(Long.parseLong(color));
				//尺码
				sku.setSize(size);
				//市场价
				sku.setMarketPrice(999f);
				//售价
				sku.setPrice(666f);
				//运费
				sku.setDeliveFee(8f);
				//库存
				sku.setStock(0);
				//限制
				sku.setUpperLimit(200);
				//时间
				sku.setCreateTime(new Date());

				skuDao.insertSelective(sku);

			}
		}




	}

	
	
	
	
}
