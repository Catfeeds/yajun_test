package com.wis.mes.metalplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jinterop.dcom.core.JIVariant;
import org.junit.Test;
import org.openscada.opc.lib.da.Item;
import org.springframework.beans.factory.annotation.Autowired;

import com.wis.basis.common.utils.ConstantUtils;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.basis.BizBaseTestCase;
import com.wis.mes.master.boardmanage.entity.TmBoardManage;
import com.wis.mes.master.boardmanage.service.TmBoardManageService;
import com.wis.mes.master.metalPlate.entity.TmSheetMetalMaterial;
import com.wis.mes.master.metalPlate.service.TmSheetMetalMaterialService;
import com.wis.mes.opc.util.OpcPropertiesConfig;
import com.wis.mes.opc.util.metalplate.OpcInitMetalPlateUtil;
import com.wis.mes.opc.util.metalplate.OpcMetalPlateHelper;
import com.wis.mes.production.metalplate.dao.TbMetalPlateSchedulDao;
import com.wis.mes.production.metalplate.entity.TbMetalPlateSchedul;

public class MPRegionMarkMonitorTest extends BizBaseTestCase{

	@Autowired
	private TmSheetMetalMaterialService sheetMetalMaterialService;
	@Autowired
	private TbMetalPlateSchedulDao dao;
	@Autowired
	private TmBoardManageService boardManageService;
	
	//区域生产状态标签名称
	private static String[] regionalStateTagNames = OpcPropertiesConfig.getPropertyByKey("opc.bj.regional.production.state").split(",");
	
	//区域生产状态Items
	private static List<Item> regionalStateItems = new ArrayList<Item>();
	
	//区域区分标签名称(1：3+2+1,2：4+4)
	private static String[] regionalDivisionTagNames = new String[] { "Banjin.1756-L72.ORDERCOMM.Zone_Type" };
	//区域区分Items
	List<Item> regionalDivisionItems = new ArrayList<Item>();
	private static String REGION_MARK_ZONE_TYPE = "Zone_Type";
	
	@Test
	public void mpregionMarkMonitor() {
		OpcInitMetalPlateUtil.getOpcItemListMap(regionalStateTagNames, regionalStateItems);
		OpcInitMetalPlateUtil.getOpcItemListMap(regionalDivisionTagNames, regionalDivisionItems);
		//区域管理
		List<TmBoardManage> boardManageList = boardManageService.findAll();
		
	final ExecutorService pool1 = Executors.newSingleThreadExecutor();
		/*pool1.execute(new Runnable() {
			@Override
			public void run() {
				while (true) {*/
					try {
						for(TmBoardManage boardManage:boardManageList) {
							String regionMark = boardManage.getRegionMark();
							final Map<String, String> orderCommMap = getOrdercommItemsByIndex(regionMark);
							//检查对应区域是否有任务需要派工
							TbMetalPlateSchedul schedul = ((TbMetalPlateSchedulDao) dao).queryNextSchedulByRegionMark(regionMark);
							if(null == schedul)continue;
							// 先从数据库读取该区域是否有已下发并没有完成的数据(1在产 3已下发 4已校验 )
							int validateWorkOrder  = ((TbMetalPlateSchedulDao) dao).queryBeProductionNumberByRegionMark(regionMark);
							if(validateWorkOrder != 0)continue;
							Map<String, Object> sendPlcMap = null;
							
							//检查当前区域是否可以派单
							boolean isSend = checkRegionalState(schedul.getStatus(),regionMark);
							// PLC准备就绪下发该区域订单，且当前订单为需要下发的
							if(isSend && ConstantUtils.BJ_MP_SCHEDUL_STATUS_0.equals(schedul.getStatus())) {
								TmSheetMetalMaterial material = sheetMetalMaterialService.findById(schedul.getMaterialId());
								if(null == material) {
									continue;
								}
								Integer zoneType = getPlcZoneType();
								if(zoneType != 0) {
									Integer updateZoneTypeValue = 0;
									//根据区域名称判断即可
									if (regionMark == "D" ||regionMark == "E"&& zoneType == 1) {
										updateZoneTypeValue = 2;
									} else if (regionMark == "A" ||regionMark == "B" ||regionMark == "C" && zoneType == 2) {
										updateZoneTypeValue = 1;
									}
									if (updateZoneTypeValue != 0) {
										//查看区域是否全部空闲，全部空闲才能更换区域生产模式，然后派单
										boolean checkAllRegion = checkAllRegion();
										System.out.println(checkAllRegion);
										if (checkAllRegion) {
											sendPlcMap = new HashMap<String, Object>();
											sendPlcMap.put(REGION_MARK_ZONE_TYPE, updateZoneTypeValue);
											OpcMetalPlateHelper.sendDataToOpc(regionalDivisionTagNames, sendPlcMap);
										}else {
											continue;
										}
									}
								}
								sendPlcMap = new HashMap<String, Object>();
								sendPlcMap.put("BJ_PLC_01_"+ regionMark + "_ODRINFO_BATCH", schedul.getBatchNumber());
								sendPlcMap.put("BJ_PLC_01_" + regionMark + "_ODRINFO_STATUS", ConstantUtils.BJ_MP_SCHEDUL_STATUS_1);		
								sendPlcMap.put("BJ_PLC_01_" + regionMark + "_ODRINFO_PGCODE", material.getPlcNo());		
								OpcMetalPlateHelper.sendDataToOpc(new String[] {orderCommMap.get("odrInfoBatch"),orderCommMap.get("odrInfoStatus"),orderCommMap.get("odrInfoPgcode")}, sendPlcMap);

							}
						}
					} catch (BusinessException e) {
						regionalStateItems.clear();
						regionalDivisionItems.clear();
						OpcInitMetalPlateUtil.getOpcItemListMap(regionalStateTagNames, regionalStateItems);
						OpcInitMetalPlateUtil.getOpcItemListMap(regionalDivisionTagNames, regionalDivisionItems);
					}finally {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {}
					}
		/*	}
			}
		});*/
}
	
	/***
	 * @author yajun_ren
	 * @date 2018/9/13
	 * @desc 获取当前生产模式
	 * @return isFree
	 * **/
	private Integer getPlcZoneType() {
		int zoneType = 0;
		JIVariant value = null;
		try {
			value = regionalDivisionItems.get(0).read(false).getValue();
			zoneType = (int) value.getObjectAsShort();
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		return zoneType;
	}
	
	/***
	 * @author yajun_ren
	 * @date 2018/9/13
	 * @desc 检查对应区域是否空闲
	 * @return isFree
	 * @param schedulState 工单当前状态,regionMark 区域标识
	 * **/
	private boolean checkRegionalState(Integer schedulState,String regionMark) {
		Map<String, String> orderCommMap = getOrdercommItemsByIndex(regionMark);
		Map<String, Object> sendPlcMap = null;
		boolean isSend = true;
		for(Item item:regionalStateItems) {
			String itemId = item.getId();
			if(orderCommMap.get("odrInfoStatus").equals(itemId)) {
				int status = 0;
				try {
					JIVariant value = item.read(false).getValue();//false读缓存，true读取真实数据。false快
					status = (int) value.getObjectAsChar();
				} catch (Exception e) {
					throw new BusinessException(e.getMessage());
				}
				//状态不是待产,也不是已完成
				if (!ConstantUtils.BJ_MP_SCHEDUL_STATUS_0.equals(status) && !ConstantUtils.BJ_MP_SCHEDUL_STATUS_5.equals(status)) {
					isSend = false;
				}
				//状态等于已完成刷新batch 批号
				if (ConstantUtils.BJ_MP_SCHEDUL_STATUS_5.equals(status)) {
					sendPlcMap = new HashMap<String, Object>();
					sendPlcMap.put("BJ_PLC_01_"+ regionMark + "_ODRINFO_BATCH", "0");
					sendPlcMap.put("BJ_PLC_01_" + regionMark + "_ODRINFO_STATUS", 0);
					OpcMetalPlateHelper.sendDataToOpc(new String[] {orderCommMap.get("odrInfoBatch"),orderCommMap.get("odrInfoStatus")}, sendPlcMap);
				}
				if (ConstantUtils.BJ_MP_SCHEDUL_STATUS_2.equals(status)) {
					// 查看模具是否全部校验完成，完成后给PLC写入3
					if (ConstantUtils.BJ_MP_SCHEDUL_STATUS_4.equals(schedulState)) {
						String[] ORDERCOMM_ROWS = new String[] {
								"Banjin.1756-L72.ORDERCOMM.MFG AREA " + regionMark
										+ ".BJ_PLC_01_" + regionMark + "_ODRINFO_STATUS" };
						sendPlcMap = new HashMap<String, Object>();
						sendPlcMap.put("BJ_PLC_01_" + regionMark + "_ODRINFO_STATUS", 3);
						OpcMetalPlateHelper.sendDataToOpc(ORDERCOMM_ROWS, sendPlcMap);
					}
				}
				break;
			}
		}
		return isSend;
	}
	
	/***
	 * @author yajun_ren
	 * @date 2018/9/13
	 * @desc 检查所有区域是否空闲
	 * @return isFree
	 * 
	 * **/
	private boolean checkAllRegion() {
		//区域管理
		List<TmBoardManage> boardManageList = boardManageService.findAll();
		boolean isFree = true;//是否空闲
		for (TmBoardManage boardManageMark:boardManageList) {
			//获取allRegionalStateTagNames
			Map<String, String> markMap = getOrdercommItemsByIndex(boardManageMark.getRegionMark());
			for (Item item : regionalStateItems) {
				String markItemId = item.getId();
				if (markMap.get("odrInfoStatus").equals(markItemId)) {
					int status = 0;
					try {
						JIVariant value = item.read(false).getValue();
						status = (int) value.getObjectAsChar();
					} catch (Exception e) {
						throw new BusinessException(e.getMessage());
					}
					if (!ConstantUtils.BJ_MP_SCHEDUL_STATUS_0.equals(status) && !ConstantUtils.BJ_MP_SCHEDUL_STATUS_5.equals(status) ) {
						isFree = false;
						break;
					}
				}
			}
		}
		return isFree;
	}
	
	/***
	 * @author yajun_ren
	 * @date 2018/9/12
	 * @desc 通过区域标识拼接 程序号/批号/生产状态 tagName
	 * @param regionMark
	 * @return orderCommMap
	 * 
	 * **/
	private static Map<String,String> getOrdercommItemsByIndex(String regionMark) {
		Map<String,String> orderCommMap = new HashMap<String,String>();
		String ordercommName = "Banjin.1756-L72.ORDERCOMM.MFG AREA {0}.";
		//批号
		orderCommMap.put("odrInfoBatch", ordercommName.replace("{0}", regionMark)+"BJ_PLC_01_{0}_ODRINFO_BATCH".replace("{0}", regionMark));
		//生产状态
		orderCommMap.put("odrInfoStatus", ordercommName.replace("{0}", regionMark)+"BJ_PLC_01_{0}_ODRINFO_STATUS".replace("{0}", regionMark));
		//程序号
		orderCommMap.put("odrInfoPgcode", ordercommName.replace("{0}", regionMark)+"BJ_PLC_01_{0}_ODRINFO_PGCODE".replace("{0}", regionMark));
		return orderCommMap;
	}
}
