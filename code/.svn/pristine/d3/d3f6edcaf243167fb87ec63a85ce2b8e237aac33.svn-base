package com.wis.mes.production.pmc.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.ConstantUtils;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.opc.util.OpcHelper;
import com.wis.mes.production.pmc.dao.TbPmcPmstDao;
import com.wis.mes.production.pmc.entity.TbPmcPmst;
import com.wis.mes.production.pmc.service.TbPmcPmstService;

@Service
public class TbPmcPmstServiceImpl extends BaseServiceImpl<TbPmcPmst> implements TbPmcPmstService {

	@Autowired
	public void setDao(TbPmcPmstDao dao) {
		this.dao = dao;
	}

	@Override
	public void sendParameterToOPC(TbPmcPmst bean) {
		if (ConstantUtils.PMST_AFFICHE.equals(bean.getModelType())) {
		} else if (ConstantUtils.FIN_PUNCH_AFFICHE.equals(bean.getModelType())) {
		} else if (ConstantUtils.FIN_PUNCH_PARAMETER.equals(bean.getModelType())) {
			String[] items = new String[] { 
					"Channel4.L33.Setting.Given_Punch_Speed",
					"Channel4.L33.Setting.Run_Rate_Green",
					"Channel4.L33.Setting.Run_Rate_Red",
					"Channel4.L33.Setting.Punch_Speed_Green",
					"Channel4.L33.Setting.Punch_Speed_Red",
					"Channel4.L33.Setting.Reference_Run_Rate",
					"Channel4.L33.Setting.Screen_Protect_Minute",
					"Channel4.L33.Setting.Screen_Protect_Second"};
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("Given_Punch_Speed", bean.getTargetImpulse());// 目标冲速
			map.put("Run_Rate_Green", bean.getPbTargetMobility()); // 警戒值
			map.put("Run_Rate_Red", bean.getMobilityExtreme());// 
			map.put("Punch_Speed_Green", bean.getPbTargetImpulse());// 
			map.put("Punch_Speed_Red", bean.getImpulseExtreme());//
			map.put("Reference_Run_Rate", bean.getTargetMobility());//目标可动率
			map.put("Screen_Protect_Minute", bean.getSpacingInterval());// 屏保分钟
			map.put("Screen_Protect_Second", bean.getRetentionTime());// 屏保秒
			OpcHelper.sendDataToOpc(items, map);
		} else if (ConstantUtils.PMST_WORK_PARAMETER.equals(bean.getModelType())) {
			String[] items = new String[] { "Channel3.L72_7.Setting_Real.HMI_S_Run_Rate",
					"Channel3.L72_7.Setting_Real.HMI_S_AC_Energy_Work",
					"Channel3.L72_7.Setting_Real.HMI_S_Device_Energy_Work",
					"Channel3.L72_7.Setting_Real.HMI_S_Light_Energy_Work" };
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("HMI_S_Run_Rate", bean.getTargetMobility());// 目标可动率
			map.put("HMI_S_AC_Energy_Work", Float.valueOf(bean.getAirConditionerEc())); // 空调能耗
			map.put("HMI_S_Device_Energy_Work", Float.valueOf(bean.getEquipmentEc()));// 设备能耗
			map.put("HMI_S_Light_Energy_Work", Float.valueOf(bean.getLightingEc()));// 照明能耗
			OpcHelper.sendDataToOpc(items, map);
		} else if (ConstantUtils.PMST_RECESS_PARAMETER.equals(bean.getModelType())) {
			String[] items = new String[] { "Channel3.L72_7.Setting_Real.HMI_S_AC_Energy_Rest",
					"Channel3.L72_7.Setting_Real.HMI_S_Device_Energy_Rest",
					"Channel3.L72_7.Setting_Real.HMI_S_Light_Energy_Rest" };
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("HMI_S_AC_Energy_Rest", Float.valueOf(bean.getAirConditionerEc())); // 空调能耗
			map.put("HMI_S_Device_Energy_Rest", Float.valueOf(bean.getEquipmentEc()));// 设备能耗
			map.put("HMI_S_Light_Energy_Rest", Float.valueOf(bean.getLightingEc()));// 照明能耗
			OpcHelper.sendDataToOpc(items, map);
		}else if(ConstantUtils.EARLY_INSPECTION_TIME.equals(bean.getModelType())) {
			String[] items = new String[] { "Channel3.L72_7.Setting_Real.HMI_S_Before_Work_Duration" };
			Map<String, Object> map = new HashMap<String, Object>();
			if(null != bean.getEarlyInspectionTime()) {
				map.put("HMI_S_Before_Work_Duration", bean.getEarlyInspectionTime()); //早会点检时间
				OpcHelper.sendDataToOpc(items, map);
			}
		}
	}
}
