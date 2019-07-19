package com.mes.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mes.beans.PageQuery;
import com.mes.beans.PageResult;
import com.mes.common.JsonData;
import com.mes.model.MesPlan;
import com.mes.param.MesPlanVo;
import com.mes.param.SearchPlanParam;
import com.mes.service.PlanService;

@Controller
@RequestMapping("/plan")
public class MesPlanController {

	@Resource
	private PlanService planService;
	private static String FPATH="plan/";
	
	@RequestMapping("/plan.page")
	public String  planPage() {
		return FPATH+"plan";
	}
	@RequestMapping("/planStarted.page")
	public String planStartedPage() {
		return FPATH+"planStarted";
	}
	
	
	//分页显示
	@RequestMapping("/plan.json")
	@ResponseBody
	public JsonData searchPage(SearchPlanParam param,PageQuery page) {
		PageResult<MesPlan> pr=(PageResult<MesPlan>)planService.searchPageList(param,page);
		return JsonData.success(pr);
	}
	//批量启动
	@ResponseBody
	@RequestMapping("/planBatchStart.json")
	public JsonData planBatchStart(String ids) {
		planService.batchStartWithIds(ids);
		return JsonData.success();
	}
	@RequestMapping("/update.json")
	@ResponseBody
	public JsonData updatePlan(MesPlanVo mesPlanVo) {
		planService.update(mesPlanVo);
		return JsonData.success();
	}
}
