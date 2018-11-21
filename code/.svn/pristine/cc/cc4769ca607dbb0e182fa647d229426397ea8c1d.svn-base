package com.wis.mes.util;

import java.util.HashMap;
import java.util.Map;

import com.wis.core.context.WebContextHolder;
import com.wis.core.framework.entity.SerialNum;
import com.wis.core.framework.service.SerialNumService;

public class NewSerialNumUtil {

	private static NewSerialNumUtil INSTANCE = null;

	private Map<String, SerialNumber> keyMap;
	private SerialNumService serialNumSerivce;

	private NewSerialNumUtil() {
		keyMap = new HashMap<String, SerialNumber>();
		serialNumSerivce = (SerialNumService) WebContextHolder
				.getWebAppContext().getBean(SerialNumService.class);
	}

	public static NewSerialNumUtil getInstance() {
		if (INSTANCE == null) {
			synchronized (NewSerialNumUtil.class) {
				if (INSTANCE == null) {
					INSTANCE = new NewSerialNumUtil();
				}
			}
		}
		return INSTANCE;
	}

	public int nextInt(String key) {
		return new Long(nextLong(key)).intValue();
	}

	public long nextLong(String key) {
		SerialNumber serialNumber;
		synchronized (keyMap) {
			if (keyMap.containsKey(key)) {
				serialNumber = keyMap.get(key);
			} else {
				serialNumber = new SerialNumber(key);
				keyMap.put(key, serialNumber);
			}
		}
		return serialNumber.nextLong();
	}

	public class SerialNumber {

		private long serialNum;
		private String key;
		private int seed;

		public SerialNumber(String key) {
			this.key = key;
			this.seed = 1;
			init();
		}

		protected void init() {
			SerialNum eg = findEg();
			if (eg != null) {
				serialNum = eg.getSn();
				eg.setSn(serialNum + seed);
			} else {
				eg = new SerialNum();
				eg.setSnKey(key);
				eg.setSn((long) seed);
			}
			serialNumSerivce.doSaveOrUpdate(eg);
		}

		private SerialNum findEg() {
			SerialNum eg = new SerialNum();
			eg.setSnKey(key);
			eg = serialNumSerivce.findUniqueByEg(eg);
			return eg;
		}

		public synchronized long nextLong() {
			if (seed == 0) {
				seed = 1;
				SerialNum eg = findEg();
				serialNum = eg.getSn();
				eg.setSn(serialNum + seed);
				serialNumSerivce.doSaveOrUpdate(eg);
			}
			serialNum++;
			seed--;
			return serialNum;
		}
	}

}
