package com.wis.mes.master.equipment.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.master.equipment.dao.TmpEquipmentStatusDao;
import com.wis.mes.master.equipment.entity.TmpEquipmentStatus;

@Repository
public class TmpEquipmentStatusDaoImpl extends BaseDaoImpl<TmpEquipmentStatus> implements TmpEquipmentStatusDao {

	
	@Override
	public String getSqlBy(QueryCriteria queryCriteria) {
		String sql = super.getSqlBy(queryCriteria);
		sql = sql.replace("FROM tmp_equipment_status tmpequipmentstatus_0",
				",coderule.code as\"codeRule.code\", coderule.code_desc as \"codeRule.codeDesc\"  FROM tmp_equipment_status tmpequipmentstatus_0 ");
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(" left join tm_code_rule coderule");
		sqlBuffer.append(" on coderule.plc_code =  tmequipment_0.no || ' ' || tmpequipmentstatus_0.status_number");
		sqlBuffer.append(" and coderule.tm_line_id = tmpequipmentstatus_0.tm_line_id  where 1 = 1");
		sql = sql.replace("where 1=1", sqlBuffer);
		return sql;
	}
	
	@Override
	public PageResult<Map<String,Object>> warmReport(BootstrapTableQueryCriteria criteria) {
		PageResult<Map<String,Object>> pageResult = new PageResult();
		Integer offsetIndex = criteria.getCurrentIndex();
		Integer pageSize = criteria.getRows();
		pageResult.setCurrentIndex(criteria.getCurrentIndex());
		pageResult.setPageSize(criteria.getRows());
		Map<String, Object> params = criteria.getQueryCondition();

		pageResult.setTotalCount(warmReportCnt(params));
        params.put("firstRow", offsetIndex);
        params.put("pageSize", (pageSize * criteria.getPage()));


		if (0 != pageSize) {
			pageResult.setTotalPage((int) ((pageResult.getTotalCount() + pageSize - 1) / pageSize));
			pageResult.setCurrentPage((int) ((offsetIndex + pageSize) / pageSize));
		}
		List<Map<String,Object>> content = getSqlSession().selectList("TmpEquipmentStatusMapper.warmReport", params);
		pageResult.setContent(content);
		return pageResult;
	}
	
	
	private int warmReportCnt(Map<String, Object> params) {
		return getSqlSession().selectOne("TmpEquipmentStatusMapper.warmReportCnt", params);
	}
	
	@Override
	public PageResult<TmpEquipmentStatus> queryPageTbEquipmentStatus(QueryCriteria criteria) {
		PageResult<TmpEquipmentStatus> pageResult = new PageResult<TmpEquipmentStatus>();
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
		List<TmpEquipmentStatus> content = getSqlSession().selectList("TmpEquipmentStatusMapper.queryPageTbEquipmentStatus",
				params);
		pageResult.setContent(content);
		return pageResult;
	}

	private int queryPageTbEquipmentStatus(Map<String, Object> param) {
		return getSqlSession().selectOne("TmpEquipmentStatusMapper.queryPageTbEquipmentStatusCount", param);
	}



    public List<Map<String, Object>> getFinNoFaultStopDetails(String createTimeStart, String createTimeEnd) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("createTimeStart", createTimeStart);
        param.put("createTimeEnd", createTimeEnd);
//        return getSqlSession().selectList("FinNoFaultDetailsMapper.getFinNoFaultStopDetails", param);
        return getSqlSession().selectList("TmpEquipmentStatusMapper.getTbNoFaultStopDetails", param);
    }

}
