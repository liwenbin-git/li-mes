package com.mes.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mes.beans.PageQuery;
import com.mes.beans.PageResult;
import com.mes.dto.ProductDto;
import com.mes.dto.SearchProductDto;
import com.mes.model.MesProduct;

public interface MesProductCustomerMapper {

	Long getProductCount();

	int countBySearchDto(@Param("dto") SearchProductDto dto);

	List<MesProduct> getPageListBySearchDto(@Param("dto") SearchProductDto dto, @Param("page") PageQuery page);

	void batchStart(@Param("list")String[] idsArray);

	List<ProductDto> getPageBindListBySearchDto(@Param("dto") SearchProductDto dto, @Param("page")PageQuery page);

	int countBySearchBindListDto(@Param("dto") SearchProductDto dto);

	int countBySearchChildBindListDto(@Param("dto") SearchProductDto dto);

	List<ProductDto> getPageChildBindListBySearchDto(@Param("dto")SearchProductDto dto,@Param("page") PageQuery page);

	int countBySearchParentBindListDto(@Param("dto")SearchProductDto dto);

	List<ProductDto> getPageListBySearchParentBindListDto(@Param("dto")SearchProductDto dto, @Param("page")PageQuery page);
}
