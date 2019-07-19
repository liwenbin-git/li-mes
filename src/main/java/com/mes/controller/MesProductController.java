package com.mes.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mes.beans.PageQuery;
import com.mes.beans.PageResult;
import com.mes.common.JsonData;
import com.mes.dao.MesProductMapper;
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
	//������ҳ��
	@RequestMapping("/productBind.page")
	public String productBind(String id,Model model) {
		MesProduct product = productService.selectProductByid(id);
		if(product!=null) {
			model.addAttribute("product", product);
			return FPATH+"/productBind";
		}else {
			return FPATH+"/productBindList";
		}
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
	//δ�󶨲��Ϸ�ҳ
	@RequestMapping("/productChildBindList.json")
	@ResponseBody
	public JsonData productChildBindList(SearchProductParam param,PageQuery page) {
		PageResult<ProductDto> pr=(PageResult<ProductDto>) productService.searchPageChildBindList(param,page);
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
	//�󶨲���
	@RequestMapping("/bind.json")
	@ResponseBody
	public JsonData Bind(String childId,String parentId) {
		productService.bind(childId,parentId);
		return JsonData.success(true);
	}
	//�Ѱ󶨲����б��ҳ
	@RequestMapping("/productParentBindList.json")
	@ResponseBody
    public JsonData searchParentBindListPage(SearchProductParam param, PageQuery page) {
    	PageResult<ProductDto> pr=(PageResult<ProductDto>) productService.searchPageParentBindList(param, page);
    	return JsonData.success(pr);
    }
	//���Ͻ��
	@RequestMapping("/parentUnbound.json")
	@ResponseBody
	public JsonData unbound(String childId) {
		boolean result=productService.unbound(childId);
		return JsonData.success(result);
	}
}
