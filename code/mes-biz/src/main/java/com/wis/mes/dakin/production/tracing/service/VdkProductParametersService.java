package com.wis.mes.dakin.production.tracing.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

import com.wis.core.service.BaseService;
import com.wis.mes.dakin.production.tracing.entity.VdkProductParameters;
import com.wis.mes.dakin.production.tracing.vo.ProductParameterVo;

public interface VdkProductParametersService extends BaseService<VdkProductParameters>{
	List<ProductParameterVo> queryProductParameters(Map<String, Object> param);

	void exportProductTracing(List<ProductParameterVo> list, Workbook wb);
}
