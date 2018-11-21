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
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.bom.entity.TmBomDetail;
import com.wis.mes.master.bom.service.TmBomDetailService;
import com.wis.mes.production.plan.porder.dao.ToPorderAviBomDao;
import com.wis.mes.production.plan.porder.entity.ToPorderAviBom;
import com.wis.mes.production.plan.porder.service.ToPorderAviBomService;
import com.wis.mes.util.StringUtil;

@Service
public class ToPorderAviBomServiceImpl extends BaseServiceImpl<ToPorderAviBom> implements ToPorderAviBomService {

	@Autowired
	public void setDao(ToPorderAviBomDao dao) {
		this.dao = dao;
	}

	@Autowired
	private TmBomDetailService bomDetailService;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;

	@Override
	public void doSaveBom(Integer aviId, Integer tmBomId) {
		List<TmBomDetail> tmBomDetails = bomDetailService.getTmBomDetails(tmBomId);
		List<ToPorderAviBom> aviBoms = new ArrayList<ToPorderAviBom>();
		for (TmBomDetail bomDetail : tmBomDetails) {
			ToPorderAviBom aviBom = new ToPorderAviBom();
			aviBom.setToPorderAviId(aviId);//生产序列
			aviBom.setTmUlocId(bomDetail.getTmUlocId());//工位
			aviBom.setTmPartId(bomDetail.getTmPartId());//物料
			aviBom.setQty(bomDetail.getQty());// 数量
			aviBom.setSeq(bomDetail.getSeq());//顺序
			aviBom.setIsTrac(bomDetail.getIsTrac());//是否追溯
			aviBom.setIsBatchTrac(bomDetail.getIsBatchTrac());//是否批次追溯 
			aviBom.setNote(bomDetail.getNote());
			aviBoms.add(aviBom);
		}
		doSaveBatch(aviBoms);
		logService.doAddLog("ToPorderAviBom", aviBoms);
	}

	@Override
	public Map<String, Object> exportExcelData(HttpServletResponse response, List<ToPorderAviBom> content,
			String filePath, String[] header) {

		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		// YES/NO
		Map<String, DictEntry> yesOrNo = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO);
		for (ToPorderAviBom bean : content) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("工位", bean.getUloc().getNo() + "-" + bean.getUloc().getName());
			map.put("物料", bean.getPart().getNo() + "-" + bean.getPart().getNameCn());
			map.put("数量", bean.getQty());
			map.put("顺序号", bean.getSeq());
			map.put("是否物料追溯", yesOrNo.get(bean.getIsTrac()).getName());
			map.put("是否批次追溯",
					yesOrNo.containsKey(bean.getIsBatchTrac()) ? "" : yesOrNo.get(bean.getIsBatchTrac()).getName());
			map.put("备注", StringUtil.getString(bean.getNote()));
			exportDataList.add(map);
		}
		return BaseExcelUtil.exportData(response, exportDataList, filePath, header);
	}
}
