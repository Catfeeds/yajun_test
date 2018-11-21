package com.wis.mes.dakin.production.tracing.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.basis.common.utils.SystemConfig;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.dakin.production.tracing.dao.VdkProductParametersDao;
import com.wis.mes.dakin.production.tracing.entity.VdkProductParameters;
import com.wis.mes.dakin.production.tracing.vo.ProductParameterVo;
import com.wis.mes.util.StringUtil;

@Repository
public class VdkProductParametersDaoImpl extends BaseDaoImpl<VdkProductParameters> implements VdkProductParametersDao {
	private static final String TABLE_NAME_DAKIN_VIEW_PRODUCT_PARAMETERS = SystemConfig
			.getPropertyByKey("TABLE_NAME_DAKIN_VIEW_PRODUCT_PARAMETERS");

	@Override
	public List<ProductParameterVo> queryProductParameters(Map<String, Object> param) {
		param.put("productParameterViewName", TABLE_NAME_DAKIN_VIEW_PRODUCT_PARAMETERS);
		return getSqlSession().selectList("ProductParametersMapper.queryProductParameters", param);
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME_DAKIN_VIEW_PRODUCT_PARAMETERS;
	}

	@Override
	public String getSqlBy(QueryCriteria queryCriteria) {
		Map<String, Object> queryCondition = queryCriteria.getQueryCondition();
		String paramExplain = null;
		if (queryCondition.containsKey("paramExplain")) {
			paramExplain = queryCondition.get("paramExplain").toString();
			queryCondition.remove("paramExplain");
		}
		String sql = super.getSqlBy(queryCriteria);
		if (StringUtil.isNotEmpty(paramExplain)) {
			sql = sql.replace("where 1=1",
					"  where 1 = 1 and tmEquipmentParam.parameter_explain like '%" + paramExplain + "%'");
		}
		StringBuffer sb = new StringBuffer();
		sb.append(",tmEquipmentParam.note as \"note\",");
		sb.append("tmEquipmentParam.parameter_explain as \"paramExplain\",");
		sb.append("tmEquipmentParam.tm_equipment_id as \"equipmentId\",");
		sb.append("tmEquipmentParam.unit as \"unit\",tmEquipmentParam.isConfig as \"isConfig\"");
		sb.append(" FROM ").append(TABLE_NAME_DAKIN_VIEW_PRODUCT_PARAMETERS).append(" vdkproductparameters_0 ");
		sb.append(" LEFT JOIN ");
		sb.append("(SELECT  t.parameter_name,Max(t.note) note,Max(t.parameter_explain) parameter_explain,");
		sb.append("Max(t.tm_equipment_id) tm_equipment_id,Max(t.parameter_uloc) unit,Max(t.IF_CNF_SCOPE) isConfig ");
		sb.append(" FROM TM_EQUIPMENT_PARAM t GROUP BY t.parameter_name) tmEquipmentParam");
		sb.append(" ON tmEquipmentParam.Parameter_Name = vdkproductparameters_0.params ");
		sql = sql.replace("FROM " + TABLE_NAME_DAKIN_VIEW_PRODUCT_PARAMETERS + " vdkproductparameters_0",
				sb.toString());
		return sql;
	}
}
