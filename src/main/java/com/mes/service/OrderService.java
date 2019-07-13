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
import com.mes.dao.MesOrderCustomerMapper;
import com.mes.dao.MesOrderMapper;
import com.mes.dto.SearchOrderDto;
import com.mes.exception.ParamException;
import com.mes.exception.SysMineException;
import com.mes.model.MesOrder;
import com.mes.param.MesOrderVo;
import com.mes.param.SearchOrderParam;
import com.mes.util.BeanValidator;
import com.mes.util.MyStringUtils;


@Service
public class OrderService {
	@Resource
	private MesOrderMapper mesOrderMapper;
	@Resource
	private MesOrderCustomerMapper mesOrderCustomerMapper;
	@Resource
	private SqlSession sqlSession;
	@Resource
	private PlanService planService;

	// һ��ʼ�Ͷ���һ��id������
	private IdGenerator ig = new IdGenerator();


	public void orderBatchInserts(MesOrderVo mesOrderVo) {
		// ����У��
		BeanValidator.check(mesOrderVo);
		// ��ȥ�ж��Ƿ����������
		Integer counts = mesOrderVo.getCount();
		// ����counts�ĸ�����������Ҫ��ӵ�ids�����ݼ���
		// zx180001 zx180002
		List<String> ids = createOrderIdsDefault(Long.valueOf(counts));
		// sql��������Ӵ���
		MesOrderMapper mesOrderBatchMapper = sqlSession.getMapper(MesOrderMapper.class);
		for (String orderid : ids) {
			try {
				// ��voת��Ϊpo
				MesOrder mesOrder = MesOrder.builder().orderId(orderid)
						.orderClientname(mesOrderVo.getOrderClientname())//
						.orderProductname(mesOrderVo.getOrderProductname()).orderContractid(mesOrderVo.getOrderContractid())//
						.orderImgid(mesOrderVo.getOrderImgid()).orderMaterialname(mesOrderVo.getOrderMaterialname())
						.orderCometime(MyStringUtils.string2Date(mesOrderVo.getComeTime(), null))//
						.orderCommittime(MyStringUtils.string2Date(mesOrderVo.getCommitTime(), null))
						.orderInventorystatus(mesOrderVo.getOrderInventorystatus()).orderStatus(mesOrderVo.getOrderStatus())//
						.orderMaterialsource(mesOrderVo.getOrderMaterialsource())
						.orderHurrystatus(mesOrderVo.getOrderHurrystatus()).orderStatus(mesOrderVo.getOrderStatus())
						.orderRemark(mesOrderVo.getOrderRemark()).build();

				// �����û��ĵ�¼��Ϣ
				// TODO
				if(mesOrder.getOrderStatus()==1) {
					planService.prePlan(mesOrder);
				}
				mesOrderBatchMapper.insertSelective(mesOrder);
				//
			} catch (Exception e) {
				throw new SysMineException("��������������");
			}
		}
	}

	// ��ȡ���ݿ����е�����
	public Long getOrderCount() {
		return mesOrderCustomerMapper.getOrderCount();
	}

	// ��ȡid����
	public List<String> createOrderIdsDefault(Long ocounts) {
		if (ig == null) {
			ig = new IdGenerator();
		}

		ig.setCurrentdbidscount(getOrderCount());
		List<String> list = ig.initIds(ocounts);
		ig.clear();
		return list;
	}

	// bean:�Զ�����࣬�������÷�Χ���
	// domain-javabean-pojo-po--���Ǳ��������java��
	// vo-param poVo xxParam page SearchOrderParam..
	// dto �����Զ���������ݲ㽻������ SearchOrderDto
	// SearchOrderParam--SearchOrderVo



	///////////////////////////////////////////////////////////////////////////////////////////////
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
				this.ids.add(getIdPre() + yearStr() + getIdAfter(i));
			}
			return this.ids;
		}

		//
		private String getIdAfter(int addcount) {
			// ϵͳĬ������5λ ZX1700001
			int goallength = 5;
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
			this.idpre = "ZX";
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

	public PageResult<MesOrder> searchPageList(SearchOrderParam param, PageQuery page) {
		// TODO Auto-generated method stub
		//У��page�Ƿ�Ϊ��
		BeanValidator.check(page);
		//��param���ֶδ���dto��������ݽ���
		SearchOrderDto dto= new SearchOrderDto();
		//�ж�param�е� �����Ƿ�Ϊ��
		if(StringUtils.isNotBlank(param.getKeyword())) {
			dto.setKeyword("%"+param.getKeyword()+"%");
		}
		if(StringUtils.isNotBlank(param.getSearch_status())) {
			dto.setSearch_status(Integer.parseInt(param.getSearch_status()));
		}
		try {
			//�������ڸ�ʽ
			SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
			if(StringUtils.isNotBlank(param.getFromTime())) {
				dto.setFromTime(dateFormat.parse(param.getFromTime()));
			}
			if(StringUtils.isNotBlank(param.getToTime())) {
				dto.setToTime(dateFormat.parse(param.getToTime()));
				
			}
		} catch (Exception e) {
			throw new ParamException("��������ڸ�ʽ�����⣬��ȷ�ĸ�ʽΪ��yyyy-MM-dd");
		}
		//��ö���������
		int count = mesOrderCustomerMapper.countBySearchDto(dto);
		if(count>0) {
			List<MesOrder> orderList = mesOrderCustomerMapper.getPageListBySearchDto(dto, page);
			return PageResult.<MesOrder>builder().total(count).data(orderList).build();
		}
		return PageResult.<MesOrder>builder().build();
	}
//�޸�
	public void update(MesOrderVo mesOrderVo) {
		
		//У��Ҫ�޸ĵ������Ƿ�Ϊ��
		BeanValidator.check(mesOrderVo);
		//�ƶ����ڸ�ʽ
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		//ͨ��id�õ�Ҫ�޸����� 
		MesOrder before= mesOrderMapper.selectByPrimaryKey(mesOrderVo.getId());
		//�жϲ����Ƿ�Ϊ��
		Preconditions.checkNotNull(before,"�����µĲ��ϲ�����");
		try {
			MesOrder after=MesOrder.builder().id(mesOrderVo.getId())
					.orderClientname(mesOrderVo.getOrderClientname())//
					.orderProductname(mesOrderVo.getOrderProductname()).orderContractid(mesOrderVo.getOrderContractid())//
					.orderImgid(mesOrderVo.getOrderImgid()).orderMaterialname(mesOrderVo.getOrderMaterialname())
					.orderCometime(MyStringUtils.string2Date(mesOrderVo.getComeTime(), null))//
					.orderCommittime(MyStringUtils.string2Date(mesOrderVo.getCommitTime(), null))
					.orderInventorystatus(mesOrderVo.getOrderInventorystatus()).orderStatus(mesOrderVo.getOrderStatus())//
					.orderMaterialsource(mesOrderVo.getOrderMaterialsource())
					.orderHurrystatus(mesOrderVo.getOrderHurrystatus()).orderStatus(mesOrderVo.getOrderStatus())
					.orderRemark(mesOrderVo.getOrderRemark()).build();
			//���ݿ��������
			mesOrderMapper.updateByPrimaryKeySelective(after);
			
		} catch (Exception e) {
			throw new SysMineException("���¹���������");
		}
	}
//������������
	public void batchStart(String ids) {
		if(null != ids && ids.length()>0) {
			//���������sqlSession����
			String[] idArray = ids.split("&");
			mesOrderCustomerMapper.batchStart(idArray);
			//����������ִ�мƻ�
			planService.startPlanByOrderIds(idArray);
		}
	}

}
