package com.wis.mes.rfid.service.impl;

import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.rfid.dao.TbRfidLogDao;
import com.wis.mes.rfid.entity.TbRfidLog;
import com.wis.mes.rfid.service.TbRfidLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TbRfidLogServiceImpl extends BaseServiceImpl<TbRfidLog> implements TbRfidLogService {

	@Autowired
	public void setDao(TbRfidLogDao dao) {
		this.dao = dao;
	}

}
