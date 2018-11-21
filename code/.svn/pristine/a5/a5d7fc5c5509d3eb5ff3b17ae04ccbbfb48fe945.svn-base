package com.wis.mes.rfid.service;

import java.util.Map;

import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.mes.rfid.vo.RfidReadVo;

public interface RfidService {
	/**rfid读写
	 * @throws Exception **/
	JsonActionResult doRfidReadWrite(String sn) throws Exception;
	
	/**
	 * PLC获取标签USERTAGID
	 * @throws Exception 
	 *@return JsonActionResult
	 * **/
	public RfidReadVo  plcRfidRead(String ulocNo);
	
	/**
	 * @rfidReadVo
	 * @groupCode 
	 * @whetherInOrOut 流入流出口，区分是否是流入流出
	 * 根据工位特征，验证工位是出口/入口，做相应的事情
	 * ***/
	public void stationWrite(String groupCode,RfidReadVo rfidReadVo,String whetherInOrOut);
	
	/**
	 * 验证托盘到席是否可写
	 * @rfidTag
	 * **/
	boolean validateRfidTag(String sn);
	
	/**
	 * 
	 * 根据工位编号验证RFID连接状态
	 * @ulocNo 工位编号
	 * */
	Map<String,Object> getRfidConnectStatus(String ulocNo);
	
	/**
	 * 
	 * @sn
	 * @写入SN
	 * */
	boolean rfidStationWriteFlag(String sn);
	
}
