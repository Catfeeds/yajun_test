package com.wis.mes.job;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wis.basis.common.utils.LogConstant;
import com.wis.mes.master.maintenance.service.TmDeviceMaintenanceService;

@Component(value = "maintenanceWarningNoticeJob")
public class MaintenanceWarningNoticeJob {
	/**
	 * 保养预警通知
	 */
	protected final Log log = LogFactory.getLog(MaintenanceWarningNoticeJob.class);
	@Autowired
	private TmDeviceMaintenanceService maintenanceService;
	private Log logger = LogFactory.getLog(LogConstant.BIZ_LOG);
	public void execue() {
		log.info("MaintenanceWarningNoticeJob start!");
		try {
			maintenanceService.maintenanceWarningNotice();
		} catch (Exception e) {
			logger.error("MaintenanceWarningNoticeJob.java" + ExceptionUtils.getStackTrace(e));
		} finally {
			log.info("MaintenanceWarningNoticeJob end!");
		}
	}
}
