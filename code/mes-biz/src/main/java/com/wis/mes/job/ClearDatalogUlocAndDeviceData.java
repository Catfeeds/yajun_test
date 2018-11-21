package com.wis.mes.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wis.mes.opc.group.service.KsGroupService;

/**
 * @author liuzejun
 * 
 * @desc 清除 datalog 工位和设备的状态 信息
 */
@Component(value = "clearDataLogUlocAndDeviceData")
public class ClearDatalogUlocAndDeviceData {
	@Autowired
	private KsGroupService service;

	public void execue() {
		service.clearPlcStationDeviceStatusData();
		String[] tableNames = new String[] { "fin_add_oil_stop_temp", "fin_add_oil_temp",
				"fin_change_material_stop_temp", "fin_change_material_temp", "fin_check_temp",
				"fin_communication_lost_temp", "fin_device_status_temp", "fin_faulted_temp", "fin_fetch_material_temp",
				"fin_frame_rotate_temp", "fin_no_fault_stop_temp", "fin_overtime_time_temp", "fin_real_punch_time_temp",
				"fin_switch_stop_temp", "fin_switch_temp", "fin_wait_add_oil_temp", "fin_wait_change_material_temp",
				"fin_wait_frame_rotate_temp", "fin_wait_switch_temp","tmp_maintenance_info_temp","tmp_device_status_temp",
				"tmp_production_temp","tmp_region_temp"};
		for (String tableName : tableNames) {
			service.clearFinTempTable(tableName);
		}
	}
}
