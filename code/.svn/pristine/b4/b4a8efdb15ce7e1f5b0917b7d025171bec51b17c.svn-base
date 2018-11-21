package com.wis.mes.production.ulocstatus.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.production.ulocstatus.dao.TbUlocStatusDao;
import com.wis.mes.production.ulocstatus.entity.TbUlocStatus;

@Repository
public class TbUlocStatusDaoImpl extends BaseDaoImpl<TbUlocStatus> implements TbUlocStatusDao {

	@Override
	public String getSqlBy(QueryCriteria queryCriteria) {
		Map<String, Object> queryCondition = queryCriteria.getQueryCondition();
		boolean isExport = false;
		if ("YES".equals(String.valueOf(queryCondition.get("EXPORT")))) {
			queryCondition.remove("EXPORT");
			isExport = true;
		}
		String sql = super.getSqlBy(queryCriteria);
		String orderSrc = "ORDER BY \"uloc.no\" " + queryCriteria.getOrderDirection();
		if (sql.indexOf(orderSrc) > -1) {
			sql = sql.replace(orderSrc, "ORDER BY to_number(regexp_substr(uloc_0.no,'[0-9]*[0-9]',1)) ASC,tbulocstatus_0.create_time DESC ");
		}
		if (isExport) {
			sql = sql.replace("ORDER BY tbulocstatus_0.ID DESC",
					" ORDER BY to_number(regexp_substr(uloc_0.no,'[0-9]*[0-9]',1))asc, worktime_0.work_date asc,worktime_0.shiftno asc,tbulocstatus_0.create_time asc");
		}
		sql = sql.replace("FROM tb_uloc_status tbulocstatus_0",
				", coderule.code as \"codeRule.code\",coderule.code_desc as \"codeRule.codeDesc\" FROM tb_uloc_status tbulocstatus_0");
		StringBuffer buffer = new StringBuffer();
		buffer.append(" left join tm_code_rule coderule on ");
		buffer.append("coderule.plc_code = uloc_0.no || ' ' || tbulocstatus_0.status_code ");
		buffer.append("and coderule.tm_line_id = uloc_0.tm_line_id ");
		buffer.append("where 1 = 1");
		sql = sql.replace("where 1=1", buffer);
		if (queryCondition.get("stationState") != null) {
			sql = sql.replace("tbulocstatus_0.STATION_STATE like  #{stationState,jdbcType=VARCHAR}",
					getStationStateQuerySql(queryCondition.get("stationState").toString()));
		}
		return sql;
	}

	private String getStationStateQuerySql(String stationState) {
		StringBuffer sql = new StringBuffer();
		if ("1".equals(stationState)) {// 异常
			sql.append("(substr(tbulocstatus_0.STATION_STATE,5,1)='1' ");
			sql.append(" or substr(tbulocstatus_0.STATION_STATE,6,1)='1' ");
			sql.append(" or substr(tbulocstatus_0.STATION_STATE,7,1)='1' ");
			sql.append(" or substr(tbulocstatus_0.STATION_STATE,8,1)='1' )");
		} else if ("2".equals(stationState)) {// 手动
			sql.append(" substr(tbulocstatus_0.STATION_STATE,3,1)='1' ");
			sql.append(" and substr(tbulocstatus_0.STATION_STATE,5,1)<>'1' ");
			sql.append(" and substr(tbulocstatus_0.STATION_STATE,6,1)<>'1' ");
			sql.append(" and substr(tbulocstatus_0.STATION_STATE,7,1)<>'1' ");
			sql.append(" and substr(tbulocstatus_0.STATION_STATE,8,1)<>'1' ");
		} else if ("3".equals(stationState)) {// 通过
			sql.append(" substr(tbulocstatus_0.STATION_STATE,2,1)='1' ");
			sql.append(" and substr(tbulocstatus_0.STATION_STATE,5,1)<>'1' ");
			sql.append(" and substr(tbulocstatus_0.STATION_STATE,6,1)<>'1' ");
			sql.append(" and substr(tbulocstatus_0.STATION_STATE,7,1)<>'1' ");
			sql.append(" and substr(tbulocstatus_0.STATION_STATE,8,1)<>'1' ");
		} else if ("4".equals(stationState)) {// 正常
			sql.append(" substr(tbulocstatus_0.STATION_STATE,4,1)='1' ");
			sql.append(" and substr(tbulocstatus_0.STATION_STATE,3,1)<>'1' ");
			sql.append(" and substr(tbulocstatus_0.STATION_STATE,2,1)<>'1' ");
			sql.append(" and substr(tbulocstatus_0.STATION_STATE,5,1)<>'1' ");
			sql.append(" and substr(tbulocstatus_0.STATION_STATE,6,1)<>'1' ");
			sql.append(" and substr(tbulocstatus_0.STATION_STATE,7,1)<>'1' ");
			sql.append(" and substr(tbulocstatus_0.STATION_STATE,8,1)<>'1' ");
		}
		return sql.toString();
	}

	@Override
	public PageResult<TbUlocStatus> queryPageTbUlocStatus(QueryCriteria criteria) {
		PageResult<TbUlocStatus> pageResult = new PageResult<TbUlocStatus>();
		Integer offsetIndex = criteria.getCurrentIndex();
		Integer pageSize = criteria.getRows() * criteria.getPage();
		pageResult.setCurrentIndex(criteria.getCurrentIndex());
		pageResult.setPageSize(criteria.getRows());
		Map<String, Object> params = criteria.getQueryCondition();
		if (params.get("stationState") != null) {
			String stationStateSql = getStationStateQuerySql(params.get("stationState").toString());
			stationStateSql = stationStateSql.replace("tbulocstatus_0.", "t.");
			params.put("stationState", stationStateSql);
		}
		pageResult.setTotalCount(queryPageTbUlocStatusCount(params));
		params.put("firstRow", offsetIndex);
		params.put("pageSize", pageSize);
		params.put("orderField", criteria.getOrderField());
		params.put("orderDirection", criteria.getOrderDirection());
		if (0 != pageSize) {
			pageResult.setTotalPage((int) ((pageResult.getTotalCount() + pageSize - 1) / pageSize));
			pageResult.setCurrentPage((int) ((offsetIndex + pageSize) / pageSize));
		}
		List<TbUlocStatus> content = getSqlSession().selectList("UlocStatusMapper.queryPageTbUlocStatus", params);
		pageResult.setContent(content);
		return pageResult;
	}

	private int queryPageTbUlocStatusCount(Map<String, Object> param) {
		return getSqlSession().selectOne("UlocStatusMapper.queryPageTbUlocStatusCount", param);
	}
}
