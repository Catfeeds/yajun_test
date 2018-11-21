package com.wis.mes.opc;

import java.lang.reflect.Field;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.common.JISystem;
import org.jinterop.dcom.core.JIString;
import org.jinterop.dcom.core.JIUnsignedShort;
import org.junit.Test;
import org.openscada.opc.dcom.list.ClassDetails;
import org.openscada.opc.lib.common.AlreadyConnectedException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.AccessBase;
import org.openscada.opc.lib.da.AddFailedException;
import org.openscada.opc.lib.da.Async20Access;
import org.openscada.opc.lib.da.AutoReconnectController;
import org.openscada.opc.lib.da.AutoReconnectListener;
import org.openscada.opc.lib.da.AutoReconnectState;
import org.openscada.opc.lib.da.DataCallback;
import org.openscada.opc.lib.da.DuplicateGroupException;
import org.openscada.opc.lib.da.Group;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.ItemState;
import org.openscada.opc.lib.da.Server;
import org.openscada.opc.lib.list.Categories;
import org.openscada.opc.lib.list.Category;
import org.openscada.opc.lib.list.ServerList;
import org.springframework.beans.factory.annotation.Autowired;

import com.wis.basis.common.utils.ConstantUtils;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.basis.BizBaseTestCase;
import com.wis.mes.file.service.TsFileService;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.uloc.service.TmUlocService;
import com.wis.mes.opc.callback.vo.ProductData;
import com.wis.mes.opc.group.entity.KsGroup;
import com.wis.mes.opc.group.entity.KsGroupItem;
import com.wis.mes.opc.group.service.KsGroupItemService;
import com.wis.mes.opc.group.service.KsGroupService;
import com.wis.mes.opc.util.ConnectionUtil;
import com.wis.mes.opc.util.OpcPropertiesConfig;
import com.wis.mes.opc.util.OpcServer;
import com.wis.mes.production.stationqueuerecord.entity.TbStationQueueRecord;
import com.wis.mes.production.stationqueuerecord.service.TbStationQueueRecordService;
import com.wis.mes.rfid.vo.RfidReadVo;

public class OpcTest extends BizBaseTestCase{
	
	@Autowired
	private KsGroupService groupService;
	@Autowired
	private KsGroupItemService groupItemService;
	@Autowired
	private TsFileService tsFileService;
	@Autowired
	private TbStationQueueRecordService stationQueueRecordService;
	@Autowired
	private TmUlocService tmUlocService;
	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	
	private static Map<String, Map<String, Item>> itemMap = new ConcurrentHashMap<String, Map<String, Item>>();
	
	private static final int PERIOD = 100;
	private static final int SLEEP = 2000;
	private static Server servcer = null;
	
	
	public static void test1() throws Exception{
			 ConnectionInformation ci = new ConnectionInformation();
			 ci.setHost("192.168.90.26");
			 ci.setDomain("");
			 ci.setProgId("Kepware.KEPServerEnterprise.V6");
			 ci.setClsid("7BC0CC8E-482C-47CA-ABDC-0FE7F9C6E729");
			 ci.setUser("Administrator");
			 ci.setPassword("shengji");

			Server server = new Server(ci,
					Executors.newSingleThreadScheduledExecutor());

			server.connect();
			AccessBase access = new Async20Access(server, PERIOD, false);

			access.addItem("Banjin.1756-L72.EQUIPMA.PUNCHPRESS_09.EQUIPLIST[25]MALIST[0]TRIGGER", new DataCallback() {

				private int count;

				public void changed(Item item, ItemState itemstate) {
					System.out.println("[" + (++count) + "],ItemName:["
							+ item.getId() + "],value:" + itemstate.getValue());
				}
			});
			access.bind();
			Thread.sleep(SLEEP);
			access.unbind();
			server.dispose();
		
	}
	
	public static void syncRead() throws Exception { 
		OpcServer server = ConnectionUtil.longConnect();
		AccessBase access = new Async20Access(server, PERIOD, false);
		access.addItem("Channel1.L72.Scanner.SN", new DataCallback() {

			private int count;

			public void changed(Item item, ItemState itemstate) {
				System.out.println("[" + (++count) + "],ItemName:["
						+ item.getId() + "],value:" + itemstate.getValue());
			}
		});

		access.bind();
		Thread.sleep(SLEEP);
		access.unbind();
		server.dispose();
	} 
	
	@Test
	public void groupItemMap() {
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
	}
	
	@Test
	public void doSaveProduct() {
		try {
			Map<String, String[]> itemsMap = getItemsMap();
			ConcurrentLinkedQueue<Map<String, Object>> dataList = new ConcurrentLinkedQueue<Map<String, Object>>();
			boolean readData = getData(dataList, itemsMap);
			if(readData) {
				for(Map<String,Object> map:dataList) {
					for (Map<String, Object> dataMap : dataList) {
						ProductData data = new ProductData();
						Field[] declaredFields = data.getClass().getDeclaredFields();
						for (Field field : declaredFields) {
							String fieldName = field.getName().toUpperCase();
							String str = "";
							if (dataMap.containsKey(fieldName)) {
								Object obj = dataMap.get(fieldName);
								if (obj instanceof JIUnsignedShort) {
									JIUnsignedShort value = (JIUnsignedShort) obj;
									str = String.valueOf(value.getValue().longValue());
								} else if (obj instanceof JIString) {
									str = ((JIString) obj).getString();
								} else if (obj instanceof Integer) {
									str = String.valueOf(obj);
								} else if (obj instanceof Long) {
									str = String.valueOf(obj);
								} else if (obj instanceof Short) {
									str = String.valueOf(obj);
								}
							}
							field.setAccessible(true);
							field.set(data, str);
						}
						
					}
				}
			}
			System.out.println();
		}catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
	}
	
	public Map<String, String[]> getItemsMap() {
		Map<String, String[]> groupItemsListMap = new HashMap<String,String[]>();
		if (groupItemsListMap.isEmpty()) {
			List<KsGroupItem> groupItems = groupItemService
					.getRedisItemListByGroupCode(OpcPropertiesConfig.getPropertyByKey("opc.config.uloc.product.group"));
			String[] channels = OpcPropertiesConfig.getPropertyByKey("opc.config.uloc.product.channel").split(",");// 通道
			for (String channel : channels) {
				String[] itemsCode = OpcPropertiesConfig.getPropertyByKey("opc.config.uloc.product.channel." + channel)
						.split(",");
				String tag = channel + ".Product";
				for (String str : itemsCode) {
					String tag1 = tag + "." + str;
					String[] items = new String[groupItems.size()];
					for (int i = 0, size = groupItems.size(); i < size; i++) {
						String itemCode = groupItems.get(i).getItemCode();
						items[i] = tag1 + "." + itemCode;
					}
					groupItemsListMap.put(tag1, items);
				}
			}
		}
		return groupItemsListMap;
	}
	
	public boolean getData(final ConcurrentLinkedQueue<Map<String, Object>> dataList,
			Map<String, String[]> itemListMap) {
		boolean flag = true;
		try {
			final AtomicInteger atomicInteger = new AtomicInteger(0);
			final OpcServer server = ConnectionUtil.longConnect();
			final CountDownLatch latch = new CountDownLatch(itemListMap.size());
			ExecutorService pool = Executors.newFixedThreadPool(29);
			for (final Map.Entry<String, String[]> entry : itemListMap.entrySet()) {
				pool.execute(new Runnable() {
					@Override
					public void run() {
						try {
							Map<String, Object> opcDataMap = getOpcDataMap(entry.getKey(), entry.getValue(), server);
							dataList.add(opcDataMap);
						} catch (Exception e) {
							atomicInteger.incrementAndGet();
							logger.error(ExceptionUtils.getStackTrace(e));
						}
						latch.countDown();
					}
				});
			}
			latch.await();
			pool.shutdown();
			if (atomicInteger.get() > 0) {
				flag = false;
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			flag = false;
		}
		return flag;
	}
	
	private Map<String, Object> getOpcDataMap(String groupCode, String[] items, OpcServer server) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, Item> addItems = null;
			if (!itemMap.containsKey(groupCode)) {
				Group group = server.addGroup();
				addItems = group.addItems(items);
				itemMap.put(groupCode, addItems);
			} else {
				addItems = itemMap.get(groupCode);
			}
			for (Map.Entry<String, Item> temp : addItems.entrySet()) {
				Item item = temp.getValue();
				for (int i = 0; i < 10; i++) {
					ItemState read = item.read(false);
					if (read.getQuality() == 192) {
						Object value = read.getValue().getObject();
						if (value.toString().contains("EMPTY")) {
							continue;
						}
						String[] split = item.getId().split("\\.");
						map.put(split[split.length - 1].toUpperCase().replaceAll("_", ""), value);
						break;
					}
				}
			}
		} catch (JIException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			itemMap.clear();
		} catch (AddFailedException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			itemMap.clear();
		} catch (Exception e) {
			itemMap.clear();
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return map;
	}
	
	@Test
	public void testFile() {
		try {
			tsFileService.doDeleteFiles();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestStationQueueRecord() {
		TbStationQueueRecord record = new TbStationQueueRecord();
		record.setFruit("失败");
		record.setPlcSn("123456789023456789");
		record.setReadTagSn("123456789876543");
		record.setRfidReadFruit("失败，读取失败");
		record.setUlocNo("10");
		stationQueueRecordService.doSave(record);
	}
	
	
	public static void ServerListTest() throws IllegalArgumentException, UnknownHostException, JIException {
		ServerList serverList = new ServerList("192.168.90.13", "Administrator",
				"Oliveryang", "");

		Collection<ClassDetails> classDetails = serverList
				.listServersWithDetails(new Category[] {
						Categories.OPCDAServer10, Categories.OPCDAServer20,
						Categories.OPCDAServer30 }, new Category[] {});

		for (ClassDetails cds : classDetails) {
			System.out.println(cds.getProgId() + "=" + cds.getDescription());
		}
	}
	
	public static void accessBaseTest() throws JIException, AddFailedException, IllegalArgumentException, UnknownHostException, NotConnectedException, DuplicateGroupException, AlreadyConnectedException, InterruptedException{
		AutoReconnectController autos = null;
		JISystem.setAutoRegisteration(true);
		ConnectionInformation ci = new ConnectionInformation();
		ci.setHost("192.168.90.13");
		ci.setDomain("");
		ci.setUser("Administrator");
		ci.setPassword("Oliveryang");
		ci.setClsid("7BC0CC8E-482C-47CA-ABDC-0FE7F9C6E729");
		Server server = new Server(ci,
				Executors.newSingleThreadScheduledExecutor());
		autos = new AutoReconnectController(server);
		autos.connect();
		Thread.sleep(100);
		Group group = server.addGroup("Channel1.L72.Scanner.SN");
		group.setActive(true);
		final Item item = group.addItem("Channel1.Device1.D0");
		item.setActive(true);
		Thread.sleep(100);
		System.out.println("读取值："+item.read(false).getValue().getObjectAsUnsigned().getValue());
	}
	
	//private static ExecutorService station62pool = Executors.newSingleThreadExecutor();
	private static ExecutorService station62pool = Executors.newFixedThreadPool(3);
	
	public static void executorServiceTest() {
		station62pool.execute(new Runnable() {
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName()+"12345678901234567890");
			}
		});
	}
	
	public static void main(String[] args) throws IllegalArgumentException, UnknownHostException, JIException, AddFailedException, NotConnectedException, DuplicateGroupException, AlreadyConnectedException, InterruptedException {
		ExecutorService pool = Executors.newFixedThreadPool(10);
		MyThread1 myThread1 = new MyThread1();
		MyThread1 myThread2 = new MyThread1();
		MyThread1 myThread3 = new MyThread1();
		pool.execute(myThread1);
		pool.execute(myThread2);
		pool.execute(myThread3);
	}
	
	public static RfidReadVo plcRfidRead()
    {
        //创建一个线程池对象
        ExecutorService pool = Executors.newCachedThreadPool();
        //创建一个有返回值的任务
        PlcRfidReadThread task = new PlcRfidReadThread("10");
        //执行任务并获取Future对象
        Future<RfidReadVo> future  = pool.submit(task);
        //从 Future 对象 获取任务返回值
        RfidReadVo rfidReadVo = null;
        while(true)
        {
            //可以用isDone()方法来查询Future是否已经完成，任务完成后，可以调用get()方法来获取结果
            //注意： 如果不加判断直接调用get方法，此时如果线程未完成，get将阻塞，直至结果准备就绪
            if(future.isDone())
            {
                try
                {
                	rfidReadVo = future.get();
                }catch (Exception e){
                    e.printStackTrace();
                }
                //关闭线程池
                pool.shutdown();
                //跳出循环
                break;
            }
        }
        return rfidReadVo;
    }
	
	public TmUloc getUloc(String ulocNo) {
		TmUloc uloc = null;
		if (StringUtils.isNotBlank(ulocNo)) {
			String lineNo = globalConfigurationService.getValueByKey(ConstantUtils.R5_LINE_NO);
			uloc = tmUlocService.getRedisUloc(lineNo, ulocNo);
			if (null == uloc) {
				throw new BusinessException("获取" + ulocNo + "工位失败，检查工位基础数据。");
			}
			if (null != uloc) {
				if (StringUtils.isBlank(uloc.getRfidIP()) || null == uloc.getRfidPort()) {
					throw new BusinessException("工位编号：" + ulocNo + "：没有维护IP或没有维护端口号。");
				}
			}
		}
		return uloc;
	}
}

class MyThread1 implements Runnable{
    @Override
    public void run() {
    	RfidReadVo plcRfidRead = OpcTest.plcRfidRead();
    	System.out.println(Thread.currentThread().getName());
    	System.out.println(plcRfidRead.getSn());
    }   
}

class MyThreadTest implements Runnable{
	@Override
	public void run() {
		while (true) {
			System.out.println(Thread.currentThread().getName()+"-->我是通过实现接口的线程实现方式！");
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}
}

class PlcRfidReadThread implements Callable<RfidReadVo> 
{
    private RfidReadVo rfidReadVo;
    private String ulocNo;

    public PlcRfidReadThread(String value)
    {
        this.ulocNo = value;
    }
    public RfidReadVo call() throws Exception
    {
    	
    	String sn = "1234567890";
		String message = "";
		this.rfidReadVo = new RfidReadVo();
		TmUloc uloc = null;
		/*AlienClass1Reader reader = null;
		try {
			uloc = getUloc(this.ulocNo);
			if (null != uloc && StringUtils.isNotBlank(uloc.getServiceName())) {
				Object bean = WebContextHolder.getWebAppContext().getBean(uloc.getServiceName());
				if (null != bean) {
					IRfidCallBack callback = (IRfidCallBack) bean;
					reader = callback.doCreateConnection(uloc);
				}
				TagVo tagVo = getECPTagId(reader, 0);
				if (null != tagVo && StringUtils.isNotBlank(tagVo.getUserTagId())) {
					sn = tagVo.getUserTagId();
				}
			} else {
				message = "工位：" + ulocNo + " 没有维护servicename。";
			}
		} catch (BusinessException e) {
			message = e.getMessage();
		} catch (Exception e) {
			message = "写入失败，请重试!";
		}*/
		this.rfidReadVo.setSn(sn);
		this.rfidReadVo.setTmUloc(uloc);
		this.rfidReadVo.setMessage(message);
        return  this.rfidReadVo;
    }
}
