package com.mes.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mes.beans.PageQuery;
import com.mes.dto.SearchPlanDto;
import com.mes.model.MesPlan;


public interface MesPlanCustomerMapper {

	int countbySearchDto(@Param("dto") SearchPlanDto dto);

	List<MesPlan> getPageListBySearchDto(@Param("dto") SearchPlanDto dto,@Param("page") PageQuery page);

}
