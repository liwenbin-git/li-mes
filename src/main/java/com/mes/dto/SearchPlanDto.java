package com.mes.dto;



import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SearchPlanDto {

	//�ؼ���
	private String keyword;
	
	//����Ԥ��
	private Date fromTime;
	
	//��ͬ����
	private Date toTime;
	
	//��ѯ״̬
	private Integer search_status=0;
	
	//������Դ
	private String search_msource;
	
}
