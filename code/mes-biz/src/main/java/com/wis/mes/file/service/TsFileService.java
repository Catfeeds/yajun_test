package com.wis.mes.file.service;

import java.text.ParseException;

import com.wis.core.service.BaseService;
import com.wis.mes.file.entity.TsFile;

public interface TsFileService extends BaseService<TsFile> {
	
	TsFile doSaveTsFile(String type,String filePath,String fileName,String queryCondition);
	
	void doDeleteFiles() throws ParseException;
}
