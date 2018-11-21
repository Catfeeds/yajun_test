package com.wis.mes.opc.group.service.impl;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.builder.BuilderException;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIString;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.AddFailedException;
import org.openscada.opc.lib.da.DuplicateGroupException;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.ItemState;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.LogConstant;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.mes.opc.group.entity.KsGroupItem;
import com.wis.mes.opc.group.service.RfidReadAndWriteService;
import com.wis.mes.opc.util.ConnectionUtil;
import com.wis.mes.opc.util.OpcHelper;
import com.wis.mes.opc.util.OpcServer;

@Service
public class RfidReadAndWriteServiceImpl implements RfidReadAndWriteService {
	private Log logger = LogFactory.getLog(LogConstant.OPC_LOG);

	@Override
	public void rfidStationWrite(JsonActionResult result, OpcServer server, String sn, List<KsGroupItem> itemList)
			throws Exception {
		String[] items = getItems(itemList);
		// rfidStation8Write(result, items, server, sn, 0, false, false);
		rfidStation8Write(result, items, server, sn, false, false, 0);
	}

	@Override
	public void stationNgIn(JsonActionResult result, OpcServer server, String sn, List<KsGroupItem> itemList,
			boolean repairDone) throws Exception {
		String[] items = getItems(itemList);
		// rfidStation8Write(result, items, server, sn, 0, true, repairDone);
		rfidStation8Write(result, items, server, sn, true, repairDone, 0);
	}

	/**
	 * 获取 flag 和Flag 数据
	 * 
	 * @param items
	 * @param server
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getFlagAndSNMap(List<Item> opcData) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// List<Item> opcData = OpcHelper.getOpcData(items, server.addGroup());
		for (Item item : opcData) {
			for (int i = 0; i < 5; i++) {
				ItemState read = item.read(false);
				if (read.getQuality() == 192) {
					JIVariant value = read.getValue();
					String itemId = item.getId().toUpperCase();
					if (value.getType() == 11) {
						boolean flag = value.getObjectAsBoolean();
						if (itemId.contains("ARRIVAL")) {
							returnMap.put("ARRIVAL", flag);
						}
						if (itemId.contains("FLAG")) {
							returnMap.put("FLAG", flag);
						}
					}
					if (itemId.contains("SN")) {
						returnMap.put("SN", value.getObjectAsString().getString());
					}
					break;
				}
			}
		}
		return returnMap;
	}

	private String[] getItems(List<KsGroupItem> itemMapByGroupCode) {
		String[] str = new String[itemMapByGroupCode.size()];
		for (int i = 0; i < itemMapByGroupCode.size(); i++) {
			KsGroupItem ksGroupItem = itemMapByGroupCode.get(i);
			str[i] = ksGroupItem.getItemCode();
		}
		return str;
	}

	public Map<String, Object> rfidStation8Read(String[] items, OpcServer server, int count) throws Exception {
		Map<String, Object> readMap = null;
		try {
			List<Item> opcData = OpcHelper.getOpcData(items, server.addGroup());
			readMap = getFlagAndSNMap(opcData);
		} catch (Exception e) {
			if (e instanceof NotConnectedException || e instanceof JIException) {
				server = ConnectionUtil.longConnect();
			}
			if (count < 5) {
				rfidStation8Read(items, server, ++count);
			} else {
				throw e;
			}
		}
		return readMap;
	}

	private void rfidStation8Write(JsonActionResult result, String[] items, OpcServer server, String sn,
			boolean isHaveNgFlag, boolean repairDone, int count) {
		try {
			List<Item> opcData = OpcHelper.getOpcData(items, server.addGroup());
			for (int i = 0; i < 5; i++) {
				for (Item item : opcData) {
					String itemId = item.getId().toUpperCase();
					if (itemId.contains("REPAIR_DONE")) {
						item.write(new JIVariant(repairDone));
					}
					if (itemId.contains("SN")) {
						item.write(new JIVariant(sn));
					}
				}
				for (Item item : opcData) {
					String itemId = item.getId().toUpperCase();
					if (itemId.contains("FLAG")) {
						item.write(new JIVariant(false));
					}
				}
				Map<String, Object> readMap = getFlagAndSNMap(opcData);
				if (!sn.equals(String.valueOf(readMap.get("SN")))) {
					result.setData(false);
					result.setSuccess(false);
					result.setMessage("写入失败!");
				} else {
					result.setSuccess(true);
					result.setData(true);
					result.setMessage("写入成功!");
					break;
				}
			}
		} catch (Exception e) {
			if (e instanceof NotConnectedException || e instanceof JIException) {
				server = ConnectionUtil.longConnect();
			}
			if (count < 5) {
				rfidStation8Write(result, items, server, sn, isHaveNgFlag, repairDone, ++count);
			} else {
				logger.error("rfidStation8Write:::" + ExceptionUtils.getStackTrace(e));
				result.setData(false);
				result.setSuccess(false);
				result.setMessage("写入失败!");
			}
		}
	}

	@Override
	public String getSnByUlocNo(OpcServer server, List<KsGroupItem> redisItemListByGroupCode, String ulocNo)
			throws IllegalArgumentException, UnknownHostException, JIException, AddFailedException,
			NotConnectedException, DuplicateGroupException {
		String[] items = getItems(redisItemListByGroupCode);
		List<Item> opcData = OpcHelper.getOpcData(items, server.addGroup());
		for (int i = 0; i < 5; i++) {
			for (Item item : opcData) {
				String itemId = item.getId();
				if (itemId.contains("ID")) {
					item.write(new JIVariant(ulocNo));
				}
			}
			for (Item item : opcData) {
				String itemId = item.getId();
				if (itemId.contains("Read_Flag")) {
					item.write(new JIVariant(true));
				}
			}
			boolean isEqual = true;
			for (Item item : opcData) {
				String itemId = item.getId();
				JIVariant value = item.read(false).getValue();
				if (value.getObject().toString().contains("EMPTY")) {
					isEqual = false;
					continue;
				}
				if (itemId.contains("ID")) {
					String objectAsInt = value.getObjectAsString().getString();
					if (!ulocNo.equals(objectAsInt)) {
						isEqual = false;
					}
				} else if (itemId.contains("Read_Flag")) {
					if (!value.getObjectAsBoolean()) {
						isEqual = false;
					}
				}
			}
			if (isEqual) {
				break;
			}
		}
		return getSn(opcData, 0);
	}

	private String getSn(List<Item> opcData, int count) throws JIException {
		boolean isCompleteSn = false;
		while (count < 100) {
			count++;
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
			for (Item item : opcData) {
				String itemId = item.getId();
				JIVariant value = item.read(false).getValue();
				Object obj = value.getObject();
				if (obj.toString().contains("EMPTY")) {
					continue;
				}
				if (itemId.contains("SN")) {
					if (isCompleteSn) {
						if (obj instanceof JIString) {
							return ((JIString) obj).getString();
						} else {
							throw new BuilderException("ERROR_OPC_PLC_CONNECT");
						}
					}
				} else if (itemId.contains("Read_Flag")) {
					if (obj instanceof Boolean) {
						boolean b = (Boolean) obj;
						if (!b) {
							isCompleteSn = true;
						}
					} else {
						throw new BuilderException("ERROR_OPC_PLC_CONNECT");
					}
				}
			}
		}
		return null;
	}

	@Override
	public void writeSN(OpcServer server, List<KsGroupItem> redisItemListByGroupCode, String ulocNo, String SN)
			throws IllegalArgumentException, DuplicateGroupException, UnknownHostException, JIException,
			AddFailedException, NotConnectedException {
		String[] items = getItems(redisItemListByGroupCode);
		List<Item> opcData = OpcHelper.getOpcData(items, server.addGroup());
		for (int i = 0; i < 5; i++) {
			for (Item item : opcData) {
				String itemId = item.getId();
				if (itemId.contains("ID")) {
					item.write(new JIVariant(ulocNo));
				} else if (itemId.contains("SN")) {
					item.write(new JIVariant(new JIString(SN)));
				}
			}
			for (Item item : opcData) {
				String itemId = item.getId();
				if (itemId.contains("Write_Flag")) {
					item.write(new JIVariant(true));
				}
			}
			boolean isEqual = true;
			for (Item item : opcData) {
				String itemId = item.getId();
				JIVariant value = item.read(false).getValue();
				if (value.getObject().toString().contains("EMPTY")) {
					isEqual = false;
					continue;
				}
				if (itemId.contains("ID")) {
					String string = value.getObjectAsString().getString();
					if (!ulocNo.equals(string)) {
						isEqual = false;
					}
				} else if (itemId.contains("Write_Flag")) {
					boolean objectAsBoolean = value.getObjectAsBoolean();
					if (!objectAsBoolean) {
						isEqual = false;
					}
				} else if (itemId.contains("SN")) {
					String string = value.getObjectAsString().getString();
					if (!SN.equals(string)) {
						isEqual = false;
					}
				}
			}
			if (isEqual) {
				break;
			}
		}
	}
}
