package com.wis.mes.master.labelmanage.service.impl;

import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.labelmanage.dao.TbLabelManageDao;
import com.wis.mes.master.labelmanage.entity.TbLabelManage;
import com.wis.mes.master.labelmanage.service.TbLabelManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TbLabelManageServiceImpl extends BaseServiceImpl<TbLabelManage> implements TbLabelManageService {

	@Autowired
	public void setDao(TbLabelManageDao dao) {
		this.dao = dao;
	}

}
