package com.wis.mes.rfid.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alien.enterpriseRFID.reader.AlienClass1Reader;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.DateUtils;
import com.wis.basis.common.utils.LogConstant;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.context.WebContextHolder;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.uloc.service.TmUlocService;
import com.wis.mes.master.workcalendar.entity.TmWorktime;
import com.wis.mes.master.workcalendar.service.TmWorktimeService;
import com.wis.mes.opc.service.OpcService;
import com.wis.mes.production.producttracing.entity.TbProductTracing;
import com.wis.mes.production.producttracing.service.TbProductTracingService;
import com.wis.mes.production.qualitytracing.service.TbQualityTracingService;
import com.wis.mes.rfid.callback.IRfidCallBack;
import com.wis.mes.rfid.entity.TbRfidLog;
import com.wis.mes.rfid.service.RfidService;
import com.wis.mes.rfid.service.TbRfidLogService;
import com.wis.mes.rfid.util.RfidConnectionUtil;
import com.wis.mes.rfid.util.RfidUtil;
import com.wis.mes.rfid.vo.RfidReadVo;
import com.wis.mes.rfid.vo.SnInfoObjVo;
import com.wis.mes.rfid.vo.TagVo;

@Service
public class RfidServiceImpl implements RfidService {

	@Autowired
	private TbProductTracingService productTracingService;
	@Autowired
	private TmWorktimeService tmWorktimeService;
	@Autowired
	private TbRfidLogService tbRfidLogService;
	@Autowired
	private TmUlocService tmUlocService;
	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private TbQualityTracingService tbQualityTracingService;
	@Autowired
	private OpcService opcService;

	private Log rfidLogger = LogFactory.getLog(LogConstant.RFID_LOG);
	
	private TbProductTracing saveProductTracing(SnInfoObjVo snInfoObj, TmUloc uloc) {
		Date date = new Date();
		TmWorktime tmWorktime = tmWorktimeService.thisWorkTime(uloc.getTmLineId());
		TbProductTracing bean = new TbProductTracing();
		if (StringUtils.isNotBlank(snInfoObj.getNewSn())) {
			String beginTime = DateUtils.formatFull(date);
			bean.setSn(snInfoObj.getNewSn());
			bean.setMachineName(snInfoObj.getMachineName());
			bean.setBackNumber(snInfoObj.getBackNumber());
			bean.setMachineOfName(productTracingService.getEgModelName(snInfoObj.getBackNumber()));
			bean.setBeginTime(beginTime.substring(11, beginTime.length()));
			bean.setCreateTime(date);
			bean.setLineNo(snInfoObj.getPlantNo());
			bean.setTmPlantId(uloc.getTmPlantId());// 事部ID
			bean.setTmLineId(uloc.getTmLineId());// 产线ID
			if (null != tmWorktime) {
				bean.setTmWorktimeId(tmWorktime.getId());// 工作日历ID
				bean.setTmClassManagerId(tmWorktime.getTmClassManagerId());// 班组
			}
			bean.setUnhealthyCount(0);
			bean = productTracingService.doSave(bean);
			logService.doAddLog("TbProductTracing", bean);
		}
		return bean;

	}

	public boolean validateRfidTag(String sn) {
		boolean FLAG = false;// 验证rfid标签
		TbProductTracing bean = productTracingService.getTbProductTracingSn(sn);
		if (null != bean) {// 查不到数据的话证明托盘RFID标签还没有被使用
			// 验证上线，下线时间是否存在，存在的话证明该标签可用
			/*
			 * if (StringUtils.isNotEmpty(bean.getBeginTime()) &&
			 * StringUtils.isNotEmpty(bean.getFinishTime())) { FLAG = true; }
			 */
			return FLAG;
		} else {
			FLAG = true;
		}
		return FLAG;
	}

	/***
	 * 获取ECP tagId
	 **/
	private TagVo getECPTagId(AlienClass1Reader reader) throws Exception {
		TagVo tagVo = null;
		for(int i=0;i<5;i++) {
			tagVo = RfidUtil.rfidRead(reader);
			if(null != tagVo) {
				break;
			}
		}
		if(null == tagVo) {
			throw new BusinessException("获取标签信息失败，请检查标签是否到位。");
		}
		return tagVo;
	}

	@Override
	public synchronized JsonActionResult doRfidReadWrite(String sn) throws Exception {
		JsonActionResult jsonActionResult = new JsonActionResult();
		SnInfoObjVo snObj = SnInfoObjVo.splitSn(sn);
		String ulocNo = globalConfigurationService.getValueByKey(ConstantUtils.RFID_WRITE_ENTRY);
		TbProductTracing productTracing = new TbProductTracing();
		boolean FLAG = false;
		String message = "";
		TmUloc uloc = null;
		TagVo tagVo = null;
		AlienClass1Reader reader = null;
		try {
			uloc = tmUlocService.getUloc(ulocNo);
			rfidLogger.info("工位：" + uloc.getNo() + "      SN:" + sn);
			reader = RfidConnectionUtil.writeConnection(uloc.getRfidIP(), Integer.valueOf(uloc.getRfidPort()),
					Integer.valueOf(uloc.getRfidAnt()));
			tagVo = getECPTagId(reader);// 锁定标签
			RfidUtil.writeUser(reader, snObj.getNewSn(), 0); // rfidWrite写入新的标签
			if (validateRfidTag(snObj.getNewSn())) {
				if (rfidStationWriteFlag(snObj.getNewSn())) {
					productTracing = saveProductTracing(snObj, uloc);
					jsonActionResult.setData(productTracing);
					FLAG = true;
				} else {
					message = "SN写入PLC失败，请联系管理员!";
				}
			} else {
				boolean rfidStationWriteFlag = rfidStationWriteFlag(snObj.getNewSn());// 产品是已经上线或者需要返修的产品。
				if (!rfidStationWriteFlag) {
					message = "SN写入PLC失败，请联系管理员!";
					FLAG = false;
				} else {
					FLAG = true;
				}
				productTracing.setSn(snObj.getNewSn());
				productTracing.setMachineName(snObj.getMachineName());
				productTracing.setBackNumber(snObj.getBackNumber());
				productTracing.setMachineOfName(productTracingService.getEgModelName(snObj.getBackNumber()));
				productTracing.setLineNo(snObj.getLineNo());
				jsonActionResult.setData(productTracing);
			}
			reader.doReaderCommand(RfidUtil.readLine(RfidUtil.ACQG2MASK_RESETTING));
		} catch (BusinessException e) {
			message = e.getMessage();
		} catch (Exception e) {
			rfidLogger.error("doRfidReadWrite>>SN:" + sn + "   e:" + e.getMessage());
			rfidLogger.error("请求异常：" + ExceptionUtils.getStackTrace(e));
			message = "写入失败，请重试!";
		}

		jsonActionResult.setSuccess(FLAG);
		jsonActionResult.setMessage(message);
		doSavaRfidLog(snObj, productTracing, FLAG, uloc, message, tagVo);
		return jsonActionResult;
	}

	private void doSavaRfidLog(SnInfoObjVo snObj, TbProductTracing productTracing, boolean FLAG, TmUloc uloc,
			String msg, TagVo tagVo) {
		TbRfidLog rfidLog = new TbRfidLog();
		rfidLog.setFruit(FLAG ? "成功" : "失败：" + msg);
		rfidLog.setSn(snObj.getNewSn());
		rfidLog.setBackNumber(productTracing.getBackNumber());
		rfidLog.setMachineName(productTracing.getMachineName());
		rfidLog.setTbPlantId(productTracing.getTmPlantId());
		rfidLog.setTmLineId(productTracing.getTmLineId());
		rfidLog.setTmWorktimeId(productTracing.getTmWorktimeId());
		if (tagVo == null) {
			rfidLog.setEpcInfo("(No Tags)");
		} else {
			rfidLog.setEpcInfo(tagVo.getEpcTagInfo());
		}
		if (null != uloc) {
			rfidLog.setUlocNo(uloc.getNo());
			rfidLog.setRfidIp(uloc.getRfidIP());
		}
		tbRfidLogService.doSave(rfidLog);
	}

	public boolean rfidStationWriteFlag(String sn) {
		JsonActionResult result = opcService.rfidStationWrite(sn);
		return result.getSuccess();
	}

	@Override
	public RfidReadVo plcRfidRead(String ulocNo) {
		RfidReadVo rfidReadVo = new RfidReadVo(); 
		try {
			TmUloc uloc = tmUlocService.getUloc(ulocNo);
			if (null != uloc && StringUtils.isNotBlank(uloc.getServiceName())) {
				Object bean = WebContextHolder.getWebAppContext().getBean(uloc.getServiceName());
				if (null != bean) {
					IRfidCallBack callback = (IRfidCallBack) bean;
					rfidReadVo = callback.getRfidReadVo(uloc);
				}
			} else {
				rfidReadVo.setMessage("工位：" + ulocNo + " 没有维护servicename。");
			}
		} catch (BusinessException e) {
			rfidReadVo.setMessage(e.getMessage());
			rfidLogger.error(e.getMessage() + "::" + ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			rfidLogger.error("请求异常：" + ExceptionUtils.getStackTrace(e));
			rfidReadVo.setMessage("写入失败，请重试!");
		}
		return rfidReadVo;
	}

	@SuppressWarnings("unchecked")
	public void stationWrite(String groupCode, RfidReadVo rfidReadVo, String inOrOut) {
		rfidLogger.info("NG出入口：工位(" + groupCode + "),SN(" + rfidReadVo.getSn() + ")" + "inOrOut(" + inOrOut + ")");
		if (StringUtils.isNotBlank(rfidReadVo.getSn())) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("groupCode", groupCode);
			map.put("SN", rfidReadVo.getSn());
			if (null != rfidReadVo.getTmUloc()) {
				String ulocProperty = rfidReadVo.getTmUloc().getIsEntrance();
				String ulocNo = rfidReadVo.getTmUloc().getNo();
				if (StringUtils.isNotBlank(ulocProperty)) {

					if ((ulocProperty.equals(ConstantUtils.NG_EXIT_ENTER) && StringUtils.isNotBlank(inOrOut)
							&& inOrOut.equals("IN")) || ulocProperty.equals(ConstantUtils.NG_ENTER)) {
						// NG入口和NG出入口
						Map<String, Object> failureStateMap = tbQualityTracingService
								.verifyFailureState(rfidReadVo.getSn());
						map.put("repairDone", "false");
						boolean repairDone = false;
						if ((Boolean) failureStateMap.get("flag")) {
							Map<Integer, Boolean> ngEntryMap = (Map<Integer, Boolean>) failureStateMap.get("ngEntry");
							if (ngEntryMap.containsKey(Integer.valueOf(ulocNo))
									&& ngEntryMap.get(Integer.valueOf(ulocNo))) {
								map.put("repairDone", "true");
								repairDone = true;
							}
						}
						JsonActionResult result = opcService.stationNgIn(groupCode, rfidReadVo.getSn(), repairDone);
						if (result.getSuccess() && repairDone) {
							tbQualityTracingService.doOnSnUpdateQualityTracing(rfidReadVo.getSn(), ulocNo);
						}
					} else if ((ulocProperty.equals(ConstantUtils.NG_EXIT_ENTER) && StringUtils.isNotBlank(inOrOut)
							&& inOrOut.equals("OUT")) || ulocProperty.equals(ConstantUtils.NG_EXIT)) {
						// NG出口和NG出入口
						JsonActionResult result = opcService.stationNgOut(groupCode, rfidReadVo.getSn());
						if(result.getSuccess()) {
							productTracingService.updateUnhealthyCount(rfidReadVo.getSn());
						}
					}
				}
			}
		} else {
			if (null != rfidReadVo.getTmUloc()) {
				String ulocProperty = rfidReadVo.getTmUloc().getIsEntrance();
				if ((ulocProperty.equals(ConstantUtils.NG_EXIT_ENTER) && StringUtils.isNotBlank(inOrOut)
						&& inOrOut.equals("OUT")) || ulocProperty.equals(ConstantUtils.NG_EXIT)) {
					opcService.stationNgOut(groupCode, "Unknown");
				}
			}
		}
	}

	@Override
	public Map<String, Object> getRfidConnectStatus(String ulocNo) {
		boolean FLAG = false;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			TmUloc uloc = tmUlocService.getUloc(ulocNo);
			RfidConnectionUtil.writeConnection(uloc.getRfidIP(), uloc.getRfidPort(), uloc.getRfidAnt());
			FLAG = true;
		} catch (Exception e) {
			rfidLogger.error(ExceptionUtils.getStackTrace(e));
		}
		map.put("connectStatus", FLAG);
		return map;
	}
}
