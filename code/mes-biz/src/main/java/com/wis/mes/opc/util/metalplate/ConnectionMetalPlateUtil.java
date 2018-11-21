package com.wis.mes.opc.util.metalplate;

import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.common.AlreadyConnectedException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.da.Group;
import org.openscada.opc.lib.da.Item;

import com.wis.basis.common.utils.LogConstant;
import com.wis.mes.opc.util.OpcPropertiesConfig;
import com.wis.mes.opc.util.OpcServer;

public class ConnectionMetalPlateUtil {
	private static Log logger = LogFactory.getLog(LogConstant.OPC_LOG);
	private static final int RECONNECT = Integer.valueOf(OpcPropertiesConfig.getPropertyByKey("opc.reconnect.time"));
	private static final String USER_NAME = OpcPropertiesConfig.getPropertyByKey("opc.metalplate.user-name");
	private static final String PASSWORD = OpcPropertiesConfig.getPropertyByKey("opc.metalplate.user-password");
	private static final String HOST = OpcPropertiesConfig.getPropertyByKey("opc.metalplate.host");
	private static final String DOMAIN = OpcPropertiesConfig.getPropertyByKey("opc.metalplate.domain");
	private static final String PROGID = OpcPropertiesConfig.getPropertyByKey("opc.metalplate.progid");
	private static final String CLISID = OpcPropertiesConfig.getPropertyByKey("opc.metalplate.clsid");
	private static ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(10);
	private static OpcServer server = null;
	private static ConnectionInformation ci = null;
	volatile static boolean isConnectFlag = false;
	private static Item item = null;
	static {
		ci = new ConnectionInformation();
		ci.setHost(HOST);
		ci.setDomain(DOMAIN);
		ci.setUser(USER_NAME);
		ci.setPassword(PASSWORD);
		ci.setProgId(PROGID);
		ci.setClsid(CLISID);
		server = new OpcServer(ci, scheduledService);
		try {
			server.connect();
		} catch (UnknownHostException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} catch (JIException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		} catch (AlreadyConnectedException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	public static OpcServer longConnect() {
		if (null == server.getServerState()) {
			disConnect(server);
			try {
				if(null == server) {
					ci = new ConnectionInformation();
					ci.setHost(HOST);
					ci.setDomain(DOMAIN);
					ci.setUser(USER_NAME);
					ci.setPassword(PASSWORD);
					ci.setProgId(PROGID);
					ci.setClsid(CLISID);
					server = new OpcServer(ci, scheduledService);
				}
				server.connect();
			} catch (UnknownHostException e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			} catch (JIException e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			} catch (AlreadyConnectedException e) {
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
		if(validateConnect()) {
			return server;
		}else {
			try {
				Thread.sleep(RECONNECT);
			} catch (InterruptedException e) {}
			return null;
		}
	}
	public static boolean getIsConnectFlag() {
		return isConnectFlag;
	}
	
	public static void setIsConnectFlag(boolean flag) {
		isConnectFlag = flag;
	}
	
	private static boolean validateConnect() {
		try {
			item.read(false).getValue().getObject();
			setIsConnectFlag(true);
			return true;
		} catch(Exception e) {
			subscriptionPublication();
			setIsConnectFlag(false);
			logger.error(ExceptionUtils.getStackTrace(e));
			return false;
		}
	}
	private static void subscriptionPublication(){
		try {
			Group group = server.addGroup();
			item = group.addItem("Banjin2.1756-L72_1.Heartbeat");
		} catch (Exception e) {
			logger.error(e.getStackTrace());
		}
	}

	public static void disConnect(OpcServer server){
		if (server != null) {
			server.dispose();
			try {
				Thread.sleep(RECONNECT);
			} catch (InterruptedException e) {}
		}
	}
}
