package com.wis.mes.production.equipmentstatus.dao.impl;

import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.production.equipmentstatus.dao.TbEquipmentStatusDao;
import com.wis.mes.production.equipmentstatus.entity.TbEquipmentStatus;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class TbEquipmentStatusDaoImpl extends BaseDaoImpl<TbEquipmentStatus> implements TbEquipmentStatusDao {

	@Override
	public String getSqlBy(QueryCriteria queryCriteria) {
		String sql = super.getSqlBy(queryCriteria);
		sql = sql.replace("FROM tb_equipment_status tbequipmentstatus_0",
				",coderule.code as\"codeRule.code\", coderule.code_desc as \"codeRule.codeDesc\"  FROM tb_equipment_status tbequipmentstatus_0 ");
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(" left join tm_code_rule coderule");
		sqlBuffer.append(" on coderule.plc_code =  tmequipment_0.no || ' ' || tbequipmentstatus_0.status_number");
		sqlBuffer.append(" and coderule.tm_line_id = tbequipmentstatus_0.tm_line_id  where 1 = 1");
		sql = sql.replace("where 1=1", sqlBuffer);
		return sql;
	}

	@Override
	public PageResult<TbEquipmentStatus> queryPageTbEquipmentStatus(QueryCriteria criteria) {
		PageResult<TbEquipmentStatus> pageResult = new PageResult<TbEquipmentStatus>();
		Integer offsetIndex = criteria.getCurrentIndex();
		Integer pageSize = criteria.getRows() * criteria.getPage();
		pageResult.setCurrentIndex(criteria.getCurrentIndex());
		pageResult.setPageSize(criteria.getRows());
		Map<String, Object> params = criteria.getQueryCondition();
		pageResult.setTotalCount(queryPageTbEquipmentStatus(params));
		params.put("firstRow", offsetIndex);
		params.put("pageSize", pageSize);
		params.put("orderField", criteria.getOrderField());
		params.put("orderDirection", criteria.getOrderDirection());
		if (0 != pageSize) {
			pageResult.setTotalPage((int) ((pageResult.getTotalCount() + pageSize - 1) / pageSize));
			pageResult.setCurrentPage((int) ((offsetIndex + pageSize) / pageSize));
		}
		List<TbEquipmentStatus> content = getSqlSession().selectList("EquipmentStatusMapper.queryPageTbEquipmentStatus",
				params);
		pageResult.setContent(content);
		return pageResult;
	}

	private int queryPageTbEquipmentStatus(Map<String, Object> param) {
		return getSqlSession().selectOne("EquipmentStatusMapper.queryPageTbEquipmentStatusCount", param);
	}
}
