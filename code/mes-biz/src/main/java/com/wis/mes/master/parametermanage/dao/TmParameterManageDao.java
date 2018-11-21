package com.wis.mes.master.parametermanage.dao;

import java.util.List;
import java.util.Map;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.BaseDao;
import com.wis.core.dao.PageResult;
import com.wis.mes.master.equipment.entity.TmEquipmentParam;
import com.wis.mes.master.parametermanage.entity.TmParameterManage;
import com.wis.mes.master.parametermanage.vo.ParameterManageVo;

public interface TmParameterManageDao extends BaseDao<TmParameterManage> {
	/**获取所有符合条件的参数，当列表的列**/
	List<ParameterManageVo> parameterColumn();
	
	List<TmEquipmentParam> saveOrUpdateColumn();
	
	PageResult<Map<String,Object>> queryParameterManage(BootstrapTableQueryCriteria criteria);
	
	/**通过参数主表Id获取参数范围**/
	TmParameterManage findByParameterManageId(String parameterManageId);
	
	/**删除参数范围**/
	void doDeleteParameterDetail(String ids);
	
	/**根据背番号获取维护好的参数范围**/
	List<ParameterManageVo> getParameterRange(String backNumber);
	
}
