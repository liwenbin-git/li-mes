package com.mes.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MesPlanVo {

	private Integer id;// �������ҪУ�飬�Զ�auto����

    private String planOrderid;

    private String planProductname;

    private String planClientname;

    private String planContractid;

    private String planImgid;

    private String planMaterialname;

    private String planMaterialdesc;

    private String planCurrentstatus;

    private String planCurrentremark;

    private String planSalestatus;

    private String planMaterialsource;

    private Integer planHurrystatus;

    private Integer planStatus;
    
    private String planCometime;

    private String planCommittime;

    private String planWorkstarttime;

    private String planWorkendtime;

    private Integer planInventorystatus;

    private String planRemark;
}
