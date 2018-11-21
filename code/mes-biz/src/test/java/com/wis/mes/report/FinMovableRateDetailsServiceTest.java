package com.wis.mes.report;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wis.mes.basis.BizBaseTestCase;
import com.wis.mes.dakin.report.service.FinMovableRateDetailsService;

public class FinMovableRateDetailsServiceTest extends BizBaseTestCase {
	@Autowired
	private FinMovableRateDetailsService service;

	@Test
	public void getMovableRateAndCalculate() {
		//service.getMovableRateAndCalculate("2018-06-21 00:00:00", "2018-06-26 10:30:00", 5);
	}
}
