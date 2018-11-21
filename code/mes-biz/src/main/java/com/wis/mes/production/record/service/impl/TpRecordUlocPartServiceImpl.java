package com.wis.mes.production.record.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.production.record.dao.TpRecordUlocPartDao;
import com.wis.mes.production.record.entity.TpRecordUlocPart;
import com.wis.mes.production.record.service.TpRecordUlocPartService;

@Service
public class TpRecordUlocPartServiceImpl extends BaseServiceImpl<TpRecordUlocPart> implements TpRecordUlocPartService {

	@Autowired
	public void setDao(TpRecordUlocPartDao dao) {
		this.dao = dao;
	}

	@Override
	public List<TpRecordUlocPart> getUlocAlreadyBindPart(String SN, Integer ulocId, String routingSeq) {
		return ((TpRecordUlocPartDao) dao).getUlocAlreadyBindPart(SN, ulocId, routingSeq);
	}

	@Override
	public PageResult<TpRecordUlocPart> getPartPageResult(QueryCriteria criteria) {
		return ((TpRecordUlocPartDao) dao).getPartPageResult(criteria);
	}

	/**
	 * 新插入新 物料数据  
	 * 更新 旧物料的 
	 */
	@Override
	public void updateUntrated(TpRecordUlocPart newPart, String ordId) {
		String replaceBarCode = newPart.getReplaceBarCode();
		//String barCode = newPart.getBarCode();
		Integer placeQty = newPart.getPerlaceQty();
		//检查物料条码是否 符合规则   代码 还没写
		newPart.setBarCode(replaceBarCode);
		newPart.setReplaceBarCode(null);
		newPart.setQty(placeQty);
		newPart.setPerlaceQty(null);
		newPart.setIsReplace("NO"); 
		dao.save(newPart);
		TpRecordUlocPart oldPart = new TpRecordUlocPart();
		oldPart.setId(Integer.parseInt(ordId)); 
		oldPart = dao.findUniqueByEg(oldPart);
		oldPart.setIsReplace("YES");
		oldPart.setPerlaceQty(placeQty);
		oldPart.setReplaceBarCode(replaceBarCode);
		dao.saveOrUpdate(oldPart);
	}

	@Override
	public TpRecordUlocPart doSaveTpRecordUlocPart(Integer tpRecordId, Integer tpRecordUlocId, Integer tmPartId,
			Integer qty, String barCode, String partNo, String partName, String isReplace, String note) {
		TpRecordUlocPart bean = new TpRecordUlocPart();
		bean.setBarCode(barCode);
		bean.setIsReplace(isReplace);
		bean.setNote(note);
		bean.setPartName(partName);
		bean.setPartNo(partNo);
		bean.setQty(qty);
		bean.setTmPartId(tmPartId);
		bean.setTpRecordId(tpRecordId);
		bean.setTpRecordUlocId(tpRecordUlocId);
		return doSave(bean);
	}
}
