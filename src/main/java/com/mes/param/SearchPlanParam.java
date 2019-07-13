package com.mes.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchPlanParam {

	private String keyword;
	
	private String fromTime;
	
	private String toTime;
	
	private String search_status;
	
	private String search_msource;
	
}
