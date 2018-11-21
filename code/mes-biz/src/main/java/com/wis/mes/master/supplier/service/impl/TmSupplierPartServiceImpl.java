package com.wis.mes.master.supplier.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.BaseExcelUtil;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.supplier.dao.TmSupplierPartDao;
import com.wis.mes.master.supplier.entity.TmSupplierPart;
import com.wis.mes.master.supplier.service.TmSupplierPartService;

@Service
public class TmSupplierPartServiceImpl extends BaseServiceImpl<TmSupplierPart> implements TmSupplierPartService {

	@Autowired
	public void setDao(TmSupplierPartDao dao) {
		this.dao = dao;
	}

	@Autowired
	private DictEntryService entryService;

	@Override
	public PageResult<?> getPartPageResult(BootstrapTableQueryCriteria criteria) {
		return ((TmSupplierPartDao) dao).getPartPageResult(criteria);
	}

	@Override
	public List<TmSupplierPart> doSaveBatch(Integer tmSupplierId, String[] tmPartId, String[] enabled) {
		Map<String, TmSupplierPart> supplierPartsMap = getSupplierPartsMap(String.valueOf(tmSupplierId));
		// 新增
		List<TmSupplierPart> addList = new ArrayList<TmSupplierPart>();
		// 更新
		List<TmSupplierPart> updateList = new ArrayList<TmSupplierPart>();

		for (int i = 0; i < tmPartId.length; i++) {
			// 如果数据已经存在则更新
			if (supplierPartsMap.containsKey(tmPartId[i])) {
				TmSupplierPart tmSupplierPart = supplierPartsMap.get(tmPartId[i]);
				tmSupplierPart.setTmPartId(Integer.valueOf(tmPartId[i]));
				tmSupplierPart.setEnabled(getEnabled(enabled, i));
				updateList.add(tmSupplierPart);
			} else {
				TmSupplierPart tmSupplierPart = new TmSupplierPart();
				tmSupplierPart.setTmSupplierId(tmSupplierId);
				tmSupplierPart.setTmPartId(Integer.valueOf(tmPartId[i]));
				tmSupplierPart.setEnabled(getEnabled(enabled, i));
				addList.add(tmSupplierPart);
			}
		}
		doSaveBatch(addList);
		if (updateList.size() > 0) {
			doUpdateBatch(updateList);
		}
		return addList;
	}

	private Map<String, TmSupplierPart> getSupplierPartsMap(String tmSupplierId) {
		Map<String, TmSupplierPart> map = new HashMap<String, TmSupplierPart>();
		QueryCriteria criteria = new QueryCriteria();
		criteria.setQueryPage(false);
		criteria.getQueryCondition().put("tmSupplierId", tmSupplierId);
		List<TmSupplierPart> content = findBy(criteria).getContent();
		for (TmSupplierPart tmSupplierPart : content) {
			map.put(tmSupplierPart.getTmPartId().toString(), tmSupplierPart);
		}
		return map;
	}

	private String getEnabled(String[] enabled, int index) {
		return (enabled.length == 0) ? "" : enabled[index];
	}

	/**
	 * 供应商物料导出
	 * @author ryy
	 */
	@Override
	public void exportExcelData(HttpServletResponse response, List<TmSupplierPart> list, String fileName,
			String[] header) {
		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();

		Map<String, DictEntry> enabled = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_ENABLED);
		Map<String, DictEntry> type = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_PART_TYPE);
		for (TmSupplierPart supplierPart : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("零件编号", supplierPart.getPart().getNo());
			map.put("零件名称", supplierPart.getPart().getNameCn());
			map.put("类型", getStr(type.get(supplierPart.getPart().getType()).getName()));
			map.put("启用", getStr(enabled.get(supplierPart.getEnabled()).getName()));
			exportDataList.add(map);
		}
		BaseExcelUtil.exportData(response, exportDataList, fileName, header);
	}

	private String getStr(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	@Override
	public List<TmSupplierPart> findByForeignKey(Integer foreignKeyValue) {
		QueryCriteria criteria = new QueryCriteria();
		criteria.setQueryRelationEntity(true);
		criteria.addQueryCondition("tmSupplierId", foreignKeyValue.toString());
		criteria.setQueryPage(false);
		PageResult<TmSupplierPart> result = (PageResult<TmSupplierPart>) dao.findBy(criteria);
		return result.getContent();
	}
}
