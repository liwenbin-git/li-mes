package com.mes.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchProductParam {

	
	private Integer pid;

	private String search_msource;

	private String keyword;

	private Integer search_status;
	
	
}
