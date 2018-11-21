package com.wis.mes.master.equipment.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.systemadmin.entity.User;
import com.wis.core.dao.PageResult;
import com.wis.mes.master.base.dao.impl.MasterBaseDaoImpl;
import com.wis.mes.master.equipment.dao.TmEquipmentParamDao;
import com.wis.mes.master.equipment.entity.TmEquipmentParam;

@Repository
public class TmEquipmentParamDaoImpl extends MasterBaseDaoImpl<TmEquipmentParam> implements TmEquipmentParamDao {

	@Override
	public PageResult<?> getParamPageResult(BootstrapTableQueryCriteria criteria) {
		PageResult<User> pageResult = new PageResult<User>();
		Integer offsetIndex = criteria.getCurrentIndex();
		Integer pageSize = criteria.getRows();
		Integer tmEquipmentId = (Integer) criteria.getQueryCondition().get("tmEquipmentId");
		pageResult.setCurrentIndex(criteria.getCurrentIndex());
		pageResult.setPageSize(criteria.getRows());
		Map<String, Object> params = criteria.getQueryCondition();
		pageResult.setTotalCount(getUserCount(params));
		params.put("firstRow", offsetIndex);
		params.put("pageSize", (pageSize * criteria.getPage()));
		params.put("tmEquipmentId",tmEquipmentId);
		if (0 != pageSize) {
			pageResult.setTotalPage((int) ((pageResult.getTotalCount() + pageSize - 1) / pageSize));
			pageResult.setCurrentPage((int) ((offsetIndex + pageSize) / pageSize));
		}
		List<User> content = getSqlSession().selectList("EquipmentMapper.getParamPageResult", params);
		pageResult.setContent(content);
		return pageResult;
	}
	
	private int getUserCount(Map<String, Object> params) {
		return getSqlSession().selectOne("EquipmentMapper.getParamCount", params);
	}
}
