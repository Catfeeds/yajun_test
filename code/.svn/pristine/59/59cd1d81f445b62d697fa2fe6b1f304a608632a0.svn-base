package com.wis.mes.job;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wis.basis.common.utils.LogConstant;
import com.wis.mes.opc.util.OpcPropertiesConfig;
import com.wis.mes.production.metalplate.service.TbMetalPlateSchedulService;

@Component(value = "mpregionMarkMonitorJob")
public class MPRegionMarkMonitorJob {
/**
 * 查看区域是否空闲，空闲状态下给派发待产任务，一分钟执行一次
 */
	protected final Log log = LogFactory.getLog(MPRegionMarkMonitorJob.class);

	@Autowired
	private TbMetalPlateSchedulService schedulService;

	private Log logger = LogFactory.getLog(LogConstant.BIZ_LOG);

	public void execue() {
		log.info("MPRegionMarkMonitorJob start!");
		try {
			boolean isDataEnable = new Boolean(OpcPropertiesConfig.getPropertyByKey("opc.metalplate.data.enabled"));
			if(isDataEnable) {
			
				schedulService.mpregionMarkMonitor();
			}
		} catch (Exception e) {
			logger.error("PMCBoardProductionInfoJob.java" + ExceptionUtils.getStackTrace(e));
		} finally {
			log.info("MPRegionMarkMonitorJob end!");
		}
	}
}
