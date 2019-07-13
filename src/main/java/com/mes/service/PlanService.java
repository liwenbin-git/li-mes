package com.mes.service;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.mes.beans.PageQuery;
import com.mes.beans.PageResult;
import com.mes.dao.MesOrderCustomerMapper;
import com.mes.dao.MesOrderMapper;
import com.mes.dao.MesPlanCustomerMapper;
import com.mes.dao.MesPlanMapper;
import com.mes.dao.MesProductMapper;
import com.mes.dto.SearchPlanDto;
import com.mes.exception.ParamException;
import com.mes.model.MesOrder;
import com.mes.model.MesPlan;
import com.mes.model.MesProduct;
import com.mes.param.MesPlanVo;
import com.mes.param.SearchPlanParam;
import com.mes.util.BeanValidator;
import com.mes.util.MyStringUtils;
import com.mes.util.UUIDUtil;

@Service
public class PlanService {
	
	@Resource
	private MesOrderCustomerMapper mesOrderCustomerMapper;
    @Resource
    private MesOrderMapper mesOrderMapper;
	@Resource
	private MesPlanCustomerMapper mesPlanCustomerMapper;
	@Resource
	private MesPlanMapper planMapper;
	@Resource
	private SqlSession sqlSession;
	@Resource
	private MesProductMapper mesProductMapper;
	
	//分页
	public PageResult<MesPlan> searchPageList(SearchPlanParam param, PageQuery page) {
		// TODO Auto-generated method stub
		//校验page
		BeanValidator.check(page);
		//将param中的字段传入dto进行交互
		SearchPlanDto dto= new SearchPlanDto();
		//传值
		if(StringUtils.isNotBlank(param.getKeyword())) {
			dto.setKeyword(param.getKeyword());
		}
		if(StringUtils.isNotBlank(param.getSearch_msource())) {
			dto.setSearch_msource(param.getSearch_msource());
		}
		if(StringUtils.isNotBlank(param.getSearch_status())) {
			dto.setSearch_status(Integer.parseInt(param.getSearch_status()));
		}
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			if(StringUtils.isNotBlank(param.getFromTime())) {
				dto.setFromTime(dateFormat.parse(param.getFromTime()));
			}
			if(StringUtils.isNotBlank(param.getToTime())) {
				dto.setToTime(dateFormat.parse(param.getToTime()));
			}
		} catch (Exception e) {
		 throw new ParamException("传入的日期格式有问题，正确的格式是：yyyy-MM-dd");
		}
		//获得计划的总条数
		int count = mesPlanCustomerMapper.countbySearchDto(dto);
		if(count>0) {
			List<MesPlan> planList=mesPlanCustomerMapper.getPageListBySearchDto(dto,page);
			return PageResult.<MesPlan>builder().total(count).data(planList).build();
		}
		return PageResult.<MesPlan>builder().build();
	}

	public void prePlan(MesOrder mesOrder) {

		MesPlanMapper planMapper = sqlSession.getMapper(MesPlanMapper.class);
		MesPlan mesPlan =MesPlan.builder().planOrderid(mesOrder.getOrderId()).planProductname(mesOrder.getOrderProductname())//
				.planClientname(mesOrder.getOrderClientname()).planContractid(mesOrder.getOrderContractid()).planImgid(mesOrder.getOrderImgid())//
				.planMaterialname(mesOrder.getOrderMaterialname()).planCurrentstatus("计划").planCurrentremark("计划待执行").planSalestatus(mesOrder.getOrderSalestatus())//
				.planMaterialsource(mesOrder.getOrderMaterialsource()).planHurrystatus(mesOrder.getOrderHurrystatus()).planStatus(0).planCometime(mesOrder.getOrderCometime())//
				.planCommittime(mesOrder.getOrderCommittime()).planInventorystatus(mesOrder.getOrderInventorystatus()).build();
		//添加数据
		planMapper.insertSelective(mesPlan);
				
	}
//批量启动计划
	public void startPlanByOrderIds(String[] ids) {
		// TODO Auto-generated method stub
		//批量启动订单后的批量启动计划
		for(String tempId : ids) {
			Integer id = Integer.parseInt(tempId);
			MesOrder order= mesOrderMapper.selectByPrimaryKey(id);
			//查询内容非空，使用google的工具
			Preconditions.checkNotNull(order);
			prePlan(order);
		}
	}
	//启动计划
	public void batchStartWithIds(String ids) {
		// TODO Auto-generated method stub
		if(ids!=null&&ids.length()>0) {
			//批量处理
			MesPlanMapper mapper=sqlSession.getMapper(MesPlanMapper.class);
			//批处理
			String[] strs=ids.split(",");
			String[] idsTemp=strs[0].split("&");
			String[] datesTemp=strs[1].split("&");
			String startTime=datesTemp[0];
			String endTime=datesTemp[1];
			for(int i=0;i<idsTemp.length;i++) {
				MesPlan mesPlan=new MesPlan();
				mesPlan.setId(Integer.parseInt(idsTemp[i]));
				mesPlan.setPlanWorkstarttime(MyStringUtils.string2Date(startTime, null));
				mesPlan.setPlanWorkendtime(MyStringUtils.string2Date(endTime, null));
				mesPlan.setPlanStatus(1);
				mesPlan.setPlanCurrentremark("计划已启动");
				mapper.updateByPrimaryKeySelective(mesPlan);
				//半成品材料 生成
				MesPlan plan=planMapper.selectByPrimaryKey(Integer.parseInt(idsTemp[i]));
				//产生半成品材料
				String orderid=plan.getPlanOrderid();
				MesOrder order = mesOrderCustomerMapper.selectByOrderId(orderid);
				MesProduct mesProduct=MesProduct.builder().productId(UUIDUtil.generateUUID())//
						.productOrderid(order.getId()).productPlanid(plan.getId())//
						.productMaterialname(order.getOrderMaterialname())//
						.productImgid(order.getOrderImgid())//
						.productMaterialsource(order.getOrderMaterialsource())//\
						.productStatus(0).build();
				mesProductMapper.insertSelective(mesProduct);
			}
		}
	}

	public void update(MesPlanVo mesPlanVo) {
		// TODO Auto-generated method stub
		BeanValidator.check(mesPlanVo);
		MesPlan mesPlan=new MesPlan();
		BeanUtils.copyProperties(mesPlanVo, mesPlan);
		mesPlan.setPlanCometime(MyStringUtils.string2Date(mesPlanVo.getPlanCometime(),null));
		mesPlan.setPlanCommittime(MyStringUtils.string2Date(mesPlanVo.getPlanCommittime(),null));
		mesPlan.setPlanWorkstarttime(MyStringUtils.string2Date(mesPlanVo.getPlanWorkstarttime(),null));
		mesPlan.setPlanWorkendtime(MyStringUtils.string2Date(mesPlanVo.getPlanWorkendtime(),null));
		planMapper.updateByPrimaryKeySelective(mesPlan);
	}

}
