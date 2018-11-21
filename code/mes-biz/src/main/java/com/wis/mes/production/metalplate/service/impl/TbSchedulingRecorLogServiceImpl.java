package com.wis.mes.production.metalplate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.production.metalplate.dao.TbSchedulingRecorLogDao;
import com.wis.mes.production.metalplate.entity.TbSchedulingRecorLog;
import com.wis.mes.production.metalplate.service.TbSchedulingRecorLogService;

/**
 * @author caixia
 *
 */
@Service
public class TbSchedulingRecorLogServiceImpl extends BaseServiceImpl<TbSchedulingRecorLog> implements TbSchedulingRecorLogService {

	@Autowired
	public void setDao(TbSchedulingRecorLogDao dao) {
		this.dao = dao;
	}
}
