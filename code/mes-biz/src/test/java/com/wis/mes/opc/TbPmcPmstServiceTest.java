package com.wis.mes.opc;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.jinterop.dcom.common.JIException;
import org.junit.Test;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.AddFailedException;
import org.openscada.opc.lib.da.DuplicateGroupException;
import org.openscada.opc.lib.da.Group;
import org.openscada.opc.lib.da.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.wis.basis.common.utils.LogConstant;
import com.wis.basis.common.utils.SystemConfig;
import com.wis.mes.basis.BizBaseTestCase;
import com.wis.mes.opc.util.ConnectionUtil;
import com.wis.mes.opc.util.OpcHelper;
import com.wis.mes.opc.util.OpcServer;

public class TbPmcPmstServiceTest extends BizBaseTestCase {

	
	private static Log logger = LogFactory.getLog(LogConstant.JOB_LOG);
	
	private final static String opcKepserverShutdownWsdl = SystemConfig.getPropertyByKey("opc.kepserver.webservice.remoteShutdown");
	private final static String sendCommand= SystemConfig.getPropertyByKey("opc.kepserver.send.command ");
	private final static Integer timeOut = 3;
	
	private static Client client = null;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Test
	public void execue() {
		HashOperations<String, String, Object> opsForHash = redisTemplate.opsForHash();
		Date date = new Date();
		Object heartbeatVal = opsForHash.get("WIS_HEARTBEAT_KEY", "HEARTBEATVAL");
		Object heartbeatTimeOut = opsForHash.get("WIS_HEARTBEAT_KEY", "HEARTBEATTIMEOUT");
		OpcServer service = ConnectionUtil.longConnect();
		if(null != service) {
			try {
				Group addGroup = service.addGroup();
				Item item = addGroup.addItem("Channel1.L72.Scanner.Read_Flag");
				try {
					Object object = item.read(false).getValue().getObject();
					if (!object.toString().contains("EMPTY")) {
						if (object instanceof Boolean) {
							object = (Boolean) object;
						}
						if(heartbeatVal.equals(object)) {
							if(null != heartbeatTimeOut) {
								if(date.getTime()-Long.valueOf(heartbeatTimeOut.toString()) > (timeOut*60*1000)) {
									restartComputer();
									opsForHash.put("WIS_HEARTBEAT_KEY", "HEARTBEATTIMEOUT", "");
								}
							}else {
								opsForHash.put("WIS_HEARTBEAT_KEY", "HEARTBEATTIMEOUT", date.getTime());
							}
						}else {
							boolean readVal = (boolean) object;
							boolean sendVal = updateFlag(readVal);
							opsForHash.put("WIS_HEARTBEAT_KEY", "HEARTBEATVAL", sendVal);
							opsForHash.put("WIS_HEARTBEAT_KEY", "HEARTBEATTIMEOUT", date.getTime());
						}
					}
				} catch (Exception e1) {
					if(null != heartbeatTimeOut) {
						if(date.getTime()-Long.valueOf(heartbeatTimeOut.toString()) > (timeOut*60*1000)) {
							restartComputer();
							opsForHash.put("WIS_HEARTBEAT_KEY", "HEARTBEATTIMEOUT", "");
						}
					}else {
						opsForHash.put("WIS_HEARTBEAT_KEY", "HEARTBEATTIMEOUT", date.getTime());
					}
				}
			} catch (IllegalArgumentException e) {
				logger.error(e.getStackTrace());
			} catch (UnknownHostException e) {
				logger.error(e.getStackTrace());
			} catch (NotConnectedException e) {
				logger.error(e.getStackTrace());
			} catch (JIException e) {
				logger.error(e.getStackTrace());
			} catch (DuplicateGroupException e) {
				logger.error(e.getStackTrace());
			} catch (AddFailedException e) {
				logger.error(e.getStackTrace());
			}
		}
				
	}
	
	private boolean updateFlag(boolean readVal) {
		boolean sendVal;
		if(readVal) {
			sendVal = false;
		}else {
			sendVal = true;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		String[] items = new String[] { "Channel1.L72.Scanner.Read_Flag" };
		map.put("Read_Flag", sendVal);
		OpcHelper.sendDataToOpc(items, map);
		return sendVal;
	}
	
	private void restartComputer() {
		if(webserviceConnection()) {
			try {
				client.invoke("sendExeCmd", sendCommand);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean webserviceConnection() {
		boolean FLAG= true;
		try {
			if(null == client) {
				JaxWsDynamicClientFactory factory = JaxWsDynamicClientFactory.newInstance();
				client = factory.createClient(opcKepserverShutdownWsdl);
			}
		} catch (Exception e) {
			client = null;
			FLAG=false;
			logger.error("createWebserviceClient:" + ExceptionUtils.getFullStackTrace(e));
		}
		return FLAG;
	}
}
