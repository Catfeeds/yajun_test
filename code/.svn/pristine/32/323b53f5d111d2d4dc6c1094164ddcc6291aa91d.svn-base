package com.wis.mes.job;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wis.basis.common.utils.LogConstant;
import com.wis.mes.master.workcalendar.service.TmWorktimeService;

@Component(value = "checkWorkTimeAndCopyJob")
public class CheckWorkTimeAndCopyJob {

	@Autowired
	private TmWorktimeService workTimeService;
	
	private Log logger = LogFactory.getLog(LogConstant.BIZ_LOG);
	
	public void execue() {
		try {
			workTimeService.checkWorkTimeMain();
		} catch (Exception e) {
			logger.error("checkWorkTimeAndCopyJob.java"+ExceptionUtils.getStackTrace(e));
		}
	}
}
