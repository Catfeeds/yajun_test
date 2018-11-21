package com.wis.mes.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.stereotype.Component;

import com.wis.mes.service.SpiSerialCommunicat;
import com.wis.mes.service.impl.SpiSerialCommunicatImpl;

@Component
public class InitUtil implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("---------------创建监听串口连接----------------");
		SpiSerialCommunicat spiSerialCommunicat = new SpiSerialCommunicatImpl();
		spiSerialCommunicat.openConnect();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("-------------关闭监听串口连接------------------");
		SpiSerialCommunicat spiSerialCommunicat = new SpiSerialCommunicatImpl();
		spiSerialCommunicat.closeConnect();
	}

}
