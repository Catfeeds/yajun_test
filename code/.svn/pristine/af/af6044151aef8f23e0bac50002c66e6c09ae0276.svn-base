/****************************************************
 * 项目名称:  WISFrameWeb
 * �?         �?  Hibernate公共工具�?
 * 前置条件:
 * 后置条件:
 * 实施资源:
 * �? �? �?: 
 * 被调用�?:
 * 重载说明:
 * Copyright 2010 WestInfoSoft CO., LTD. All rights reserved.
 * @author   Jacky Wong
 * @version  1.0, 2010-4-2
 * @see HISTORY
 *      Date      Desc               Author      Operation
 *  	2010-4-2   创建文件                              Jacky Wong   create
 *  	2010-4-2   增加提示功能	     Joe Chou     modify
 * @since WISFrame1.0
 **************************************************/
package com.wis.basis.common.utils;

import java.io.InputStream;
import java.util.Properties;

import com.wis.core.dao.QueryCriteria;

public class SystemConfig {

	private static Properties prop = new Properties();

	private SystemConfig() {
	}

	static {
		try {
			prop = new Properties();
			InputStream in = SystemConfig.class.getClassLoader().getResourceAsStream("sysconfig.properties");
			prop.load(in);
			in.close();
			QueryCriteria.MULTI_QUERY_SEPARATOR = prop.getProperty("multiQuery.separator", ",");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static String getPropertyByKey(String key) {
		return prop.getProperty(key);
	}

	public static String getVersion() {
		return prop.getProperty("version");
	}

	public static String getSystemName() {
		return prop.getProperty("systemName");
	}
}
