package com.wis.mes.entry;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.DictEntryService;
import com.wis.mes.basis.BizBaseTestCase;

public class DictEntryTest extends BizBaseTestCase {

	@Autowired
	private DictEntryService entryService;

	@Test
	public void updateEntry() {
		DictEntry entry = new DictEntry();
		entry.setTypeId(242);
		List<DictEntry> findByEg = entryService.findByEg(entry);
		for (DictEntry bean : findByEg) {
			bean.setCode(String.valueOf(Integer.valueOf(bean.getCode())));
			entryService.doUpdate(bean);
		}
	}
}
