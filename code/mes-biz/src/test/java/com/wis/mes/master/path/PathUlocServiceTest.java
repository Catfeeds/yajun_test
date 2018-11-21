package com.wis.mes.master.path;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wis.mes.basis.BizBaseTestCase;
import com.wis.mes.master.path.service.TmPathUlocService;

public class PathUlocServiceTest extends BizBaseTestCase {

	@Autowired
	private TmPathUlocService pathUlocService;

	@Test
	public void testEcharsData() {
		pathUlocService.getEchartsData("8");
	}

	@Test
	public void testQueryPathUloc() {
		List<Map<String, Object>> queryPathUloc = pathUlocService.queryPathUloc(8);
		System.out.println(queryPathUloc);
	}
}
