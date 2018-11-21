package com.wis.mes.opc.util;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.AddFailedException;
import org.openscada.opc.lib.da.DuplicateGroupException;
import org.openscada.opc.lib.da.Group;
import org.openscada.opc.lib.da.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.wis.basis.common.utils.LogConstant;
import com.wis.core.context.WebContextHolder;
import com.wis.mes.opc.callback.ReadCallBack;
import com.wis.mes.opc.group.entity.KsGroup;
import com.wis.mes.opc.group.entity.KsGroupItem;
import com.wis.mes.opc.group.service.KsGroupItemService;
import com.wis.mes.opc.group.service.KsGroupService;
import com.wis.mes.opc.service.OpcService;

@Component
@Lazy(false)
public class OpcInitUtil{
	private static Log logger = LogFactory.getLog(LogConstant.OPC_LOG);
	private final static int REFRESH_RATE = Integer.valueOf(OpcPropertiesConfig.getPropertyByKey("opc.refresh.rate"));
	/**过程数据groupserver**/
	private final static List<String> productServerKeys = Arrays.asList(OpcPropertiesConfig.getPropertyByKey("opc.group.server.productOpcData.keys").split(","));
	/**工位状态groupserver**/
	private final static List<String> ngStationServerKeys = Arrays.asList(OpcPropertiesConfig.getPropertyByKey("opc.group.server.ngStationOpcData.keys").split(","));
	/**7工位groupserver**/
	private final static String station7ServerKey = OpcPropertiesConfig.getPropertyByKey("opc.group.server.station7Opc.keys");
	/**62工位状态groupserver**/
	private final static String station62ServerKey = OpcPropertiesConfig.getPropertyByKey("opc.group.server.station62Opc.keys");
	/**120工位状态groupserver**/
	private final static String station120ServerKey = OpcPropertiesConfig.getPropertyByKey("opc.group.server.statio120Opc.keys");
	@Autowired
	private SpringUtil springUtil;
	@Autowired
	private KsGroupService groupService;
	@Autowired
	private KsGroupItemService groupItemService;
	@Autowired
	private OpcService opcService;

	private static boolean isInit = false;

	@PostConstruct
	public void initOpc() {
		boolean isDataEnable = new Boolean(OpcPropertiesConfig.getPropertyByKey("opc.data.enabled"));
		boolean isStation7Enable = new Boolean(OpcPropertiesConfig.getPropertyByKey("opc.station7.enabled"));
		if(!isInit) {
		   isInit = true;
		   if(isDataEnable) {
			   final Map<String, Object> groupItemMap = groupItemMap();
				@SuppressWarnings("unchecked")
				final Map<KsGroup, String[]> itemMap = (Map<KsGroup, String[]>) groupItemMap.get("ITEMSMAP");
				if (groupItemMap.isEmpty()) {
					return;
				}
				// 产品数据 线程
				productOpcData(itemMap);
				// 工位状态 线程
				ngStationOpcData(itemMap);
				//62工位编号状态
				station62Opc();
				//120工位编号状态
				station120Opc();
		   }else if(isStation7Enable) {
			   // 7工位
				station7Opc();
		   }
			
		}
	}

	/**
	 * 产品数据线程
	 * 
	 * @param opcItemListMap
	 * @param productPool
	 */
	private void productOpcData(final Map<KsGroup, String[]> itemMap) {
		Map<KsGroup, List<Item>> opcItemListMap = new HashMap<KsGroup, List<Item>>();
		getOpcItemListMap(opcItemListMap, itemMap, productServerKeys);
		final ExecutorService productPool = Executors.newSingleThreadExecutor();
		productPool.execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if(ConnectionUtil.getIsConnectFlag()) {
						try {
							for (final Map.Entry<KsGroup, List<Item>> entry : opcItemListMap.entrySet()) {
								KsGroup key = entry.getKey();
								if (StringUtils.isNotEmpty(key.getGroupServer())) {
									Object bean = null; 
									if(null != WebContextHolder.getWebAppContext()) {
										bean = WebContextHolder.getWebAppContext().getBean(key.getGroupServer());
										if (null != bean) {
											ReadCallBack callBack = (ReadCallBack) bean;
											OpcHelper.startOpc(key.getGroupCode(), entry.getValue(), callBack);
										}
									}
								}
							}
						} catch (JIException e) {
							getOpcItemListMap(opcItemListMap, itemMap, productServerKeys);
							logger.error("ProductDataThread:" + ExceptionUtils.getFullStackTrace(e));
						} catch (Exception e) {
							logger.error("ProductDataThread:" + ExceptionUtils.getFullStackTrace(e));
						}
					}
				}
			}
		});
	}

	/**
	 * NG出入口线程
	 * 
	 * @param pool
	 * @param opcItemListMap
	 */

	private void ngStationOpcData(final Map<KsGroup, String[]> itemMap) {
		final int THREADS = Integer.valueOf(OpcPropertiesConfig.getPropertyByKey("opc.threads-pool"));
		final ExecutorService pool = Executors.newFixedThreadPool(THREADS);
		Map<KsGroup, List<Item>> opcItemListMap = new HashMap<KsGroup, List<Item>>();
		getOpcItemListMap(opcItemListMap, itemMap, ngStationServerKeys);
		pool.execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						getOpcDataThreadPool(pool, itemMap, opcItemListMap);
						try {
							Thread.sleep(REFRESH_RATE);
						} catch (InterruptedException e) {
						}
					} catch (Exception e) {
						logger.error("ngStationOpcData:" + ExceptionUtils.getFullStackTrace(e));
					}
				}
			}
		});
	}

	//工位NG出入口状态线程
	private void getOpcDataThreadPool(final ExecutorService pool, final Map<KsGroup, String[]> itemMap,
			Map<KsGroup, List<Item>> opcItemListMap) {
		for (final Map.Entry<KsGroup, List<Item>> entry : opcItemListMap.entrySet()) {
			final KsGroup key = entry.getKey();
			if (key.getGroupCode().equals("PRODUCT")) {
				continue;
			}
			pool.execute(new Runnable() {
				@Override
				public void run() {
					if(ConnectionUtil.getIsConnectFlag()) {
						try {
							if (StringUtils.isNotEmpty(key.getGroupServer())) {
								Object bean = null;
								if(null != WebContextHolder.getWebAppContext()) {
									bean = WebContextHolder.getWebAppContext().getBean(key.getGroupServer());
									if (null != bean) {
										ReadCallBack callBack = (ReadCallBack) bean;
										OpcHelper.startOpc(key.getGroupCode(), entry.getValue(), callBack);
									}
								}
							}
						} catch (JIException e) {
							getOpcItemListMap(opcItemListMap, itemMap,ngStationServerKeys);
							try {
								Thread.sleep(REFRESH_RATE);
							} catch (InterruptedException e1) {
							}
						} catch (Exception e) {
							logger.error("getOpcDataThreadPool:" + ExceptionUtils.getStackTrace(e));
						}
					}
				}
			});
		}
	}

	/**
	 * 7工位线程
	 * 
	 * @param opcItemListMap
	 * @param station62pool
	 */
	private void station7Opc() {
		final ExecutorService stationPool = Executors.newSingleThreadExecutor();
		List<Item> itemList = new ArrayList<Item>();
		String[] channels = new String[] {"Channel1.L72.Station7_RFID.Station7_Arrival","Channel1.L72.Station7_RFID.Station7_Flag","Channel1.L72.Station7_RFID.Station7_SN"};
		getOpcItemListMap(channels,itemList);
		stationPool.execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if(ConnectionUtil.getIsConnectFlag()) {
						try {
							Object bean = null;
							if(null != WebContextHolder.getWebAppContext()) {
								bean = WebContextHolder.getWebAppContext().getBean(station7ServerKey);
								if (null != bean) {
									ReadCallBack callBack = (ReadCallBack) bean;
									OpcHelper.startOpc("STATION_7", itemList, callBack);
								}
							}
						} catch (JIException e) {
							opcService.atTheTableSignal(false, false, false);
							getOpcItemListMap(channels,itemList);
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e1) {
							}
							logger.error("station7Opc:" + ExceptionUtils.getFullStackTrace(e));
						} catch (Exception e) {
							opcService.atTheTableSignal(false, false, false);
							logger.error("station7Opc:" + ExceptionUtils.getFullStackTrace(e));
						}
						try {
							Thread.sleep(REFRESH_RATE);
						} catch (InterruptedException e) {
						}
					}
				}
			}
		});
	}

	/**
	 * 62工位线程
	 * 
	 * @param opcItemListMap
	 * @param station62pool
	 */
	private void station62Opc() {
		final ExecutorService station62pool = Executors.newSingleThreadExecutor();
		List<Item> itemList = new ArrayList<Item>();
		String[] channels = new String[] {"Channel1.L72.Station62_RFID.Station62_Flag","Channel1.L72.Station62_RFID.Station62_SN_This"};
		getOpcItemListMap(channels,itemList);
		station62pool.execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if(ConnectionUtil.getIsConnectFlag()) {
						try {
							Object bean = null;
							if(null != WebContextHolder.getWebAppContext()) {
								bean = WebContextHolder.getWebAppContext().getBean(station62ServerKey);
								if (null != bean) {
									ReadCallBack callBack = (ReadCallBack) bean;
									OpcHelper.startOpc("STATION_62", itemList, callBack);
								}
							}
						} catch (JIException e) {
							getOpcItemListMap(channels,itemList);
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e1) {
							}
							logger.error("station62Opc:" + ExceptionUtils.getFullStackTrace(e));
						} catch (Exception e) {
							logger.error("station62Opc:" + ExceptionUtils.getFullStackTrace(e));
						}
						try {
							Thread.sleep(REFRESH_RATE);
						} catch (InterruptedException e) {
						}
					}
				}
			}
		});
	}
	
	/**
	 * 120工位线程
	 * 
	 * @param opcItemListMap
	 * @param station120pool
	 */
	private void station120Opc() {
		final ExecutorService station120pool = Executors.newSingleThreadExecutor();
		List<Item> itemList = new ArrayList<Item>();
		String[] channels = new String[] {"Channel1.L72.Station120_RFID.Station120_Flag"};
		getOpcItemListMap(channels,itemList);
		station120pool.execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if(ConnectionUtil.getIsConnectFlag()) {
						try {
							Object bean = null;
							if(null != WebContextHolder.getWebAppContext()) {
								bean = WebContextHolder.getWebAppContext().getBean(station120ServerKey);
								if (null != bean) {
									ReadCallBack callBack = (ReadCallBack) bean;
									OpcHelper.startOpc("STATION_120", itemList, callBack);
								}
							}
						} catch (JIException e) {
							getOpcItemListMap(channels,itemList);
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e1) {
							}
							logger.error("station120Opc:" + ExceptionUtils.getFullStackTrace(e));
						} catch (Exception e) {
							logger.error("station120Opc:" + ExceptionUtils.getFullStackTrace(e));
						}
						try {
							Thread.sleep(REFRESH_RATE);
						} catch (InterruptedException e) {
						}
					}
				}
			}
		});
	}
	
	private Map<String, Object> groupItemMap() {
		Map<KsGroup, String[]> itemMap = new HashMap<KsGroup, String[]>();
		Map<KsGroup, List<KsGroupItem>> itemList = new HashMap<KsGroup, List<KsGroupItem>>();
		List<KsGroup> findAll = groupService.getGroupServerIsNotNull();
		for (KsGroup group : findAll) {
			KsGroupItem eg = new KsGroupItem();
			eg.setGroupId(group.getId());
			List<KsGroupItem> findByEg = groupItemService.getRedisItemListByGroupCode(group.getGroupCode());
			String[] str = new String[findByEg.size()];
			for (int i = 0; i < findByEg.size(); i++) {
				str[i] = findByEg.get(i).getItemCode();
			}
			itemMap.put(group, str);
			itemList.put(group, findByEg);
		}
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("ITEMSMAP", itemMap);
		returnMap.put("ITEMLIST", itemList);
		return returnMap;
	}

	public synchronized static void getOpcItemListMap(String[] channels,List<Item> itemList) {
		OpcServer server = ConnectionUtil.longConnect();
		if(null != server) {
			itemList.clear();
			try {
				Group group = server.addGroup();
				Map<String, Item> addItems = group.addItems(channels);
				for (Map.Entry<String, Item> temp : addItems.entrySet()) {
					itemList.add(temp.getValue());
				}
			} catch (IllegalArgumentException e) {
				logger.error(ExceptionUtils.getFullStackTrace(e));
				getOpcItemListMap(channels,itemList);
			} catch (UnknownHostException e) {
				logger.error(ExceptionUtils.getFullStackTrace(e));
				getOpcItemListMap(channels,itemList);
			} catch (NotConnectedException e) {
				logger.error(ExceptionUtils.getFullStackTrace(e));
				getOpcItemListMap(channels,itemList);
			} catch (JIException e) {
				logger.error(ExceptionUtils.getFullStackTrace(e));
				getOpcItemListMap(channels,itemList);
			} catch (DuplicateGroupException e) {
				logger.error(ExceptionUtils.getFullStackTrace(e));
				getOpcItemListMap(channels,itemList);
			} catch (AddFailedException e) {
				logger.error(ExceptionUtils.getFullStackTrace(e));
				getOpcItemListMap(channels,itemList);
			} catch(Exception e){
				logger.error("OpcInitUtil---340行："+ExceptionUtils.getFullStackTrace(e));
			}finally {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
				}
			}
		}else {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			getOpcItemListMap(channels,itemList);
		}
	}
	
	private synchronized void getOpcItemListMap(Map<KsGroup, List<Item>> map, Map<KsGroup, String[]> itemMap,List<String> groupServerKeys){
		OpcServer server = ConnectionUtil.longConnect();
		if(null != server){
			map.clear();
			try {
				for (Map.Entry<KsGroup, String[]> entry : itemMap.entrySet()) {
					String groupServer = entry.getKey().getGroupServer();
					if(null != groupServerKeys && groupServerKeys.contains(groupServer)) {
						List<Item> listList = new ArrayList<Item>();
						Group group = server.addGroup();
						Map<String, Item> addItems = group.addItems(entry.getValue());
						for (Map.Entry<String, Item> items : addItems.entrySet()) {
							listList.add(items.getValue());
						}
						map.put(entry.getKey(), listList);
					}
				}
			}catch (IllegalArgumentException e) {
				logger.error(ExceptionUtils.getFullStackTrace(e));
				getOpcItemListMap(map, itemMap,groupServerKeys);
			} catch (UnknownHostException e) {
				logger.error(ExceptionUtils.getFullStackTrace(e));
				getOpcItemListMap(map, itemMap, groupServerKeys);
			} catch (NotConnectedException e) {
				logger.error(ExceptionUtils.getFullStackTrace(e));
				getOpcItemListMap(map, itemMap, groupServerKeys);
			} catch (JIException e) {
				logger.error(ExceptionUtils.getFullStackTrace(e));
				getOpcItemListMap(map, itemMap,groupServerKeys);
			} catch (DuplicateGroupException e) {
				logger.error(ExceptionUtils.getFullStackTrace(e));
				getOpcItemListMap(map, itemMap, groupServerKeys);
			} catch (AddFailedException e) {
				logger.error(ExceptionUtils.getFullStackTrace(e));
				getOpcItemListMap(map, itemMap, groupServerKeys);
			} catch(Exception e){
				logger.error("OpcInitUtil---382行："+ExceptionUtils.getFullStackTrace(e));
			}finally {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
				}
			}
		}else {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			getOpcItemListMap(map, itemMap, groupServerKeys);
		}
	}
}
