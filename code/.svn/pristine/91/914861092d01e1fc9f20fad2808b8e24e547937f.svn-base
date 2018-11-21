package com.wis.mes.master.metalPlate.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.master.metalPlate.dao.TmSheetMetalMaterialDao;
import com.wis.mes.master.metalPlate.entity.TmSheetMetalMaterial;

@Repository
public class TmSheetMetalMaterialDaoImpl extends BaseDaoImpl<TmSheetMetalMaterial> implements TmSheetMetalMaterialDao {
	public List<TmSheetMetalMaterial> findByMachiningSurface(String model){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("machiningSurface", model);
		return getSqlSession().selectList("SheetMetalMaterialMapper.findByMachiningSurface",
				params);
	}
	@Override
	public String getSqlBy(QueryCriteria queryCriteria) {
		Map<String, Object> queryCondition = queryCriteria.getQueryCondition();
		boolean FLAG = false;
		String param = (String) queryCondition.get("machineTagsFind");
		if (queryCondition.containsKey("machineTagsFind")) {
			if(param!=null){
				FLAG = true;
			}
			queryCondition.remove("machineTagsFind");
		}
		String sql = super.getSqlBy(queryCriteria);
		if(FLAG){
			sql = sql.replace("where 1=1", " where 1 = 1 and tmsheetmetalmaterial_0.machine_tags like '%"+param+"%'");
		}
		return sql;
	}	
	

	@Override
	public Integer getInventoryNumber(String model) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne("SheetMetalMaterialMapper.getInventoryNumber", model);
	}
}
