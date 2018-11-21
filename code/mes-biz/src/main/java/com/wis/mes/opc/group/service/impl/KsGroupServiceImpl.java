package com.wis.mes.opc.group.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.opc.group.dao.KsGroupDao;
import com.wis.mes.opc.group.entity.KsGroup;
import com.wis.mes.opc.group.service.KsGroupService;

@Service
public class KsGroupServiceImpl extends BaseServiceImpl<KsGroup> implements KsGroupService {

	@Autowired
	public void setDao(KsGroupDao dao) {
		this.dao = dao;
	}

	@Override
	public List<KsGroup> getGroupServerIsNotNull() {
		return ((KsGroupDao) dao).getGroupServerIsNotNull();
	}

	@Override
	public void clearPlcStationDeviceStatusData() {
		((KsGroupDao) dao).clearPlcStationDeviceStatusData();
	}

	public void clearFinTempTable(String tableName) {
		((KsGroupDao) dao).clearFinTempTable(tableName);
	}
}
