package com.wis.mes.master.path.service.impl;

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
import com.wis.mes.master.path.dao.TmPathUlocPartDao;
import com.wis.mes.master.path.entity.TmPathUlocPart;
import com.wis.mes.master.path.service.TmPathUlocPartService;
import com.wis.mes.util.StringUtil;

@Service
public class TmPathUlocPartServiceImpl extends BaseServiceImpl<TmPathUlocPart> implements TmPathUlocPartService {

	@Autowired
	public void setDao(TmPathUlocPartDao dao) {
		this.dao = dao;
	}

	@Autowired
	private DictEntryService entryService;

	@Override
	public void exportExcelData(HttpServletResponse response, List<TmPathUlocPart> content, String filePath,
			String[] header) {
		Map<String, DictEntry> yesOrNo = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO);
		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		for (TmPathUlocPart bean : content) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("物料",
					bean.getPart().getId() == null ? "" : (bean.getPart().getNo() + "-" + bean.getPart().getNameCn()));
			map.put("数量", bean.getQty());
			map.put("顺序号", bean.getSeq() == null ? "" : bean.getSeq());
			map.put("是否批次追溯", yesOrNo.get(bean.getIsBatchTrac()).getName());
			map.put("备注", StringUtil.getString(bean.getNote()));
			exportDataList.add(map);
		}
		BaseExcelUtil.exportData(response, exportDataList, filePath, header);

	}

	@Override
	public List<TmPathUlocPart> getPathUlocParts(Integer tmPathUlocId) {
		TmPathUlocPart eg = new TmPathUlocPart();
		eg.setTmPathUlocId(tmPathUlocId);
		return findByEg(eg);
	}

}
