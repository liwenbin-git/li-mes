package com.mes.dto;



import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SearchPlanDto {

	//关键词
	private String keyword;
	
	//来料预期
	private Date fromTime;
	
	//合同交期
	private Date toTime;
	
	//查询状态
	private Integer search_status=0;
	
	//材料来源
	private String search_msource;
	
}
