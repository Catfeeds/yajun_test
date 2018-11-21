package com.wis.mes.master.equipment;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wis.mes.basis.BizBaseTestCase;
import com.wis.mes.master.equipment.entity.TmEquipment;
import com.wis.mes.master.equipment.entity.TmEquipmentAbnormal;
import com.wis.mes.master.equipment.service.TmEquipmentAbnormalService;
import com.wis.mes.master.equipment.service.TmEquipmentService;
import com.wis.mes.opc.util.OpcPropertiesConfig;

public class TmEquipmentAbnormalServiceTest extends BizBaseTestCase {
	@Autowired
	private TmEquipmentAbnormalService service;
	@Autowired
	private TmEquipmentService equipmentService;

	private Integer getEquipmentId(String equipmentNo) {
		TmEquipment bean = new TmEquipment();
		bean.setNo(equipmentNo);
		TmEquipment findUniqueByEg = equipmentService.findUniqueByEg(bean);
		assertNotNull(findUniqueByEg);
		return findUniqueByEg.getId();
	}

	@Test
	public void saveEquipmentAbnormalTest() {
		List<TmEquipmentAbnormal> list = new ArrayList<TmEquipmentAbnormal>();
		TmEquipmentAbnormal bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000000000001");
		bean.setAbnormalName("急停报警");
		bean.setEquipmentNo("XR5-001");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000000000001");
		bean.setAbnormalName("急停报警");
		bean.setEquipmentNo("XR5-002");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000000000001");
		bean.setAbnormalName("急停报警");
		bean.setEquipmentNo("XR5-004");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);

		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000000000001");
		bean.setAbnormalName("主柜急停");
		bean.setEquipmentNo("XR5-018");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);

		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000000000001");
		bean.setAbnormalName("捆包机异常");
		bean.setEquipmentNo("XR5-019");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);

		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000000000001");
		bean.setAbnormalName("捆包机异常");
		bean.setEquipmentNo("XR5-020");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);

		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000000000001");
		bean.setAbnormalName("操作盒1急停");
		bean.setEquipmentNo("XR5-021");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);

		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000000000001");
		bean.setAbnormalName("操作盒1急停");
		bean.setEquipmentNo("XR5-022");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);

		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000000000010");
		bean.setEquipmentNo("XR5-004");
		bean.setAbnormalName("马达报警");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);

		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000000000010");
		bean.setEquipmentNo("XR5-018");
		bean.setAbnormalName("系统低气压");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);

		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000000000010");
		bean.setEquipmentNo("XR5-019");
		bean.setAbnormalName("捆包超时");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);

		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000000000010");
		bean.setEquipmentNo("XR5-020");
		bean.setAbnormalName("捆包超时");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);

		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000000000010");
		bean.setEquipmentNo("XR5-021");
		bean.setAbnormalName("操作盒2急停");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);

		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000000000010");
		bean.setEquipmentNo("XR5-022");
		bean.setAbnormalName("操作盒2急停");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000000000100");
		bean.setEquipmentNo("XR5-004");
		bean.setAbnormalName("工作气压低报警");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000000000100");
		bean.setEquipmentNo("XR5-018");
		bean.setAbnormalName("操作盒急停");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000000000100");
		bean.setEquipmentNo("XR5-019");
		bean.setAbnormalName("粘带不良");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000000000100");
		bean.setEquipmentNo("XR5-020");
		bean.setAbnormalName("粘带不良");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000000000100");
		bean.setEquipmentNo("XR5-022");
		bean.setAbnormalName("操作盒3急停");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000000001000");
		bean.setEquipmentNo("XR5-004");
		bean.setAbnormalName("N2气压低报警");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000000001000");
		bean.setEquipmentNo("XR5-018");
		bean.setAbnormalName("提升机机种错误");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000000001000");
		bean.setEquipmentNo("XR5-019");
		bean.setAbnormalName("温度异常");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000000001000");
		bean.setEquipmentNo("XR5-020");
		bean.setAbnormalName("温度异常");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000000001000");
		bean.setEquipmentNo("XR5-022");
		bean.setAbnormalName("操作盒4急停");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000000010000");
		bean.setEquipmentNo("XR5-004");
		bean.setAbnormalName("油温异常");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000000010000");
		bean.setEquipmentNo("XR5-018");
		bean.setAbnormalName("安全门报警");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000000100000");
		bean.setEquipmentNo("XR5-004");
		bean.setAbnormalName("油圧异常");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000001000000");
		bean.setEquipmentNo("XR5-004");
		bean.setAbnormalName("油面异常");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000100000000");
		bean.setEquipmentNo("XR5-001");
		bean.setAbnormalName("与上位机通讯超时");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000100000000");
		bean.setEquipmentNo("XR5-002");
		bean.setAbnormalName("A号泵异常");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000000100000000");
		bean.setEquipmentNo("XR5-004");
		bean.setAbnormalName("浄油机异常");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000001000000000");
		bean.setEquipmentNo("XR5-001");
		bean.setAbnormalName("B号泵异常");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000001000000000");
		bean.setEquipmentNo("XR5-002");
		bean.setAbnormalName("B号泵异常");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000001000000000");
		bean.setEquipmentNo("XR5-004");
		bean.setAbnormalName("浄油机油罐空");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000010000000000");
		bean.setEquipmentNo("XR5-001");
		bean.setAbnormalName("门信号报警");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000010000000000");
		bean.setEquipmentNo("XR5-002");
		bean.setAbnormalName("C号泵异常");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000010000000000");
		bean.setEquipmentNo("XR5-004");
		bean.setAbnormalName("浄油机真空槽空");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000100000000000");
		bean.setEquipmentNo("XR5-001");
		bean.setAbnormalName("总气压异常");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000100000000000");
		bean.setEquipmentNo("XR5-002");
		bean.setAbnormalName("D号泵异常");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0000100000000000");
		bean.setEquipmentNo("XR5-004");
		bean.setAbnormalName("浄油机贮油槽空");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0001000000000000");
		bean.setEquipmentNo("XR5-002");
		bean.setAbnormalName("E号泵异常");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0001000000000000");
		bean.setEquipmentNo("XR5-004");
		bean.setAbnormalName("贮油槽空");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0010000000000000");
		bean.setEquipmentNo("XR5-002");
		bean.setAbnormalName("F号泵异常");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0010000000000000");
		bean.setEquipmentNo("XR5-004");
		bean.setAbnormalName("贮油槽最下限");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		bean = new TmEquipmentAbnormal();
		bean.setAbnormalCode("0010000000000000");
		bean.setEquipmentNo("XR5-002");
		bean.setAbnormalName("G号泵异常");
		bean.setTmEquipmentId(getEquipmentId(bean.getEquipmentNo()));
		list.add(bean);
		
		service.doSaveBatch(list);
	}
	
	@Test
	public void testEquipment() {
		 String[] ORDERCOMM_STATUSKEY = OpcPropertiesConfig.getPropertyByKey("opc.bj.regional.production.state").split(",");
		 System.out.println("aaa");
	}
}
