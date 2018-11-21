package com.wis.mes.webservice;

import java.util.HashMap;
import java.util.Map;

import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wis.basis.common.utils.LogConstant;
import com.wis.mes.opc.service.OpcService;
import com.wis.mes.push.WebSocket;
import com.wis.mes.util.Code39Utils;

import net.sf.json.JSONObject;

/**
 * webservice接口实现
 */
@Component("scanningGun")
@WebService
public class ScanningGunServiceImpl implements ScanningGunService {
	private final static String AT_THE_TABLE_SIGNAL = "AT_THE_TABLE_SIGNAL";
	@Autowired
	private OpcService opcService;

	protected Log logger = LogFactory.getLog(LogConstant.BIZ_LOG);

	public ScanningGunServiceImpl() {
		logger.info("初始化ScanningGunServiceImpl");
	}

	@Override
	public void acceptBarCode(String code) {
		WebSocket socket = new WebSocket();
		socket.onMessage("UNIQUE_IDENTIFICATION_CODE"+Code39Utils.checkCode(code));
	}

	@Override
	public void atTheTableSignal(boolean status, boolean writeFlag, boolean plcStatus) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", status);
		map.put("writeFlag", writeFlag);
		map.put("plcStatus", plcStatus);
		WebSocket socket = new WebSocket();
		socket.onMessage(AT_THE_TABLE_SIGNAL + JSONObject.fromObject(map).toString());
	}

	@Override
	public void plcRfidRead(String groupCode, String inOrOut) {
		opcService.plcRfidRead(groupCode, inOrOut);
	}

	@Override
	public void station62Queuerecord(String groupCode,String plcSn) {
		opcService.station62Queuerecord(groupCode, plcSn);
	}

	@Override
	public void station120ClearTag(String groupCode) {
		opcService.station120ClearTag(groupCode);
	}
}
