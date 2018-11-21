package com.wis.mes.master.equipment.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.BaseExcelUtil;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.mes.master.base.service.impl.MasterBaseServiceImpl;
import com.wis.mes.master.equipment.dao.TmEquipmentResponsiblePersonDao;
import com.wis.mes.master.equipment.entity.TmEquipmentResponsiblePerson;
import com.wis.mes.master.equipment.service.TmEquipmentResponsiblePersonService;

@Service
public class TmEquipmentResponsiblePersonServiceImpl extends MasterBaseServiceImpl<TmEquipmentResponsiblePerson> implements TmEquipmentResponsiblePersonService {

	@Autowired
	public void setDao(TmEquipmentResponsiblePersonDao dao) {
		this.dao = dao;
	}

	@Override
	public List<TmEquipmentResponsiblePerson> doSaveBatch(Integer tmEquipmentId, String[] tsUserId,String[] note) {
		Map<String, TmEquipmentResponsiblePerson> equipmentResponsiblePersonMap = getEquipmentResponsiblePersonMap(String.valueOf(tmEquipmentId));
		//新增
		List<TmEquipmentResponsiblePerson> equipmentResponsiblePersonAddList = new ArrayList<TmEquipmentResponsiblePerson>();
		//更新
		List<TmEquipmentResponsiblePerson> equipmentResponsiblePersonUpdateList = new ArrayList<TmEquipmentResponsiblePerson>();
		
		for(int i=0;i<tsUserId.length;i++){
			//如果数据已经存在则更新
			if(equipmentResponsiblePersonMap.containsKey(tsUserId[i])){
				TmEquipmentResponsiblePerson tmEquipmentResponsiblePerson = equipmentResponsiblePersonMap.get(tsUserId[i]);
				tmEquipmentResponsiblePerson.setTsUserId(Integer.valueOf(tsUserId[i]));
				tmEquipmentResponsiblePerson.setNote(getNote(note,i));
				equipmentResponsiblePersonUpdateList.add(tmEquipmentResponsiblePerson);
			}else{
				TmEquipmentResponsiblePerson tmEquipmentResponsiblePerson = new TmEquipmentResponsiblePerson();
				tmEquipmentResponsiblePerson.setTmEquipmentId(tmEquipmentId);
				tmEquipmentResponsiblePerson.setTsUserId(Integer.valueOf(tsUserId[i]));
				tmEquipmentResponsiblePerson.setNote(getNote(note,i));
				equipmentResponsiblePersonAddList.add(tmEquipmentResponsiblePerson);
			}
		}
		doSaveBatch(equipmentResponsiblePersonAddList);
		if (equipmentResponsiblePersonUpdateList.size() > 0) {
			doUpdateBatch(equipmentResponsiblePersonUpdateList);
		}
		return equipmentResponsiblePersonAddList;
	}
	
	private Map<String, TmEquipmentResponsiblePerson> getEquipmentResponsiblePersonMap(String tmEquipmentId) {
		Map<String, TmEquipmentResponsiblePerson> map = new HashMap<String, TmEquipmentResponsiblePerson>();
		QueryCriteria criteria = new QueryCriteria();
		criteria.setQueryPage(false);
		criteria.getQueryCondition().put("tmEquipmentId", tmEquipmentId);
		List<TmEquipmentResponsiblePerson> content = findBy(criteria).getContent();
		for (TmEquipmentResponsiblePerson equipmentResponsiblePerson : content) {
			map.put(equipmentResponsiblePerson.getTsUserId().toString(), equipmentResponsiblePerson);
		}
		return map;
	}
	
	private String getNote(String[] note, int index) {
		return (note.length == 0)?"":note[index];
	}

	@Override
	public PageResult<?> getUserPageResult(QueryCriteria criteria) {
		return ((TmEquipmentResponsiblePersonDao)dao).getUserPageResult(criteria);
	}

	@Override
	public Map<String, Object> exportExcelData(HttpServletResponse response, List<TmEquipmentResponsiblePerson> list,
			String fileName, String[] header) {

		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();

		for (TmEquipmentResponsiblePerson eqPerson : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("姓名", eqPerson.getUser().getName());
			map.put("号码", getStr(eqPerson.getUser().getPhone()));
			map.put("邮箱", getStr(eqPerson.getUser().getEmail()));
			map.put("备注", getStr(eqPerson.getNote()));
			exportDataList.add(map);
		}
		return BaseExcelUtil.exportData(response, exportDataList, fileName, header);
	}

	private String getStr(Object obj) {
		return obj == null ? "" : obj.toString();
	}

}
