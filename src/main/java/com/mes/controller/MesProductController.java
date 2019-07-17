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
	
	//��Ӳ���ҳ��
	@RequestMapping("/productinsert.page")
	public String productInsertPage() {
		return FPATH+"/productInsert";
	}
	//��������ҳ��
	@RequestMapping("/product.page")
	public String productPage() {
		return FPATH+"/product";
	}
	//�����ѯҳ��
	@RequestMapping("/productCome.page")
	public String productCome() {
		return FPATH+"/productCome";
	}
	//�ֶ���ѯҳ��
	@RequestMapping("/productIron.page")
	public String productIron() {
		return FPATH+"/productIron";
	}
	//���ϰ�ҳ��
	@RequestMapping("/productBindList.page")
	public String productBindList() {
		return FPATH+"/productBindList";
	}
	//��Ӳ���
	@RequestMapping("/insert.json")
	//@ResponseBody
	public String insertProduct(MesProductVo mesProductVo) {
		productService.ProductInsert(mesProductVo);
		return FPATH+"/product";
		
	}
	//��ҳ
	@RequestMapping("/product.json")
	@ResponseBody
	public JsonData productPageList(SearchProductParam param,PageQuery page) {
		PageResult<MesProduct> pr=(PageResult<MesProduct>) productService.searchPageList(param,page);
		return JsonData.success(pr);
	}
	//���ϰ󶨷�ҳ
	@RequestMapping("/productBindList.json")
	@ResponseBody
	public JsonData prooductBindList(SearchProductParam param,PageQuery page) {
		PageResult<ProductDto> pr=(PageResult<ProductDto>) productService.searchPageBindList(param,page);
		return JsonData.success(pr);
		
	}
	
	//�޸�����
	@RequestMapping("/update.json")
	@ResponseBody
	public JsonData updateProduct(MesProductVo mesProductVo) {
		productService.update(mesProductVo);
		return JsonData.success();
	}
	//�������
	@RequestMapping("/productBatchStart.json")
	@ResponseBody
	public JsonData productBatchStart(String ids) {
		productService.batchStart(ids);
		return JsonData.success();
	}
}
