/****************************************************
 * �?         �?  Hibernate公共工具�?
 * 前置条件:
 * 后置条件:
 * 实施资源:
 * �? �? �?: 
 * 被调用�?:
 * 重载说明:
 * Copyright 2010 WestInfoSoft CO., LTD. All rights reserved.
 * @author   ryj
 * @version  1.0, 2018-5-9
 * @see HISTORY
 *      Date      Desc               Author      Operation
 *  	2010-4-2   创建文件                              Jacky Wong   create
 *  	2010-4-2   增加提示功能	     Joe Chou     modify
 * @since WISFrame1.0
 **************************************************/
package com.wis.mes.utils;

import java.io.InputStream;
import java.util.Properties;

public class ScanningConfig {

	private static Properties prop = new Properties();

	private ScanningConfig() {
	}

	static {
		try {
			prop = new Properties();
			InputStream in = ScanningConfig.class.getClassLoader().getResourceAsStream("scanning.properties");
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
