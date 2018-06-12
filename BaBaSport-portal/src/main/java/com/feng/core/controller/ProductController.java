package com.feng.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.feng.core.bean.product.Product;
import com.feng.core.service.SearchServuce;

import cn.itcast.common.page.Pagination;

@Controller
public class ProductController {
	@Autowired
	private SearchServuce searchServuce;
	
	//首页
	@RequestMapping(value = "/index.do")
	public String index(Model model) {

		return "index";
	}
	
	//搜索
	@RequestMapping(value = "/search")
	public String index(String keyword ,Integer pageNo,Model model) throws Exception {
		Pagination pagination  = searchServuce.selectPaginationByQuery(keyword,pageNo);
		model.addAttribute("pagination", pagination);
		return "search";
	}
}
