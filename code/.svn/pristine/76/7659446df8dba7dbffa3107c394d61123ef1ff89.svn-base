package com.wis.mes.production.plan.porder.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.BaseExcelUtil;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.configuration.entity.TsParameter;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.path.entity.TmPathUlocParameter;
import com.wis.mes.master.path.service.TmPathUlocParameterService;
import com.wis.mes.production.plan.porder.dao.ToPorderAviPathParameterDao;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPathParameter;
import com.wis.mes.production.plan.porder.service.ToPorderAviPathParameterService;

@Service
public class ToPorderAviPathParameterServiceImpl extends BaseServiceImpl<ToPorderAviPathParameter>
		implements ToPorderAviPathParameterService {

	@Autowired
	public void setDao(ToPorderAviPathParameterDao dao) {
		this.dao = dao;
	}

	@Autowired
	private TmPathUlocParameterService pathUlocParameterService;
	@Autowired
	private DictEntryService entryService;

	@Override
	public void doSaveAviPathParameter(Integer toPorderAviPathId, Integer tmPathUlocId) {
		List<ToPorderAviPathParameter> aviPathParameterAddList = new ArrayList<ToPorderAviPathParameter>();
		List<TmPathUlocParameter> tmPathUlocParameters = pathUlocParameterService.getTmPathUlocParameters(tmPathUlocId);
		for (TmPathUlocParameter parameter : tmPathUlocParameters) {
			ToPorderAviPathParameter bean = new ToPorderAviPathParameter();
			bean.setToPorderAviPathId(toPorderAviPathId);
			bean.setEnabled(parameter.getEnabled());//是否启用
			bean.setIsRequired(parameter.getIsRequired());//是否必填
			bean.setNote(parameter.getNote());//备注
			bean.setTsParameterId(parameter.getTmEquipmentParamId());//参数ID
			aviPathParameterAddList.add(bean);
		}
		doSaveBatch(aviPathParameterAddList);
	}

	@Override
	public PageResult<TsParameter> getParameterPageResult(QueryCriteria queryCriteria) {
		return ((ToPorderAviPathParameterDao) dao).getParameterPageResult(queryCriteria);
	}

	@Override
	public List<ToPorderAviPathParameter> doSaveAviPathParameter(Integer toPorderAviPathId, String[] tsParameterIds,
			String[] notes, String[] isRequeired, String[] enabled) {
		Map<String, ToPorderAviPathParameter> parameterIdMap = getParameterIdMap(toPorderAviPathId);
		//新增
		List<ToPorderAviPathParameter> parameterAddList = new ArrayList<ToPorderAviPathParameter>();
		//修改
		List<ToPorderAviPathParameter> parameterUpdateList = new ArrayList<ToPorderAviPathParameter>();
		for (int i = 0; i < tsParameterIds.length; i++) {
			String tsParameterId = tsParameterIds[i];
			if (parameterIdMap.containsKey(tsParameterId)) {//如果已经存在，则修改
				ToPorderAviPathParameter parameter = parameterIdMap.get(tsParameterId);
				parameter.setNote(getNote(notes, i));
				parameter.setEnabled(enabled[i]);
				parameter.setIsRequired(isRequeired[i]);
				parameter.setToPorderAviPathId(toPorderAviPathId);
				parameterUpdateList.add(parameter);
			} else {
				ToPorderAviPathParameter parameter = new ToPorderAviPathParameter();
				parameter.setToPorderAviPathId(toPorderAviPathId);
				parameter.setNote(getNote(notes, i));
				parameter.setEnabled(enabled[i]);
				parameter.setIsRequired(isRequeired[i]);
				parameter.setTsParameterId(Integer.valueOf(tsParameterId));
				parameterAddList.add(parameter);
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

	private Map<String, ToPorderAviPathParameter> getParameterIdMap(Integer toPorderAviPathId) {
		Map<String, ToPorderAviPathParameter> map = new HashMap<String, ToPorderAviPathParameter>();
		ToPorderAviPathParameter eg = new ToPorderAviPathParameter();
		eg.setToPorderAviPathId(toPorderAviPathId);
		for (ToPorderAviPathParameter parameter : findByEg(eg)) {
			map.put(parameter.getTsParameterId().toString(), parameter);
		}
		return map;
	}

	@Override
	public void exportExcelData(HttpServletResponse response, List<ToPorderAviPathParameter> content, String filePath,
			String[] header) {
		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		// YES/NO
		Map<String, DictEntry> yesOrNo = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO);
		//状态
		Map<String, DictEntry> enabled = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_ENABLED);
		for (ToPorderAviPathParameter bean : content) {
			Map<String, Object> map = new HashMap<String, Object>();
			TsParameter parameter = bean.getParameter();
			map.put("参数编号", parameter.getCode());
			map.put("参数名称", getString(parameter.getName()));
			map.put("参数变量", getString(parameter.getVariuableName()));
			map.put("参数默认值", getString(parameter.getDefaultValue()));
			map.put("参数表达式", getString(parameter.getRegularExpression()));
			map.put("参数说明", getString(parameter.getNote()));
			map.put("是否必填", yesOrNo.get(bean.getIsRequired()).getName());
			map.put("启用", enabled.get(bean.getEnabled()).getName());
			map.put("备注", getString(bean.getNote()));
			exportDataList.add(map);
		}
		BaseExcelUtil.exportData(response, exportDataList, filePath, header);
	}

	private String getString(String str) {
		return str == null ? "" : str;
	}
}
