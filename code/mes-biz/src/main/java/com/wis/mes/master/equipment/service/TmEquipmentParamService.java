package com.wis.mes.master.equipment.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.mes.master.base.service.MasterBaseService;
import com.wis.mes.master.equipment.entity.TmEquipmentParam;

public interface TmEquipmentParamService extends MasterBaseService<TmEquipmentParam> {
	/**
	 * 
	* @Description: 保存设备可提供的参数
	* @param @param tmEquipmentId
	* @param @param tsParameterId
	* @param @param type
	* @param @param note
	* @return List<TmEquipmentParam>    返回类型 
	* @throws
	 */
	List<TmEquipmentParam> doSaveEquipmentParameter(Integer tmEquipmentId,String[] tsParameterId, String[] type, String[] note);

	PageResult<?> getParamPageResult(BootstrapTableQueryCriteria criteria);

	Map<String, Object> exportExcelData(HttpServletResponse response, List<TmEquipmentParam> content, String string,
			String[] header);
	
	/***验证参数名称是否重复*/
	Integer checkParameterName(String parameterName,Integer tmEquipmentId);
	
}
