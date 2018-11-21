package com.wis.mes.opc.callback.service;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.da.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wis.basis.common.utils.LogConstant;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.opc.callback.ReadCallBack;
import com.wis.mes.opc.service.OpcService;
import com.wis.mes.opc.util.OpcHelper;

@Component(value = "station119ReadService")
public class Station119ReadService implements ReadCallBack {
	private Log logger = LogFactory.getLog(LogConstant.OPC_LOG);

	@Autowired
	private OpcService opcService;
	
	@Override
	public synchronized void doChanged(List<Item> itemList, String groupCode) throws JIException {
		try {
			boolean flag = false;
			String outOrIn = "";
			for (Item items : itemList) {
				Object value = null;
				try {
					value = items.read(false).getValue().getObject();
				} catch (Exception e) {
					throw new BusinessException(e.getMessage());
				}
				if (value instanceof Boolean) {
					String item = items.getId().toUpperCase();
					if (item.contains("FLAG")) {
						flag = new Boolean(value.toString());
						if (!flag) {
							return;
						}
					} else if (item.contains("OUT")) {
						Boolean b = new Boolean(value.toString());
						if (b) {
							outOrIn = "OUT";
						}
					} else if (item.contains("IN")) {
						Boolean b = new Boolean(value.toString());
						if (b) {
							outOrIn = "IN";
						}
					}
				}
			}
			if (flag) {
				//opcService.plcRfidRead(groupCode, outOrIn);
				OpcHelper.sendDataToApp("plcRfidRead", groupCode, outOrIn);
			}
		}catch(BusinessException e) {
			throw new JIException(0,e);
		}catch (Exception e) {
			String errMsg = ExceptionUtils.getFullStackTrace(e);
			logger.error(errMsg);
			//logService.doAddLog(ConstantUtils.AUDIT_TYPE_OPC_RFIDSTATION_READ, e.getMessage());
		}
	}
}
