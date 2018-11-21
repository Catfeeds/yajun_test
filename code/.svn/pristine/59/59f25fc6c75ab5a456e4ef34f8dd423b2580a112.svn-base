package com.wis.mes.master.parametermanage.service;

import java.util.List;
import java.util.Map;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.core.service.BaseService;
import com.wis.mes.master.equipment.entity.TmEquipmentParam;
import com.wis.mes.master.parametermanage.entity.TmParameterManage;
import com.wis.mes.master.parametermanage.vo.ParameterManageVo;

public interface TmParameterManageService extends BaseService<TmParameterManage> {
	/**获取所有符合条件的参数，当列表的列**/
	List<ParameterManageVo> parameterColumn();
	
	List<TmEquipmentParam> saveOrUpdateColumn();
	
	/**获取参数管理列表**/
	PageResult<Map<String,Object>> queryParameterManage(BootstrapTableQueryCriteria criteria);
	/**新增参数*/
	void doSaveParameter(String parameterManage);
	/**修改参数*/
	void doUpdateParameter(String parameterManage);
	/**通过参数主表Id获取参数范围**/
	TmParameterManage findByParameterManageId(String parameterManageId);
	
	/**删除参数范围**/
	void doDeleteParameterDetail(String ids);
	
	/**根据背番号获取维护好的参数范围**/
	List<ParameterManageVo> getParameterRange(String backNumber);
}
