package com.mes.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mes.common.JsonData;
import com.mes.param.MesOrderVo;
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
	
	
	//添加接收json数据的注解
	@ResponseBody
	@RequestMapping("/insert.json")
	public JsonData insertAjax(MesOrderVo mesOrderVo) {
		orderService.orderBatchInserts(mesOrderVo);//batch 批量
		return JsonData.success();
	}
	
	
}
