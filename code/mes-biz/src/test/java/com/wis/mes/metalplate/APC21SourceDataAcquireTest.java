package com.wis.mes.metalplate;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wis.mes.basis.BizBaseTestCase;
import com.wis.mes.production.metalplate.service.TbMetalPlateSchedulService;
import com.wis.mes.production.metalplate.service.TbMetalPlateSourceDataService;

public class APC21SourceDataAcquireTest extends BizBaseTestCase{
	
	@Autowired
	private TbMetalPlateSourceDataService sourceDataService;
	@Autowired
	private TbMetalPlateSchedulService schedulService;
	
	@Test
	public void metalPlateSourceDataTest() {
		schedulService.mpregionMarkMonitor();
	}
	@Test
	public void test() {
		sourceDataService.apc21SourceDataAcquire();
	}
	@Test
	public void pmcBoardProductionInfo() {
		schedulService.pmcBoardProductionInfo();
	}

}
