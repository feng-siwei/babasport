package com.feng.core.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.feng.core.bean.product.Color;
import com.feng.core.bean.product.Sku;
import com.feng.core.bean.product.SkuQuery;
import com.feng.core.dao.product.ColorDao;
import com.feng.core.dao.product.ProductDao;
import com.feng.core.dao.product.SkuDao;

@Service("skuService")
@Transactional
public class SkuServiceIpml implements SkuService {
	@Autowired
	private SkuDao skuDao;
	@Autowired
	private ColorDao colorDao;
	@Autowired
	private ProductDao ProductDao;
	
	//查询库存
	@Override
	public List<Sku> selectSkuListByProductId(Long produtId) {
		SkuQuery skuQuery = new SkuQuery();
		skuQuery.createCriteria().andProductIdEqualTo(produtId);
		List<Sku> skus = skuDao.selectByExample(skuQuery);
		for (Sku sku : skus) {
			Color color = colorDao.selectByPrimaryKey(sku.getColorId());
			sku.setColor(color);
		}
		return skus;
	}
	
	//更改库存
	public void updateSkuById(Sku sku) {
		skuDao.updateByPrimaryKeySelective(sku);
	}
	
	//通过ID查询SKU对象
	@Override
	public Sku selectSkuByid(Long id) {
		Sku sku = skuDao.selectByPrimaryKey(id);
		//商品对象
		sku.setProduct(ProductDao.selectByPrimaryKey(sku.getProductId()));
		//颜色对象
		sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
		return sku;
	}
	
}
