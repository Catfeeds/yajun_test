package com.wis.mes.job;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wis.basis.common.utils.LogConstant;
import com.wis.mes.production.metalplate.service.TbMetalPlateSourceDataService;

@Component(value = "apc21SourceDataAcquireJob")
public class APC21SourceDataAcquireJob {

	protected final Log log = LogFactory.getLog(APC21SourceDataAcquireJob.class);

	@Autowired
	private TbMetalPlateSourceDataService sourceDataService;

	private Log logger = LogFactory.getLog(LogConstant.BIZ_LOG);

	public void execue() {
		log.info("apc21SourceDataAcquire start!");
		try {
			sourceDataService.apc21SourceDataAcquire();
		} catch (Exception e) {
			logger.error("APC21SourceDataAcquireJob.java" + ExceptionUtils.getStackTrace(e));
		} finally {
			log.info("apc21SourceDataAcquire end!");
		}
	}
}
