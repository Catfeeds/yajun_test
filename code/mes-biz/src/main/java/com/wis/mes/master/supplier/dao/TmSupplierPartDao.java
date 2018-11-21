package com.wis.mes.master.supplier.dao;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.BaseDao;
import com.wis.core.dao.PageResult;
import com.wis.mes.master.supplier.entity.TmSupplierPart;


public interface TmSupplierPartDao extends BaseDao<TmSupplierPart> {

	PageResult<?> getPartPageResult(BootstrapTableQueryCriteria criteria);

}
