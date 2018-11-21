package com.wis.basis.systemadmin.service.impl;

import java.util.List;

import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.systemadmin.dao.ImportLogDao;
import com.wis.basis.systemadmin.entity.ImportLog;
import com.wis.basis.systemadmin.service.ImportLogService;
import com.wis.core.context.WebContextHolder;
import com.wis.core.framework.model.IUser;
import com.wis.core.service.impl.BaseServiceImpl;

@Service("importLogService")
public class ImportLogServiceImpl extends BaseServiceImpl<ImportLog> implements ImportLogService {

	@Autowired
	public void setDao(ImportLogDao dao) {
		this.dao = dao;
	}
	
	@Override
	public ImportLog doSave(ImportLog log) {
		log.setOperatorId(WebContextHolder.getCurrentUser().getId());
		log.setOperatorName(WebContextHolder.getCurrentUser().getName());
		dao.save(log);
		return log;
	}
	
	@Override
	public void doSaveBatch(List<ImportLog> logs) {
		IUser currentUser = WebContextHolder.getCurrentUser();
		for (ImportLog importLog : logs) {
			importLog.setOperatorId(currentUser.getId());
			importLog.setOperatorName(currentUser.getName());
		}
		dao.saveBatch(logs);
	}
}
