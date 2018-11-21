package com.wis.mes.production.record.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.production.record.dao.TpRecordUlocParameterDao;
import com.wis.mes.production.record.entity.TpRecord;
import com.wis.mes.production.record.entity.TpRecordUloc;
import com.wis.mes.production.record.entity.TpRecordUlocParameter;
import com.wis.mes.production.record.service.TpRecordService;
import com.wis.mes.production.record.service.TpRecordUlocParameterService;
import com.wis.mes.production.record.service.TpRecordUlocService;

@Service
public class TpRecordUlocParameterServiceImpl extends BaseServiceImpl<TpRecordUlocParameter>
		implements TpRecordUlocParameterService {
	@Autowired
	private TpRecordService tpRecordService;
	@Autowired
	private TpRecordUlocService tpRecordUlocService;
	
	@Autowired
	public void setDao(TpRecordUlocParameterDao dao) {
		this.dao = dao;
	}

	@Override
	public List<TpRecordUlocParameter> getUlocAlreadyBindParameter(String SN, Integer ulocId, String routingSeq) {
		return ((TpRecordUlocParameterDao) dao).getUlocAlreadyBindParameter(SN, ulocId, routingSeq);
	}

	@Override
	public PageResult<TpRecordUlocParameter> getParameterPageResult(BootstrapTableQueryCriteria criteria) {
		return ((TpRecordUlocParameterDao) dao).getParameterPageResult(criteria);
	}

	@Override
	public void doSaveRecordUlocParameter(String parameterCode[], String parameterType, String parameterValue[],
			Integer tpRecordUlocId, String[] note, Integer tpRecordId, String isReplace) {
		if (parameterCode == null || parameterCode.length == 0) {
			throw new BusinessException("BIND_PARAMETER_CODE_NULL_ERROR");
		}
		List<TpRecordUlocParameter> list = new ArrayList<TpRecordUlocParameter>();
		if (parameterValue == null || parameterValue.length == 0) {
			parameterValue = new String[parameterCode.length];
		}
		if (note == null || note.length == 0) {
			note = new String[parameterCode.length];
		}
		for (int i = 0; i < parameterCode.length; i++) {
			TpRecordUlocParameter bean = new TpRecordUlocParameter();
			bean.setParameterCode(parameterCode[i]);
			bean.setParameterType(parameterType);
			bean.setParameterValue(parameterValue[i]);
			bean.setTpRecordUlocId(tpRecordUlocId);
			bean.setTpRecordId(tpRecordId);
			bean.setNote(note[i]);
			bean.setIsReplace(isReplace);
			list.add(bean);
		}
		doSaveBatch(list);
	}

	/**
	 *  替换更新
	 */
	@Override
	public void updateUntrated(TpRecordUlocParameter newParam, String ordId) {
		newParam.setIsReplace("NO"); 
		dao.save(newParam);
		TpRecordUlocParameter oldParam = new TpRecordUlocParameter();
		oldParam.setId(Integer.parseInt(ordId));
		oldParam = dao.findUniqueByEg(oldParam);
		oldParam.setIsReplace("YES");
		dao.saveOrUpdate(oldParam) 	;
	}

	@Override
	public TpRecordUlocParameter findRelObjById(Integer id) {
		TpRecordUlocParameter param = new TpRecordUlocParameter();
		param.setId(id);
		param = dao.findUniqueByEg(param);
		if(param.getTpRecordId()!=null){
			TpRecord tpRecord = tpRecordService.findById(param.getTpRecordId());
			param.setTpRecord(tpRecord);
		}
		if(param.getTpRecordUlocId()!=null){
			TpRecordUloc tpRecordUloc = tpRecordUlocService.findById(param.getTpRecordUlocId());
			param.setTpRecordUloc(tpRecordUloc);
		}
		return param;
	}
}
