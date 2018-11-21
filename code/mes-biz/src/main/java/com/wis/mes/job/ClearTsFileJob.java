package com.wis.mes.job;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wis.basis.common.utils.LogConstant;
import com.wis.mes.file.service.TsFileService;

@Component(value = "clearTsFileJob")
public class ClearTsFileJob {

	@Autowired
	private TsFileService fileService;

	private Log logger = LogFactory.getLog(LogConstant.BIZ_LOG);

	public void execue() {
		try {
			fileService.doDeleteFiles();
		} catch (Exception e) {
			logger.error("ClearTsFileJob.java" + ExceptionUtils.getStackTrace(e));
		} 
	}
}
