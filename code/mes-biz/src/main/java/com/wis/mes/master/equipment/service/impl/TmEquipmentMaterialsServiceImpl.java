package com.wis.mes.master.equipment.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.BaseExcelUtil;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.DictEntryService;
import com.wis.mes.master.base.service.impl.MasterBaseServiceImpl;
import com.wis.mes.master.equipment.dao.TmEquipmentMaterialsDao;
import com.wis.mes.master.equipment.entity.TmEquipmentMaterials;
import com.wis.mes.master.equipment.service.TmEquipmentMaterialsService;

@Service
public class TmEquipmentMaterialsServiceImpl extends MasterBaseServiceImpl<TmEquipmentMaterials> implements TmEquipmentMaterialsService {

	@Autowired
	public void setDao(TmEquipmentMaterialsDao dao) {
		this.dao = dao;
	}

	@Autowired
	private DictEntryService entryService;

	@Override
	public List<TmEquipmentMaterials> doSaveBatch(Integer tmEquipmentId,String[] tmPartId, String[] note) {
		Map<String, TmEquipmentMaterials> equipmentMaterialsMap = getEquipmentMaterialsMap(String.valueOf(tmEquipmentId));
		//新增
		List<TmEquipmentMaterials> equipmentMaterialsAddList = new ArrayList<TmEquipmentMaterials>();
		//更新
		List<TmEquipmentMaterials> equipmentMaterialsUpdateList = new ArrayList<TmEquipmentMaterials>();
		
		for(int i=0;i<tmPartId.length;i++){
			//如果数据已经存在则更新
			if(equipmentMaterialsMap.containsKey(tmPartId[i])){
				TmEquipmentMaterials tmEquipmentMaterials = equipmentMaterialsMap.get(tmPartId[i]);
				tmEquipmentMaterials.setTmPartId(Integer.valueOf(tmPartId[i]));
				tmEquipmentMaterials.setNote(getNote(note,i));
				equipmentMaterialsUpdateList.add(tmEquipmentMaterials);
			}else{
				TmEquipmentMaterials tmEquipmentMaterials = new TmEquipmentMaterials();
				tmEquipmentMaterials.setTmEquipmentId(tmEquipmentId);
				tmEquipmentMaterials.setTmPartId(Integer.valueOf(tmPartId[i]));
				tmEquipmentMaterials.setNote(getNote(note,i));
				equipmentMaterialsAddList.add(tmEquipmentMaterials);
			}
		}
		doSaveBatch(equipmentMaterialsAddList);
		if (equipmentMaterialsUpdateList.size() > 0) {
			doUpdateBatch(equipmentMaterialsUpdateList);
		}
		return equipmentMaterialsAddList;
	}
	
	private Map<String, TmEquipmentMaterials> getEquipmentMaterialsMap(String tmEquipmentId) {
		Map<String, TmEquipmentMaterials> map = new HashMap<String, TmEquipmentMaterials>();
		QueryCriteria criteria = new QueryCriteria();
		criteria.setQueryPage(false);
		criteria.getQueryCondition().put("tmEquipmentId", tmEquipmentId);
		List<TmEquipmentMaterials> content = findBy(criteria).getContent();
		for (TmEquipmentMaterials equipmentMaterials : content) {
			map.put(equipmentMaterials.getTmPartId().toString(), equipmentMaterials);
		}
		return map;
	}
	
	private String getNote(String[] note, int index) {
		return (note.length == 0)?"":note[index];
	}

	@Override
	public PageResult<?> getPartPageResult(BootstrapTableQueryCriteria criteria) {
		return ((TmEquipmentMaterialsDao)dao).getPartPageResult(criteria);
	}

	@Override
	public Map<String, Object> exportExcelData(HttpServletResponse response, List<TmEquipmentMaterials> list,
			String fileName, String[] header) {
		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();

		// 物料类型
		Map<String, DictEntry> eqMaterialType = entryService.getMapTypeCode(ConstantUtils.TYPE_CODE_PART_TYPE);
		for (TmEquipmentMaterials eqMaterial : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("辅材", eqMaterial.getPart().getNo() + "-" + eqMaterial.getPart().getNameCn());
			map.put("物料编号", eqMaterial.getPart().getNo());
			map.put("中文简称", getStr(eqMaterial.getPart().getNameCnS()));
			map.put("英文简称", getStr(eqMaterial.getPart().getNameEnS()));
			map.put("物料类型",
					eqMaterialType.containsKey(eqMaterial.getPart().getType())
							? eqMaterialType.get(eqMaterial.getPart().getType()).getName() : "");
			map.put("最大批次数量", getStr(eqMaterial.getPart().getBatchQty()));
			map.put("备注", getStr(eqMaterial.getNote()));
			exportDataList.add(map);
		}
		return BaseExcelUtil.exportData(response, exportDataList, fileName, header);
	}

	private String getStr(Object obj) {

		return obj == null ? "" : obj.toString();
	}

}
