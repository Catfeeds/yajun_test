package com.wis.mes.opc;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wis.mes.basis.BizBaseTestCase;
import com.wis.mes.job.ClearDatalogUlocAndDeviceData;

public class ClearTempTableTest extends BizBaseTestCase {

	@Autowired
	private ClearDatalogUlocAndDeviceData service;

	@Test
	public void testClearTempTable() {
		service.execue();
	}
}	
