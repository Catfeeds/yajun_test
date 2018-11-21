package com.wis.mes.file.service.impl;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.DateUtils;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.file.dao.TsFileDao;
import com.wis.mes.file.entity.TsFile;
import com.wis.mes.file.service.TsFileService;

@Service(value="tsFileService")
public class TsFileServiceImpl extends BaseServiceImpl<TsFile> implements TsFileService {

	@Autowired
	public void setDao(TsFileDao dao){
		this.dao = dao;
	}
	@Override
	public TsFile doSaveTsFile(String type, String filePath, String fileName, String queryCondition) {
		TsFile file = new TsFile();
		file.setType(type);
		file.setFilePath(filePath);
		file.setFileName(fileName);
		file.setQueryCondition(queryCondition);
		return this.doSave(file);
	}
	@Override
	public void doDeleteFiles() throws ParseException {
		List<TsFile> files = findAll();
		if(null != files) {
			for(TsFile tsFile:files) {
				long createTime = DateUtils.subMonth(tsFile.getCreateTime()).getTime();//创建时间+1个月
				long thisTime = new Date().getTime();//当前时间
				if(thisTime>=createTime) {//当前时间大于等于创建时间+一个月
					File file = new File(tsFile.getFilePath());
					if(file.exists()) {
						file.delete();
						doDeleteById(tsFile.getId());
					}
				 }
			}
		}
	}

}
