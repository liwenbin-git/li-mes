package com.mes.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mes.beans.PageQuery;
import com.mes.beans.PageResult;
import com.mes.common.JsonData;
import com.mes.model.MesOrder;
import com.mes.param.MesOrderVo;
import com.mes.param.SearchOrderParam;
import com.mes.service.OrderService;



///order/orderBatch.page
@Controller
@RequestMapping("/order")
public class OrderController {
	@Resource
	private OrderService orderService;
	private static String FPATH="order/";

	@RequestMapping("/orderBatch.page")
	public String orderBatchPage() {
		return FPATH+"orderBatch";
	}
	
	@RequestMapping("/order.page")
	public String orderPage() {
		return FPATH+"order";
	}
	
	//��ӽ���json���ݵ�ע��
	@ResponseBody
	@RequestMapping("/insert.json")
	public JsonData insertAjax(MesOrderVo mesOrderVo) {
		orderService.orderBatchInserts(mesOrderVo);//batch ����
		return JsonData.success();
	}
	//�޸�
	@ResponseBody
	@RequestMapping("update.json")
	public JsonData updateOrder(MesOrderVo mesOrderVo) {
		orderService.update(mesOrderVo);
		return JsonData.success();
	}
	//��ҳ��ʾ
	@RequestMapping("order.json")
	@ResponseBody
	public JsonData serchPage(SearchOrderParam param,PageQuery page) {
		PageResult<MesOrder> pr = (PageResult<MesOrder>) orderService.searchPageList(param,page);
		return JsonData.success(pr);
}
//��������
	@RequestMapping("/orderBatchStart.json")
	@ResponseBody
	public JsonData orderBatchStart(String ids) {
		orderService.batchStart(ids);
		return JsonData.success();
	}
	
	
}








