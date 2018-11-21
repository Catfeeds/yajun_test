package com.wis.mes.master.equipment.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.mes.master.base.service.MasterBaseService;
import com.wis.mes.master.equipment.entity.TmEquipmentResponsiblePerson;

public interface TmEquipmentResponsiblePersonService extends MasterBaseService<TmEquipmentResponsiblePerson> {
	
	List<TmEquipmentResponsiblePerson> doSaveBatch(Integer tmEquipmentId,String[] tsUserId, String[] note);

	PageResult<?> getUserPageResult(QueryCriteria criteria);

	Map<String, Object> exportExcelData(HttpServletResponse response, List<TmEquipmentResponsiblePerson> content,
			String string, String[] header);
}
