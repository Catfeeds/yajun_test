package com.wis.mes.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wis.mes.basis.BizBaseTestCase;
import com.wis.mes.dakin.report.dao.ReportDao;
import com.wis.mes.dakin.report.service.MetalPlateFaultedDetailReportService;
import com.wis.mes.dakin.report.service.ReportService;
import com.wis.mes.production.metalplate.service.TbMetalPlateProductionRecordService;

public class ReportTest extends BizBaseTestCase {
	@Autowired
	private ReportService service;
	@Autowired
	private ReportDao dao;
	@Autowired
	private TbMetalPlateProductionRecordService metalPlateProductionRecordServiceImpl;
	@Autowired
	private MetalPlateFaultedDetailReportService metalPlateFaultedDetailReportService;

	@Test
	public void ngAndShiftNoRealationReportTest() {
		Map<String, String> ngAndShiftNoRealationReport = service.ngAndShiftNoRealationReport("2018-04-07", null);
		System.out.println(ngAndShiftNoRealationReport);
	}

	@Test
	public void energyTest() {
		List<Map<String, Object>> energReport = dao.energReport("yyyy-MM-dd","2018-05-01","2018-06-01");
		System.out.println(energReport);
	}
	
	@Test
	public void productionStatusData() {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("type", "yearBar");
		map.put("createTime", "2018-11");
		metalPlateFaultedDetailReportService.productionStateYearData(map);
		System.out.println();
	}
	@Test
	public void noFaultDetailsReportData() {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("createTimeStart", "2018-10-14 13:45");
		map.put("createTimeEnd", "2018-11-14 13:45");
		metalPlateFaultedDetailReportService.noFaultDetailsReportData(map);
		System.out.println();
	}
	@Test
	public void noHaltReportData() {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("createTimeStart", "2018-10-14 13:45");
		map.put("createTimeEnd", "2018-11-14 13:45");
		metalPlateFaultedDetailReportService.noHaltReportData(map);
		System.out.println();
	}
}
