package com.wis.mes.job;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wis.basis.common.utils.LogConstant;
import com.wis.mes.opc.util.OpcPropertiesConfig;
import com.wis.mes.production.metalplate.service.TbMetalPlateSchedulService;

@Component(value = "pmcBoardProductionInfoJob")
public class PMCBoardProductionInfoJob {
/**
 * --PMC看板排产数据，一分钟执行一次
 */
	protected final Log log = LogFactory.getLog(PMCBoardProductionInfoJob.class);

	@Autowired
	private TbMetalPlateSchedulService schedulService;

	private Log logger = LogFactory.getLog(LogConstant.BIZ_LOG);

	public void execue() {
		log.info("pmcBoardProductionInfoJob start!");
		try {
			boolean isDataEnable = new Boolean(OpcPropertiesConfig.getPropertyByKey("opc.metalplate.data.enabled"));
			if(isDataEnable) {
				schedulService.pmcBoardProductionInfo();
			}
		} catch (Exception e) {
			logger.error("PMCBoardProductionInfoJob.java" + ExceptionUtils.getStackTrace(e));
		} finally {
			log.info("pmcBoardProductionInfoJob end!");
		}
	}
}
