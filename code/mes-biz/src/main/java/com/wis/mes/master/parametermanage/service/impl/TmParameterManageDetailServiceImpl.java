package com.wis.mes.master.parametermanage.service.impl;

import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.parametermanage.dao.TmParameterManageDetailDao;
import com.wis.mes.master.parametermanage.entity.TmParameterManageDetail;
import com.wis.mes.master.parametermanage.service.TmParameterManageDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TmParameterManageDetailServiceImpl extends BaseServiceImpl<TmParameterManageDetail> implements TmParameterManageDetailService {

	@Autowired
	public void setDao(TmParameterManageDetailDao dao) {
		this.dao = dao;
	}

}
