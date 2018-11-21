package com.wis.mes.master.equipment.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.systemadmin.entity.User;
import com.wis.core.dao.PageResult;
import com.wis.mes.master.base.dao.impl.MasterBaseDaoImpl;
import com.wis.mes.master.equipment.dao.TmEquipmentMaterialsDao;
import com.wis.mes.master.equipment.entity.TmEquipmentMaterials;

@Repository
public class TmEquipmentMaterialsDaoImpl extends MasterBaseDaoImpl<TmEquipmentMaterials> implements TmEquipmentMaterialsDao {

	@Override
	public PageResult<?> getPartPageResult(BootstrapTableQueryCriteria criteria) {
		PageResult<User> pageResult = new PageResult<User>();
		Integer offsetIndex = criteria.getCurrentIndex();
		Integer pageSize = criteria.getRows();
		Integer tmEquipmentId = (Integer) criteria.getQueryCondition().get("tmEquipmentId");
		Integer tmPlantId = (Integer) criteria.getQueryCondition().get("tmPlantId");
		String type = criteria.getQueryCondition().get("type").toString();
		pageResult.setCurrentIndex(criteria.getCurrentIndex());
		pageResult.setPageSize(criteria.getRows());
		Map<String, Object> params = criteria.getQueryCondition();
		pageResult.setTotalCount(getUserCount(params));
		params.put("firstRow", offsetIndex);
		params.put("pageSize", pageSize);
		params.put("tmEquipmentId",tmEquipmentId);
		params.put("tmPlantId",tmPlantId);
		params.put("type",type);
		if (0 != pageSize) {
			pageResult.setTotalPage((int) ((pageResult.getTotalCount() + pageSize - 1) / pageSize));
			pageResult.setCurrentPage((int) ((offsetIndex + pageSize) / pageSize));
		}
		List<User> content = getSqlSession().selectList("EquipmentMapper.getPartPageResult", params);
		pageResult.setContent(content);
		return pageResult;
	}
	
	private int getUserCount(Map<String, Object> params) {
		return getSqlSession().selectOne("EquipmentMapper.getPartCount", params);
	}
}
