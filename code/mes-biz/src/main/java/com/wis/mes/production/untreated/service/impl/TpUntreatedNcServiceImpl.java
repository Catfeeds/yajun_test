package com.wis.mes.production.untreated.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.ConstantUtils;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.production.untreated.dao.TpUntreatedNcDao;
import com.wis.mes.production.untreated.entity.TpUntreatedNc;
import com.wis.mes.production.untreated.service.TpUntreatedNcService;

@Service
public class TpUntreatedNcServiceImpl extends BaseServiceImpl<TpUntreatedNc> implements TpUntreatedNcService {
	@Autowired
	private TpUntreatedNcDao tpUntreatedNcDao;

	@Autowired
	public void setDao(TpUntreatedNcDao dao) {
		this.dao = dao;
	}

	@Override
	public void doSaveUnTratedNc(String sn, Integer tmNcGroupId, Integer tmNcId, String ncType, String note,
			Integer tmUlocId, String rountingSeq) {
		TpUntreatedNc bean = new TpUntreatedNc();
		bean.setTmNcGroupId(tmNcGroupId);
		bean.setTmNcId(tmNcId);
		bean.setSn(sn);
		bean.setNote(note);
		bean.setNcType(ncType);
		bean.setTmUlocId(tmUlocId);
		bean.setRountingSeq(rountingSeq);
		doSave(bean);
	}

	@Override
	public void doSaveUnTratedNc(String sn, Integer[] tmNcGroupId, Integer[] tmNcId, String[] ncType, String[] note,
			Integer tmUlocId, String rountingSeq) {
		if (tmNcGroupId == null || tmNcGroupId.length == 0) {
			throw new BusinessException("PRODUCTION_ONLINE_GROUP_MUST");
		}
		if (tmNcId == null || tmNcId.length == 0) {
			throw new BusinessException("PRODUCTION_ONLINE_UNTREATEDNC_HAVE_NO_NC");
		}
		if (tmNcGroupId.length != tmNcId.length) {
			throw new BusinessException("PRODUCTION_ONLINE_UNTREATEDNC_HAVE_NO_NC");//有不合格代码没有选择，
		}
		if (note == null || note.length == 0) {
			note = new String[tmNcId.length];
		}
		List<TpUntreatedNc> ncList = new ArrayList<TpUntreatedNc>();
		for (int i = 0; i < tmNcId.length; i++) {
			if (tmNcGroupId[i] == null) {
				continue;
			}
			TpUntreatedNc bean = new TpUntreatedNc();
			bean.setTmUlocId(tmUlocId);
			bean.setRountingSeq(rountingSeq);
			bean.setTmNcGroupId(tmNcGroupId[i]);
			if (tmNcId[i] == null) {
				throw new BusinessException("PRODUCTION_ONLINE_UNTREATEDNC_HAVE_NO_NC");
			}
			bean.setTmNcId(tmNcId[i]);
			bean.setSn(sn);
			bean.setNote(note[i]);
			bean.setNcType(ncType[i]);
			ncList.add(bean);
		}
		doSaveBatch(ncList);
	}

	/**
	 * 2) TP_WIP 将状态为 “WAIT_REPAIR” 修改成 “PORDUCTION” 查询条件 SN TM_ULOC_ID
	 * ROUTING_SEQ
	 */
	@Override
	public void changeWipStatus(List<TpUntreatedNc> untreatedList) {
		tpUntreatedNcDao.changeWipStatus(untreatedList, ConstantUtils.PRODUCT_WIP_STATUS_WAIT_REPAIR,
				ConstantUtils.PRODUCT_WIP_STATUS_PRODUCT);
	}
}
