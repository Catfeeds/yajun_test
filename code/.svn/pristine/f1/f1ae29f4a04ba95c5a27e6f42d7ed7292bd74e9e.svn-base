package com.wis.mes.sequence;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wis.mes.basis.BizBaseTestCase;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.plant.service.TmPlantService;

public class SequenceTest extends BizBaseTestCase {
	@Autowired
	private TmPlantService service;

	@Test
	public void testService() {
		List<TmPlant> findAll = service.findAll();
		for (TmPlant tsSequence : findAll) {
			System.out.println(tsSequence);
		}
	}
}
