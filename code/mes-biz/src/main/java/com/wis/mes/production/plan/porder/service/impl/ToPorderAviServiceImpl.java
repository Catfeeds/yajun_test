package com.wis.mes.production.plan.porder.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.BaseExcelUtil;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.path.entity.TmPath;
import com.wis.mes.master.path.service.TmPathService;
import com.wis.mes.production.plan.porder.dao.ToPorderAviDao;
import com.wis.mes.production.plan.porder.entity.ToPorder;
import com.wis.mes.production.plan.porder.entity.ToPorderAvi;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPath;
import com.wis.mes.production.plan.porder.service.ToPorderAviBomService;
import com.wis.mes.production.plan.porder.service.ToPorderAviPathService;
import com.wis.mes.production.plan.porder.service.ToPorderAviService;
import com.wis.mes.production.plan.porder.service.ToPorderService;

@Service
public class ToPorderAviServiceImpl extends BaseServiceImpl<ToPorderAvi> implements ToPorderAviService {

	@Autowired
	public void setDao(ToPorderAviDao dao) {
		this.dao = dao;
	}

	@Autowired
	private AuditLogService logService;
	@Autowired
	private ToPorderAviPathService aviPathService;
	@Autowired
	private ToPorderAviBomService aviBomService;
	@Autowired
	private ToPorderService porderService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmPathService pathService;

	@Override
	public void doSaveAvi(Integer toPorderId, Integer everyQty, Integer tmPathId, Integer tmBomId, String note,
			String no) {
		ToPorderAvi avi = new ToPorderAvi();
		avi.setToPorderId(toPorderId);// 工单 
		avi.setTmLineId(getLineId(tmPathId));
		avi.setNo(no);//编号
		avi.setTmPathId(tmPathId);//工艺路径
		avi.setTmBomId(tmBomId);//BOM
		avi.setQty(everyQty);//数量
		avi.setStatus(ConstantUtils.ENTRY_CODE_AVI_STATUS_NEW);//新单
		avi.setIsHold(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO);//是否HOLD
		avi.setNote(note);//备注
		avi = doSave(avi);
		aviPathService.doSavePath(avi.getId(), avi.getTmPathId());
		if(tmBomId!=null){
			aviBomService.doSaveBom(avi.getId(), avi.getTmBomId());
		}
		logService.doAddLog("ToPorderAvi", avi);
	}

	@Override
	public Integer getLineId(Integer tmPathId) {
		TmPath path = pathService.findById(tmPathId);
		if (path.getTmLineId() != null) {
			return path.getTmLineId();
		} else {
			return ((ToPorderAviDao) dao).getLineId(tmPathId);
		}
	}

	@Override
	public void deleteAviBomAndPathByAviId(Integer aviId) {
		((ToPorderAviDao) dao).deleteAviBomAndPathByAviId(aviId);
	}

	@Override
	public void deleteAviPathDetailByAviId(Integer aviId) {
		((ToPorderAviDao) dao).deleteAviPathDetailByAviId(getAviPathIds(aviId));
	}

	@Override
	public List<ToPorderAvi> getAvis(Integer toPorderId) {
		ToPorderAvi avi = new ToPorderAvi();
		avi.setToPorderId(toPorderId);
		return findByEg(avi);
	}

	@Override
	public void doCancelTask(Integer id) {
		ToPorderAvi avi = findById(id);
		ToPorder porder = porderService.findById(avi.getToPorderId());
		if (ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES.equals(porder.getIsHold())) {
			throw new BusinessException("PORDER_HOLD_CANNOT_OPERATE_ERROR");//工单HOLD，不能取消派工
		}
		porder.setTaskQty(porder.getTaskQty() - avi.getQty());
		if (porder.getTaskQty() == 0) {
			porder.setTaskStatus(ConstantUtils.ENTRY_CODE_PORDER_TASK_STATUS_NOT);
		} else {
			porder.setTaskStatus(ConstantUtils.ENTRY_CODE_PORDER_TASK_STATUS_PART);
		}
		porderService.doUpdate(porder);
		deleteAviPathDetailByAviId(id);
		deleteAviBomAndPathByAviId(id);
		doDeleteById(id);
	}

	private Integer[] getAviPathIds(Integer toPorderAviId) {
		List<ToPorderAviPath> aviPaths = aviPathService.getAviPaths(toPorderAviId, false);
		Integer[] aviPathIds = new Integer[aviPaths.size()];
		for (int i = 0; i < aviPaths.size(); i++) {
			ToPorderAviPath toPorderAviPath = aviPaths.get(i);
			aviPathIds[i] = toPorderAviPath.getId();
		}
		return aviPathIds;
	}

	@Override
	public Integer getPorderPlantIdByAviId(Integer aviId) {
		return ((ToPorderAviDao) dao).getPorderPlantIdByAviId(aviId);
	}

	@Override
	public void exportExcelData(HttpServletResponse response, List<ToPorderAvi> content, String filePath,
			String[] header) {
		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		// YES/NO
		Map<String, DictEntry> yesOrNo = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO);
		//状态
		Map<String, DictEntry> status = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_AVI_STATUS);
		for (ToPorderAvi bean : content) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("编号", bean.getNo());
			map.put("工艺路径", bean.getPath().getNo());
			map.put("BOM", bean.getBom().getBomversion());
			map.put("数量", bean.getQty());
			map.put("上线数量", bean.getOnlineQty() == null ? "" : bean.getOnlineQty());
			map.put("下线数量", bean.getOfflineQty() == null ? "" : bean.getOfflineQty());
			map.put("报废数量", bean.getScrapQty() == null ? "" : bean.getScrapQty());
			map.put("生产开始时间", bean.getProductStartTime() == null ? "" : bean.getProductStartTime());
			map.put("生产结束时间", bean.getProductEndTime() == null ? "" : bean.getProductEndTime());
			map.put("状态", status.get(bean.getStatus()).getName());
			map.put("是否HOLD", yesOrNo.get(bean.getIsHold()).getName());
			map.put("备注", bean.getNote() == null ? "" : bean.getNote());
			exportDataList.add(map);
		}
		BaseExcelUtil.exportData(response, exportDataList, filePath, header);

	}
}
