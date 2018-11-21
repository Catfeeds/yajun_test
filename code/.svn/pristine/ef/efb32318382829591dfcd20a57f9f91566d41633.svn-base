package com.wis.mes.master.equipment.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.basis.systemadmin.entity.User;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.mes.master.base.dao.impl.MasterBaseDaoImpl;
import com.wis.mes.master.equipment.dao.TmEquipmentResponsiblePersonDao;
import com.wis.mes.master.equipment.entity.TmEquipmentResponsiblePerson;

@Repository
public class TmEquipmentResponsiblePersonDaoImpl extends MasterBaseDaoImpl<TmEquipmentResponsiblePerson> implements TmEquipmentResponsiblePersonDao {

	@Override
	public PageResult<?> getUserPageResult(QueryCriteria criteria) {
		PageResult<User> pageResult = new PageResult<User>();
		Integer offsetIndex = criteria.getCurrentIndex();
		Integer pageSize = criteria.getRows();
		Integer tmEquipmentId = (Integer) criteria.getQueryCondition().get("tmEquipmentId");
		pageResult.setCurrentIndex(criteria.getCurrentIndex());
		pageResult.setPageSize(criteria.getRows());
		Map<String, Object> params = criteria.getQueryCondition();
		pageResult.setTotalCount(getUserCount(params));
		params.put("firstRow", offsetIndex);
		params.put("pageSize", pageSize);
		params.put("tmEquipmentId",tmEquipmentId);
		if (0 != pageSize) {
			pageResult.setTotalPage((int) ((pageResult.getTotalCount() + pageSize - 1) / pageSize));
			pageResult.setCurrentPage((int) ((offsetIndex + pageSize) / pageSize));
		}
		List<User> content = getSqlSession().selectList("EquipmentMapper.getUserPageResult", params);
		pageResult.setContent(content);
		return pageResult;
	}
	
	
	private int getUserCount(Map<String, Object> params) {
		return getSqlSession().selectOne("EquipmentMapper.getUserCount", params);
	}
}
