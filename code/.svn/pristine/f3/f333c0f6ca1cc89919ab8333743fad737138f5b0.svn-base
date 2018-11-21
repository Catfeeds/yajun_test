package com.wis.mes.master.supplier.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.master.part.entity.TmPart;
import com.wis.mes.master.supplier.dao.TmSupplierPartDao;
import com.wis.mes.master.supplier.entity.TmSupplierPart;

@Repository
public class TmSupplierPartDaoImpl extends BaseDaoImpl<TmSupplierPart> implements TmSupplierPartDao {

	@Override
	public PageResult<?> getPartPageResult(BootstrapTableQueryCriteria criteria) {
		PageResult<TmPart> pageResult = new PageResult<TmPart>();
		Integer offsetIndex = criteria.getCurrentIndex();
		Integer pageSize = criteria.getRows();
		Integer tmSupplierId = (Integer) criteria.getQueryCondition().get("tmSupplierId");
		pageResult.setCurrentIndex(criteria.getCurrentIndex());
		pageResult.setPageSize(criteria.getRows());
		Map<String, Object> params = criteria.getQueryCondition();
		pageResult.setTotalCount(getPartCount(params));
		params.put("firstRow", offsetIndex);
		params.put("pageSize", pageSize);
		params.put("tmSupplierId", tmSupplierId);
		if (0 != pageSize) {
			pageResult.setTotalPage((int) ((pageResult.getTotalCount() + pageSize - 1) / pageSize));
			pageResult.setCurrentPage((int) ((offsetIndex + pageSize) / pageSize));
		}
		List<TmPart> content = getSqlSession().selectList("SupplierPartMapper.getPartPageResult", params);
		pageResult.setContent(content);
		return pageResult;
	}

	private int getPartCount(Map<String, Object> params) {
		return getSqlSession().selectOne("SupplierPartMapper.getPartCount", params);
	}

}
