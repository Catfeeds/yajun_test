package com.wis.mes.master.supplier.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.core.context.WebContextHolder;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.core.entity.AuditEntity;
import com.wis.core.entity.Removable;
import com.wis.core.framework.entity.UserDataPermission;
import com.wis.core.utils.ConstantUtil;
import com.wis.core.utils.CriteriaUtil;
import com.wis.core.utils.HqlUtil;
import com.wis.mes.master.supplier.dao.TmSupplierDao;
import com.wis.mes.master.supplier.entity.TmSupplier;

@Repository
public class TmSupplierDaoImpl extends BaseDaoImpl<TmSupplier> implements TmSupplierDao {
	@Override
	public String getSqlBy(QueryCriteria queryCriteria) {
		Map<String, Object> parameters = clearBlankAndChangeType(queryCriteria.getQueryCondition());
		StringBuffer queryHql = new StringBuffer(generateQueryHql(queryCriteria));
		boolean flag = (queryCriteria.getQueryCondition().get("type") != null);
		if (flag) {
			queryHql.append(
					" LEFT JOIN tm_supplier_part tm_supplier_part_0 ON tmsupplier_0.ID =tm_supplier_part_0.TM_SUPPLIER_ID ");
			queryHql.append(" LEFT JOIN tm_part tm_part_0 ON tm_part_0.ID =tm_supplier_part_0.TM_PART_ID ");
		}
		queryHql.append(" where 1=1 ");
		if (flag) {
			queryHql.append(" AND tm_part_0.TYPE ='");
			queryHql.append(queryCriteria.getQueryCondition().get("type"));
			queryHql.append("' ");
			queryCriteria.getQueryCondition().remove("type");
		}
		if (!queryCriteria.isIncludesReomved()) {
			appendValid(parameters, queryHql);
		}
		if (parameters.size() > 0) {
			queryHql.append(getConditionHql(parameters, queryCriteria));
		}
		appendDataPermission(queryHql);
		if (flag) {
			queryHql.append(" GROUP BY tmsupplier_0.ID ");
		}
		if (queryCriteria.getOrderField() == null) {
			queryCriteria.setOrderField(getEntityClassName().toLowerCase() + SUFFIX + "." + COLUMN_NAME_ID);
		} else {
			String orderStr = queryCriteria.getOrderField();
			// if (orderStr.contains(".")) {
			orderStr = "\"" + orderStr + "\"";
			// }
			queryCriteria.setOrderField(orderStr);
		}
		queryHql.append(
				HqlUtil.generateHqlOrderClause(queryCriteria.getOrderField(), queryCriteria.getOrderDirection()));
		return queryHql.toString();
	}

	private Map<String, Object> clearBlankAndChangeType(Map<String, Object> parameters) {
		List<String> removedKeys = new ArrayList<String>();
		for (String key : parameters.keySet()) {
			if (parameters.get(key) == null || parameters.get(key).equals("")) {
				removedKeys.add(key);
				continue;
			}
			changeParamaterToFieldType(key, parameters);
		}
		for (String key : removedKeys) {
			parameters.remove(key);
		}
		return parameters;
	}

	private void appendValid(Map<String, Object> parameters, StringBuffer queryHql) {
		if (!parameters.containsKey(Removable.REMOVE_FIELD_NAME)
				&& Arrays.asList(entityClass.getInterfaces()).contains(Removable.class)) {
			queryHql.append(" and ");
			queryHql.append(getEntityClassName().toLowerCase());
			queryHql.append(SUFFIX + ".");
			queryHql.append(getColumnName(Removable.REMOVE_FIELD_NAME));
			queryHql.append("=");
			queryHql.append(Removable.NOT_LOGIC_DELETE);
		}
	}

	private void appendDataPermission(StringBuffer result) {
		if (!AuditEntity.class.isAssignableFrom(getEntityClass())) {
			return;
		}
		if (null == WebContextHolder.getAuthentication()) {
			return;
		}
		Map<String, Map<String, String>> dataPermissionMap = WebContextHolder.getUserDetailsImpl()
				.getDataPermissionMap();
		String daoInterface = getClass().getInterfaces()[0].getName();
		if (dataPermissionMap.containsKey(daoInterface)) {
			Map<String, String> roleDataPermission = dataPermissionMap.get(daoInterface);
			result.append(" AND (");
			StringBuffer dataPermissionCond = new StringBuffer();
			for (String permissionType : roleDataPermission.keySet()) {
				if (dataPermissionCond.length() > 0) {
					dataPermissionCond.append(" OR ");
				}
				if (ConstantUtil.ENTRY_CODE_DATA_PERMISSION_TYPE_ALL.equals(permissionType)) {
					result.append("1=1");
				} else if (ConstantUtil.ENTRY_CODE_DATA_PERMISSION_TYPE_DEPTANDSUB.equals(permissionType)) {
					List<String> orgCodes = WebContextHolder.getCurrentUser().getOrgnizationCodes();
					if (orgCodes.isEmpty()) {
						result.append("1=2");
					} else {
						String beanName = entityClass.getSimpleName().toLowerCase() + SUFFIX;
						result.append(beanName);
						result.append(
								".CREATE_USER IN (SELECT distinct ruo_99.USER_ID FROM TR_USER_ORGNIZATION ruo_99 where ruo_99.ORGNIZATION_ID IN (SELECT orgnization_99.ID from TS_ORGNIZATION orgnization_99 where ");
						for (int i = 0; i < orgCodes.size(); i++) {
							if (i > 0) {
								result.append(" OR ");
							}
							result.append("orgnization_99.SYSTEM_CODE like ");
							result.append("'");
							result.append(orgCodes.get(i));
							result.append("%'");
						}
						result.append("))");
					}
				} else if (ConstantUtil.ENTRY_CODE_DATA_PERMISSION_TYPE_DEPT.equals(permissionType)) {
					List<String> orgCodes = WebContextHolder.getCurrentUser().getOrgnizationCodes();
					if (orgCodes.isEmpty()) {
						result.append("1=2");
					} else {
						String beanName = entityClass.getSimpleName().toLowerCase() + SUFFIX;
						result.append(beanName);
						result.append(
								".CREATE_USER IN (SELECT distinct ruo_99.USER_ID FROM TR_USER_ORGNIZATION ruo_99 where ruo_99.ORGNIZATION_ID IN (SELECT ruo_98.ORGNIZATION_ID from TR_USER_ORGNIZATION ruo_98 where ruo_98.USER_ID=");
						result.append(WebContextHolder.getCurrentUser().getId() + "))");
					}
				} else if (ConstantUtil.ENTRY_CODE_DATA_PERMISSION_TYPE_SELF.equals(permissionType)) {
					String beanName = entityClass.getSimpleName().toLowerCase() + SUFFIX;
					result.append(beanName);
					result.append(".CREATE_USER=");
					result.append(WebContextHolder.getCurrentUser().getId());
				} else if (ConstantUtil.ENTRY_CODE_DATA_PERMISSION_TYPE_SCRIPT.equals(permissionType)) {
					result.append(roleDataPermission.get(permissionType));
				}
			}
			result.append(")");
		}
	}

	private void changeParamaterToFieldType(String fieldName, Map<String, Object> parameter) {
		Class<?> fieldType = getFieldType(fieldName);
		if (parameter.get(fieldName) instanceof UserDataPermission) {
			return;
		}
		if (Integer.class.equals(fieldType)) {
			CriteriaUtil.changeToInt(parameter, fieldName);
		} else if (Boolean.class.equals(fieldType)) {
			CriteriaUtil.changeToBoolean(parameter, fieldName);
		}
		if (Double.class.equals(fieldType)) {
			CriteriaUtil.changeToDouble(parameter, fieldName);
		}
		if (Date.class.equals(fieldType)) {
			CriteriaUtil.changeToDate(parameter, fieldName);
		}
	}

	private Class<?> getFieldType(String fieldName) {
		try {
			return entityClass.getMethod("get" + firstCharUpper(fieldName)).getReturnType();
		} catch (Exception e) {
		}
		try {
			int index = fieldName.indexOf(".");
			int startIndex = fieldName.indexOf(ConstantUtil.QUERY_CONDITION_SUFFIX_START);
			int endIndex = fieldName.indexOf(ConstantUtil.QUERY_CONDITION_SUFFIX_END);
			String thisFieldName = fieldName.substring(0, index);
			String relationFieldName = "";
			if (startIndex != -1) {
				relationFieldName = fieldName.substring(index + 1, startIndex);
			} else if (endIndex != -1) {
				relationFieldName = fieldName.substring(index + 1, endIndex);
			} else {
				relationFieldName = fieldName.substring(index + 1);
			}
			return entityClass.getMethod("get" + firstCharUpper(thisFieldName)).getReturnType()
					.getMethod("get" + firstCharUpper(relationFieldName)).getReturnType();

		} catch (Exception e) {
		}
		try {
			int index = fieldName.indexOf(ConstantUtil.QUERY_CONDITION_SUFFIX_START);
			String thisFieldName = fieldName.substring(0, index);
			return entityClass.getMethod("get" + firstCharUpper(thisFieldName)).getReturnType();
		} catch (Exception e) {

		}
		try {
			int index = fieldName.indexOf(ConstantUtil.QUERY_CONDITION_SUFFIX_END);
			String thisFieldName = fieldName.substring(0, index);
			return entityClass.getMethod("get" + firstCharUpper(thisFieldName)).getReturnType();
		} catch (Exception e) {

		}
		return null;
	}

	private String firstCharUpper(String str) {
		if (Character.isUpperCase(str.charAt(0))) {
			str = str.toLowerCase();
		}
		return str.replaceFirst(str.charAt(0) + "", (str.charAt(0) + "").toUpperCase().charAt(0) + "");
	}
}
