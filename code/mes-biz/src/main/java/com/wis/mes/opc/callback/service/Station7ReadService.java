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

@Component(value = "station7ReadService")
public class Station7ReadService implements ReadCallBack {
	private Log logger = LogFactory.getLog(LogConstant.OPC_LOG);
	
	@Autowired
	private OpcService opcService;

	@Override
	public synchronized void doChanged(List<Item> itemList, String groupCode) throws JIException {
		try {
			boolean flag = false;
			boolean arrival = false;
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
					} else if (item.contains("ARRIVAL")) {
						arrival = new Boolean(value.toString());
					}
				}
			}
			opcService.atTheTableSignal(arrival, flag, true);
		}catch(BusinessException e) {
			throw new JIException(0,e);
		}catch (Exception e) {
			opcService.atTheTableSignal(false, false, false);
			String errMsg = ExceptionUtils.getFullStackTrace(e);
			logger.error(errMsg);
		}
	}

}
