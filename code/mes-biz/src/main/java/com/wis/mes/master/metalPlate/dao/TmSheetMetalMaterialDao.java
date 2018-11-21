package com.wis.mes.master.metalPlate.dao;

import java.util.List;

import com.wis.core.dao.BaseDao;
import com.wis.mes.master.metalPlate.entity.TmSheetMetalMaterial;

public interface TmSheetMetalMaterialDao extends BaseDao<TmSheetMetalMaterial> {

	List<TmSheetMetalMaterial> findByMachiningSurface(String model);
	public Integer getInventoryNumber(String model);
}
