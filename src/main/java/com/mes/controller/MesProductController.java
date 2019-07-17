package com.mes.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mes.beans.PageQuery;
import com.mes.beans.PageResult;
import com.mes.common.JsonData;
import com.mes.dto.ProductDto;
import com.mes.model.MesProduct;
import com.mes.param.MesProductVo;
import com.mes.param.SearchProductParam;
import com.mes.service.ProductService;

@Controller
@RequestMapping("/product")
public class MesProductController {
	
	@Resource
	private ProductService productService;
	
	private String FPATH="product";
	
	//添加材料页面
	@RequestMapping("/productinsert.page")
	public String productInsertPage() {
		return FPATH+"/productInsert";
	}
	//批量到库页面
	@RequestMapping("/product.page")
	public String productPage() {
		return FPATH+"/product";
	}
	//到库查询页面
	@RequestMapping("/productCome.page")
	public String productCome() {
		return FPATH+"/productCome";
	}
	//钢锭查询页面
	@RequestMapping("/productIron.page")
	public String productIron() {
		return FPATH+"/productIron";
	}
	//材料绑定页面
	@RequestMapping("/productBindList.page")
	public String productBindList() {
		return FPATH+"/productBindList";
	}
	//添加材料
	@RequestMapping("/insert.json")
	//@ResponseBody
	public String insertProduct(MesProductVo mesProductVo) {
		productService.ProductInsert(mesProductVo);
		return FPATH+"/product";
		
	}
	//分页
	@RequestMapping("/product.json")
	@ResponseBody
	public JsonData productPageList(SearchProductParam param,PageQuery page) {
		PageResult<MesProduct> pr=(PageResult<MesProduct>) productService.searchPageList(param,page);
		return JsonData.success(pr);
	}
	//材料绑定分页
	@RequestMapping("/productBindList.json")
	@ResponseBody
	public JsonData prooductBindList(SearchProductParam param,PageQuery page) {
		PageResult<ProductDto> pr=(PageResult<ProductDto>) productService.searchPageBindList(param,page);
		return JsonData.success(pr);
		
	}
	
	//修改数据
	@RequestMapping("/update.json")
	@ResponseBody
	public JsonData updateProduct(MesProductVo mesProductVo) {
		productService.update(mesProductVo);
		return JsonData.success();
	}
	//批量入库
	@RequestMapping("/productBatchStart.json")
	@ResponseBody
	public JsonData productBatchStart(String ids) {
		productService.batchStart(ids);
		return JsonData.success();
	}
}
