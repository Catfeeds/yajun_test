package com.wis.mes.master.base.dao.impl;

import java.lang.reflect.Method;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.wis.core.context.WebContextHolder;
import com.wis.core.dao.Field;
import com.wis.core.dao.OrderBy;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.exception.DaoException;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.core.entity.AbstractEntity;
import com.wis.core.entity.AuditEntity;
import com.wis.core.entity.Removable;
import com.wis.core.framework.entity.DataPermission;
import com.wis.core.framework.entity.UserDataPermission;
import com.wis.core.utils.ConstantUtil;
import com.wis.core.utils.CriteriaUtil;
import com.wis.core.utils.HqlUtil;
import com.wis.mes.master.base.dao.MasterBaseDao;
import com.wis.mes.util.StringUtil;

/**
 * @author Jixueyuan
 * @date 2017年6月14日
 * @Description: 主数据BaseDaoImpl
 */
public class MasterBaseDaoImpl<T extends AbstractEntity> extends BaseDaoImpl<T> implements MasterBaseDao<T> {

	@Override
	public T get(Integer id) {
		Map<String, Object> params = generateSqlParam(generateSelectAll().toString());
		params.put("id", id);
		params.put("idColumnName", COLUMN_NAME_ID);
		String tableName = entityClass.getAnnotation(Table.class).name();
		String srcs = "";
		if (tableName.equalsIgnoreCase("tm_plant")) {
			List<String> arrays = getSqlSession().selectList("UserMapper.queryPlantPermissionByUserId", WebContextHolder.getCurrentUser().getId());
			srcs = StringUtil.joins(arrays,',');
		}
		if (tableName.equalsIgnoreCase("tm_workshop")) {
			List<String> arrays = getSqlSession().selectList("UserMapper.queryWorkshopPermissionByUserId", WebContextHolder.getCurrentUser().getId());
			srcs = StringUtil.joins(arrays,',');
		}
		if (tableName.equalsIgnoreCase("tm_line")) {
			List<String> arrays = getSqlSession().selectList("UserMapper.queryLinePermissionByUserId", WebContextHolder.getCurrentUser().getId());
			srcs = StringUtil.joins(arrays,',');
		}
		if (tableName.equalsIgnoreCase("tm_uloc")) {
			List<String> arrays = getSqlSession().selectList("UserMapper.queryWorkshopPermissionByUserId", WebContextHolder.getCurrentUser().getId());
			srcs = StringUtil.joins(arrays, ',');
		}
		if(StringUtils.isNotBlank(srcs)){
			params.put("roleMasterSetData",srcs);
		}
		return changeMapToBean(getSqlSession().selectOne("MasterBaseMapper.get", params));
	}

	@Override
	public List<T> findAll(OrderBy... orderBies) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = generateSelectAll();
		StringBuffer orderBySb = new StringBuffer();
		if (orderBies.length > 0) {
			orderBySb.append(" ORDER BY ");

			for (OrderBy orderBy : orderBies) {
				orderBySb.append(orderBy.getOrderField());
				orderBySb.append(" ");
				orderBySb.append(orderBy.getOrder().name());
				orderBySb.append(",");
			}
			orderBySb.deleteCharAt(orderBySb.length() - 1);
		}
		params.put("sql", sql.toString());
		if (orderBySb.length() > 0) {
			params.put("orderBy", orderBySb.toString());
		}
		if (isRemovableBO()) {
			params.put("removed", Removable.LOGIC_DELETE);
		}
		String tableName = entityClass.getAnnotation(Table.class).name();
		String srcs = "";
		if (tableName.equalsIgnoreCase("tm_plant")) {
			List<String> arays = getSqlSession().selectList("UserMapper.queryPlantPermissionByUserId",WebContextHolder.getCurrentUser().getId());
			srcs = StringUtil.joins(arays,',');
		}
		if (tableName.equalsIgnoreCase("tm_workshop")) {
			List<String> arays = getSqlSession().selectList("UserMapper.queryWorkshopPermissionByUserId",WebContextHolder.getCurrentUser().getId());
			srcs = StringUtil.joins(arays,',');
		}
		if (tableName.equalsIgnoreCase("tm_line")) {
			List<String> arays = getSqlSession().selectList("UserMapper.queryLinePermissionByUserId",WebContextHolder.getCurrentUser().getId());
			srcs = StringUtil.joins(arays,',');
		}
		if (tableName.equalsIgnoreCase("tm_uloc")) {
			List<String> arays = getSqlSession().selectList("UserMapper.queryUlocPermissionByUserId",WebContextHolder.getCurrentUser().getId());
			srcs = StringUtil.joins(arays,',');
		}
		if(StringUtils.isNotBlank(srcs)){
			params.put("roleMasterSetData", srcs);
		}
		return changeMapsToBeans(getSqlSession().selectList("MasterBaseMapper.findAll", params));
	}

	@Override
	public List<T> findByEg(T eg) {
		return changeMapsToBeans(getSqlSession().selectList("MasterBaseMapper.findByEg", getCritByEg(eg)));
	}

	@Override
	public T findUniqueByEg(T eg) {
		return changeMapToBean(getSqlSession().selectOne("BaseMapper.findByEg", getCritByEg(eg)));
	}

	@Override
	protected Map<String, Object> getCritByEg(T bean) {
		if (isRemovableBO()) {
			Removable obj = (Removable) bean;
			obj.setRemoved(Removable.NOT_LOGIC_DELETE);
		}

		StringBuffer sql = new StringBuffer();
		String beanName = getEntityClassName().toLowerCase() + SUFFIX;
		for (Field field : fields) {
			try {
				if (field.getValue(bean) != null) {
					sql.append(beanName);
					sql.append(".");
					sql.append(field.columnName);
					sql.append(" = #{bean.");
					sql.append(field.name);
					sql.append("} and ");
				}
			} catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		sql.delete(sql.length() - 4, sql.length() - 1);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("table_name", getTableName());
		params.put("querySql", generateSelectAll());
		params.put("whereSql", sql.toString());
		params.put("bean", bean);
		String tableName = entityClass.getAnnotation(Table.class).name();
		String srcs = "";
		if (tableName.equalsIgnoreCase("tm_plant")) {
			List<String> arays = getSqlSession().selectList("UserMapper.queryPlantPermissionByUserId",WebContextHolder.getCurrentUser().getId());
			srcs = StringUtil.joins(arays,',');
		}
		if (tableName.equalsIgnoreCase("tm_workshop")) {
			List<String> arays = getSqlSession().selectList("UserMapper.queryWorkshopPermissionByUserId",WebContextHolder.getCurrentUser().getId());
			srcs = StringUtil.joins(arays,',');
		}
		if (tableName.equalsIgnoreCase("tm_line")) {
			List<String> arays = getSqlSession().selectList("UserMapper.queryLinePermissionByUserId",WebContextHolder.getCurrentUser().getId());
			srcs = StringUtil.joins(arays,',');
		}
		if (tableName.equalsIgnoreCase("tm_uloc")) {
			List<String> arays = getSqlSession().selectList("UserMapper.queryUlocPermissionByUserId",WebContextHolder.getCurrentUser().getId());
			srcs = StringUtil.joins(arays,',');
		}
		if(StringUtils.isNotBlank(srcs)){
			params.put("roleMasterSetData",srcs);
		}
		return params;
	}

	@Override
	public List<T> findAllInIds(Integer[] ids) {
		if (ids.length < 1)
			return null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sql", generateSelectAll());
		params.put("ids", ids);
		params.put("idColumnName", COLUMN_NAME_ID);
		if (isRemovableBO()) {
			params.put("removed", Removable.LOGIC_DELETE);
		}
		String tableName = entityClass.getAnnotation(Table.class).name();
		String srcs = "";
		if (tableName.equalsIgnoreCase("tm_plant")) {
			List<String> arays = getSqlSession().selectList("UserMapper.queryPlantPermissionByUserId",WebContextHolder.getCurrentUser().getId());
			srcs = StringUtil.joins(arays,',');
		}
		if (tableName.equalsIgnoreCase("tm_workshop")) {
			List<String> arays = getSqlSession().selectList("UserMapper.queryWorkshopPermissionByUserId",WebContextHolder.getCurrentUser().getId());
			srcs = StringUtil.joins(arays,',');
		}
		if (tableName.equalsIgnoreCase("tm_line")) {
			List<String> arays = getSqlSession().selectList("UserMapper.queryLinePermissionByUserId",WebContextHolder.getCurrentUser().getId());
			srcs = StringUtil.joins(arays,',');
		}
		if (tableName.equalsIgnoreCase("tm_uloc")) {
			List<String> arays = getSqlSession().selectList("UserMapper.queryUlocPermissionByUserId",WebContextHolder.getCurrentUser().getId());
			srcs = StringUtil.joins(arays,',');
		}
		if(StringUtils.isNotBlank(srcs)){
			params.put("roleMasterSetData",srcs);
		}
		return changeMapsToBeans(getSqlSession().selectList("MasterBaseMapper.findAll", params));
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageResult<T> findBy(QueryCriteria queryCriteria) {

		Map<String, Object> queryCondition = queryCriteria.getQueryCondition();
		for (String fuzzyQueryFiled : queryCriteria.getFuzzyQueryFileds()) {
			if (queryCondition.containsKey(fuzzyQueryFiled)
					&& !(queryCondition.get(fuzzyQueryFiled) instanceof DataPermission)) {
				CriteriaUtil.bothPercent(queryCriteria, fuzzyQueryFiled);
			}
		}

		final String sql = getSqlBy(queryCriteria);
		final Integer offsetIndex = queryCriteria.getCurrentIndex();
		final Integer pageSize = queryCriteria.getRows();
		boolean queryPage = queryCriteria.isQueryPage();

		return (PageResult<T>) query(sql, queryCriteria.getQueryCondition(), offsetIndex, pageSize, queryPage);
	}

	@Override
	public List<Map<String, Object>> findAllSql(StringBuffer sql) {
		return super.findAllSql(sql);
	}

	private PageResult<?> query(String sql, Map<String, Object> params, int offsetIndex, int pageSize,
			boolean queryPage) {

		PageResult<T> pr = new PageResult<T>();
		if (queryPage) {
			pr.setCurrentIndex(offsetIndex);
			pr.setPageSize(pageSize);
			pr.setTotalCount(queryCountSql(sql, params));
			params.put("sql", sql);
			params.put("firstRow", offsetIndex);
			params.put("lastRow", pageSize + offsetIndex);
			params.put("pageSize", pageSize);
			if (0 != pageSize) {
				pr.setTotalPage((int) ((pr.getTotalCount() + pageSize - 1) / pageSize));
				pr.setCurrentPage((offsetIndex + pageSize) / pageSize);
			}
		} else {
			params.put("sql", sql);
		}
		if (sql.indexOf("ORDER BY") > 0) {
			String orderBy = sql.substring(sql.indexOf("ORDER BY"));
			if (orderBy.indexOf(SUFFIX) > 0) {
				String orderFeild = orderBy.substring(orderBy.lastIndexOf(SUFFIX) + SUFFIX.length() + 1);
				params.put("orderBy", "ORDER BY " + orderFeild);
			} else {
				params.put("orderBy", orderBy);
			}

		} else {
			params.put("orderBy", "ORDER BY ID");
		}
		pr.setContent(changeMapsToBeans(
				getSqlSession().selectList("MasterBaseMapper.queryByPage" + "_" + getDatabaseType(), params)));
		return pr;

	}

	@Override
	public String getSqlBy(QueryCriteria queryCriteria) {
		Map<String, Object> parameters = clearBlankAndChangeType(queryCriteria.getQueryCondition());
		StringBuffer queryHql = new StringBuffer(generateQueryHql(queryCriteria));
		queryHql.append(" where 1=1 ");
		if (!queryCriteria.isIncludesReomved()) {
			appendValid(parameters, queryHql);
		}
		if (parameters.size() > 0) {
			queryHql.append(getConditionHql(parameters, queryCriteria));
		}
		appendDataPermission(queryHql);
		if (queryHql.indexOf("AND ()") > 0) {
			queryHql.replace(queryHql.indexOf("AND ()"), queryHql.indexOf("AND ()") + 6, "");
		}

		if (queryHql.indexOf("tm_plant") > 0) {
			List<String> arrays = getSqlSession().selectList("UserMapper.queryPlantPermissionByUserId", WebContextHolder.getCurrentUser().getId());
			String srcs = StringUtil.joins(arrays, ',');
			if (StringUtils.isNoneBlank(srcs)) {
				queryHql.append(" and tmplant_0.id in (" +srcs + ")");
			}
		}
		if (queryHql.indexOf("tm_workshop") > 0) {
			List<String> arrays = getSqlSession().selectList("UserMapper.queryWorkshopPermissionByUserId", WebContextHolder.getCurrentUser().getId());
			String srcs = StringUtil.joins(arrays, ',');
			if (StringUtils.isNoneBlank(srcs)) {
				queryHql.append(" and tmworkshop_0.id in (" + srcs + ")");
			}
		}
		if (queryHql.indexOf("tm_line") > 0) {
			List<String> arrays = getSqlSession().selectList("UserMapper.queryLinePermissionByUserId", WebContextHolder.getCurrentUser().getId());
			String srcs = StringUtil.joins(arrays, ',');
			if (StringUtils.isNoneBlank(srcs)) {
				queryHql.append(" and tmline_0.id in (" + srcs + ")");
			}
		}
		if (queryHql.indexOf("tm_uloc") > 0) {
			List<String> arrays = getSqlSession().selectList("UserMapper.queryUlocPermissionByUserId", WebContextHolder.getCurrentUser().getId());
			String srcs = StringUtil.joins(arrays, ',');
			if (StringUtils.isNoneBlank(srcs)) {
				queryHql.append(" and tmuloc_0.id in (" + srcs + ")");
			}
		}

		if (queryCriteria.getOrderField() == null) {
			queryCriteria.setOrderField(getEntityClassName().toLowerCase() + SUFFIX + "." + COLUMN_NAME_ID);
		} else {
			String orderStr = queryCriteria.getOrderField();
			String dbType = getDatabaseType();
			if (orderStr.contains(".")) {
				if (ConstantUtil.DATABASE_PROD_NAME_MYSQL.equalsIgnoreCase(dbType)) {
					orderStr = "`" + orderStr + "`";
				} else if (ConstantUtil.DATABASE_PROD_NAME_SQL_SERVER.equalsIgnoreCase(dbType)) {
					orderStr = "[" + orderStr + "]";
				} else if (ConstantUtil.DATABASE_PROD_NAME_ORACLE.equalsIgnoreCase(dbType)
						|| ConstantUtil.DATABASE_PROD_NAME_SAP_HANA.equalsIgnoreCase(dbType)) {
					orderStr = "\"" + orderStr + "\"";
				} else {
					throw new DaoException("the database " + dbType + " is not registered for basedao");
				}
			}
			queryCriteria.setOrderField(orderStr);
		}
		queryHql.append(
				HqlUtil.generateHqlOrderClause(queryCriteria.getOrderField(), queryCriteria.getOrderDirection()));
		return queryHql.toString();
	}

	@Override
	protected StringBuffer generateQueryHql(QueryCriteria queryCriteria) {
		if (queryCriteria.isUseCacheSql() && sqlCacheMap.containsKey(ConstantUtil.SQL_CACHE_TYPE_PAGE_QUERY)) {
			return (StringBuffer) sqlCacheMap.get(ConstantUtil.SQL_CACHE_TYPE_PAGE_QUERY);
		}

		StringBuffer queryHql = new StringBuffer("SELECT ");
		StringBuffer fromSql = new StringBuffer(" FROM ");

		if (ConstantUtil.DATABASE_TYPE_SQL_SERVER.equalsIgnoreCase(getDatabaseType())) {
			queryHql.append(" top 100 percent 1 as _order_tmp, ");
		}

		fromSql.append(getTableName());
		fromSql.append(" ");
		fromSql.append(getEntityClassName().toLowerCase());
		fromSql.append(SUFFIX);

		List<String> excludeFields = queryCriteria.getExcludeFields();

		String beanName = entityClass.getSimpleName().toLowerCase() + SUFFIX;
		addField(queryHql, fromSql, entityClass, queryCriteria.isQueryRelationEntity(), excludeFields, beanName, "");

		queryHql.deleteCharAt(queryHql.length() - 1);
		queryHql.append(fromSql);
		if (queryCriteria.isUseCacheSql()) {
			sqlCacheMap.put(ConstantUtil.SQL_CACHE_TYPE_PAGE_QUERY, queryHql);
		}
		return queryHql;
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

	private void addField(StringBuffer sql, StringBuffer fromSql, Class<?> beanClass, Boolean addSuplerFiled,
			List<String> excludeFields, String beanName, String fieldName) {
		List<Field> fields = getAllField(beanClass, true);
		for (Field field : fields) {
			List<Class<?>> supers = ClassUtils.getAllSuperclasses(field.type);
			if (!excludeFields.contains(field.name) && fieldTypeMap.containsKey(field.type)) {
				sql.append(beanName);
				sql.append(".");
				// if (sql.indexOf(field.name + "\",") != -1) {
				// sql.append(getColumnName(field.name, beanClass));
				// sql.append(" as \"");
				// sql.append(firstCharLower(fieldName));
				// sql.append(firstCharUpper(field.name));
				// } else {
				sql.append(getColumnName(field.name, beanClass));
				sql.append(" as \"");
				StringBuffer fieldNameAlias = new StringBuffer();
				if (StringUtils.isNotEmpty(fieldName)) {
					fieldNameAlias.append(firstCharLower(fieldName));
					fieldNameAlias.append(".");
				}
				fieldNameAlias.append(field.name);
				if (fieldNameAlias.length() > 30) {
					fieldNameAlias = new StringBuffer(fieldNameAlias.substring(0, 30));
				}
				sql.append(fieldNameAlias);
				// }
				sql.append("\",");
			} else if (addSuplerFiled && !Collections.disjoint(supers, supperClass)) {
				String relationEntityName = field.name.toLowerCase() + SUFFIX;
				if (!relationEntityName.startsWith("tm")) {
					relationEntityName = "tm" + relationEntityName;
				}
				fromSql.append(" left join ");
				fromSql.append(field.type.getAnnotation(Table.class).name());
				fromSql.append(" ");
				fromSql.append(relationEntityName);
				fromSql.append(" on ");
				fromSql.append(beanName);
				fromSql.append(".");
				fromSql.append(getColumnName(field.name, beanClass));
				fromSql.append("=");
				fromSql.append(relationEntityName);
				fromSql.append(".");
				fromSql.append(getColumnName("id", field.type));
				List<String> subExcludeFields = new ArrayList<String>(excludeFields);
				// subExcludeFields.add("id");
				addField(sql, fromSql, field.type, false, subExcludeFields, relationEntityName, field.name);
			}
		}
	}

	private String getColumnName(String fieldName, Class<?> beanClass) {
		Method method = null;
		try {
			method = beanClass.getMethod("get" + firstCharUpper(fieldName));
		} catch (Exception e) {
		}
		try {
			if (method == null && fieldName.indexOf(ConstantUtil.QUERY_CONDITION_SUFFIX_START) != -1) {
				method = beanClass.getMethod("get" + firstCharUpper(fieldName).substring(0,
						fieldName.indexOf(ConstantUtil.QUERY_CONDITION_SUFFIX_START)));
			}
		} catch (Exception e) {
		}
		try {
			if (method == null && fieldName.indexOf(ConstantUtil.QUERY_CONDITION_SUFFIX_END) != -1) {
				method = beanClass.getMethod("get" + firstCharUpper(fieldName).substring(0,
						fieldName.indexOf(ConstantUtil.QUERY_CONDITION_SUFFIX_END)));
			}
			return method.getAnnotation(Column.class).name();
		} catch (Exception e) {
		}
		try {
			if (method == null && fieldName.endsWith(ConstantUtil.QUERY_CONDITION_SUFFIX_IN)) {
				method = beanClass.getMethod("get" + firstCharUpper(fieldName).substring(0,
						fieldName.lastIndexOf(ConstantUtil.QUERY_CONDITION_SUFFIX_IN)));
			}
			return method.getAnnotation(Column.class).name();
		} catch (Exception e) {
		}
		throw new DaoException(
				"not find a Column Annotation at class:" + beanClass.getSimpleName() + ";fieldName:" + fieldName);
	}

	private List<Field> getAllField(Class<?> beanClass, Boolean searchBaseEntity) {
		List<Field> result = new ArrayList<Field>();
		for (Method method : beanClass.getMethods()) {
			if (searchBaseEntity) {
				Class<?> returnType = method.getReturnType();
				List<?> supers = ClassUtils.getAllSuperclasses(returnType);
				if (!fieldTypeMap.containsKey(returnType) && Collections.disjoint(supperClass, supers)) {
					continue;
				}
			} else {
				if (!fieldTypeMap.containsKey(method.getReturnType())) {
					continue;
				}
			}
			if (method.getAnnotation(Transient.class) != null) {
				continue;
			}
			if (method.getName().startsWith("get")) {
				try {
					result.add(new Field(firstCharLower(method.getName().substring(3)), method.getReturnType(), method,
							method.getAnnotation(Column.class).name()));
				} catch (NullPointerException e) {
					throw new DaoException("class:" + getEntityClassName() + ", method :" + method.getName()
							+ "  column definition not found!");
				}
			}
		}
		Collections.sort(result, new Comparator<Field>() {

			@Override
			public int compare(Field o1, Field o2) {
				if (fieldTypeMap.containsKey(o1.type) && fieldTypeMap.containsKey(o2.type)) {
					return o1.name.compareTo(o2.name);
				} else if (!fieldTypeMap.containsKey(o1.type) && !fieldTypeMap.containsKey(o2.type)) {
					return o1.name.compareTo(o2.name);
				} else if (fieldTypeMap.containsKey(o1.type)) {
					return -1;
				}
				return 1;
				// return o1.name.compareTo(o2.name);
			}
		});
		return result;
	}

	private String firstCharLower(String str) {
		return str.replaceFirst(str.charAt(0) + "", (str.charAt(0) + "").toLowerCase().charAt(0) + "");
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

	private String getDatabaseType() {
		if (null == dataBaseType) {
			String dbProdName = getDatabaseProductName();
			if (dbProdName.toLowerCase().contains(ConstantUtil.DATABASE_PROD_NAME_ORACLE.toLowerCase())) {
				dataBaseType = ConstantUtil.DATABASE_PROD_NAME_ORACLE;
			} else if (dbProdName.toLowerCase().contains(ConstantUtil.DATABASE_PROD_NAME_MYSQL.toLowerCase())) {
				dataBaseType = ConstantUtil.DATABASE_PROD_NAME_MYSQL;
			} else if (dbProdName.toLowerCase().contains(ConstantUtil.DATABASE_PROD_NAME_SQL_SERVER.toLowerCase())) {
				dataBaseType = ConstantUtil.DATABASE_TYPE_SQL_SERVER;
			} else if (dbProdName.toLowerCase().contains(ConstantUtil.DATABASE_PROD_NAME_SAP_HANA.toLowerCase())) {
				dataBaseType = ConstantUtil.DATABASE_PROD_NAME_SAP_HANA;
			} else {
				throw new DaoException("the database " + dbProdName + " is not registered for basedao");
			}
		}
		return dataBaseType;
	}

	private String getDatabaseProductName() {
		DatabaseMetaData metaData;
		try {
			metaData = getSqlSession().getConfiguration().getEnvironment().getDataSource().getConnection()
					.getMetaData();
			return metaData.getDatabaseProductName();
		} catch (SQLException e) {
			logger.error(e);
			throw new DaoException("cannot determine database product name");
		}
	}

	private boolean isRemovableBO() {
		boolean result = false;
		try {
			result = Removable.class.isInstance(getEntityClass().newInstance());
		} catch (InstantiationException e) {
			throw new DaoException("system/wf_system.S0101");
		} catch (IllegalAccessException e) {
			throw new DaoException("system/wf_system.S0101");
		}
		return result;
	}

}
