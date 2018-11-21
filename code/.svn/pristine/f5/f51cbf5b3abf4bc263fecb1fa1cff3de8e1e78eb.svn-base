package com.wis.mes.production.plan.porder.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.BaseExcelUtil;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.numRule.entity.NumRuleValue;
import com.wis.basis.numRule.service.TsNumRuleService;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.production.plan.porder.dao.ToPorderDao;
import com.wis.mes.production.plan.porder.entity.ToPorder;
import com.wis.mes.production.plan.porder.entity.ToPorderAvi;
import com.wis.mes.production.plan.porder.service.ToPorderAviService;
import com.wis.mes.production.plan.porder.service.ToPorderService;

@Service
public class ToPorderServiceImpl extends BaseServiceImpl<ToPorder> implements ToPorderService {

	@Autowired
	public void setDao(ToPorderDao dao) {
		this.dao = dao;
	}

	@Autowired
	private TsNumRuleService numRuleService;
	@Autowired
	private ToPorderAviService aviService;
	@Autowired
	private DictEntryService entryService;

	@Override
	public ToPorder doSavePorder(ToPorder bean) {
		bean.setInstorageStatus(ConstantUtils.ENTRY_CODE_PORDER_INSTORAGE_STATUS_NOT);
		bean.setIsHold(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO);
		bean.setIsErp(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO);
		bean.setProductStatus(ConstantUtils.ENTRY_CODE_PORDER_PRODUCT_STATUS_NEW);
		bean.setTaskStatus(ConstantUtils.ENTRY_CODE_PORDER_TASK_STATUS_NOT);
		bean.setNo(String.valueOf(new Random().nextInt(100000)));
		ToPorder porder = doSave(bean);
		porder = findById(porder.getId(), true);
		NumRuleValue seq = getSeqRuleNo(porder);
		porder.setNo(numRuleService.getSerialNumber(ConstantUtils.SERIALIZABLE_TYPE_PORDER_NO, seq));
		doUpdate(porder);
		return porder;
	}

	@Override
	public void doTask(Integer toPorderId, Integer taskQty, Integer everyQty, Integer tmPathId, Integer tmBomId,
			String note) {
		ToPorder porder = findById(toPorderId, true);
		NumRuleValue seqRuleNo = getSeqRuleNo(porder);
		//工单总数量
		int allQty = porder.getQty() - (porder.getTaskQty() == null ? 0 : porder.getTaskQty());
		for (int i = 0; i < taskQty; i++) {
			if (everyQty > allQty) {
				everyQty = allQty;
			}
			aviService.doSaveAvi(toPorderId, everyQty, tmPathId, tmBomId, note,
					numRuleService.getSerialNumber(ConstantUtils.SERIALIZABLE_TYPE_AVI_NO, seqRuleNo));
			allQty -= everyQty;
			if (allQty <= 0) {
				break;
			}
		}
		//完全派工
		if (allQty <= 0) {
			porder.setTaskQty(porder.getQty());
			porder.setTaskStatus(ConstantUtils.ENTRY_CODE_PORDER_TASK_STATUS_ALL);
		} else {//部分派工 
			porder.setTaskQty(porder.getQty() - allQty);
			porder.setTaskStatus(ConstantUtils.ENTRY_CODE_PORDER_TASK_STATUS_PART);
		}
		doUpdate(porder);
	}

	/**
	 * 编码规则编号
	 * 
	 * @param porder
	 * @return
	 */
	private NumRuleValue getSeqRuleNo(ToPorder porder) {
		NumRuleValue seq = new NumRuleValue();
		if (porder.getPart().getId() != null) {
			seq.setPartNo(porder.getPart().getNo());
		}
		if (porder.getPlant().getId() != null) {
			seq.setPlantNo(porder.getPlant().getNo());
		}
		seq.setPorderNo(porder.getNo());
		return seq;
	}

	@Override
	public void doCancelTask(Integer toPorderId) {
		List<ToPorderAvi> avis = aviService.getAvis(toPorderId);
		for (ToPorderAvi avi : avis) {
			aviService.deleteAviPathDetailByAviId(avi.getId());
			aviService.deleteAviBomAndPathByAviId(avi.getId());
		}
		deleteAviByPorderId(toPorderId);
		ToPorder porder = findById(toPorderId);
		porder.setTaskQty(0);
		porder.setTaskStatus(ConstantUtils.ENTRY_CODE_PORDER_TASK_STATUS_NOT);
		doUpdate(porder);
	}

	@Override
	public void deleteAviByPorderId(Integer toPorderId) {
		((ToPorderDao) dao).deleteAviByPorderId(toPorderId);
	}

	@Override
	public void doClose(Integer id, String closeReason) {
		ToPorder porder = findById(id);
		porder.setCloseReason(closeReason);
		porder.setProductStatus(ConstantUtils.ENTRY_CODE_PORDER_PRODUCT_STATUS_CLOSE);
		doUpdate(porder);
		//生产序列
		List<ToPorderAvi> avis = aviService.getAvis(id);
		for (ToPorderAvi avi : avis) {
			avi.setStatus(ConstantUtils.ENTRY_CODE_AVI_STATUS_CLOSE);
		}
		aviService.doUpdateBatch(avis);
	}

	@Override
	public void exportExcelData(HttpServletResponse response, List<ToPorder> content, String filePath,
			String[] header) {

		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		// YES/NO
		Map<String, DictEntry> yesOrNo = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO);
		//生产状态
		Map<String, DictEntry> productStatus = entryService
				.getMapTypeCode(ConstantUtils.TYPE_CODE_PORDER_PRODUCT_STATUS);
		//派工状态
		Map<String, DictEntry> taskStatus = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_PORDER_TASK_STATUS);
		//入库状态
		Map<String, DictEntry> instorage = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_PORDER_INSTORAGE_STATUS);
		//优先级
		Map<String, DictEntry> prioirty = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_PORDER_PRIORITY);
		for (ToPorder bean : content) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("工单编号", bean.getNo());
			map.put("工厂", bean.getPlant().getId() == null ? ""
					: (bean.getPlant().getNo() + "-" + bean.getPlant().getNameCn()));
			map.put("物料",
					bean.getPart().getId() == null ? "" : (bean.getPart().getNo() + "-" + bean.getPart().getNameCn()));
			map.put("数量", bean.getQty());
			map.put("派工数量", bean.getTaskQty() == null ? "" : bean.getTaskQty());
			map.put("工艺路径",
					bean.getPath().getId() == null ? "" : (bean.getPath().getNo() + "-" + bean.getPath().getName()));
			map.put("BOM", bean.getBom().getId() == null ? "" : (bean.getBom().getBomversion()));
			map.put("计划开始时间", bean.getPlanStartTime());
			map.put("计划结束时间", bean.getPlanEndTime());
			map.put("实际开始时间", bean.getFactStartTime() == null ? "" : bean.getFactStartTime());
			map.put("实际结束时间", bean.getFactEndTime() == null ? "" : bean.getFactEndTime());
			map.put("生产状态", productStatus.get(bean.getProductStatus()).getName());
			map.put("派工状态", taskStatus.get(bean.getTaskStatus()).getName());
			map.put("入库状态", instorage.get(bean.getInstorageStatus()).getName());
			map.put("上线数量", bean.getOnlineQty() == null ? "" : bean.getOnlineQty());
			map.put("下线数量", bean.getOfflineQty() == null ? "" : bean.getOfflineQty());
			map.put("报废数量", bean.getScrapQty() == null ? "" : bean.getScrapQty());
			map.put("入库数量", bean.getInstorageQty() == null ? "" : bean.getInstorageQty());
			map.put("优先级", prioirty.containsKey(bean.getPriority()) ? "" : prioirty.get(bean.getPriority()).getName());
			map.put("是否ERP下达", yesOrNo.get(bean.getIsErp()).getName());
			map.put("是否HOLD", yesOrNo.get(bean.getIsHold()).getName());
			map.put("备注", bean.getNote() == null ? "" : bean.getNote());
			map.put("关闭理由", bean.getCloseReason() == null ? "" : bean.getCloseReason());
			exportDataList.add(map);
		}
		BaseExcelUtil.exportData(response, exportDataList, filePath, header);
	}
}
