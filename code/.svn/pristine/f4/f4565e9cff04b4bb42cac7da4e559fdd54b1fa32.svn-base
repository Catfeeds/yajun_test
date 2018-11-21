package com.wis.mes.uloc;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wis.mes.basis.BizBaseTestCase;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.uloc.entity.TmUlocAbnormal;
import com.wis.mes.master.uloc.service.TmUlocAbnormalService;
import com.wis.mes.master.uloc.service.TmUlocService;
import com.wis.mes.rfid.service.RfidService;

public class TestUlocAbnormal extends BizBaseTestCase {

	@Autowired
	private TmUlocAbnormalService service;
	@Autowired
	private TmUlocService ulocService;
	@Autowired
	private RfidService rfidService;

	@Test
	public void updateData() {
		List<TmUlocAbnormal> findAll = service.findAll();
		for (TmUlocAbnormal bean : findAll) {
			TmUloc uloc = new TmUloc();
			uloc.setTmLineId(162);
			uloc.setNo(bean.getUlocNo());
			TmUloc u = ulocService.findUniqueByEg(uloc);
			if (u != null) {
				bean.setTmUlocId(u.getId());
				service.doUpdate(bean);
			}
		}
	}
	
	@Test
	public void plcRfidRead() {
		rfidService.plcRfidRead("63");
	}

}
