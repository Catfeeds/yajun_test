package com.wis.mes.opc.group.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.opc.group.dao.KsGroupDao;
import com.wis.mes.opc.group.entity.KsGroup;

@Repository
public class KsGroupDaoImpl extends BaseDaoImpl<KsGroup> implements KsGroupDao {

	@Override
	public List<KsGroup> getGroupServerIsNotNull() {
		return getSqlSession().selectList("KsGroupMapper.getGroupServerIsNotNull");
	}

	public void clearFinTempTable(String tableName) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("tableName", tableName);
		getSqlSession().delete("KsGroupMapper.clearFinDeviceData", param);
	}

	@Override
	public void clearPlcStationDeviceStatusData() {
		getSqlSession().delete("KsGroupMapper.clearPlcStationDeviceData");
		getSqlSession().delete("KsGroupMapper.clearPlcStationStatusData");
	}
}
