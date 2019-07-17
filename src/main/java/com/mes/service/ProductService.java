package com.mes.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.mes.beans.PageQuery;
import com.mes.beans.PageResult;
import com.mes.dao.MesProductCustomerMapper;
import com.mes.dao.MesProductMapper;
import com.mes.dto.SearchProductDto;
import com.mes.exception.SysMineException;
import com.mes.model.MesProduct;
import com.mes.param.MesProductVo;
import com.mes.param.SearchProductParam;
import com.mes.util.BeanValidator;

@Service
public class ProductService {

	@Resource
	private MesProductCustomerMapper mesProductCustomerMapper;
	@Resource
	private MesProductMapper mesProductMapper;
	@Resource
	private SqlSession sqlSession;
	// һ��ʼ�Ͷ���һ��id������
	private IdGenerator ig = new IdGenerator();
		
	public void ProductInsert(MesProductVo mesProductVo) {
		// TODO Auto-generated method stub
		//У��
		BeanValidator.check(mesProductVo);
		//�������ɵĸ���
		Integer counts=mesProductVo.getCount();
		//����id
		List<String> ids = createOrderIdsDefault(Long.valueOf(counts));
		
		MesProductMapper mesProductBatchMapper=sqlSession.getMapper(MesProductMapper.class);
		for(String productid : ids) {
			try {
				MesProduct mesProduct= MesProduct.builder().productId(productid)//
						.pId(mesProductVo.getPId()).productOrderid(mesProductVo.getProductOrderid())
						.productPlanid(mesProductVo.getProductPlanid()).productTargetweight(mesProductVo.getProductTargetweight())
						.productRealweight(mesProductVo.getProductRealweight()).productLeftweight(mesProductVo.getProductLeftweight())
						.productBakweight(mesProductVo.getProductBakweight()).productIrontype(mesProductVo.getProductIrontype())
						.productIrontypeweight(mesProductVo.getProductIrontypeweight()).productMaterialname(mesProductVo.getProductMaterialname())
						.productImgid(mesProductVo.getProductImgid()).productStatus(mesProductVo.getProductStatus())
						.productRemark(mesProductVo.getProductRemark()).productOperator(mesProductVo.getProductOperator())
						.productOperateTime(mesProductVo.getProductOperateTime()).productOperateIp(mesProductVo.getProductOperateIp())
						.furnacenumber(mesProductVo.getFurnacenumber()).productMaterialsource(mesProductVo.getProductMaterialsource()).build();

				mesProductMapper.insertSelective(mesProduct);
			
			} catch (Exception e) {
				// TODO: handle exception
				throw new SysMineException("��������������");
			}
		}
		
	}
	public Long getProductCount() {
		return mesProductCustomerMapper.getProductCount();
	}

	// ��ȡid����
	public List<String> createOrderIdsDefault(Long ocounts) {
		if (ig == null) {
			ig = new IdGenerator();
		}

		ig.setCurrentdbidscount(getProductCount());
		List<String> list = ig.initIds(ocounts);
		ig.clear();
		return list;
	}

	// bean:�Զ�����࣬�������÷�Χ���
	// domain-javabean-pojo-po--���Ǳ��������java��
	// vo-param poVo xxParam page SearchOrderParam..
	// dto �����Զ���������ݲ㽻������ SearchOrderDto
	// SearchOrderParam--SearchOrderVo
	// 1 Ĭ�����ɴ���
	// 2 �ֹ����ɴ���
	// id������
	class IdGenerator {
		// ������ʼλ��
		private Long currentdbidscount;
		private List<String> ids = new ArrayList<String>();
		private String idpre;
		private String yearstr;
		private String idafter;

		public IdGenerator() {
		}

		public Long getCurrentdbidscount() {
			return currentdbidscount;
		}

		public void setCurrentdbidscount(Long currentdbidscount) {
			this.currentdbidscount = currentdbidscount;
			if (null == this.ids) {
				this.ids = new ArrayList<String>();
			}
		}

		public List<String> getIds() {
			return ids;
		}

		public void setIds(List<String> ids) {
			this.ids = ids;
		}

		public String getIdpre() {
			return idpre;
		}

		public void setIdpre(String idpre) {
			this.idpre = idpre;
		}

		public String getYearstr() {
			return yearstr;
		}

		public void setYearstr(String yearstr) {
			this.yearstr = yearstr;
		}

		public String getIdafter() {
			return idafter;
		}

		public void setIdafter(String idafter) {
			this.idafter = idafter;
		}

		public List<String> initIds(Long ocounts) {
			for (int i = 0; i < ocounts; i++) {
				this.ids.add(getIdPre() + getIdAfter(i));
			}
			return this.ids;
		}

		//
		private String getIdAfter(int addcount) {
			// ϵͳĬ������5λ ZX1700001
			int goallength = 6;
			// ��ȡ���ݿ�order��������+1+ѭ������(addcount)
			int count = this.currentdbidscount.intValue() + 1 + addcount;
			StringBuilder sBuilder = new StringBuilder("");
			// ������5λ���Ĳ�ֵ
			int length = goallength - new String(count + "").length();
			for (int i = 0; i < length; i++) {
				sBuilder.append("0");
			}
			sBuilder.append(count + "");
			return sBuilder.toString();
		}

		private String getIdPre() {
			// idpre==null?this.idpre="ZX":this.idpre=idpre;
			this.idpre = "zx_p_";
			return this.idpre;
		}

		private String yearStr() {
			Date currentdate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String yearstr = sdf.format(currentdate).substring(2, 4);
			return yearstr;
		}

		public void clear() {
			this.ids = null;
		}

		@Override
		public String toString() {
			return "IdGenerator [ids=" + ids + "]";
		}
	}

	public PageResult<MesProduct> searchPageList(SearchProductParam param, PageQuery page) {
		// TODO Auto-generated method stub
		//У��ҳ��
		BeanValidator.check(page);
		//��param�е��ֶδ���dto�н������ݽ���
		SearchProductDto dto= new SearchProductDto();
		if(StringUtils.isNotBlank(param.getKeyword())) {
			dto.setKeyword("%"+param.getKeyword()+"%");
		}
		if(StringUtils.isNotBlank(param.getSearch_msource())) {
			dto.setSearch_msource(param.getSearch_msource());
		}
		if(StringUtils.isNotBlank(param.getSearch_status())) {
			dto.setSearch_status(Integer.parseInt(param.getSearch_status()));
		}
		int count = mesProductCustomerMapper.countBySearchDto(dto);
		if(count>0) {
			List<MesProduct> prolductList=mesProductCustomerMapper.getPageListBySearchDto(dto,page);
			return PageResult.<MesProduct>builder().total(count).data(prolductList).build();
		}
		return PageResult.<MesProduct>builder().build();
	}
	//�޸�����
	public void update(MesProductVo mesProductVo) {
		// TODO Auto-generated method stub
		//У������
		BeanValidator.check(mesProductVo);
		MesProduct product= mesProductMapper.selectByPrimaryKey(mesProductVo.getId());
		//У��Ҫ���µ������Ƿ����
		
		product.setProductImgid(mesProductVo.getProductImgid());
		product.setProductMaterialname(mesProductVo.getProductMaterialname());
		product.setProductMaterialsource(mesProductVo.getProductMaterialsource());
		product.setProductTargetweight(mesProductVo.getProductTargetweight());
		product.setProductRealweight(mesProductVo.getProductRealweight());
		product.setProductLeftweight(mesProductVo.getProductLeftweight());
		product.setProductIrontype(mesProductVo.getProductIrontype());
		product.setProductIrontypeweight(mesProductVo.getProductIrontypeweight());
		product.setProductRemark(mesProductVo.getProductRemark());	
		product.setFurnacenumber(mesProductVo.getFurnacenumber());
		
	    mesProductMapper.updateByPrimaryKeySelective(product);
		
	}
	
	//��������
	public void batchStart(String ids) {
		// TODO Auto-generated method stub
	if(ids !=null && ids.length()>0) {
		String[] idsArray=ids.split("&");
		mesProductCustomerMapper.batchStart(idsArray);
	}
	}

}
