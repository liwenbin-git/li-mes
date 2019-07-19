package com.mes.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import com.mes.beans.PageQuery;
import com.mes.beans.PageResult;
import com.mes.dao.MesProductCustomerMapper;
import com.mes.dao.MesProductMapper;
import com.mes.dto.ProductDto;
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
	// 一开始就定义一个id生成器
	private IdGenerator ig = new IdGenerator();
		
	public void ProductInsert(MesProductVo mesProductVo) {
		// TODO Auto-generated method stub
		//校验
		BeanValidator.check(mesProductVo);
		//批量生成的个数
		Integer counts=mesProductVo.getCount();
		//生成id
		List<String> ids = createOrderIdsDefault(Long.valueOf(counts));
		
		MesProductMapper mesProductBatchMapper=sqlSession.getMapper(MesProductMapper.class);
		for(String productid : ids) {
			try {
				MesProduct mesProduct= MesProduct.builder().productId(productid)//
						.pId(mesProductVo.getPId()).productOrderid(mesProductVo.getProductOrderid())
						.productPlanid(mesProductVo.getProductPlanid()).productTargetweight(mesProductVo.getProductTargetweight())
						.productRealweight(mesProductVo.getProductRealweight()).productLeftweight(mesProductVo.getProductLeftweight())
						.productBakweight(mesProductVo.getProductLeftweight()).productIrontype(mesProductVo.getProductIrontype())
						.productIrontypeweight(mesProductVo.getProductIrontypeweight()).productMaterialname(mesProductVo.getProductMaterialname())
						.productImgid(mesProductVo.getProductImgid()).productStatus(mesProductVo.getProductStatus())
						.productRemark(mesProductVo.getProductRemark()).productOperator(mesProductVo.getProductOperator())
						.productOperateTime(mesProductVo.getProductOperateTime()).productOperateIp(mesProductVo.getProductOperateIp())
						.furnacenumber(mesProductVo.getFurnacenumber()).productMaterialsource(mesProductVo.getProductMaterialsource()).build();

				mesProductMapper.insertSelective(mesProduct);
			
			} catch (Exception e) {
				// TODO: handle exception
				throw new SysMineException("创建过程有问题");
			}
		}
		
	}
	public Long getProductCount() {
		return mesProductCustomerMapper.getProductCount();
	}

	// 获取id集合
	public List<String> createOrderIdsDefault(Long ocounts) {
		if (ig == null) {
			ig = new IdGenerator();
		}

		ig.setCurrentdbidscount(getProductCount());
		List<String> list = ig.initIds(ocounts);
		ig.clear();
		return list;
	}

	// bean:自定义的类，功能适用范围最广
	// domain-javabean-pojo-po--就是表翻译过来的java类
	// vo-param poVo xxParam page SearchOrderParam..
	// dto 用于自定义的与数据层交互的类 SearchOrderDto
	// SearchOrderParam--SearchOrderVo
	// 1 默认生成代码
	// 2 手工生成代码
	// id生成器
	class IdGenerator {
		// 数量起始位置
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
			// 系统默认生成5位 ZX1700001
			int goallength = 6;
			// 获取数据库order的总数量+1+循环次数(addcount)
			int count = this.currentdbidscount.intValue() + 1 + addcount;
			StringBuilder sBuilder = new StringBuilder("");
			// 计算与5位数的差值
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
		//校验页码
		BeanValidator.check(page);
		//将param中的字段传入dto中进行数据交互
		SearchProductDto dto= new SearchProductDto();
		if(StringUtils.isNotBlank(param.getKeyword())) {
			dto.setKeyword("%"+param.getKeyword()+"%");
		}
		if(StringUtils.isNotBlank(param.getSearch_msource())) {
			dto.setSearch_msource(param.getSearch_msource());
		}
		if (param.getSearch_status() != null) {
			dto.setSearch_status(param.getSearch_status());
		}
		int count = mesProductCustomerMapper.countBySearchDto(dto);
		if(count>0) {
			List<MesProduct> prolductList=mesProductCustomerMapper.getPageListBySearchDto(dto,page);
			return PageResult.<MesProduct>builder().total(count).data(prolductList).build();
		}
		return PageResult.<MesProduct>builder().build();
	}
	//修改数据
	public void update(MesProductVo mesProductVo) {
		// TODO Auto-generated method stub
		//校验数据
		BeanValidator.check(mesProductVo);
		MesProduct product= mesProductMapper.selectByPrimaryKey(mesProductVo.getId());
		//校验要更新的数据是否存在
		
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
		product.setProductBakweight(mesProductVo.getProductLeftweight());
		
	    mesProductMapper.updateByPrimaryKeySelective(product);
		
	}
	
	//批量启动
	public void batchStart(String ids) {
		// TODO Auto-generated method stub
	if(ids !=null && ids.length()>0) {
		String[] idsArray=ids.split("&");
		mesProductCustomerMapper.batchStart(idsArray);
	}
	}
	//绑定页面分页
	public PageResult<ProductDto> searchPageBindList(SearchProductParam param, PageQuery page) {
		// TODO Auto-generated method stub
		//校验
		BeanValidator.check(page);
		SearchProductDto dto = new SearchProductDto();
		if(StringUtils.isNotBlank(param.getKeyword())) {
			dto.setKeyword("%"+param.getKeyword()+"%");
		}
		if(StringUtils.isNotBlank(param.getSearch_msource())) {
			dto.setSearch_msource(param.getSearch_msource());
		}
		if(param.getSearch_status() !=null) {
			dto.setSearch_status(param.getSearch_status());
		}
		//得到数据库中总条数
		int counts=mesProductCustomerMapper.countBySearchBindListDto(dto);
		
		if(counts>0) {
			List<ProductDto> productList=mesProductCustomerMapper.getPageBindListBySearchDto(dto,page);
			return PageResult.<ProductDto>builder().total(counts).data(productList).build();
		}
		return PageResult.<ProductDto>builder().build();
		
		
	}
	//根据id得到id所在的材料
	public MesProduct selectProductByid(String id) {
		// TODO Auto-generated method stub
		MesProduct product = mesProductMapper.selectByPrimaryKey(Integer.parseInt(id));
		return product;
	}
	//未绑定页面分页
	public PageResult<ProductDto> searchPageChildBindList(SearchProductParam param, PageQuery page) {
		// TODO Auto-generated method stub
		//校验
		BeanValidator.check(page);
		SearchProductDto dto=new SearchProductDto();
		if(StringUtils.isNotBlank(param.getSearch_msource())) {
			dto.setSearch_msource(param.getSearch_msource());
		}
		int count=mesProductCustomerMapper.countBySearchChildBindListDto(dto);
		if(count>0) {
			List<ProductDto> productList=mesProductCustomerMapper.getPageChildBindListBySearchDto(dto,page);
			return PageResult.<ProductDto>builder().total(count).data(productList).build();
		}
		return PageResult.<ProductDto>builder().build();
	}
	//绑定材料
	public void bind(String childId, String parentId) {
		// TODO Auto-generated method stub
		Integer cid=Integer.parseInt(childId);
		Integer pid=Integer.parseInt(parentId);
		MesProduct child=mesProductMapper.selectByPrimaryKey(cid);
		MesProduct parent=mesProductMapper.selectByPrimaryKey(pid);
		if(parent.getProductBakweight()>=child.getProductTargetweight() && parent.getProductLeftweight()>=parent.getProductBakweight()) {
			
			parent.setProductBakweight(parent.getProductBakweight()-child.getProductTargetweight());
			
			child.setProductBakweight(child.getProductBakweight());
			child.setpId(pid);
			child.setProductStatus(1);
			
			mesProductMapper.updateByPrimaryKeySelective(parent);
			mesProductMapper.updateByPrimaryKeySelective(child);
			
		}
	}
	
	public PageResult<ProductDto> searchPageParentBindList(SearchProductParam param, PageQuery page) {
		// TODO Auto-generated method stub
		//校验页码
		BeanValidator.check(page);
		//将param中的数据传入dto中进行交互
		SearchProductDto dto=new SearchProductDto();
		if(StringUtils.isNotBlank(param.getSearch_msource())) {
			dto.setSearch_msource(param.getSearch_msource());
		}
		if(param.getPid() !=null) {
			dto.setPid(param.getPid());
		}
		//查找个数
		int count = mesProductCustomerMapper.countBySearchParentBindListDto(dto);
		if (count > 0) {
			List<ProductDto> productList = mesProductCustomerMapper.getPageListBySearchParentBindListDto(dto, page);
			return PageResult.<ProductDto>builder().total(count).data(productList).build();
		}
		return PageResult.<ProductDto>builder().build();
	}
	//材料解绑
	public boolean unbound(String childId) {
		if(StringUtils.isNoneBlank(childId)&&StringUtils.isNotEmpty(childId)) {
			Integer cid=Integer.parseInt(childId);
			MesProduct product=mesProductMapper.selectByPrimaryKey(cid);
			//解绑过程需要查看投料重量是否为空，如果为空则可以解绑
			if(product.getProductRealweight()!=null&&product.getProductRealweight()>0) {
				return false;
			}
			//status-0
			product.setProductStatus(0);
			//backweight-0
			product.setProductBakweight(0f);
			//pid-null
			Integer pid=product.getpId();
			product.setpId(null);
			MesProduct parent=mesProductMapper.selectByPrimaryKey(pid);
			parent.setProductBakweight(parent.getProductBakweight()+product.getProductTargetweight());
			//执行绑定双方修改
			mesProductMapper.updateByPrimaryKey(product);
			mesProductMapper.updateByPrimaryKeySelective(parent);
		}
		return false;
	}

}
