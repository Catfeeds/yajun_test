package com.wis.mes.master.maintenance.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.master.equipment.entity.TmpEquipmentStatus;
import com.wis.mes.master.maintenance.dao.TmDeviceMaintenanceDao;
import com.wis.mes.master.maintenance.entity.TmDeviceMaintenance;

@Repository
public class TmDeviceMaintenanceDaoImpl extends BaseDaoImpl<TmDeviceMaintenance> implements TmDeviceMaintenanceDao {

	@Override
	public Integer findMaintenanceWarningNotice() {
		return getSqlSession().selectOne("SheetMetalMaterialMapper.findMaintenanceWarningNotice");
	}

	@Override
	public List<TmDeviceMaintenance> findMaintainList() {
		return getSqlSession().selectList("SheetMetalMaterialMapper.findMaintainList");
	}
	
	@Override
	public String getSqlBy(QueryCriteria queryCriteria) {
		Map<String, Object> queryCondition = queryCriteria.getQueryCondition();
		boolean FLAG = false;
		if (queryCondition.containsKey("maintenanceList")) {
			if("YES".equals(queryCondition.get("maintenanceList"))){
				FLAG = true;
			}
			queryCondition.remove("maintenanceList");
		}
		String sql = super.getSqlBy(queryCriteria);
		if(FLAG){
			sql = sql.replace("where 1=1", " where 1=1 and tmdevicemaintenance_0.MAINTENANCE_VALUE<=tmdevicemaintenance_0.CURRENT_VALUE ");
		}
		return sql;
	}

}

