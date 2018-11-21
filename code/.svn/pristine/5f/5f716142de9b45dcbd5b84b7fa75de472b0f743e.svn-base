package com.wis.mes.master.equipment.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.mes.master.base.service.MasterBaseService;
import com.wis.mes.master.equipment.entity.TmEquipmentMaterials;

public interface TmEquipmentMaterialsService extends MasterBaseService<TmEquipmentMaterials> {
	/**
	 * 
	* @Description: 保存设备可消耗的辅材
	* @param @param tmEquipmentId
	* @param @param tmPartId
	* @param @param note
	* @param @return    设定文件 
	* @return List<TmEquipmentMaterials>    返回类型 
	* @throws
	 */
	List<TmEquipmentMaterials> doSaveBatch(Integer tmEquipmentId,String[] tmPartId, String[] note);

	PageResult<?> getPartPageResult(BootstrapTableQueryCriteria criteria);

	Map<String, Object> exportExcelData(HttpServletResponse response, List<TmEquipmentMaterials> content, String string,
			String[] header);
}
