package com.wis.mes.opc;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wis.mes.basis.BizBaseTestCase;
import com.wis.mes.opc.service.OpcService;
import com.wis.mes.rfid.vo.RfidReadVo;

public class OpcTest1 extends BizBaseTestCase {
	@Autowired
	private OpcService opcService;

	volatile static boolean isConnect = false;
	
	final static ExecutorService pool = Executors.newFixedThreadPool(2);

	@Test
	public void station62Queuerecord() {
		opcService.station62Queuerecord("STATION_62", "12345678901234567890");
	}

	public static void main(String[] args) {
		OpcTest1.connect();
		OpcTest1.poolTest();
	}

	public static void poolTest() {
		pool.execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if(OpcTest1.getIsConnect()) {
						System.out.println("------------" + new Date() + "--------------");
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
					}
				}
			}
		});
	}

	public static boolean getIsConnect() {
		return isConnect;
	}

	public static void connect() {
		pool.execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (isConnect) {
						isConnect = false;
					} else {
						isConnect = true;
					}
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
					}
				}
			}
		});
	}

}
