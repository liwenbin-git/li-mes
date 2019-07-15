package com.mes.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mes.beans.PageQuery;
import com.mes.beans.PageResult;
import com.mes.common.JsonData;
import com.mes.model.MesProduct;
import com.mes.param.MesProductVo;
import com.mes.param.SearchProductParam;
import com.mes.service.ProductService;

@Controller
@RequestMapping("/product")
public class MesProductController {
	
	@Resource
	private ProductService productService;
	
	private String FPATH="product/";
	
	
	@RequestMapping("/productinsert.page")
	public String productInsertPage() {
		return FPATH+"productInsert";
	}
	@RequestMapping("/product.page")
	public String productPage() {
		return FPATH+"product";
	}

	@RequestMapping("insert.json")
	@ResponseBody
	public JsonData insertProduct(MesProductVo mesProductVo) {
		productService.ProductInsert(mesProductVo);
		return JsonData.success();
		
	}
	@RequestMapping("/product.json")
	@ResponseBody
	public JsonData productPageList(SearchProductParam param,PageQuery page) {
		PageResult<MesProduct> pr=(PageResult<MesProduct>) productService.searchPageList(param,page);
		return JsonData.success(pr);
	}
	
	//ÐÞ¸ÄÊý¾Ý
	@RequestMapping("/update.json")
	@ResponseBody
	public JsonData updateProduct(MesProductVo mesProductVo) {
		productService.update(mesProductVo);
		return JsonData.success();
	}
}
