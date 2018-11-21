
package com.wis.mes.util;

import java.io.InputStream;
import java.util.Properties;

public class RfidConfig {

	private static Properties prop = new Properties();

	private RfidConfig() {
	}

	static {
		try {
			prop = new Properties();
			InputStream in = RfidConfig.class.getClassLoader().getResourceAsStream("rfidconfig.properties");
			prop.load(in);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static String getNetworkAddress() {
		return prop.getProperty("networkAddress");
	}

	public static Integer getNetworkPort() {
		return Integer.valueOf(prop.getProperty("networkPort"));
	}

}
