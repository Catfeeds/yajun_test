package com.wis.mes.production.metalplate.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.DateUtils;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.metalPlate.entity.TmSheetMetalMaterial;
import com.wis.mes.master.metalPlate.service.TmSheetMetalMaterialService;
import com.wis.mes.production.metalplate.dao.TbMetalPlateSourceDataDao;
import com.wis.mes.production.metalplate.entity.TbMetalPlateSourceData;
import com.wis.mes.production.metalplate.service.TbMetalPlateSourceDataService;

@Service
public class TbMetalPlateSourceDataServiceImpl extends BaseServiceImpl<TbMetalPlateSourceData>
		implements TbMetalPlateSourceDataService {

	@Autowired
	public void setDao(TbMetalPlateSourceDataDao dao) {
		this.dao = dao;
	}

	@Autowired
	private TmSheetMetalMaterialService sheetMetalMaterialService;
	@Autowired
	private GlobalConfigurationService configurationService;

	@Override
	public List<TbMetalPlateSourceData> findByCollectEg(TbMetalPlateSourceData eg) {
		return ((TbMetalPlateSourceDataDao) dao).findByCollectEg(eg);
	}

	@Override
	public Integer findBySumPlannedProductionByModel(TbMetalPlateSourceData eg) {
		return ((TbMetalPlateSourceDataDao) dao).findBySumPlannedProductionByModel(eg);
	}

	@Override
	public TbMetalPlateSourceData findSourceDataByRecordId(Integer recordId) {
		return ((TbMetalPlateSourceDataDao) dao).findSourceDataByRecordId(recordId);
	}

	@Override
	public Integer findConsumeApcTotalBySourceDataId(Integer sourceDataId) {
		return ((TbMetalPlateSourceDataDao) dao).findConsumeApcTotalBySourceDataId(sourceDataId);
	}

	@Override
	public Integer findInProductionNumBySourceDataId(Integer sourceDataId) {
		return ((TbMetalPlateSourceDataDao) dao).findInProductionNumBySourceDataId(sourceDataId);
	}

	@SuppressWarnings("resource")
	@Override
	public void apc21SourceDataAcquire() {
		String date = DateUtils.getNowDateByString();
		//将加工図面作为key
		Map<String, TmSheetMetalMaterial> materials = getAllMaterialMap();

		// 每天早上更新主数据库存
		for (String model : materials.keySet()) {
			//materials.get(model).setInventoryNumber(getInventoryNumber(model, date));
			materials.get(model).setApcPlannedDemand(0);//钣金原材料主数据APC当日计划需求数每天默认重置为0
			sheetMetalMaterialService.doUpdate(materials.get(model));
		}

		// ACP21源数据获取汇总存库
		try {
			Map<String, Map<String, Integer>> datas = new HashMap<String, Map<String, Integer>>();
			String sourceFilePath =  configurationService.getValueByKey(ConstantUtils.APC21_FILE_PATH);
			File file = new File(sourceFilePath);
			BufferedReader reader = new   BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
			//先把列表和标题读出来，while里面读取数据
			reader.readLine();//标题
			reader.readLine();//列明
			String line = null;
			while ((line = reader.readLine()) != null) {
				String item[] = line.split(",");
				String model = item[4];
				String apcTotalNumber = item[9];
				String taskDate = item[11];
				if (StringUtils.isEmpty(model) || StringUtils.isEmpty(apcTotalNumber)
						|| StringUtils.isEmpty(taskDate)) {
					continue;
				}
				taskDate = DateUtils.formatObject2(taskDate, DateUtils.FORMAT_DATE_SLASH_YYYY_M_D,
						DateUtils.FORMAT_DATE_DEFAULT);
				if (!date.equals(taskDate)) {
					continue;
				}
				if (!materials.keySet().contains(model)) {
					continue;
				}
				if(!datas.containsKey(taskDate)) {
					datas.put(taskDate, new HashMap<String, Integer>());
				}
				if(datas.get(taskDate).containsKey(model)) {
					datas.get(taskDate).put(model,
							datas.get(taskDate).get(model) + Integer.parseInt(apcTotalNumber));
				}else {
					datas.get(taskDate).put(model, Integer.parseInt(apcTotalNumber));
				}
			}
			reader.close();
			TbMetalPlateSourceData eg = null;
			for (String d : datas.keySet()) {
				for (String model : datas.get(d).keySet()) {
					eg = new TbMetalPlateSourceData();
					eg.setModel(model);
					eg.setTaskDate(d);
					eg.setDataType(ConstantUtils.MP_SOURCE_DATA_TYPE_1);
					List<TbMetalPlateSourceData> sdatas = findByEg(eg);
					// 判断是否存在
					if (sdatas != null && sdatas.size() > 0) {
						continue;
					}
					Integer apcTotalNumber = datas.get(d).get(model);//apc计划需求数量
					Integer inventoryNumber =materials.get(model).getInventoryNumber();//剩余库存数
					eg.setApcTotalNumber(apcTotalNumber);
					eg.setInventoryNumber(inventoryNumber-apcTotalNumber);
					eg.setPlannedProduction(getPlannedProduction(materials.get(model), apcTotalNumber,inventoryNumber));
					int status = 0;
					if (eg.getPlannedProduction() == 0) {
						status = 3;
					}
					eg.setStatus(status);
					eg.setMaterialId(materials.get(model).getId());
					doSave(eg);
					//更新当日APC同步过来的计划需求总量,剩余库存数
					materials.get(model).setInventoryNumber(inventoryNumber-apcTotalNumber);
					materials.get(model).setApcPlannedDemand(datas.get(d).get(model));
					sheetMetalMaterialService.doUpdate(materials.get(model));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 计算每日库存数量
	@Override
	public Integer getInventoryNumber(String model, String today) {
		String date = DateUtils.getOnTheDateByString(today);
		Map<String, String> query = new HashMap<String, String>();
		//taskDate日期前一天，model加工図面的key
		query.put("model", model);
		query.put("taskDate", date);
		//算出库存
		Integer inventoryNumber = ((TbMetalPlateSourceDataDao) dao).findInventoryNumberByDate(query);
		if (inventoryNumber == null) {
			inventoryNumber = 0;
		}
		if (inventoryNumber.intValue() < 0) {
			// 库存计算出现负数，系统追加排产
			inventoryNumber = Math.abs(inventoryNumber);
			try {
				//钣金生产管理
				TbMetalPlateSourceData diffSourceData = new TbMetalPlateSourceData();
				diffSourceData.setTaskDate(today);
				diffSourceData.setDataType(ConstantUtils.MP_SOURCE_DATA_TYPE_3);
				diffSourceData.setApcTotalNumber(inventoryNumber);
				diffSourceData.setInventoryNumber(0);
				diffSourceData.setStatus(0);
				diffSourceData.setModel(model);
				// 计算计划生产数   钣金原材料
				TmSheetMetalMaterial eg = new TmSheetMetalMaterial();
				eg.setMachiningSurface(model);
				eg = sheetMetalMaterialService.findUniqueByEg(eg);
				int plannedProduction = eg.getBatchNumber()
						* (int) Math.ceil(inventoryNumber / eg.getBatchNumber().doubleValue());
				diffSourceData.setPlannedProduction(plannedProduction);//计划数量
				diffSourceData.setMaterialId(eg.getId());//关联设备主数据
				doSave(diffSourceData);
			} catch (Exception e) {
				log.error(ExceptionUtils.getStackTrace(e));
			}
			inventoryNumber = 0;
		}
		return inventoryNumber;
	}

	// 计算计划数量
	private Integer getPlannedProduction(TmSheetMetalMaterial eg, Integer apcTotalNumber, Integer inventoryNumber) {
		apcTotalNumber = apcTotalNumber - inventoryNumber;
		if (apcTotalNumber < 0) {
			apcTotalNumber = 0;
		}
		return eg.getBatchNumber() * (int) Math.ceil(apcTotalNumber.doubleValue() / eg.getBatchNumber().doubleValue());
	}

	private Map<String, TmSheetMetalMaterial> getAllMaterialMap() {
		List<TmSheetMetalMaterial> sms = sheetMetalMaterialService.findAll();
		Map<String, TmSheetMetalMaterial> materials = new HashMap<String, TmSheetMetalMaterial>();
		for (TmSheetMetalMaterial sm : sms) {
			materials.put(sm.getMachiningSurface(), sm);
		}
		return materials;
	}
}
