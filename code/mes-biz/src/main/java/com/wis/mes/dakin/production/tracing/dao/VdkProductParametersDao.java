package com.wis.mes.dakin.production.tracing.dao;

import java.util.List;
import java.util.Map;

import com.wis.core.dao.BaseDao;
import com.wis.mes.dakin.production.tracing.entity.VdkProductParameters;
import com.wis.mes.dakin.production.tracing.vo.ProductParameterVo;

public interface VdkProductParametersDao extends BaseDao<VdkProductParameters> {
	List<ProductParameterVo> queryProductParameters(Map<String, Object> param);
}
