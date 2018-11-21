package com.wis.mes.opc.util.metalplate;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.AddFailedException;
import org.openscada.opc.lib.da.DuplicateGroupException;
import org.openscada.opc.lib.da.Group;
import org.openscada.opc.lib.da.Item;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.wis.basis.common.utils.LogConstant;
import com.wis.mes.opc.util.OpcServer;

@Component
@Lazy(false)
public class OpcInitMetalPlateUtil{
	private static Log logger = LogFactory.getLog(LogConstant.OPC_LOG);

	public static void getOpcItemListMap(String[] channels,List<Item> itemList) {
		OpcServer server = ConnectionMetalPlateUtil.longConnect();
		if(null != server) {
			try {
				Group group = server.addGroup();
				Map<String, Item> addItems = group.addItems(channels);
				for (Map.Entry<String, Item> temp : addItems.entrySet()) {
					itemList.add(temp.getValue());
				}
			} catch (IllegalArgumentException e) {
				logger.error(ExceptionUtils.getFullStackTrace(e));
				getOpcItemListMap(channels,itemList);
			} catch (UnknownHostException e) {
				logger.error(ExceptionUtils.getFullStackTrace(e));
				getOpcItemListMap(channels,itemList);
			} catch (NotConnectedException e) {
				logger.error(ExceptionUtils.getFullStackTrace(e));
				getOpcItemListMap(channels,itemList);
			} catch (JIException e) {
				logger.error(ExceptionUtils.getFullStackTrace(e));
				getOpcItemListMap(channels,itemList);
			} catch (DuplicateGroupException e) {
				logger.error(ExceptionUtils.getFullStackTrace(e));
				getOpcItemListMap(channels,itemList);
			} catch (AddFailedException e) {
				logger.error(ExceptionUtils.getFullStackTrace(e));
				getOpcItemListMap(channels,itemList);
			} catch(Exception e){
				logger.error(ExceptionUtils.getFullStackTrace(e));
			}
		}else {
			getOpcItemListMap(channels,itemList);
		}
	}
	
}
