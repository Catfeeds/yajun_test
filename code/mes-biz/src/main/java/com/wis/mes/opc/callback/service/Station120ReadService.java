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

@Component(value = "station120ReadService")
public class Station120ReadService implements ReadCallBack{

	private Log logger = LogFactory.getLog(LogConstant.OPC_LOG);

	@Autowired
	private OpcService opcService;
	
	@Override
	public synchronized void doChanged(List<Item> itemList, String groupCode) throws JIException {
		try {
			boolean flag = false;
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
				}
			}
			if (flag) {
				//opcService.station120ClearTag(groupCode);
				OpcHelper.sendDataToApp("station120ClearTag", groupCode);
			}
		}catch(BusinessException e) {
			throw new JIException(0,e);
		}catch (Exception e) {
			String errMsg =  ExceptionUtils.getFullStackTrace(e);
			logger.error(errMsg);
		}
	}
}
