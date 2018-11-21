package com.wis.mes.production.record.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.production.record.dao.TpRecordUlocQualityDao;
import com.wis.mes.production.record.entity.TpRecordUlocQuality;
import com.wis.mes.production.record.service.TpRecordUlocQualityService;

@Service
public class TpRecordUlocQualityServiceImpl extends BaseServiceImpl<TpRecordUlocQuality>
		implements TpRecordUlocQualityService {

	@Autowired
	public void setDao(TpRecordUlocQualityDao dao) {
		this.dao = dao;
	}

	@Override
	public List<TpRecordUlocQuality> getUlocAlreadyBindQuality(String SN, Integer ulocId, String routingSeq) {
		return ((TpRecordUlocQualityDao) dao).getUlocAlreadyBindQuality(SN, ulocId, routingSeq);
	}

	@Override
	public PageResult<TpRecordUlocQuality> getQualityPageResult(BootstrapTableQueryCriteria criteria) {
		return ((TpRecordUlocQualityDao) dao).getQualityPageResult(criteria);
	}

	@Override
	public void doSaveTpRecordUlocQuality(Integer tpRecordUlocId, String[] checkItem, String[] checkResult,
			String[] note, Integer tpRecordId) {
		if (checkItem == null || checkItem.length == 0) {
			throw new BusinessException("");
		}
		if (checkResult == null || checkResult.length == 0) {
			throw new BusinessException("");
		}
		if (note == null || note.length == 0) {
			note = new String[checkItem.length];
		}
		List<TpRecordUlocQuality> list = new ArrayList<TpRecordUlocQuality>();
		for (int i = 0; i < checkItem.length; i++) {
			TpRecordUlocQuality bean = new TpRecordUlocQuality();
			bean.setTpRecordUlocId(tpRecordUlocId);
			bean.setCheckItem(checkItem[i]);
			bean.setCheckResult(checkResult[i]);
			bean.setNote(note[i]);
			bean.setTpRecordId(tpRecordId);
			list.add(bean);
		}
		doSaveBatch(list);
	}
}
