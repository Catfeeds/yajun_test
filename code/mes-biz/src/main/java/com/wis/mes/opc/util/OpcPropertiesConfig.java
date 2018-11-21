package com.wis.mes.opc.util;

import java.io.InputStream;
import java.util.Properties;

public class OpcPropertiesConfig {

	private static Properties prop = new Properties();

	private OpcPropertiesConfig() {
	}

	static {
		try {
			prop = new Properties();
			InputStream in = OpcPropertiesConfig.class.getClassLoader().getResourceAsStream("opc.properties");
			prop.load(in);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static String getPropertyByKey(String key) {
		return prop.getProperty(key);
	}

}
