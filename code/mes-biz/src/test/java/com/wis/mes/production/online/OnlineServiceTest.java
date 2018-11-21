package com.wis.mes.production.online;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wis.mes.basis.BizBaseTestCase;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.production.plan.porder.entity.ToPorderAviBom;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPathParameter;
import com.wis.mes.production.regulate.service.OnlineService;

public class OnlineServiceTest extends BizBaseTestCase {

	@Autowired
	private OnlineService onlineService;

	@Test
	public void testGetUloc() {
		List<TmUloc> onlineUlocByLineId = onlineService.getOnlineUlocByLineId(78);
		System.out.println(onlineUlocByLineId);
	}

	@Test
	public void testGetBindParameterByAviId() {
		List<ToPorderAviPathParameter> bindParameterByAviId = onlineService.getAviPathParameter(1059, 749, "1");
		System.out.println(bindParameterByAviId);
	}

	@Test
	public void testGetBindBom() {
		List<ToPorderAviBom> bindPartByAviId = onlineService.getAviBom(1059, 749, "1");
		System.out.println(bindPartByAviId);
	}
}
