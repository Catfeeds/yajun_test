package com.wis.mes.webservice;

/**
 * 接口定义
 * 
 * @WebService用于定义webservice对外开放的接口 Created by ht on 2017/1/20.
 */
public interface ScanningGunService {

	/**
	 * 获取序列号
	 * **/
	void acceptBarCode(String code);

	/**
	 * @author yajun_ren
	 * @status 在席状态
	 * @writeFlag 写入状态
	 * @plcStatus plc连接状态 托盘在席信号
	 */
	void atTheTableSignal(boolean status, boolean writeFlag, boolean plcStatus);

	void plcRfidRead(String groupCode, String inOrOut);
	
	void station62Queuerecord(String groupCode,String plcSn);
	
	/**
	 * @author yajun_ren
	 * @desc 清空标签信息
	 * @date 2018/09/10
	 */
	void station120ClearTag(String groupCode);
}
