package com.wis.mes.opc;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wis.mes.basis.BizBaseTestCase;
import com.wis.mes.job.PlcWorkTimeJobFin;

public class PlcWorkTimeJobFinTest extends BizBaseTestCase {
	@Autowired
	private PlcWorkTimeJobFin service;

	@Test
	public void testExecue() {
		service.execue();
	}
}
