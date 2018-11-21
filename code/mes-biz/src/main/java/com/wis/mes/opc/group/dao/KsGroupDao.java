package com.wis.mes.opc.group.dao;

import java.util.List;

import com.wis.core.dao.BaseDao;
import com.wis.mes.opc.group.entity.KsGroup;

public interface KsGroupDao extends BaseDao<KsGroup> {
	List<KsGroup> getGroupServerIsNotNull();

	void clearPlcStationDeviceStatusData();

	void clearFinTempTable(String tableName);
}
