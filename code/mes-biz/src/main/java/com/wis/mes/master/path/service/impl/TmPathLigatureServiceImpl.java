package com.wis.mes.master.path.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.path.dao.TmPathLigatureDao;
import com.wis.mes.master.path.entity.TmPathLigature;
import com.wis.mes.master.path.service.TmPathLigatureService;

@Service
public class TmPathLigatureServiceImpl extends BaseServiceImpl<TmPathLigature> implements TmPathLigatureService {

	@Autowired
	public void setDao(TmPathLigatureDao dao) {
		this.dao = dao;
	}

	@Override
	public void doDeleteAllPathsByTmPathId(Integer tmPathId) {
		((TmPathLigatureDao) dao).deleteAllPathsByTmPathId(tmPathId);
	}

}
