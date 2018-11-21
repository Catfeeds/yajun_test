package com.wis.mes.master.equipment.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.BaseExcelUtil;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.mes.master.base.service.impl.MasterBaseServiceImpl;
import com.wis.mes.master.equipment.dao.TmEquipmentParamDao;
import com.wis.mes.master.equipment.entity.TmEquipmentParam;
import com.wis.mes.master.equipment.service.TmEquipmentParamService;

@Service
public class TmEquipmentParamServiceImpl extends MasterBaseServiceImpl<TmEquipmentParam> implements TmEquipmentParamService {

	@Autowired
	public void setDao(TmEquipmentParamDao dao) {
		this.dao = dao;
	}


	@Override
	public List<TmEquipmentParam> doSaveEquipmentParameter(Integer tmEquipmentId, String[] tsParameterId, String[] type,String[] note) {
		Map<String, TmEquipmentParam> equipmentParameterMap = getEquipmentParameterMap(String.valueOf(tmEquipmentId));
		//新增
		List<TmEquipmentParam> equipmentParamAddList = new ArrayList<TmEquipmentParam>();
		//更新
		List<TmEquipmentParam> equipmentParamUpdateList = new ArrayList<TmEquipmentParam>();
		
		for(int i=0;i<tsParameterId.length;i++){
			//如果数据已经存在则更新
			if(equipmentParameterMap.containsKey(tsParameterId[i])){
				TmEquipmentParam tmEquipmentParam = equipmentParameterMap.get(tsParameterId[i]);
				tmEquipmentParam.setType(getType(type,i));
				tmEquipmentParam.setNote(getNote(note,i));
				equipmentParamUpdateList.add(tmEquipmentParam);
			}else{
				TmEquipmentParam tmEquipmentParam = new TmEquipmentParam();
				tmEquipmentParam.setTmEquipmentId(tmEquipmentId);
				tmEquipmentParam.setTsParameterId(Integer.valueOf(tsParameterId[i]));
				tmEquipmentParam.setType(getType(type,i));
				tmEquipmentParam.setNote(getNote(note,i));
				equipmentParamAddList.add(tmEquipmentParam);
			}
		}
		doSaveBatch(equipmentParamAddList);
		if (equipmentParamUpdateList.size() > 0) {
			doUpdateBatch(equipmentParamUpdateList);
		}
		return equipmentParamAddList;
	}
	
	private Map<String, TmEquipmentParam> getEquipmentParameterMap(String tmEquipmentId) {
		Map<String, TmEquipmentParam> map = new HashMap<String, TmEquipmentParam>();
		QueryCriteria criteria = new QueryCriteria();
		criteria.setQueryPage(false);
		criteria.getQueryCondition().put("tmEquipmentId", tmEquipmentId);
		List<TmEquipmentParam> content = findBy(criteria).getContent();
		for (TmEquipmentParam equipmentParameter : content) {
			map.put(equipmentParameter.getTsParameterId().toString(), equipmentParameter);
		}
		return map;
	}

	private String getType(String[] type,int index){
		return (type.length == 0)?"":type[index];
	}
	
	
	private String getNote(String[] note, int index) {
		return (note.length == 0)?"":note[index];
	}

	@Override
	public PageResult<?> getParamPageResult(BootstrapTableQueryCriteria criteria) {
		return ((TmEquipmentParamDao)dao).getParamPageResult(criteria);
	}

	@Override
	public Map<String, Object> exportExcelData(HttpServletResponse response, List<TmEquipmentParam> list,
			String fileName, String[] header) {

		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();

		/*
		for (TmEquipmentParam eqParam : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("参数", eqParam.getParameter().getName());
			map.put("参数编号", eqParam.getParameter().getCode());
			map.put("参数表达式", eqParam.getParameter().getRegularExpression());
			map.put("参数变量名", eqParam.getParameter().getVariuableName());
			map.put("类型",
					eqParamType.containsKey(eqParam.getType()) ? eqParamType.get(eqParam.getType()).getName() : "");
			map.put("备注", getStr(eqParam.getNote()));
			exportDataList.add(map);
		}*/
		return BaseExcelUtil.exportData(response, exportDataList, fileName, header);
	}

	@Override
	public Integer checkParameterName(String parameterName,Integer tmEquipmentId) {
		TmEquipmentParam eg = new TmEquipmentParam();
		eg.setParameterName(parameterName);
		eg.setTmEquipmentId(tmEquipmentId);
		List<TmEquipmentParam> rList = dao.findByEg(eg);
		if (rList.size() > 0) {
			return rList.get(0).getId();
		}
		return null;
	}

}
