package com.wis.mes.master.path.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.BaseExcelUtil;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.DateUtils;
import com.wis.basis.configuration.entity.TsParameter;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.core.utils.SerialNumUtil;
import com.wis.mes.master.path.dao.TmPathUlocParameterDao;
import com.wis.mes.master.path.entity.TmPathUlocParameter;
import com.wis.mes.master.path.service.TmPathUlocParameterService;

@Service
public class TmPathUlocParameterServiceImpl extends BaseServiceImpl<TmPathUlocParameter>
		implements TmPathUlocParameterService {

	@Autowired
	public void setDao(TmPathUlocParameterDao dao) {
		this.dao = dao;
	}


	@Override
	public PageResult<TsParameter> getParameterPageResult(QueryCriteria queryCriteria) {
		return ((TmPathUlocParameterDao) dao).getParameterPageResult(queryCriteria);
	}

	/**
	 * key 参数id,value TmPathUlocParameter
	 * 
	 * @param tmPathUlocId
	 * @return
	 */
	private Map<String, TmPathUlocParameter> getParameterIdMap(Integer tmPathUlocId) {
		Map<String, TmPathUlocParameter> map = new HashMap<String, TmPathUlocParameter>();
		QueryCriteria criteria = new QueryCriteria();
		criteria.setQueryPage(false);
		criteria.getQueryCondition().put("tmPathUlocId", tmPathUlocId.toString());
		List<TmPathUlocParameter> content = findBy(criteria).getContent();
		for (TmPathUlocParameter parameter : content) {
			map.put(parameter.getTmEquipmentParamId().toString(), parameter);
		}
		return map;
	}

	@Override
	public List<TmPathUlocParameter> doSavePathUlocParameter(Integer tmPathUlocId, String[] tsParameterIds,
			String[] notes, String[] isRequeired, String[] enabled) {
		Map<String, TmPathUlocParameter> parameterIdMap = getParameterIdMap(tmPathUlocId);
		//新增
		List<TmPathUlocParameter> parameterAddList = new ArrayList<TmPathUlocParameter>();
		//修改
		List<TmPathUlocParameter> parameterUpdateList = new ArrayList<TmPathUlocParameter>();
		for (int i = 0; i < tsParameterIds.length; i++) {
			String tsParameterId = tsParameterIds[i];
			if (parameterIdMap.containsKey(tsParameterId)) {//如果已经存在，则修改
				TmPathUlocParameter parameter = parameterIdMap.get(tsParameterId);
				parameter.setNote(getNote(notes, i));
				parameter.setEnabled(enabled[i]);
				parameter.setIsRequired(isRequeired[i]);
				parameter.setTmPathUlocId(tmPathUlocId);
				parameterUpdateList.add(parameter);
			} else {
				TmPathUlocParameter parameter = new TmPathUlocParameter();
				parameter.setTmPathUlocId(tmPathUlocId);
				parameter.setNote(getNote(notes, i));
				parameter.setEnabled(enabled[i]);
				parameter.setIsRequired(isRequeired[i]);
				parameter.setTmEquipmentParamId(Integer.valueOf(tsParameterId));
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

	@Override
	public void exportExcelData(HttpServletResponse response, List<TmPathUlocParameter> content, String filePath,
			String[] header) {
		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		for (@SuppressWarnings("unused") TmPathUlocParameter bean : content) {
//			TsParameter parameter = bean.getParameter();
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("参数编号", parameter.getCode());
//			map.put("参数名称", parameter.getName());
//			map.put("参数变量", parameter.getVariuableName());
//			map.put("参数默认值", parameter.getDefaultValue());
//			map.put("参数说明", parameter.getNote() == null ? "" : parameter.getNote());
//			map.put("是否必填", yesOrNo.get(bean.getIsRequired()).getName());
//			map.put("启用", enabled.get(bean.getEnabled()).getName());
//			map.put("备注", bean.getNote() == null ? "" : bean.getNote());
//			exportDataList.add(map);
		}
		BaseExcelUtil.exportData(response, exportDataList, filePath, header);

	}

	@Override
	public List<TmPathUlocParameter> getTmPathUlocParameters(Integer tmPathUlocId) {
		TmPathUlocParameter eg = new TmPathUlocParameter();
		eg.setTmPathUlocId(tmPathUlocId);
		return findByEg(eg);
	}

	@Override
	public List<TmPathUlocParameter> getPathUlocParameterData(Integer tmPathUlocId, String[] tsParameterIds,
			String[] notes, String[] isRequeired, String[] enabled, String rectSeq) {
		List<TmPathUlocParameter> list = new ArrayList<TmPathUlocParameter>();
		for (int i = 0; i < tsParameterIds.length; i++) {
			int serialId = SerialNumUtil.getInstance().nextInt(ConstantUtils.SERIAL_PATH_ULOC_PARAMETER_ID);
			String tsParameterId = tsParameterIds[i];
			TmPathUlocParameter parameter = new TmPathUlocParameter();
			parameter.setTmPathUlocId(tmPathUlocId);
			parameter.setNote(getNote(notes, i));
			parameter.setEnabled(enabled[i]);
			parameter.setIsRequired(isRequeired[i]);
			parameter.setTmEquipmentParamId(Integer.valueOf(tsParameterId));
			parameter.setRectSeq(rectSeq);
			parameter.setId(serialId);
			//parameter.setParameter(parameterService.findById(parameter.getTmEquipmentParamId()));
			list.add(parameter);
		}
		return list;
	}

	@Override
	public PageResult<Map<String, Object>> pagePathUlocParamList(QueryCriteria criteria) {
		return ((TmPathUlocParameterDao) dao).pagePathUlocParamList(criteria);
	}

	@Override
	public PageResult<Map<String, Object>> pageEquipmentByParameterList(QueryCriteria criteria) {
		return ((TmPathUlocParameterDao) dao).pageEquipmentByParameterList(criteria);
	}

	@Override
	public List<TmPathUlocParameter> doSavePathUlocParameterNew(Integer tmPathUlocId, String[] tmEquipmentParamIds) {
		if(tmEquipmentParamIds.length>0 && tmPathUlocId!=null){
			List<TmPathUlocParameter> addList=new ArrayList<TmPathUlocParameter>();
			Map<String, TmPathUlocParameter> parameterIdMap = getParameterIdMap(tmPathUlocId);
			for (int i = 0; i < tmEquipmentParamIds.length; i++) {
				Integer paramid=Integer.valueOf(tmEquipmentParamIds[i]);
				if(!parameterIdMap.containsKey(paramid)){
					TmPathUlocParameter parameter = new TmPathUlocParameter();
					parameter.setTmPathUlocId(tmPathUlocId);
					parameter.setTmEquipmentParamId(paramid);
					parameter.setCreateTime(DateUtils.getCurrentDate());
					parameter.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
					addList.add(parameter);
				}
			}
			doSaveBatch(addList);
			return addList;
		}else{
			return null;
		}
		
	}

	@Override
	public List<TmPathUlocParameter> getTmPathUlocIdsByParameterList(Map<String, Object> params) {
		
		return null;
	}
}
