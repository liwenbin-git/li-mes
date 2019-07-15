package com.mes.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString

public class SearchProductDto {

	private String keyword;
	
	private Integer search_status=0;
	
	private String search_msource;
}
