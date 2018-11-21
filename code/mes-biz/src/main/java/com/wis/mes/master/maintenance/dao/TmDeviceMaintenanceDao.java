package com.wis.mes.master.maintenance.dao;

import java.util.List;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.BaseDao;
import com.wis.core.dao.PageResult;
import com.wis.mes.master.maintenance.entity.TmDeviceMaintenance;

public interface TmDeviceMaintenanceDao extends BaseDao<TmDeviceMaintenance> {
	Integer findMaintenanceWarningNotice();

	List<TmDeviceMaintenance> findMaintainList();

}
