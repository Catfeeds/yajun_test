package com.wis.mes.production.plan.porder.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.BaseExcelUtil;
import com.wis.basis.configuration.entity.TsParameter;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.path.entity.TmPathUlocSip;
import com.wis.mes.master.path.service.TmPathUlocSipService;
import com.wis.mes.production.plan.porder.dao.ToPorderAviPathSipDao;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPathSip;
import com.wis.mes.production.plan.porder.service.ToPorderAviPathSipService;

@Service
public class ToPorderAviPathSipServiceImpl extends BaseServiceImpl<ToPorderAviPathSip>
		implements ToPorderAviPathSipService {

	@Autowired
	public void setDao(ToPorderAviPathSipDao dao) {
		this.dao = dao;
	}

	@Autowired
	private TmPathUlocSipService pathUlocSipService;

	@Override
	public void doSaveAviPathSip(Integer toPorderAviPathId, Integer tmPathUlocId) {
		List<ToPorderAviPathSip> aviPathSipAddList = new ArrayList<ToPorderAviPathSip>();
		List<TmPathUlocSip> pathUlocSips = pathUlocSipService.getPathUloSips(tmPathUlocId);
		for (TmPathUlocSip sip : pathUlocSips) {
			ToPorderAviPathSip bean = new ToPorderAviPathSip();
			bean.setToPorderAviPathId(toPorderAviPathId);
			bean.setNote(sip.getNote());//备注
			bean.setTsParameterId(sip.getTsParameterId());//参数ID
			aviPathSipAddList.add(bean);
		}
		doSaveBatch(aviPathSipAddList);
	}

	@Override
	public PageResult<TsParameter> getEquipmentAndUlocParameter(QueryCriteria criteria) {
		return ((ToPorderAviPathSipDao) dao).getEquipmentAndUlocParameter(criteria);
	}

	@Override
	public List<TsParameter> getSipParameterExamineAndNotInSip(Integer toPorderAviPathId) {
		return ((ToPorderAviPathSipDao) dao).getSipParameterExamineAndNotInSip(toPorderAviPathId);
	}

	@Override
	public List<TsParameter> getSipParameterExamine(Integer toPorderAviPathId) {
		return ((ToPorderAviPathSipDao) dao).getSipParameterExamine(toPorderAviPathId);
	}

	@Override
	public List<ToPorderAviPathSip> doSavePathUlocSip(String[] tsParameterIds, String[] notes,
			Integer toPorderAviPathId) {
		Map<String, ToPorderAviPathSip> parameterIdMap = getParameterIdMap(toPorderAviPathId);
		//新增
		List<ToPorderAviPathSip> parameterAddList = new ArrayList<ToPorderAviPathSip>();
		//修改
		List<ToPorderAviPathSip> parameterUpdateList = new ArrayList<ToPorderAviPathSip>();
		for (int i = 0; i < tsParameterIds.length; i++) {
			String tsParameterId = tsParameterIds[i];
			if (parameterIdMap.containsKey(tsParameterId)) {//如果已经存在，则修改
				ToPorderAviPathSip sip = parameterIdMap.get(tsParameterId);
				sip.setToPorderAviPathId(toPorderAviPathId);
				sip.setNote(getNote(notes, i));
				sip.setTsParameterId(Integer.valueOf(tsParameterId));
				parameterUpdateList.add(sip);
			} else {
				ToPorderAviPathSip sip = new ToPorderAviPathSip();
				sip.setToPorderAviPathId(toPorderAviPathId);
				sip.setNote(getNote(notes, i));
				sip.setTsParameterId(Integer.valueOf(tsParameterId));
				parameterAddList.add(sip);
			}
		}
		doSaveBatch(parameterAddList);
		if (parameterUpdateList.size() > 0) {
			doUpdateBatch(parameterUpdateList);
		}
		return parameterAddList;
	}

	private String getNote(String[] notes, int index) {
		if (notes.length == 0) {
			return "";
		} else {
			return notes[index];
		}
	}

	private Map<String, ToPorderAviPathSip> getParameterIdMap(Integer toPorderAviPathId) {
		Map<String, ToPorderAviPathSip> map = new HashMap<String, ToPorderAviPathSip>();
		ToPorderAviPathSip eg = new ToPorderAviPathSip();
		eg.setToPorderAviPathId(toPorderAviPathId);
		for (ToPorderAviPathSip parameter : findByEg(eg)) {
			map.put(parameter.getTsParameterId().toString(), parameter);
		}
		return map;
	}

	@Override
	public void exportExcelData(HttpServletResponse response, List<ToPorderAviPathSip> content, String filePath,
			String[] header) {
		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		for (ToPorderAviPathSip bean : content) {
			Map<String, Object> map = new HashMap<String, Object>();
			TsParameter parameter = bean.getParameter();
			map.put("参数编号", parameter.getCode());
			map.put("参数名称", parameter.getName());
			map.put("参数变量", parameter.getVariuableName());
			map.put("参数默认值", getString(parameter.getDefaultValue()));
			map.put("参数表达式", parameter.getRegularExpression());
			map.put("参数说明", getString(parameter.getNote()));
			map.put("备注", getString(bean.getNote()));
			exportDataList.add(map);
		}
		BaseExcelUtil.exportData(response, exportDataList, filePath, header);
	}

	private String getString(Object obj) {
		return obj == null ? "" : obj.toString();
	}
}
