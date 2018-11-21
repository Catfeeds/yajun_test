package com.wis.mes.production.regulate.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.jfree.util.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.DateUtils;
import com.wis.basis.common.utils.GlobalConfigurationUtils;
import com.wis.basis.numRule.entity.NumRuleValue;
import com.wis.basis.numRule.entity.TsNumRule;
import com.wis.basis.numRule.service.TsNumRuleService;
import com.wis.basis.systemadmin.service.UserService;
import com.wis.core.context.WebContextHolder;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.master.part.entity.TmPart;
import com.wis.mes.master.part.service.TmPartService;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.plant.service.TmPlantService;
import com.wis.mes.master.supplier.entity.TmSupplier;
import com.wis.mes.master.supplier.entity.TmSupplierPart;
import com.wis.mes.master.supplier.service.TmSupplierPartService;
import com.wis.mes.master.supplier.service.TmSupplierService;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.uloc.entity.TmUlocSipNc;
import com.wis.mes.master.uloc.service.TmUlocService;
import com.wis.mes.master.workcalendar.entity.TmWorktime;
import com.wis.mes.master.workshop.entity.TmWorkshop;
import com.wis.mes.master.workshop.service.TmWorkshopService;
import com.wis.mes.production.plan.porder.entity.ToPorder;
import com.wis.mes.production.plan.porder.entity.ToPorderAvi;
import com.wis.mes.production.plan.porder.entity.ToPorderAviBom;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPath;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPathParameter;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPathSip;
import com.wis.mes.production.plan.porder.service.ToPorderAviPathService;
import com.wis.mes.production.plan.porder.service.ToPorderAviService;
import com.wis.mes.production.plan.porder.service.ToPorderService;
import com.wis.mes.production.record.entity.TpRecord;
import com.wis.mes.production.record.entity.TpRecordUloc;
import com.wis.mes.production.record.entity.TpRecordUlocParameter;
import com.wis.mes.production.record.entity.TpRecordUlocPart;
import com.wis.mes.production.record.entity.TpRecordUlocQuality;
import com.wis.mes.production.record.service.TpRecordService;
import com.wis.mes.production.record.service.TpRecordUlocParameterService;
import com.wis.mes.production.record.service.TpRecordUlocPartService;
import com.wis.mes.production.record.service.TpRecordUlocQualityService;
import com.wis.mes.production.record.service.TpRecordUlocService;
import com.wis.mes.production.regulate.dao.OnlineDao;
import com.wis.mes.production.regulate.service.OnlineService;
import com.wis.mes.production.untreated.service.TpUntreatedNcService;
import com.wis.mes.production.wip.entity.TpWip;
import com.wis.mes.production.wip.service.TpWipService;
import com.wis.mes.util.StringUtil;

@Service
public class OnlineServiceImpl implements OnlineService {

	@Autowired
	private OnlineDao dao;
	@Autowired
	private UserService userService;
	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	@Autowired
	private ToPorderAviService aviService;
	@Autowired
	private TpRecordService recordService;
	@Autowired
	private ToPorderAviPathService aviPathService;
	@Autowired
	private TmUlocService ulocService;
	@Autowired
	private TmPartService partService;
	@Autowired
	private TsNumRuleService numRuleService;
	@Autowired
	private TpWipService wipService;
	@Autowired
	private TpRecordUlocPartService recordUlocPartService;
	@Autowired
	private TpRecordUlocQualityService recordUlocQualityService;
	@Autowired
	private TpRecordUlocParameterService recordUlocParameterService;
	@Autowired
	private TpRecordUlocService recordUlocService;
	@Autowired
	private TpUntreatedNcService untreatedNcService;
	@Autowired
	private TmPlantService plantService;
	@Autowired
	private TmWorkshopService workshopService;
	@Autowired
	private TmLineService lineService;
	@Autowired
	private TmSupplierService supplierService;
	@Autowired
	private TmSupplierPartService supplierPartService;
	@Autowired
	private ToPorderService porderService;

	@Override
	public List<TmUloc> getOnlineUlocByLineId(Integer tmLineId) {
		if (tmLineId == null) {
			return new ArrayList<TmUloc>();
		}
		String queryUlocPermissionByUserId = userService
				.queryUlocPermissionByUserId(WebContextHolder.getCurrentUser().getId());
		return dao.getOnlineUlocByLineId(tmLineId, queryUlocPermissionByUserId);
	}

	@Override
	public List<ToPorderAviBom> getAviBom(Integer toPorderAviId, Integer tmUlocId, String seq) {
		String valueByKey = globalConfigurationService
				.getValueByKey(GlobalConfigurationUtils.GLOBAL_CONFIGUR_BOM_AND_QBOM_SELECTED);
		return dao.getAviBom(toPorderAviId, tmUlocId, seq, valueByKey);
	}

	@Override
	public List<ToPorderAviPathParameter> getAviPathParameter(Integer toPorderAviId, Integer tmUlocId, String seq) {
		return dao.getAviPathParameter(toPorderAviId, tmUlocId, seq);
	}

	@Override
	public List<ToPorderAviPathSip> getAviPathSip(Integer toPorderAviId, Integer tmUlocId, String seq) {
		return dao.getAviPathSip(toPorderAviId, tmUlocId, seq);
	}

	@Override
	public List<ToPorder> getPorderByUlocId(Integer tmUlocId) {
		return dao.getPorderByUlocId(tmUlocId);
	}

	@Override
	public List<ToPorderAvi> getAviByPorderId(String aviStatus, Integer toPorderId) {
		return dao.getAviByPorderId(aviStatus, toPorderId);
	}

	@Override
	public TmWorktime getNowWorkTimeByLineId(Integer tmLineId) {
		return dao.getNowWorkTimeByLineId(tmLineId);
	}

	@Override
	public Integer getUlocCrossQty(Integer tmUlocId, String operationType, String startTime, String endTime) {
		return dao.getUlocCrossQty(tmUlocId, operationType, startTime, endTime);
	}

	@Override
	public List<TpWip> getWaitOnlineSn(Integer aviId, Integer tmUlocId) {
		ToPorderAvi avi = aviService.findById(aviId);
		Map<String, Object> map = getCurrentUlocSeqAndNextUlocIds(tmUlocId, aviId, true);
		return dao.getWaitOnlineSn(avi.getNo(), map.get("currentUlocSeq").toString(), tmUlocId);
	}

	@Override
	public List<TpRecordUloc> getAlreadyOnlineData(Integer tmUlocId) {
		return dao.getAlreadyOnlineData(tmUlocId);
	}

	/**
	 * 检查生产序列和工单
	 * 
	 * @param aviId
	 */
	private void checkAviAndPorderData(ToPorderAvi avi) {
		if (avi != null) {
			if (ConstantUtils.ENTRY_CODE_AVI_STATUS_CLOSE.equals(avi.getStatus())) {// 生产序列已关闭
				throw new BusinessException("ONLINE_AVI_CLOSE", avi.getNo());
			}
			if (ConstantUtils.ENTRY_CODE_AVI_STATUS_FINSH.equals(avi.getStatus())) {// 生产序列已生产完成
				throw new BusinessException("ONLINE_AVI_FINSH", avi.getNo());
			}
			if (ConstantUtils.ENTRY_CODE_AVI_STATUS_ALL.equals(avi.getStatus())) {// 生产序列已全部上线
				throw new BusinessException("ONLINE_AVI_ALL_ONLINE", avi.getNo());
			}
			if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(avi.getIsHold())) {// 生产序列被HOLD
				throw new BusinessException("ONLINE_AVI_HOLD", avi.getNo());
			}

			ToPorder porder = avi.getPorder();
			if (ConstantUtils.ENTRY_CODE_PORDER_PRODUCT_STATUS_CLOSE.equals(porder.getProductStatus())) {// 工单已关闭
				throw new BusinessException("ONLINE_PORDER_CLOSE", porder.getNo());
			}
			if (ConstantUtils.ENTRY_CODE_PORDER_PRODUCT_STATUS_FINSH.equals(porder.getProductStatus())) {// 工单已生产完成
				throw new BusinessException("ONLINE_PORDER_FINSH", porder.getNo());
			}
			if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(porder.getIsHold())) {// 工单被HOLD
				throw new BusinessException("ONLINE_PORDER_HOLD", porder.getNo());
			}
		}
	}

	/**
	 * 检查数据
	 * 
	 * @param SN
	 * @param currentUlocSeq
	 * @param ulocId
	 * @param aviId
	 * @return key:avi、record、wip
	 */
	private Map<String, Object> checkSNAndAviData(String SN, Integer ulocId, Integer aviId) {
		TpRecord record = recordService.getTpRecordBySN(SN);
		if (record == null) {
			throw new BusinessException("ONLINE_SN_NOT_EXIST");
		}
		if (record.getOfflineTime() != null) {
			throw new BusinessException("CROSS_POINT_SN_IS_OFFLINE");
		}
		ToPorderAvi avi = null;
		if (aviId == null) {
			avi = getAviByAviNo(record.getAviNo());
		} else {
			avi = aviService.findById(aviId, true);
		}
		TmWorktime workTime = getNowWorkTimeByLineId(avi.getTmLineId());
		String value = globalConfigurationService
				.getValueByKey(GlobalConfigurationUtils.GLOBAL_CONFIGUR_WORKTIME_IS_REQUIRED);
		if (workTime == null && Boolean.valueOf(value)) {
			throw new BusinessException("WORK_TIME_MUST");
		}
		checkAviAndPorderData(avi);
		Map<String, Object> currentUloc = getCurrentUlocSeqAndNextUlocIds(ulocId, avi.getId(), true);
		TpWip wip = getTpWip(SN, ulocId, currentUloc.get("currentUlocSeq").toString());
		if (wip == null) {
			TpWip eg = new TpWip();
			eg.setSn(SN);
			List<TpWip> wipList = wipService.findByEg(eg);
			String[] ulocNos = new String[wipList.size()];
			for (int i = 0; i < wipList.size(); i++) {
				ulocNos[i] = wipList.get(i).getUlocNo() + "-" + wipList.get(i).getUlocName();
			}
			if (ulocNos.length == 0) {
				throw new BusinessException("ONLINE_SN_NOT_IN_CURRENT_ULOC");
			} else {
				throw new BusinessException("ONLINE_SN_MAY_IN_ULOCS", Arrays.toString(ulocNos));
			}
		}

		if (record.getOnlineTime() != null) {// SN已经上线了
			throw new BusinessException("ONLINE_RECORD_ALREADY_ONLINE", record.getSn());
		}
		if (ConstantUtils.PRODUCT_WIP_STATUS_REPAIRD.equals(wip.getStatus())) {// 正在返修
			throw new BusinessException("ONLINE_WIP_REPAIRD", record.getSn());
		}
		if (ConstantUtils.PRODUCT_WIP_STATUS_WAIT_REPAIR.equals(wip.getStatus())) {// 等待返修
			throw new BusinessException("ONLINE_WIP_WAIT_REPAIRD", record.getSn());
		}
		if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(wip.getIsHold())) {// SN被HOLD
			throw new BusinessException("ONLINE_WIP_HOLD", wip.getSn());
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("avi", avi);
		map.put("record", record);
		map.put("wip", wip);
		map.put("currentUlocSeq", currentUloc.get("currentUlocSeq").toString());
		map.put("isSip", currentUloc.get("isSip").toString());
		map.put("isOffline", currentUloc.get("isOffline").toString());
		return map;
	}

	@Override
	public Map<String, Object> getCurrentUlocSeqAndNextUlocIds(Integer tmUlocId, Integer aviId,
			boolean isOnlyGetCurrentUlocSeq) {
		Map<String, ToPorderAviPath> map = new HashMap<String, ToPorderAviPath>();
		// 从哪个节点到哪个节点
		Map<String, List<String>> fromUlocTo = new HashMap<String, List<String>>();
		List<ToPorderAviPath> aviPaths = aviPathService.getAviPaths(aviId, false);
		String currentUlocSeq = null;// 当前顺序号
		Integer pathId = null;// 当前节点ID
		String isSip = ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO;// 是否质检点
		String isOffline = ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO;
		for (ToPorderAviPath bean : aviPaths) {
			if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(bean.getIsOnline())
					&& bean.getTmUlocId().equals(tmUlocId)) {
				currentUlocSeq = bean.getSeq();
				pathId = bean.getId();
				isSip = bean.getIsSip();
				isOffline = bean.getIsOffline();
				if (isOnlyGetCurrentUlocSeq) {
					Map<String, Object> returnMap = new HashMap<String, Object>();
					returnMap.put("currentUlocSeq", currentUlocSeq);
					returnMap.put("isSip", isSip);
					returnMap.put("isOffline", isOffline);
					return returnMap;
				}
			}
			map.put(bean.getId().toString(), bean);
			if (StringUtil.isNotBlank(bean.getParentId())) {// 父节点ID
				String[] parendIds = bean.getParentId().split(",");
				for (String parentId : parendIds) {
					if (fromUlocTo.containsKey(parentId)) {
						List<String> list = fromUlocTo.get(parentId);
						list.add(bean.getId().toString());
					} else {
						List<String> list = new ArrayList<String>();
						list.add(bean.getId().toString());
						fromUlocTo.put(parentId, list);
					}
				}
			}
		}

		List<String> nextPathIds = fromUlocTo.get(pathId.toString());
		StringBuffer nextUlocIds = new StringBuffer();
		StringBuffer nextUlocSeqs = new StringBuffer();
		if (nextPathIds != null) {
			for (int i = 0; i < nextPathIds.size(); i++) {
				ToPorderAviPath aviPath = map.get(nextPathIds.get(i));
				nextUlocIds.append(aviPath.getTmUlocId()).append(",");// 获取nextUlocId
				nextUlocSeqs.append(aviPath.getSeq()).append(",");// 获取下一节点对应的seq
			}
		}
		if (nextUlocIds.length() > 0) {
			nextUlocIds.deleteCharAt(nextUlocIds.length() - 1);
		}
		if (nextUlocSeqs.length() > 0) {
			nextUlocSeqs.deleteCharAt(nextUlocSeqs.length() - 1);
		}

		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("currentUlocSeq", currentUlocSeq);
		returnMap.put("nextUlocIds", nextUlocIds);
		returnMap.put("nextUlocSeqs", nextUlocSeqs);
		returnMap.put("isSip", isSip);
		returnMap.put("isOffline", isOffline);
		return returnMap;
	}

	@Override
	public String getSNBySNRule(Integer aviId, Integer tmUlocId) {
		ToPorderAvi avi = aviService.findById(aviId, true);
		ToPorder porder = avi.getPorder();
		TmPart part = partService.findById(porder.getTmPartId());
		TmUloc uloc = ulocService.findById(tmUlocId, true);
		NumRuleValue ruleNo = new NumRuleValue(part.getNo(), uloc.getTmLine().getNo(), uloc.getTmPlant().getNo(),
				uloc.getTmWorkshop().getNo(), porder.getNo(), avi.getNo());
		return numRuleService.getSerialNumber(ConstantUtils.SERIALIZABLE_TYPE_SN, ruleNo);
	}

	@Override
	public Map<String, Object> checkWhickNeedBind(Integer aviId, Integer ulocId, String SN, String currentUlocSeq,
			String isSip) {
		TmUloc uloc = ulocService.findById(ulocId);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<ToPorderAviBom> needBindPart = getNeedBindPart(SN, aviId, ulocId, currentUlocSeq);// 绑定关键件
		if (needBindPart != null) {
			returnMap.put("bindPart", needBindPart);
			return returnMap;
		}
		List<ToPorderAviPathParameter> needBindParameter = getNeedBindParameter(SN, aviId, ulocId, currentUlocSeq);// 需要绑定的参数
		if (needBindParameter != null) {
			returnMap.put("bindParameter", needBindParameter);
			return returnMap;
		}
		List<ToPorderAviPathSip> needBindQuality = getNeedBindQuality(SN, aviId, ulocId, currentUlocSeq);// 需要绑定的质量项
		if (needBindQuality != null && ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(isSip)) {
			returnMap.put("bindQuality", needBindQuality);
			return returnMap;
		} else {
			List<TpRecordUlocQuality> ulocAlreadyBindQuality = recordUlocQualityService.getUlocAlreadyBindQuality(SN,
					ulocId, currentUlocSeq);
			if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(isSip)
					&& (ulocAlreadyBindQuality == null || ulocAlreadyBindQuality.size() == 0)) {
				throw new BusinessException("ONLINE_QUALITY_MUST", uloc.getNo());
			}
		}
		return null;
	}

	@Override
	public List<ToPorderAviBom> getNeedBindPart(String SN, Integer aviId, Integer ulocId, String currentUlocSeq) {
		List<ToPorderAviBom> needBindPart = null;
		List<ToPorderAviBom> aviBom = getAviBom(aviId, ulocId, currentUlocSeq);// 关键件信息
		if (aviBom == null || aviBom.size() == 0) {
			return null;
		}
		List<TpRecordUlocPart> ulocAlreadyBindPart = recordUlocPartService.getUlocAlreadyBindPart(SN, ulocId,
				currentUlocSeq);// 已经绑定的关键件
		if (ulocAlreadyBindPart == null || ulocAlreadyBindPart.size() == 0) {
			return aviBom;
		} else {
			Map<String, Integer> map = new HashMap<String, Integer>();
			for (TpRecordUlocPart partBean : ulocAlreadyBindPart) {
				map.put(partBean.getTmPartId().toString(), partBean.getQty());
			}
			needBindPart = new ArrayList<ToPorderAviBom>();// 需要绑定的关键件
			for (ToPorderAviBom bean : aviBom) {
				if (map.containsKey(bean.getTmPartId().toString())) {
					Integer qty = map.get(bean.getTmPartId().toString());
					if (bean.getQty() - qty > 0) {
						bean.setQty(bean.getQty() - qty);
						needBindPart.add(bean);
					}
				} else {
					needBindPart.add(bean);
				}
			}
			if (needBindPart.size() == 0) {
				needBindPart = null;
			}
		}
		return needBindPart;
	}

	private ToPorderAvi getAviByAviNo(String aviNo) {
		QueryCriteria criteria = new QueryCriteria();
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.getQueryCondition().put("no", aviNo);
		return aviService.findBy(criteria).getContent().get(0);
	}

	private TpWip getTpWip(String SN, Integer tmUlocId, String routingSeq) {
		TpWip eg = new TpWip();
		eg.setTmUlocId(tmUlocId);
		eg.setSn(SN);
		eg.setRoutingSeq(routingSeq);
		List<TpWip> wipList = wipService.findByEg(eg);
		return wipList.size() > 0 ? wipList.get(0) : null;
	}

	@Override
	public List<ToPorderAviPathParameter> getNeedBindParameter(String SN, Integer aviId, Integer ulocId,
			String currentUlocSeq) {
		List<ToPorderAviPathParameter> needBindParameter = null;
		List<ToPorderAviPathParameter> aviPathParameter = getAviPathParameter(aviId, ulocId, currentUlocSeq);// 参数信息
		if (aviPathParameter == null || aviPathParameter.size() == 0) {
			return null;
		}
		// 已经绑定的参数
		List<TpRecordUlocParameter> ulocAlreadyBindParameter = recordUlocParameterService
				.getUlocAlreadyBindParameter(SN, ulocId, currentUlocSeq);
		if (ulocAlreadyBindParameter == null || ulocAlreadyBindParameter.size() == 0) {
			return aviPathParameter;
		} else {
			if (ulocAlreadyBindParameter.size() == aviPathParameter.size()) {
				return null;
			}
			needBindParameter = new ArrayList<ToPorderAviPathParameter>();
			Map<String, String> map = new HashMap<String, String>();
			for (TpRecordUlocParameter bean : ulocAlreadyBindParameter) {
				map.put(bean.getParameterCode(), bean.getParameterValue());
			}
			for (ToPorderAviPathParameter bean : aviPathParameter) {
				if (!map.containsKey(bean.getParameter().getCode())) {
					needBindParameter.add(bean);
				}
			}
			if (needBindParameter.size() == 0) {
				needBindParameter = null;
			}
		}
		return needBindParameter;
	}

	@Override
	public List<ToPorderAviPathSip> getNeedBindQuality(String SN, Integer aviId, Integer ulocId,
			String currentUlocSeq) {
		List<ToPorderAviPathSip> needBindSip = null;
		List<ToPorderAviPathSip> aviPathSip = getAviPathSip(aviId, ulocId, currentUlocSeq);// 质量项信息
		if (aviPathSip == null || aviPathSip.size() == 0) {
			return null;
		}
		// 已经绑定的质检项
		List<TpRecordUlocQuality> ulocAlreadyBindQuality = recordUlocQualityService.getUlocAlreadyBindQuality(SN,
				ulocId, currentUlocSeq);
		if (ulocAlreadyBindQuality == null || ulocAlreadyBindQuality.size() == 0) {
			return aviPathSip;
		} else {
			if (ulocAlreadyBindQuality.size() == aviPathSip.size()) {
				return null;
			}
			needBindSip = new ArrayList<ToPorderAviPathSip>();
			Map<String, String> map = new HashMap<String, String>();
			for (TpRecordUlocQuality bean : ulocAlreadyBindQuality) {
				map.put(bean.getCheckItem(), bean.getCheckResult());
			}
			for (ToPorderAviPathSip bean : aviPathSip) {
				if (!map.containsKey(bean.getParameter().getCode())) {
					needBindSip.add(bean);
				}
			}
			if (needBindSip.size() == 0) {
				needBindSip = null;
			}
		}
		return needBindSip;
	}

	@Override
	public String doGenerateSN(Integer aviId, Integer ulocId) {
		ToPorderAvi avi = aviService.findById(aviId, true);
		checkAviAndPorderData(avi);// 检查avi和工单
		int recordCount = getTpRecordCountByAviNoAndPorderNo(avi.getPorder().getNo(), avi.getNo());
		if (recordCount > 0 && avi.getQty().equals(Integer.valueOf(recordCount))) {// 该生产序列已全部生成完毕
			throw new BusinessException("ONLINE_SN_ALL_GENERATE", avi.getNo());
		}
		String sn = getSNBySNRule(aviId, ulocId);
		TpRecord record = new TpRecord();
		record.setSn(sn);
		record.setPorderNo(avi.getPorder().getNo());
		record.setAviNo(avi.getNo());
		record.setTmPartId(avi.getPorder().getTmPartId());
		record.setQty(1);
		recordService.doSave(record);
		doSaveWip(ulocId, avi, sn);
		return sn;
	}

	private void doSaveWip(Integer tmUlocId, ToPorderAvi avi, String sn) {
		TmUloc uloc = this.findUlocById(tmUlocId);
		Map<String, Object> ulocMap = getCurrentUlocSeqAndNextUlocIds(tmUlocId, avi.getId(), false);
		TpWip wip = new TpWip();
		wip.setRoutingSeq(ulocMap.get("currentUlocSeq").toString());
		wip.setSn(sn);
		wip.setTmUlocId(tmUlocId);
		wip.setUlocNo(uloc.getNo());
		wip.setUlocName(uloc.getName());
		wip.setPorderNo(avi.getPorder().getNo());
		wip.setAviNo(avi.getNo());
		wip.setQty(1);
		TmPart part = partService.findById(avi.getPorder().getTmPartId());
		wip.setTmPartId(part.getId());
		wip.setPartNo(part.getNo());
		wip.setPartName(part.getNameCn());
		wip.setNextUloc(ulocMap.get("nextUlocIds").toString());
		wip.setIsHold(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO);
		wip.setStatus(ConstantUtils.PRODUCT_WIP_STATUS_WAIT);
		wipService.doSave(wip);
	}

	private int getTpRecordCountByAviNoAndPorderNo(String porderNo, String aviNo) {
		TpRecord eg = new TpRecord();
		eg.setAviNo(aviNo);
		eg.setPorderNo(porderNo);
		return recordService.findByEg(eg).size();
	}

	@Override
	public Map<String, Object> doScanSN(Integer aviId, Integer ulocId, String SN) {
		Map<String, Object> checkSNAndAviData = checkSNAndAviData(SN, ulocId, aviId);// 校验数据
		TpRecord record = (TpRecord) checkSNAndAviData.get("record");
		ToPorderAvi avi = (ToPorderAvi) checkSNAndAviData.get("avi");
		TpWip wip = (TpWip) checkSNAndAviData.get("wip");
		if (ConstantUtils.PRODUCT_WIP_STATUS_WAIT.equals(wip.getStatus())) {
			ToPorder porder = avi.getPorder();
			porder.setProductStatus(ConstantUtils.ENTRY_CODE_PORDER_PRODUCT_STATUS_PRODUCTING);
			wip.setStatus(ConstantUtils.PRODUCT_WIP_STATUS_PRODUCT);
			wipService.doUpdate(wip);
			TmUloc uloc = this.findUlocById(ulocId);
			TmWorktime workTime = getNowWorkTimeByLineId(uloc.getTmLineId());
			recordUlocService.doSaveTpRecordUloc(record.getId(), ulocId, uloc.getNo(), uloc.getName(), null,
					workTime == null ? "" : workTime.getShiftno(), ConstantUtils.PRODUCT_OPERTION_TYPE_ULOC_START,
					checkSNAndAviData.get("currentUlocSeq").toString(), null);
		}
		Map<String, Object> checkWhickNeedBind = checkWhickNeedBind(aviId, ulocId, SN,
				checkSNAndAviData.get("currentUlocSeq").toString(), checkSNAndAviData.get("isSip").toString());
		if (checkWhickNeedBind != null) {
			checkWhickNeedBind.put("currentUlocSeq", checkSNAndAviData.get("currentUlocSeq"));
		}
		return checkWhickNeedBind;
	}

	@Override
	public Map<String, Object> doConfirmOnline(Integer aviId, Integer ulocId, String SN) {
		Map<String, Object> checkSNAndAviData = checkSNAndAviData(SN, ulocId, aviId);// 校验数据
		ToPorderAvi avi = (ToPorderAvi) checkSNAndAviData.get("avi");
		Map<String, Object> currentUloc = getCurrentUlocSeqAndNextUlocIds(ulocId, avi.getId(), false);
		TpRecord record = (TpRecord) checkSNAndAviData.get("record");
		TpWip wip = (TpWip) checkSNAndAviData.get("wip");

		Map<String, Object> checkWhickNeedBind = checkWhickNeedBind(aviId, ulocId, SN,
				currentUloc.get("currentUlocSeq").toString(), currentUloc.get("isSip").toString());
		if (checkWhickNeedBind != null) {
			checkWhickNeedBind.put("currentUlocSeq", currentUloc.get("currentUlocSeq"));
			return checkWhickNeedBind;
		} else {
			Map<String, Object> returnMap = new HashMap<String, Object>();
			doSaveAndUpdateRealatedBeans(avi, record, wip, ulocId, currentUloc.get("nextUlocIds").toString(),
					currentUloc.get("nextUlocSeqs").toString(), currentUloc.get("currentUlocSeq").toString(),
					currentUloc.get("isOffline").toString(), returnMap);
			return returnMap;
		}
	}

	/**
	 * 保存相关联的bean
	 * 
	 * @param avi
	 * @param record
	 * @param wip
	 * @param tmUlocId
	 * @param nextUlocIds
	 * @param nextUlocSeq
	 * @param currentSeq
	 * @param isOffline
	 * @param map
	 */
	private void doSaveAndUpdateRealatedBeans(ToPorderAvi avi, TpRecord record, TpWip wip, Integer tmUlocId,
			String nextUlocIds, String nextUlocSeq, String currentSeq, String isOffline, Map<String, Object> map) {
		wipService.doDeleteById(wip.getId());
		Date currentTime = new Date();
		avi.setOnlineQty(avi.getOnlineQty() == null ? 1 : (avi.getOnlineQty() + 1));
		if (avi.getOnlineQty().equals(avi.getQty())) {
			avi.setStatus(ConstantUtils.ENTRY_CODE_AVI_STATUS_ALL);
		} else {
			avi.setStatus(ConstantUtils.ENTRY_CODE_AVI_STATUS_PART);
		}
		ToPorder porder = avi.getPorder();
		porder.setOnlineQty(porder.getOnlineQty() == null ? 1 : (porder.getOnlineQty() + 1));
		record.setOnlineTime(currentTime);
		porder.setProductStatus(ConstantUtils.ENTRY_CODE_PORDER_PRODUCT_STATUS_PRODUCTING);
		TmUloc uloc = this.findUlocById(tmUlocId);
		TmWorktime workTime = getNowWorkTimeByLineId(uloc.getTmLineId());
		recordUlocService.doSaveTpRecordUloc(record.getId(), tmUlocId, uloc.getNo(), uloc.getName(), nextUlocIds,
				workTime == null ? "" : workTime.getShiftno(), ConstantUtils.PRODUCT_OPERTION_TYPE_ULOC_END, currentSeq,
				"");
		if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(isOffline)) {// 如果是下线点
			checkIsHaveNc(wip.getSn());
			avi.setOfflineQty(avi.getOfflineQty() == null ? 1 : (avi.getOfflineQty() + 1));
			if (avi.getOfflineQty().equals(avi.getQty())) {
				avi.setStatus(ConstantUtils.ENTRY_CODE_AVI_STATUS_FINSH);
			}
			porder.setOfflineQty(porder.getOfflineQty() == null ? 1 : (porder.getOfflineQty() + 1));
			if (porder.getOfflineQty().equals(porder.getQty())) {
				porder.setProductStatus(ConstantUtils.ENTRY_CODE_PORDER_PRODUCT_STATUS_FINSH);
			}
			record.setOfflineTime(currentTime);
		} else {
			List<TpWip> wipList = new ArrayList<TpWip>();
			String nextUlocs[] = nextUlocIds.split(",");
			String nextUlocSeqs[] = nextUlocSeq.split(",");
			for (int i = 0; i < nextUlocs.length; i++) {
				TpWip bean = new TpWip();
				BeanUtils.copyProperties(wip, bean, "id", "nextUloc", "status", "isHold");
				if (StringUtil.isNotBlank(nextUlocs[i].trim())) {
					TmUloc nextUloc = this.findUlocById(Integer.valueOf(nextUlocs[i].trim()));
					bean.setTmUlocId(nextUloc.getId());
					bean.setUlocNo(nextUloc.getNo());
					bean.setUlocName(nextUloc.getName());
				}
				bean.setRoutingSeq(nextUlocSeqs[i].trim());
				bean.setStatus(ConstantUtils.PRODUCT_WIP_STATUS_WAIT);
				bean.setIsHold(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO);
				bean.setNextUloc(getNextUloc(bean.getAviNo(), bean.getRoutingSeq()));
				bean.setScanDate(currentTime);
				wipList.add(bean);
			}
			wipService.doSaveBatch(wipList);
		}
		recordService.doUpdate(record);
		aviService.doUpdate(avi);
		porderService.doUpdate(porder);
	}

	/**
	 * 获取下一工位信息
	 * 
	 * @param aviNo
	 * @param currentUlocSeq
	 * @return
	 */
	private String getNextUloc(String aviNo, String currentUlocSeq) {
		QueryCriteria criteria = new QueryCriteria();
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.getQueryCondition().put("avi.no", aviNo);
		Map<String, ToPorderAviPath> seqBeanMap = new HashMap<String, ToPorderAviPath>();
		Map<Integer, List<Integer>> parentUlocMap = new HashMap<Integer, List<Integer>>();
		List<ToPorderAviPath> content = aviPathService.findBy(criteria).getContent();
		for (ToPorderAviPath bean : content) {
			seqBeanMap.put(bean.getSeq(), bean);
			if (StringUtil.isNotBlank(bean.getParentId())) {
				String[] parentIds = bean.getParentId().split(",");
				for (String parentId : parentIds) {
					if (parentUlocMap.containsKey(Integer.parseInt(parentId))) {
						parentUlocMap.get(Integer.parseInt(parentId)).add(bean.getTmUlocId());
					} else {
						List<Integer> list = new ArrayList<Integer>();
						list.add(bean.getTmUlocId());
						parentUlocMap.put(Integer.parseInt(parentId), list);
					}
				}
			}
		}
		StringBuffer nextUloc = new StringBuffer();
		if (seqBeanMap.containsKey(currentUlocSeq)) {
			ToPorderAviPath bean = seqBeanMap.get(currentUlocSeq);
			List<Integer> list = parentUlocMap.get(bean.getId());
			if (null != list) {
				for (Integer integer : list) {
					nextUloc.append(integer).append(",");
				}
			}

		}
		if (nextUloc.length() > 0) {
			nextUloc.deleteCharAt(nextUloc.length() - 1);
		}
		return nextUloc.toString();
	}

	@Override
	public Map<String, String> doCheckSN(Integer toPorderAviId, Integer tmUlocId, String SN) {
		Map<String, String> map = new HashMap<String, String>();
		Map<String, Object> checkSNAndAviData = checkSNAndAviData(SN, tmUlocId, toPorderAviId);
		map.put("currentSeq", checkSNAndAviData.get("currentUlocSeq").toString());
		ToPorderAvi avi = (ToPorderAvi) checkSNAndAviData.get("avi");
		map.put("aviId", avi.getId().toString());
		return map;
	}

	@Override
	public void doSaveNcGroupAndUntreated(Integer recordId, String shiftNo, Integer tmUlocId, String currentUlocSeq,
			String[] qualityCode, String[] checkResult, String[] qualityNote, Integer[] ncGroup, Integer[] ncCode,
			String[] ncTypeInput, String SN) {
		TmUloc uloc = this.findUlocById(tmUlocId);
		TpRecordUloc recordUloc = recordUlocService.doSaveTpRecordUloc(recordId, tmUlocId, uloc.getNo(), uloc.getName(),
				"", shiftNo, ConstantUtils.PRODUCT_OPERTION_TYPE_RECORD_QUALITY_END, currentUlocSeq, "点击确认");

		recordUlocQualityService.doSaveTpRecordUlocQuality(recordUloc.getId(), qualityCode, checkResult, qualityNote,
				recordId);
		TpWip wip = wipService.getTpWip(SN, tmUlocId, currentUlocSeq);
		for (String str : ncTypeInput) {
			if (ConstantUtils.ENTRY_NC_TYPE_REPAIRED.equals(str)) {
				wip.setStatus(ConstantUtils.PRODUCT_WIP_STATUS_WAIT_REPAIR);
				wipService.doUpdate(wip);
				break;
			}
		}
		untreatedNcService.doSaveUnTratedNc(SN, ncGroup, ncCode, ncTypeInput, null, tmUlocId, currentUlocSeq);
	}

	private Map<String, String> getPartCheckRuleMap(StringBuffer str) {
		Map<String, String> map = new HashMap<String, String>();
		int start = str.indexOf("{");
		int end = str.indexOf("}");
		String finalString = null;
		String type = null;
		if (start != -1 && start != 0) {
			finalString = str.substring(0, start);
			str.delete(0, start);
		} else if (start == -1) {
			finalString = str.toString();
			start = str.length();
			str.delete(0, start);
		} else {
			type = str.substring(start + 1, end);
			str.delete(start, end + 1);
		}

		String typeStart = null;
		String typeEnd = null;
		if (type != null) {
			int a = type.indexOf("[");
			int b = type.indexOf("]");
			String[] split = type.substring(a + 1, b).split(",");
			typeStart = split[0];
			typeEnd = split[1];
			type = type.substring(0, a);
		} else {
			typeStart = "0";
			typeEnd = start + "";
		}
		map.put("type", type);
		map.put("finalString", finalString);
		map.put("start", typeStart + "");
		map.put("end", typeEnd + "");
		return map;
	}

	private int getPartBarCodeCount(String partBarCode) {
		TpRecordUlocPart bean = new TpRecordUlocPart();
		bean.setBarCode(partBarCode);
		return recordUlocPartService.findByEg(bean).size();
	}

	private Map<String, Object> needBindPartMap(List<ToPorderAviBom> needBindPart) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (ToPorderAviBom bean : needBindPart) {
			map.put(bean.getTmPartId().toString(), bean);
		}
		return map;
	}

	@Override
	public Map<String, String> checkPartBarCode(List<ToPorderAviBom> needBindPart, Integer tmPlantId,
			Integer tmWorkshopId, Integer tmLineId, String partBarCode) {
		if (needBindPart == null || needBindPart.size() == 0) {// 不需要绑定物料
			throw new BusinessException("PRODUCTION_NOT_NEED_BINDING_PART");
		}
		Map<String, Object> needBindPartMap = needBindPartMap(needBindPart);
		if (getPartBarCodeCount(partBarCode) > 0) {// 物料条码已经使用
			throw new BusinessException("PRODUCTION_PART_BAR_CODE_USED");
		}
		StringBuffer barCode = new StringBuffer(partBarCode);// 物料条码
		TsNumRule numRule = numRuleService.findByType(ConstantUtils.SERIALIZABLE_TYPE_PART_BAR_CHECK);
		if (numRule.getLength() != null && numRule.getLength() != partBarCode.length()) {// 条码长度有误
			throw new BusinessException("PRODUCTION_PART_BAR_CODE_LENGTH_ERROR", numRule.getLength(),
					partBarCode.length());
		}

		StringBuffer partBarCheckRule = new StringBuffer();// 物料 校验规则
		if (StringUtil.isNotBlank(numRule.getPrefix())) {// 前缀
			partBarCheckRule.append(numRule.getPrefix());
		}
		if (StringUtil.isNotBlank(numRule.getInfix())) {// 中缀
			partBarCheckRule.append(numRule.getInfix());
		}
		if (StringUtil.isNotBlank(numRule.getSuffix())) {// 后缀
			partBarCheckRule.append(numRule.getSuffix());
		}
		Map<String, String> map = new HashMap<String, String>();
		try {
			while (partBarCheckRule.length() > 0) {
				getPartDataMap(tmPlantId, tmWorkshopId, tmLineId, partBarCode, barCode, partBarCheckRule, map);
			}
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			Log.error(ExceptionUtils.getFullStackTrace(e));
			throw new BusinessException("PRODUCTION_PART_BAR_CODE_CHECK_RULE_ERROR");
		}
		if (map.isEmpty() || !map.containsKey("tmPartId")) {
			throw new BusinessException("PRODUCTION_PART_BAR_CODE_NOT_PART");
		}
		Integer tmPartId = Integer.valueOf(map.get("tmPartId"));
		if (map.containsKey("tmSupplierId")) {
			Integer tmSupplierId = Integer.valueOf(map.get("tmSupplierId"));
			TmSupplierPart supplierPart = new TmSupplierPart();
			supplierPart.setTmSupplierId(tmSupplierId);
			supplierPart.setTmPartId(tmPartId);
			if (supplierPartService.findByEg(supplierPart).size() == 0) {
				throw new BusinessException("PRODUCTION_PART_BAR_CODE_SUPPLIER_PART_ERROR");
			}
		}
		if (!needBindPartMap.containsKey(tmPartId.toString())) {
			throw new BusinessException("PRODUCTION_PART_BAR_CODE_NOT_NEED_THIS_PART");
		}
		return map;
	}

	private void getPartDataMap(Integer tmPlantId, Integer tmWorkshopId, Integer tmLineId, String partBarCode,
			StringBuffer barCode, StringBuffer partBarCheckRule, Map<String, String> map) {
		Map<String, String> partCheckRuleMap = getPartCheckRuleMap(partBarCheckRule);

		int start = Integer.valueOf(partCheckRuleMap.get("start"));
		int end = Integer.valueOf(partCheckRuleMap.get("end"));

		if (partCheckRuleMap.get("finalString") != null) {// 字符串
			if (barCode.length() < end) {
				throw new BusinessException("PARODUCTION_PART_BAR_CODE_STRING_NOT_MATCH",
						partCheckRuleMap.get("finalString"));
			}
			String finalStr = barCode.substring(start, end);
			if (!finalStr.equals(partCheckRuleMap.get("finalString"))) {// 字符串不批配
				throw new BusinessException("PARODUCTION_PART_BAR_CODE_STRING_NOT_MATCH",
						partCheckRuleMap.get("finalString"));
			}
			barCode.delete(start, end);
		} else {
			barCode.delete(0, end - start);
			String ruleType = partCheckRuleMap.get("type");
			if ("PLANT".equals(ruleType)) {// 工厂
				String no = partBarCode.subSequence(start, end).toString();
				TmPlant eg = new TmPlant();
				eg.setNo(no);
				if (plantService.findByEg(eg).size() == 0) {// 工厂错误
					throw new BusinessException("PRODUCTION_PART_BAR_CODE_PLANT_ERROR");
				} else {
					eg = plantService.findByEg(eg).get(0);
					if (!tmPlantId.equals(eg.getId())) {
						throw new BusinessException("PRODUCTION_PART_BAR_CODE_NOT_THIS_PLANT_ERROR");
					}
				}
			} else if ("WORKSHOP".equals(ruleType)) {
				String no = partBarCode.subSequence(start, end).toString();
				TmWorkshop eg = new TmWorkshop();
				eg.setNo(no);
				if (workshopService.findByEg(eg).size() == 0) {// 车间错误
					throw new BusinessException("PRODUCTION_PART_BAR_CODE_WORKSHOP_ERROR");
				} else {
					eg = workshopService.findByEg(eg).get(0);
					if (!tmWorkshopId.equals(eg.getId())) {
						throw new BusinessException("PRODUCTION_PART_BAR_CODE_PART_WORKSHOP_ERROR");
					}
				}
			} else if ("LINE".equals(ruleType)) {
				String no = partBarCode.subSequence(start, end).toString();
				TmLine eg = new TmLine();
				eg.setNo(no);
				if (lineService.findByEg(eg).size() == 0) {// 产线错误
					throw new BusinessException("PRODUCTION_PART_BAR_CODE_LINE_ERROR");
				} else {
					eg = lineService.findByEg(eg).get(0);
					if (!eg.getId().equals(tmLineId)) {
						throw new BusinessException("PRODUCTION_PART_BAR_CODE_PART_LINE_ERROR");
					}
				}
			} else if ("PART".equals(ruleType)) {
				String no = partBarCode.subSequence(start, end).toString();
				TmPart eg = new TmPart();
				eg.setNo(no);
				eg.setTmPlantId(tmPlantId);
				List<TmPart> findByEg = partService.findByEg(eg);
				if (findByEg.size() == 0) {// 物料错误
					throw new BusinessException("PRODUCTION_PART_BAR_CODE_PART_ERROR");
				} else {
					eg = findByEg.get(0);
					map.put("tmPartId", eg.getId().toString());
					map.put("partNo", eg.getNo());
					map.put("partName", eg.getNameCn());
				}
			} else if ("SUPPLIER".equals(ruleType)) {
				String no = partBarCode.subSequence(start, end).toString();
				TmSupplier eg = new TmSupplier();
				eg.setNo(no);
				List<TmSupplier> findByEg = supplierService.findByEg(eg);
				if (findByEg.size() == 0) {// 供应商不存在
					throw new BusinessException("PRODUCTION_SUPPLIER_BAR_CODE_SUPPLIER_ERROR");
				} else {
					eg = findByEg.get(0);
					map.put("tmSupplierId", eg.getId().toString());
				}
			}
		}
	}

	@Override
	public Map<String, Integer> getCrossQty(TmWorktime workTime, Integer tmUlocId) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		if (workTime == null) {
			Date currentData = new Date();
			// 总数量
			Integer allQty = this.getUlocCrossQty(tmUlocId, "'" + ConstantUtils.PRODUCT_OPERTION_TYPE_ULOC_END + "'",
					DateUtils.formatDate(currentData) + " 00:00:00", DateUtils.formatDateTime(currentData));
			// 记录不合格和报废数量
			Integer notQualityQty = this.getUlocCrossQty(tmUlocId,
					"'" + ConstantUtils.PRODUCT_OPERTION_TYPE_RECORD_NC_END + "','"
							+ ConstantUtils.PRODUCT_OPERTION_TYPE_RECORD_SCRAP + "'",
					DateUtils.formatDate(currentData) + " 00:00:00", DateUtils.formatDateTime(currentData));
			map.put("qualityQty", allQty == 0 ? 0 : (allQty - notQualityQty));
			map.put("unQualityQty", notQualityQty);
		} else {
			// 总数量
			// Integer allQty = this.getUlocCrossQty(tmUlocId, "'" +
			// ConstantUtils.PRODUCT_OPERTION_TYPE_ULOC_END + "'",
			// DateUtils.formatDate(workTime.getWorkDate()) + " "
			// + DateUtils.formatDate(workTime.getStartTime(),
			// DateUtils.FORMAT_TIME_HH_MM_SS),
			// DateUtils.formatDate(workTime.getWorkDate()) + " "
			// + DateUtils.formatDate(workTime.getEndTime(),
			// DateUtils.FORMAT_TIME_HH_MM_SS));
			// //记录不合格和报废数量
			// Integer notQualityQty = this.getUlocCrossQty(tmUlocId,
			// "'" + ConstantUtils.PRODUCT_OPERTION_TYPE_RECORD_NC_END + "','"
			// + ConstantUtils.PRODUCT_OPERTION_TYPE_RECORD_SCRAP + "'",
			// DateUtils.formatDate(workTime.getWorkDate()) + " "
			// + DateUtils.formatDate(workTime.getStartTime(),
			// DateUtils.FORMAT_TIME_HH_MM_SS),
			// DateUtils.formatDate(workTime.getWorkDate()) + " "
			// + DateUtils.formatDate(workTime.getEndTime(),
			// DateUtils.FORMAT_TIME_HH_MM_SS));
			// map.put("qualityQty", allQty == 0 ? 0 : (allQty -
			// notQualityQty));
			// map.put("unQualityQty", notQualityQty);
		}
		return map;
	}

	@Override
	public TmUloc findUlocById(Integer id) {
		return dao.findUloc(id);
	}

	@Override
	public List<TmUlocSipNc> findTmUlocSipNcByTmUlocId(Integer tmUlocId) {
		return dao.findTmUlocSipNcByTmUlocId(tmUlocId);
	}

	@Override
	public void checkIsHaveNc(String SN) {
		String valueByKey = globalConfigurationService
				.getValueByKey(GlobalConfigurationUtils.GLOBAL_CONFIGUR_IS_ALLOW_HAVE_NC_OFFLINE);
		if (getUnTreatedNcBySN(SN) > 0 && "false".equals(valueByKey)) {
			throw new BusinessException("PRODUCTION_NOT_ALLOW_HAVE_NC_OFFLINE");
		}
	}

	@Override
	public int getUnTreatedNcBySN(String SN) {
		return dao.getUnTreatedNcBySN(SN);
	}
}
