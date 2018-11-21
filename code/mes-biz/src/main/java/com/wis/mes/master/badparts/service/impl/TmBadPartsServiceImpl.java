package com.wis.mes.master.badparts.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.ActionUtils;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.badparts.dao.TmBadPartsDao;
import com.wis.mes.master.badparts.entity.TmBadParts;
import com.wis.mes.master.badparts.service.TmBadPartsService;

@Service
public class TmBadPartsServiceImpl extends BaseServiceImpl<TmBadParts> implements TmBadPartsService{

	@Autowired
	public void setDao(final TmBadPartsDao dao) {
		this.dao = dao;
	}

	@Override
	public List<DictEntry> queryDictItem(QueryCriteria criteria) {
		ActionUtils.addUserDataPermission(criteria, "tmLineId");
		criteria.setQueryPage(false);
		List<TmBadParts> beans = findBy(criteria).getContent();
		final List<DictEntry> dictList = new ArrayList<DictEntry>();
		for (final TmBadParts e : beans) {
			final DictEntry dict = new DictEntry();
			dict.setCode(e.getId().toString());
			dict.setName(e.getNo() + "-" + e.getName());
			dictList.add(dict);
		}
		return dictList;
	}
	
}
