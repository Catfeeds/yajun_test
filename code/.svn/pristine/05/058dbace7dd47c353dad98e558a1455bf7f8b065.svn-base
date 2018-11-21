package com.wis.mes.production.regulate.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.GlobalConfigurationUtils;
import com.wis.basis.systemadmin.service.UserService;
import com.wis.core.context.WebContextHolder;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.part.entity.TmPart;
import com.wis.mes.master.part.service.TmPartService;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.workcalendar.entity.TmWorktime;
import com.wis.mes.production.plan.porder.entity.ToPorder;
import com.wis.mes.production.plan.porder.entity.ToPorderAvi;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPath;
import com.wis.mes.production.plan.porder.service.ToPorderAviPathService;
import com.wis.mes.production.plan.porder.service.ToPorderAviService;
import com.wis.mes.production.plan.porder.service.ToPorderService;
import com.wis.mes.production.record.entity.TpRecord;
import com.wis.mes.production.record.service.TpRecordService;
import com.wis.mes.production.record.service.TpRecordUlocService;
import com.wis.mes.production.regulate.dao.CrossPointDao;
import com.wis.mes.production.regulate.service.CrossPointService;
import com.wis.mes.production.regulate.service.OnlineService;
import com.wis.mes.production.wip.entity.TpWip;
import com.wis.mes.production.wip.service.TpWipService;
import com.wis.mes.util.StringUtil;

@Service
public class CrossPointServiceImpl implements CrossPointService {
	@Autowired
	private UserService userService;
	@Autowired
	private CrossPointDao dao;
	@Autowired
	private TpRecordService recordService;
	@Autowired
	private TpWipService wipService;
	@Autowired
	private ToPorderService porderService;
	@Autowired
	private ToPorderAviService aviService;
	@Autowired
	private ToPorderAviPathService aviPathService;
	@Autowired
	private TmPartService partService;
	@Autowired
	private OnlineService onlineService;
	@Autowired
	private TpRecordUlocService recordUlocService;
	@Autowired
	private GlobalConfigurationService globalConfigurationService;

	@Override
	public List<TmUloc> getCrossPointUlocByLineId(Integer tmLineId) {
		if (tmLineId == null) {
			return new ArrayList<TmUloc>();
		}
		String queryUlocPermissionByUserId = userService
				.queryUlocPermissionByUserId(WebContextHolder.getCurrentUser().getId());
		return dao.getCrossPointUlocByLineId(tmLineId, queryUlocPermissionByUserId);
	}

	/**
	 * 根据生产序列编号查询
	 * 
	 * @param aviNo
	 * @return
	 */
	private ToPorderAvi getAviByAviNo(String aviNo) {
		QueryCriteria criteria = new QueryCriteria();
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.getQueryCondition().put("no", aviNo);
		return aviService.findBy(criteria).getContent().get(0);
	}

	/**
	 * 获取当前节点的seq以及下一节点的信息
	 * 
	 * @param aviId
	 *            工艺路径ID
	 * @param tmUlocId
	 *            工位ID
	 * @param SN
	 * @param isOnlyGetCurrentSeq
	 *            是否只获取当前seq
	 * @return key:isSip/isOffline/currentUlocSeq/nextUlocIds/nextUlocSeqs
	 */
	public Map<String, Object> getCurrentUlocSeqAndNextUlocIds(Integer aviId, Integer tmUlocId, String SN,
			boolean isOnlyGetCurrentSeq) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String lastRountingSeq = dao.getLastRountingSeq(SN);//上一工位顺序号
		List<ToPorderAviPath> aviPaths = aviPathService.getAviPaths(aviId, false);
		//艺路径ID-->
		Map<String, ToPorderAviPath> pathMap = new HashMap<String, ToPorderAviPath>();
		//从哪个节点到哪个节点
		Map<String, List<String>> fromUlocTo = new HashMap<String, List<String>>();
		//顺序号--->工艺路径ID
		Map<String, String> seqPathIdMap = new HashMap<String, String>();

		for (ToPorderAviPath bean : aviPaths) {
			pathMap.put(bean.getId().toString(), bean);
			seqPathIdMap.put(bean.getSeq(), bean.getId().toString());

			if (StringUtil.isNotBlank(bean.getParentId())) {//父节点
				String[] parentIds = bean.getParentId().split(",");
				for (String parent : parentIds) {
					if (fromUlocTo.containsKey(parent)) {
						List<String> sonList = fromUlocTo.get(parent);
						sonList.add(bean.getId().toString());
					} else {
						List<String> sonList = new ArrayList<String>();
						sonList.add(bean.getId().toString());
						fromUlocTo.put(parent, sonList);
					}
				}
			}
		}
		String pathUlocId = seqPathIdMap.get(lastRountingSeq);
		List<String> sonIds = fromUlocTo.get(pathUlocId);
		StringBuffer nextUlocIds = new StringBuffer();
		StringBuffer nextUlocSeqs = new StringBuffer();
		for (String son : sonIds) {
			ToPorderAviPath bean = pathMap.get(son);
			if (bean.getTmUlocId().equals(tmUlocId)) {
				returnMap.put("isSip", bean.getIsSip());//是否质检点
				returnMap.put("isOffline", bean.getIsOffline());//是否下线点
				returnMap.put("currentUlocSeq", bean.getSeq());//当前工位顺序号

				if (!isOnlyGetCurrentSeq) {//获取当前节点的下一节点ulocIds,下一节点的顺序号
					List<String> list = fromUlocTo.get(bean.getId().toString());//当前节点的下一节点信息
					if (list != null) {
						for (int i = 0; i < list.size(); i++) {
							String sonId = list.get(i);
							ToPorderAviPath path = pathMap.get(sonId);
							nextUlocIds.append(path.getTmUlocId()).append(",");
							nextUlocSeqs.append(path.getSeq()).append(",");
						}
					}
					if (nextUlocIds.length() > 0) {
						nextUlocIds.deleteCharAt(nextUlocIds.length() - 1);
						nextUlocSeqs.deleteCharAt(nextUlocSeqs.length() - 1);
					}
					returnMap.put("nextUlocIds", nextUlocIds);//下一节点ids
					returnMap.put("nextUlocSeqs", nextUlocSeqs);//下一节点顺序号
				}
				break;
			}
		}
		return returnMap;
	}

	private TpWip getTpWip(String SN, Integer tmUlocId, String routingSeq) {
		TpWip eg = new TpWip();
		eg.setTmUlocId(tmUlocId);
		eg.setSn(SN);
		eg.setRoutingSeq(routingSeq);
		List<TpWip> wipList = wipService.findByEg(eg);
		return wipList.size() > 0 ? wipList.get(0) : null;
	}

	/**
	 * 检查SN,并返回SN相关联的数据
	 * 
	 * @param SN
	 * @param tmUlocId
	 * @param isOnlyGetCurrentSeq
	 * @return key:
	 *         avi/record/wip/isSip/isOffline/currentUlocSeq/nextUlocIds/nextUlocSeqs
	 */
	@Override
	public Map<String, Object> checkSN(String SN, Integer tmUlocId, boolean isOnlyGetCurrentSeq) {
		TpRecord record = recordService.getTpRecordBySN(SN);
		if (record == null) {
			throw new BusinessException("ONLINE_SN_NOT_EXIST");
		}
		if (record.getOfflineTime() != null) {
			throw new BusinessException("CROSS_POINT_SN_IS_OFFLINE");
		}

		ToPorderAvi avi = getAviByAviNo(record.getAviNo());
		checkAviAndPorderData(avi);
		TmWorktime workTime = onlineService.getNowWorkTimeByLineId(avi.getTmLineId());
		String value = globalConfigurationService
				.getValueByKey(GlobalConfigurationUtils.GLOBAL_CONFIGUR_WORKTIME_IS_REQUIRED);
		if (workTime == null && Boolean.valueOf(value)) {
			throw new BusinessException("WORK_TIME_MUST");
		}
		Map<String, Object> currentUloc = getCurrentUlocSeqAndNextUlocIds(avi.getId(), tmUlocId, SN,
				isOnlyGetCurrentSeq);
		TpWip wip = getTpWip(SN, tmUlocId, String.valueOf(currentUloc.get("currentUlocSeq")));

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
		if (record.getOnlineTime() == null) {//SN没有上线
			throw new BusinessException("ONLINE_NOT_ONLINE", record.getSn());
		}
		if (ConstantUtils.PRODUCT_WIP_STATUS_REPAIRD.equals(wip.getStatus())) {//正在返修
			throw new BusinessException("ONLINE_WIP_REPAIRD", record.getSn());
		}
		if (ConstantUtils.PRODUCT_WIP_STATUS_WAIT_REPAIR.equals(wip.getStatus())) {//等待返修
			throw new BusinessException("ONLINE_WIP_WAIT_REPAIRD", record.getSn());
		}
		if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(wip.getIsHold())) {//SN被HOLD
			throw new BusinessException("ONLINE_WIP_HOLD", wip.getSn());
		}

		if (ConstantUtils.PRODUCT_WIP_STATUS_WAIT.equals(wip.getStatus())) {
			dao.doDeleteNotScanSNFromWip(SN, wip.getId());//删除其它SN
			wip.setStatus(ConstantUtils.PRODUCT_WIP_STATUS_PRODUCT);
			wip.setNextUloc(currentUloc.get("nextUlocIds").toString());
			wipService.doUpdate(wip);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("avi", avi);
		map.put("record", record);
		map.put("wip", wip);
		map.putAll(currentUloc);
		return map;
	}

	/**
	 * 检查生产序列和工单
	 * 
	 * @param aviId
	 */
	private void checkAviAndPorderData(ToPorderAvi avi) {
		if (avi != null) {
			if (ConstantUtils.ENTRY_CODE_AVI_STATUS_CLOSE.equals(avi.getStatus())) {//生产序列已关闭
				throw new BusinessException("ONLINE_AVI_CLOSE", avi.getNo());
			}
			if (ConstantUtils.ENTRY_CODE_AVI_STATUS_FINSH.equals(avi.getStatus())) {//生产序列已生产完成
				throw new BusinessException("ONLINE_AVI_FINSH", avi.getNo());
			}
			if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(avi.getIsHold())) {//生产序列被HOLD
				throw new BusinessException("ONLINE_AVI_HOLD", avi.getNo());
			}

			ToPorder porder = avi.getPorder();
			if (ConstantUtils.ENTRY_CODE_PORDER_PRODUCT_STATUS_CLOSE.equals(porder.getProductStatus())) {//工单已关闭
				throw new BusinessException("ONLINE_PORDER_CLOSE", porder.getNo());
			}
			if (ConstantUtils.ENTRY_CODE_PORDER_PRODUCT_STATUS_FINSH.equals(porder.getProductStatus())) {//工单已生产完成
				throw new BusinessException("ONLINE_PORDER_FINSH", porder.getNo());
			}
			if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(porder.getIsHold())) {//工单被HOLD
				throw new BusinessException("ONLINE_PORDER_HOLD", porder.getNo());
			}
		}
	}

	@Override
	public Map<String, Object> doScanSN(Integer tmUlocId, String SN) {
		Map<String, Object> checkSN = checkSN(SN, tmUlocId, false);//检查SN
		ToPorderAvi avi = (ToPorderAvi) checkSN.get("avi");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("currentUlocSeq", checkSN.get("currentUlocSeq").toString());//当前工位顺序号
		map.put("aviId", avi.getId());
		map.put("porderNo", avi.getPorder().getNo());
		TmPart part = partService.findById(avi.getPorder().getTmPartId());
		map.put("partNo", part.getNo());
		map.put("partName", part.getNameCn());
		Map<String, Object> checkWhickNeedBind = onlineService.checkWhickNeedBind(avi.getId(), tmUlocId, SN,
				checkSN.get("currentUlocSeq").toString(), checkSN.get("isSip").toString());
		if (checkWhickNeedBind != null) {
			map.putAll(checkWhickNeedBind);
		}
		return map;
	}

	@Override
	public Map<String, Object> doConfirmCrossPoint(Integer aviId, Integer ulocId, String SN) {
		Map<String, Object> checkSNAndAviData = checkSN(SN, ulocId, false);//校验数据
		ToPorderAvi avi = (ToPorderAvi) checkSNAndAviData.get("avi");
		TpRecord record = (TpRecord) checkSNAndAviData.get("record");
		TpWip wip = (TpWip) checkSNAndAviData.get("wip");
		Map<String, Object> checkWhickNeedBind = onlineService.checkWhickNeedBind(aviId, ulocId, SN,
				checkSNAndAviData.get("currentUlocSeq").toString(), checkSNAndAviData.get("isSip").toString());
		if (checkWhickNeedBind != null) {
			checkWhickNeedBind.put("currentUlocSeq", checkSNAndAviData.get("currentUlocSeq"));
			return checkWhickNeedBind;
		} else {
			Map<String, Object> returnMap = new HashMap<String, Object>();
			doSaveAndUpdateRealatedBeans(avi, record, wip, ulocId, checkSNAndAviData.get("nextUlocIds").toString(),
					checkSNAndAviData.get("nextUlocSeqs").toString(),
					checkSNAndAviData.get("currentUlocSeq").toString(), checkSNAndAviData.get("isOffline").toString(),
					returnMap);
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
		ToPorder porder = avi.getPorder();
		TmUloc uloc = onlineService.findUlocById(tmUlocId);
		TmWorktime workTime = onlineService.getNowWorkTimeByLineId(uloc.getTmLineId());
		recordUlocService.doSaveTpRecordUloc(record.getId(), tmUlocId, uloc.getNo(), uloc.getName(), nextUlocIds,
				workTime == null ? "" : workTime.getShiftno(), ConstantUtils.PRODUCT_OPERTION_TYPE_ULOC_END, currentSeq,
				"");
		if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(isOffline)) {//如果是下线点
			onlineService.checkIsHaveNc(wip.getSn());
			avi.setOfflineQty(avi.getOfflineQty() == null ? 1 : (avi.getOfflineQty() + 1));
			if (avi.getOfflineQty().equals(avi.getQty())) {
				avi.setStatus(ConstantUtils.ENTRY_CODE_AVI_STATUS_FINSH);
			}
			porder.setOfflineQty(porder.getOfflineQty() == null ? 1 : (porder.getOfflineQty() + 1));
			if (porder.getOfflineQty().equals(porder.getQty())) {
				porder.setProductStatus(ConstantUtils.ENTRY_CODE_PORDER_PRODUCT_STATUS_FINSH);
			}
			record.setOfflineTime(currentTime);
			recordService.doUpdate(record);
			aviService.doUpdate(avi);
			porderService.doUpdate(porder);
		} else {
			List<TpWip> wipList = new ArrayList<TpWip>();
			if (StringUtil.isNotBlank(nextUlocIds)) {
				String nextUlocs[] = nextUlocIds.split(",");
				String nextUlocSeqs[] = nextUlocSeq.split(",");
				for (int i = 0; i < nextUlocs.length; i++) {
					TpWip bean = new TpWip();
					BeanUtils.copyProperties(wip, bean, "id", "nextUloc", "status", "isHold");
					TmUloc nextUloc = onlineService.findUlocById(Integer.valueOf(nextUlocs[i].trim()));
					bean.setTmUlocId(nextUloc.getId());
					bean.setUlocNo(nextUloc.getNo());
					bean.setUlocName(nextUloc.getName());
					bean.setRoutingSeq(nextUlocSeqs[i].trim());
					bean.setStatus(ConstantUtils.PRODUCT_WIP_STATUS_WAIT);
					bean.setIsHold(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO);
					bean.setScanDate(currentTime);
					wipList.add(bean);
				}
			}
			wipService.doSaveBatch(wipList);
		}
	}

	@Override
	public List<TpWip> getWaitCrossSNByUlocId(Integer tmUlocId) {
		return dao.getWaitCrossSNByUlocId(tmUlocId);
	}

	@Override
	public Map<String, Object> holdCheckSn(String SN, Integer tmUlocId) {
		Map<String,Object> map = new HashMap<String,Object>();
		TpRecord record = recordService.getTpRecordBySN(SN);
		if (record == null) {
			throw new BusinessException("ONLINE_SN_NOT_EXIST");
		}
		if (record.getOfflineTime() != null) {
			throw new BusinessException("CROSS_POINT_SN_IS_OFFLINE");
		}
		TpWip eg = new TpWip();
		eg.setTmUlocId(tmUlocId);
		eg.setSn(SN);
		eg = wipService.findUniqueByEg(eg);
		if(eg == null){
			throw new BusinessException("ONLINE_SN_NOT_IN_CURRENT_ULOC"); 
		}
		map.put("isHold",eg.getIsHold());
		map.put("wipId", eg.getId());
		return map;
	}

}
