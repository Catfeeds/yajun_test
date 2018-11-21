package com.wis.mes.production.plan.porder.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.BaseExcelUtil;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.path.entity.TmPathUlocPart;
import com.wis.mes.master.path.service.TmPathUlocPartService;
import com.wis.mes.production.plan.porder.dao.ToPorderAviPathPartDao;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPathPart;
import com.wis.mes.production.plan.porder.service.ToPorderAviPathPartService;
import com.wis.mes.util.StringUtil;

@Service
public class ToPorderAviPathPartServiceImpl extends BaseServiceImpl<ToPorderAviPathPart>
		implements ToPorderAviPathPartService {

	@Autowired
	public void setDao(ToPorderAviPathPartDao dao) {
		this.dao = dao;
	}

	@Autowired
	private TmPathUlocPartService pathUlocPartService;
	@Autowired
	private DictEntryService entryService;

	@Override
	public void doSaveAviPart(Integer toPorderAviPathId, Integer tmPathUlocId) {
		List<ToPorderAviPathPart> aviPathPartAddList = new ArrayList<ToPorderAviPathPart>();
		List<TmPathUlocPart> tmPathUlocParameters = pathUlocPartService.getPathUlocParts(tmPathUlocId);
		for (TmPathUlocPart part : tmPathUlocParameters) {
			ToPorderAviPathPart bean = new ToPorderAviPathPart();
			bean.setIsBatchTrac(part.getIsBatchTrac());
			bean.setIsTrac(part.getIsTrac());
			bean.setQty(part.getQty());
			bean.setSeq(part.getSeq());
			bean.setTmPartId(part.getTmPartId());
			bean.setToPorderAviPathId(toPorderAviPathId);
			bean.setNote(part.getNote());
			aviPathPartAddList.add(bean);
		}
		doSaveBatch(aviPathPartAddList);
	}

	@Override
	public void exportExcelData(HttpServletResponse response, List<ToPorderAviPathPart> content, String filePath,
			String[] header) {
		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		// YES/NO
		Map<String, DictEntry> yesOrNo = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO);
		for (ToPorderAviPathPart bean : content) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("物料", bean.getPart().getNo() + "-" + bean.getPart().getNameCn());
			map.put("数量", bean.getQty());
			map.put("顺序号", bean.getSeq());
			map.put("是否追溯", yesOrNo.get(bean.getIsTrac()).getName());
			map.put("是否批次追溯",
					yesOrNo.containsKey(bean.getIsBatchTrac()) ? yesOrNo.get(bean.getIsBatchTrac()).getName() : "");
			map.put("备注", StringUtil.getString(bean.getNote()));
			exportDataList.add(map);
		}
		BaseExcelUtil.exportData(response, exportDataList, filePath, header);
	}
}
