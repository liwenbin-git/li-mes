package com.mes.param;

import java.util.Date;

import javax.validation.constraints.Min;

import com.mes.param.MesPlanVo.MesPlanVoBuilder;

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
public class MesProductVo {
	//批量生成的材料个数
	    @Min(1)
	    private Integer count=1;
	 
	    private Integer id;

	    private Integer pId;

	    private String productId;

	    private Integer productOrderid;

	    private Integer productPlanid;

	    private Float productTargetweight;

	    private Float productRealweight;

	    private Float productLeftweight;

	    private Float productBakweight;

	    private String productIrontype;

	    private Float productIrontypeweight;

	    private String productMaterialname;

	    private String productImgid;

	    private String productMaterialsource;

	    private Integer productStatus;

	    private String productRemark;

	    private String productOperator;

	    private Date productOperateTime;

	    private String productOperateIp;

	    private String furnacenumber;

	
	   

}
