package com.wis.mes.opc;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.mes.basis.BizBaseTestCase;
import com.wis.mes.opc.service.OpcService;
import com.wis.mes.opc.util.OpcHelper;
import com.wis.mes.opc.util.OpcInitUtil;
import com.wis.mes.rfid.service.RfidService;

public class OpcServerTest extends BizBaseTestCase {

	@Autowired
	private OpcService opcService;
	@Autowired
	private OpcInitUtil initUtil;
	@Autowired
	private RfidService rfidService;

	@Test
	public void testWrite() {
		JsonActionResult read = opcService.rfidStationWrite("70219A004F0104253311Q ");
		System.out.println(read.getData());
	}

	@Test
	public void testInit() {
		initUtil.initOpc();
	}

	@Test
	public void testGetSnByUlocNo() {
		String snByUlocNo = opcService.getSnByUlocNo("1");
		System.out.println(snByUlocNo);
	}

	@Test
	public void rfidStationWriteTest() {
		opcService.rfidStationWrite("QQ12345678901234567890");
	}

	@Test
	public void testRfid() {
		rfidService.plcRfidRead("25");
	}

	@Test
	public void testWebService() {
		while (true) {
			OpcHelper.sendDataToApp("plcRfidRead", "3", "3");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
		}
	}
}
