package com.wis.mes.master.supplier.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.core.service.BaseService;
import com.wis.mes.master.supplier.entity.TmSupplierPart;

public interface TmSupplierPartService extends BaseService<TmSupplierPart> {

	PageResult<?> getPartPageResult(BootstrapTableQueryCriteria criteria);

	List<TmSupplierPart> doSaveBatch(Integer tmSupplierId, String[] tmPartId, String[] enabled);

	void exportExcelData(HttpServletResponse response, List<TmSupplierPart> content, String string, String[] header);

	/**
	 * 根据外键查询（含关联查询）
	 * @param id
	 * @return
	 */
	List<TmSupplierPart> findByForeignKey(Integer foreignKeyValue);

}
