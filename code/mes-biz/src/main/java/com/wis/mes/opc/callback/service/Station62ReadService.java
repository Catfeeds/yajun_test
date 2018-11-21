package com.wis.mes.opc.callback.service;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIString;
import org.openscada.opc.lib.da.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wis.basis.common.utils.LogConstant;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.opc.callback.ReadCallBack;
import com.wis.mes.opc.service.OpcService;
import com.wis.mes.opc.util.OpcHelper;

@Component(value = "station62ReadService")
public class Station62ReadService implements ReadCallBack{

	private Log logger = LogFactory.getLog(LogConstant.OPC_LOG);

	@Autowired
	private OpcService opcService;
	
	@Override
	public synchronized void doChanged(List<Item> itemList, String groupCode) throws JIException {
		try {
			boolean flag = false;
			String plcSn = "";
			for (Item items : itemList) {
				Object value = null;
				try {
					value = items.read(false).getValue().getObject();
				} catch (Exception e) {
					throw new BusinessException(e.getMessage());
				}
				String itemId = items.getId().toUpperCase();
				if (value instanceof Boolean) {
					if (itemId.contains("FLAG")) {
						flag = new Boolean(value.toString());
						if (!flag) {
							return;
						}
					}
				} else if (value instanceof JIString) {
					if (itemId.contains("STATION62_SN_THIS")) {
						plcSn = ((JIString) value).getString();
					}
				}
			}
			if (flag) {
				//opcService.station62Queuerecord(groupCode, plcSn);
				OpcHelper.sendDataToApp("station62Queuerecord", groupCode, plcSn);
			}
		}catch(BusinessException e) {
			throw new JIException(0,e);
		}catch (Exception e) {
			String errMsg =  ExceptionUtils.getFullStackTrace(e);
			logger.error(errMsg);
		}
	}
}
