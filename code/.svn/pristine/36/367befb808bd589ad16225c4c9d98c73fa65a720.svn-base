package com.wis.mes.production.TbProductStation;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.mes.basis.BizBaseTestCase;
import com.wis.mes.dakin.production.tracing.entity.VbProductOperation;
import com.wis.mes.dakin.production.tracing.service.VbProductOperationService;
import com.wis.mes.production.producttracing.service.TbProductStationService;
import com.wis.mes.production.producttracing.vo.ProductStationVo;

public class TbProductStationServiceTest extends BizBaseTestCase {

	@Autowired
	private TbProductStationService service;
	@Autowired
	private VbProductOperationService vbProductOperationService;
	

	@Test
	public void getProductStationVoTest() {
		ProductStationVo vo = service.getProductStationVo("7", "1234567890123456789012");
		System.out.println(vo);
	}
	
	
	@Test
	public void exportQuery(){
		BootstrapTableQueryCriteria criteriaNew = new BootstrapTableQueryCriteria();
		criteriaNew.setPage(0);
		criteriaNew.setRows(100);
		PageResult<VbProductOperation> findBy = vbProductOperationService.findBy(criteriaNew);
		System.out.println(findBy.getContent());
		criteriaNew = new BootstrapTableQueryCriteria();
		criteriaNew.setPage(1);
		criteriaNew.setRows(100);
		PageResult<VbProductOperation> findBy1 = vbProductOperationService.findBy(criteriaNew);
		System.out.println(findBy1.getContent());
	}
}
