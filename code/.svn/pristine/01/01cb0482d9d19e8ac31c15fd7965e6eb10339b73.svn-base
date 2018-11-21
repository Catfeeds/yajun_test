package com.wis.mes.opc;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wis.mes.basis.BizBaseTestCase;
import com.wis.mes.master.equipment.entity.TmEquipmentAbnormal;
import com.wis.mes.master.equipment.service.TmEquipmentAbnormalService;

public class EquipmentStatus extends BizBaseTestCase {
	@Autowired
	private TmEquipmentAbnormalService service;

	@Test
	public void saveEquipmentStatus() {
		TmEquipmentAbnormal eg = new TmEquipmentAbnormal();
		eg.setAbnormalName("贮油槽最下限");
		String code = "R5JE0017";
		String name = eg.getAbnormalName();
		List<TmEquipmentAbnormal> findByEg = service.findByEg(eg);
		StringBuffer sql = new StringBuffer();
		for (TmEquipmentAbnormal bean : findByEg) {
			String equipmentNo = bean.getEquipmentNo();
			String codeContent = equipmentNo + " " + bean.getAbnormalCode();
			String insertSql = "insert into tm_code_rule(id, code, code_desc, tm_line_id, distinguish, data_source,plc_code)values(seq_code_rule_id.nextval,'"
					+ code + "','" + name + "',162,'J','E','" + codeContent + "' );";
			sql.append(insertSql).append("\n");
		}
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
		System.out.println(sql);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	}
}
