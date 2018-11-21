package com.wis.mes.opc.service;

import com.wis.basis.common.web.model.JsonActionResult;

public interface OpcService {

	/**
	 * 七工位写
	 * 
	 * @param SN
	 * @return
	 */
	JsonActionResult rfidStationWrite(String SN);

	/**
	 * 根据groupCode 写SN
	 * 
	 * @param groupCode
	 * @param SN
	 * @return
	 */
	JsonActionResult stationWrite(String groupCode, String SN);

	/**
	 * 工位NG 入口
	 * 
	 * @param groupCode
	 * @param SN
	 * @param repairDone
	 */
	JsonActionResult stationNgIn(String groupCode, String SN, boolean repairDone);

	/**
	 * 工位NG出
	 * 
	 * @param groupCode
	 * @param SN
	 */
	JsonActionResult stationNgOut(String groupCode, String SN);

	void plcRfidRead(String groupCode, String inOrOut);

	/**
	 * 通过工位编号获取 该工位上的SN
	 * 
	 * @param ulocNo
	 * @return
	 */
	String getSnByUlocNo(String ulocNo);

	/**
	 * 写入该工位的SN
	 * 
	 * @param ulocNo
	 * @param SN
	 */
	void writeByUlocAndSN(String ulocNo, String SN);
	
	/**
	 * 7工位写入标识
	 * **/
	void atTheTableSignal(boolean status, boolean writeFlag, boolean plcStatus);
	
	void station62Queuerecord(String groupCode,String plcSn);
	
	/**
	 * 清空标签USER区信息
	 * **/
	void station120ClearTag(String groupCode);
	

}
