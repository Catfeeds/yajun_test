package com.wis.mes.opc.callback.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIString;
import org.jinterop.dcom.core.JIUnsignedShort;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.lib.da.AddFailedException;
import org.openscada.opc.lib.da.Group;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.ItemState;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wis.basis.common.utils.DateUtils;
import com.wis.basis.common.utils.LogConstant;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.uloc.service.TmUlocService;
import com.wis.mes.opc.callback.ReadCallBack;
import com.wis.mes.opc.callback.vo.ProductData;
import com.wis.mes.opc.group.entity.KsGroupItem;
import com.wis.mes.opc.group.service.KsGroupItemService;
import com.wis.mes.opc.util.ConnectionUtil;
import com.wis.mes.opc.util.OpcPropertiesConfig;
import com.wis.mes.opc.util.OpcServer;
import com.wis.mes.production.producttracing.entity.TbProductStation;
import com.wis.mes.production.producttracing.entity.TbProductTracing;
import com.wis.mes.production.producttracing.service.TbProductStationService;
import com.wis.mes.production.producttracing.service.TbProductTracingService;
import com.wis.mes.production.producttracing.vo.ProductStationVo;
import com.wis.mes.production.qualitytracing.entity.TbQualityTracing;
import com.wis.mes.production.qualitytracing.service.TbQualityTracingService;
import com.wis.mes.rfid.util.MathUtils;
import com.wis.mes.rfid.vo.SnInfoObjVo;
import com.wis.mes.util.StringUtil;

import net.sf.json.JSONArray;

@Component(value = "productReadService")
public class ProductReadService implements ReadCallBack {
	private static Map<String, Map<String, Item>> itemMap = new ConcurrentHashMap<String, Map<String, Item>>();
	private static Map<String, String[]> groupItemsListMap = new ConcurrentHashMap<String, String[]>();
	private static TemporaryQualityTracingVo temporaryQualityTracingVo = null;
	private Log logger = LogFactory.getLog(LogConstant.OPC_LOG);
	@Autowired
	private KsGroupItemService itemService;
	@Autowired
	private TmUlocService ulocService;
	@Autowired
	private TbProductStationService productStationService;
	@Autowired
	private TbQualityTracingService qualityTracingService;
	@Autowired
	private TbProductTracingService tracingService;

	@Override
	public synchronized void doChanged(List<Item> items, String groupCode) throws JIException {
		try {
			boolean isComplete = false;
			boolean isNgOut = false;
			ProductStationVo vo = new ProductStationVo();
			if (refreshFlagStatus(items)) {
				Thread.sleep(400);
				for (Item item : items) {
					Object value = null;
					try {
						value = item.read(false).getValue().getObject();
					} catch (Exception e) {
						throw new BusinessException(e.getMessage());
					}
					String itemId = item.getId();
					if (value instanceof Boolean) {
						if (itemId.contains("Complete_Flag")) {
							isComplete = (Boolean) value;
						} else if (itemId.contains("NG_Out_Flag")) {
							isNgOut = (Boolean) value;
						}
					} else if (value instanceof JIString) {
						String str = ((JIString) value).getString();
						if (itemId.contains("SN")) {
							vo.setSn(str);
						} else if (itemId.contains("NG_Enter_Time")) {
							vo.setNgEnterTime(str);
						} else if (itemId.contains("NG_Exit_Time")) {
							vo.setNgExitTime(str);
						}
					} else if (value instanceof JIUnsignedShort) {
						JIUnsignedShort num = (JIUnsignedShort) value;
						String str = String.valueOf(num.getValue().intValue());
						if (itemId.contains("NG_Exit_NO")) {
							vo.setNgExitNo(str);
						} else if (itemId.contains("NG_Enter_NO")) {
							vo.setNgEnterNo(str);
						} else if (itemId.contains("Exit_NO")) {
							vo.setExitNo(str);
						}
					}else  if(value instanceof Short){
						Integer intVal = Integer.valueOf((Short)value);
						if (itemId.contains("Trans_Cycle")) {
							vo.setTransCycle(intVal);
						}
					}
				}
				long start = System.currentTimeMillis();
				ProductStationVo getProductStationVo = productStationService.getProductStationVo(vo.getExitNo(),
						vo.getSn());
				BeanUtils.copyProperties(getProductStationVo, vo, "exitNo", "sn", "ngExitTime", "ngEnterTime",
						"ngEnterNo", "ngExitNo","transCycle");
				if (vo.getProductTracingId() == null) {
					TbProductTracing saveProductTracing = saveProductTracing(vo);
					vo.setProductTracingId(saveProductTracing.getId());
				}
				logger.info("SN:>>" + vo.getSn());
				if (isComplete) {// 如果是产品下线
					boolean doSaveProduct = doSaveProduct(false, vo);
					if (doSaveProduct) {
						writeOpc(items);
					}
					tracingService.updateFinshTime(vo.getSn());
				}
				if (isNgOut) {// 如果是NG出
					boolean doSaveProduct = doSaveProduct(true, vo);
					if (doSaveProduct) {
						writeOpc(items);
					}
				}
				logger.info(">>>>>>>执行时间>>>>>>>>>>>>>>>>" + (System.currentTimeMillis() - start));
			}
		} catch (JIException e) {
			String errMsg = ExceptionUtils.getFullStackTrace(e);
			logger.error(errMsg);
			throw e;
		} catch (BusinessException e) {
			throw new JIException(0, e.getMessage());
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw new JIException(0, e);
		}
	}

	// 检查轮询刷新状态
	private boolean refreshFlagStatus(List<Item> items) throws Exception {
		boolean isRefreshFlag = false;
		for(Item item:items) {
			String itemId = item.getId();
			if (itemId.contains("Refresh_Flag")) {
				try {
					Object value = item.read(false).getValue().getObject();
					isRefreshFlag = (Boolean) value;
					break;
				} catch (Exception e) {
					throw new BusinessException(e.getMessage());
				}
			}
		}
		return isRefreshFlag;
	}

	private TbProductTracing saveProductTracing(ProductStationVo vo) {
		Date date = new Date();
		TbProductTracing bean = new TbProductTracing();
		String beginTime = DateUtils.formatFull(date);
		SnInfoObjVo objVo =  SnInfoObjVo.splitSn(vo.getSn());
		bean.setSn(objVo.getNewSn());
		bean.setMachineName(objVo.getMachineName());
		bean.setBackNumber(objVo.getBackNumber());
		if(StringUtils.isNoneBlank(objVo.getBackNumber())) {
			bean.setMachineOfName(tracingService.getEgModelName(objVo.getBackNumber()));
		}
		bean.setBeginTime(beginTime.substring(11, beginTime.length()));
		bean.setCreateTime(date);
		bean.setTmPlantId(vo.getTmPlantId());// 事部ID
		bean.setTmLineId(vo.getTmLineId());// 产线ID
		bean.setTmWorktimeId(vo.getTmWorktimeId());// 工作日历ID
		bean.setTmClassManagerId(vo.getClassManagerId());// 班组
		bean.setUnhealthyCount(0);
		bean = tracingService.doSave(bean);
		return bean;

	}

	private Map<String, String[]> getItemsMap() {
		// Map<String, String[]> groupItemsListMap = new HashMap<String,String[]>();
		if (groupItemsListMap.isEmpty()) {
			List<KsGroupItem> groupItems = itemService
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

	private boolean getData(final ConcurrentLinkedQueue<Map<String, Object>> dataList,
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
				itemMap.clear();
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			flag = false;
		}
		return flag;
	}

	private boolean doSaveProduct(boolean isNg, ProductStationVo vo) {
		List<TbProductStation> productDataList = new ArrayList<TbProductStation>();
		List<Integer>  staIdList = new ArrayList<Integer>();
		List<Integer> transCycleEnum = new ArrayList<Integer>();
		if(isNg) {
			transCycleEnum = getTransCycleEnum(vo.getTransCycle(),Integer.valueOf(vo.getNgExitNo()));
		}else if(vo.getTransCycle().equals(5)){
			transCycleEnum = getTransCycleEnum(vo.getTransCycle(),136);//136最后一轮的刷新工位
		}else {
			transCycleEnum = getTransCycleEnum(vo.getTransCycle());
		}
		boolean flag = true;
		if(!(transCycleEnum.size()>0))return flag;
		MathUtils.IntPair pair = MathUtils.minmax(transCycleEnum.toArray(new Integer[transCycleEnum.size()]));
		try {
			if(vo.getTransCycle().equals(1))Thread.sleep(1000);
			Map<String, String[]> itemsMap = getItemsMap();
			ConcurrentLinkedQueue<Map<String, Object>> dataList = new ConcurrentLinkedQueue<Map<String, Object>>();
			boolean readData = getData(dataList, itemsMap);
			if (readData) {
				long startTime = System.currentTimeMillis();
				while(true) {
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
						if (StringUtils.isNotBlank(data.getStaId())) {
							int staId = Integer.valueOf(data.getStaId());
							if (staId == 0) continue;
							if(!staIdList.contains(staId) && pair.getMin()<=staId && pair.getMax()>=staId) {
								staIdList.add(staId);
								TbProductStation tbProductStation = getTbProductStation(data, vo, isNg);
								productDataList.add(tbProductStation);
								if (isNg) {
									Integer infoSource = Integer.valueOf(data.getOkNg());
									if (infoSource.intValue() == 128 || infoSource == 32 || infoSource.intValue() == 8
											|| infoSource.intValue() == 2) {
										if (temporaryQualityTracingVo == null) {
											temporaryQualityTracingVo = new TemporaryQualityTracingVo();
											temporaryQualityTracingVo.setAbnormalCode(data.getAbnormalCode());
											temporaryQualityTracingVo.setClassManagerId(vo.getClassManagerId());
											temporaryQualityTracingVo.setExitNo(vo.getExitNo());
											temporaryQualityTracingVo.setInfoSorce(infoSource);
											temporaryQualityTracingVo.setShiftNo(vo.getShiftNo());
											temporaryQualityTracingVo.setSn(vo.getSn());
											temporaryQualityTracingVo.setTmulocId(String.valueOf(tbProductStation.getTmUlocId()));
											temporaryQualityTracingVo.setTmWorktimeId(vo.getTmWorktimeId());
										}
									}
								}
							}
						}
					}
					Collections.sort(staIdList);
					if(staIdList.equals(transCycleEnum)) {
						break;
					}else {
						long finisTime = System.currentTimeMillis();
						if(finisTime-startTime > 3000){
							logger.info(vo.getSn()+"超出当前最大时间");
							break;
						}
					}
				}
			}
			if (isNg) {
				if (transCycleEnum.contains(Integer.valueOf(vo.getNgExitNo()))) {
					if (null != temporaryQualityTracingVo) {
						qualityTracingService.doSaveTbQualityTracing(temporaryQualityTracingVo.getSn(),
								temporaryQualityTracingVo.getExitNo(),
								temporaryQualityTracingVo.getTmulocId(),
								temporaryQualityTracingVo.getShiftNo(),
								temporaryQualityTracingVo.getClassManagerId(),
								temporaryQualityTracingVo.getInfoSorce(),
								temporaryQualityTracingVo.getAbnormalCode(),
								temporaryQualityTracingVo.getTmWorktimeId());
						temporaryQualityTracingVo = null;
						/*tracingService.updateUnhealthyCount(vo.getSn());*/
					} else{
						TbQualityTracing eg = new TbQualityTracing();
						eg.setSn(vo.getSn());
						List<TbQualityTracing> findByEg = qualityTracingService.findByEg(eg);
						if (null == findByEg || findByEg.size() < 1) {
							qualityTracingService.doSaveTbQualityTracing(vo.getSn(), vo.getExitNo(), "",
									vo.getShiftNo(), vo.getClassManagerId(), 2, "", vo.getTmWorktimeId());
							/*tracingService.updateUnhealthyCount(vo.getSn());*/
						}
					}
				}
			}
			logger.info("SN"+vo.getSn()+", 周期"+vo.getTransCycle()+", staIdList"+JSONArray.fromObject(staIdList).toString()+",transCycleEnum"+JSONArray.fromObject(transCycleEnum).toString());
			productStationService.doSaveBatch(productDataList);
		} catch (IllegalArgumentException e) {
			flag = false;
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} catch (IllegalAccessException e) {
			flag = false;
			logger.error(ExceptionUtils.getFullStackTrace(e));
		} catch (Exception e) {
			flag = false;
			logger.error(ExceptionUtils.getFullStackTrace(e));
		}
		return flag;
	}

	private TbProductStation getTbProductStation(ProductData vo, ProductStationVo psvo, boolean isNg) {
		TbProductStation bean = new TbProductStation();
		bean.setAbnormalCode(vo.getAbnormalCode());
		if (StringUtils.isNotEmpty(vo.getAbnormalDuration())) {
			bean.setAbnormalDuration(Long.valueOf(vo.getAbnormalDuration()));
		}
		if (StringUtils.isNotEmpty(vo.getBackWaitDuration())) {
			bean.setBackWaitDuration(Long.valueOf(vo.getBackWaitDuration()));
		}
		bean.setEnterBeginTime(vo.getEnterBegin());
		bean.setEnterFinishTime(vo.getEnterEnd());
		bean.setEquipmentBeginTime(vo.getDeviceWorkBegin());
		bean.setEquipmentFinishTime(vo.getDeviceWorkEnd());
		if (StringUtil.isNotEmpty(vo.getDeviceWorkDuration())) {
			bean.setEquipmentWorkDuration(Long.valueOf(vo.getDeviceWorkDuration()));
		}
		if (StringUtils.isNotEmpty(vo.getFrontWaitDuration())) {
			bean.setFrontWaitDuration(Long.valueOf(vo.getFrontWaitDuration()));
		}
		bean.setOkNgResult(vo.getOkNg());
		bean.setOutBeginTime(vo.getExitBegin());
		bean.setOutFinishTime(vo.getExitEnd());
		if (StringUtils.isNotEmpty(vo.getStaffWorkDuration())) {
			bean.setStaffWorkDuration(Long.valueOf(vo.getStaffWorkDuration()));
		}
		bean.setStationFinishTime(vo.getStationDoneTime());
		if (StringUtils.isNotEmpty(vo.getStationWorkDuration())) {
			bean.setStationWorkDuration(Long.valueOf(vo.getStationWorkDuration()));
		}
		if (StringUtils.isNotEmpty(vo.getWarningDuration())) {
			bean.setWarningDuration(Long.valueOf(vo.getWarningDuration()));
		}
		if (StringUtils.isNotEmpty(vo.getProcessingValue())) {
			bean.setProcessingValue(Long.valueOf(vo.getProcessingValue()));
		}
		if (isNg) {
			if (StringUtils.isNotEmpty(psvo.getNgEnterNo()) && psvo.getNgEnterNo().equals(vo.getStaId())) {
				bean.setNgEntryNumber(psvo.getNgEnterNo());
				bean.setNgEntryTime(psvo.getNgEnterTime());
			}
		}
		if (StringUtils.isNotEmpty(psvo.getNgExitNo()) && psvo.getNgExitNo().equals(vo.getStaId())) {
			bean.setNgExitTime(psvo.getNgExitTime());
			bean.setNgExitNumber(psvo.getNgExitNo());
		}
		bean.setSn(psvo.getSn());
		bean.setStaffUserId(psvo.getStaffId());
		bean.setTbProductTracingId(psvo.getProductTracingId());
		TmUloc uloc = ulocService.getRedisUloc(psvo.getTmLineNo(), vo.getStaId());
		if (uloc != null) {
			bean.setTmUlocId(uloc.getId());
		}
		return bean;
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

	private void writeOpc(List<Item> itemList) throws JIException {
		for (Item item : itemList) {
			String itemId = item.getId();
			if (itemId.contains("Refresh_Flag")) {
				item.write(new JIVariant(false));
				break;
			}
		}
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
		}
	}
	
	private List<Integer> getTransCycleEnum(int cycle){
		List<Integer> cycleList = new ArrayList<Integer>();
		for(int i=((29*cycle)-29)+1;i<=(29*cycle);i++) {
			cycleList.add(i);
		}
		return cycleList;
	}
	
	private List<Integer> getTransCycleEnum(int cycle,int finis){
		List<Integer> cycleList = new ArrayList<Integer>();
		for(int i=((29*cycle)-29)+1;i<=(29*cycle);i++) {
			if(i>finis) break;
			cycleList.add(i);
		}
		return cycleList;
	}
}

class TemporaryQualityTracingVo {
	private String sn;
	private String exitNo;
	private String tmulocId;
	private String shiftNo;
	private Integer classManagerId;
	private Integer infoSorce;
	private String abnormalCode;
	private Integer TmWorktimeId;

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getExitNo() {
		return exitNo;
	}

	public void setExitNo(String exitNo) {
		this.exitNo = exitNo;
	}

	public String getTmulocId() {
		return tmulocId;
	}

	public void setTmulocId(String tmulocId) {
		this.tmulocId = tmulocId;
	}

	public String getShiftNo() {
		return shiftNo;
	}

	public void setShiftNo(String shiftNo) {
		this.shiftNo = shiftNo;
	}

	public Integer getClassManagerId() {
		return classManagerId;
	}

	public void setClassManagerId(Integer classManagerId) {
		this.classManagerId = classManagerId;
	}

	public Integer getInfoSorce() {
		return infoSorce;
	}

	public void setInfoSorce(Integer infoSorce) {
		this.infoSorce = infoSorce;
	}

	public String getAbnormalCode() {
		return abnormalCode;
	}

	public void setAbnormalCode(String abnormalCode) {
		this.abnormalCode = abnormalCode;
	}

	public Integer getTmWorktimeId() {
		return TmWorktimeId;
	}

	public void setTmWorktimeId(Integer tmWorktimeId) {
		TmWorktimeId = tmWorktimeId;
	}

}
