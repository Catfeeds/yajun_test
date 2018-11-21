package com.wis.mes.dakin.production.tracing.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

import com.wis.core.service.BaseService;
import com.wis.mes.dakin.production.tracing.entity.DkDisZhTbl;
import com.wis.mes.dakin.production.tracing.vo.ProductPartVo;

/**
 * 
 * @author liuzejun
 *
 * @Desc 物料绑定数据
 */
public interface DkDisZhTblService extends BaseService<DkDisZhTbl> {
	List<ProductPartVo> queryProductPart(Map<String, Object> param);

	void exportProductPart(List<ProductPartVo> list, Workbook wb);
}
