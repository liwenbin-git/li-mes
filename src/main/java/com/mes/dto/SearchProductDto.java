package com.mes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString

public class SearchProductDto {
	
	private Integer pid;

	private String search_msource;

	private String keyword;

	private Integer search_status;

}
