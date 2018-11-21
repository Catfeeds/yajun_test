package com.wis.mes.production.producttracing.service;

import com.wis.core.service.BaseService;
import com.wis.mes.production.producttracing.entity.TbProductTracing;

public interface TbProductTracingService extends BaseService<TbProductTracing> {
	/**
	 * @backNumber 背番号
	 * @machineName 机号
	 * 
	 *              根据背番号和机号获取机种名
	 **/
	String getEgModelName(String backNumber);

	/**
	 * 
	 * 跟新读写标志位，1为PLC可读，读取完毕PLC将此位改为0，则数据库可以写入
	 **/
	void doUpdatePlcRfidStatus(String sn, String readWriteFlag);

	/**
	 * @type 根据类型查询对应的读写标识位
	 **/
	String readWriteFlag(String type);

	void updateFinshTime(String sn);

	/***
	 * @sn
	 * @author yajun_ren 根据SN获取产品数据/ return TbProductTracing
	 **/
	TbProductTracing getTbProductTracingSn(String sn);
	
	/**
	 * @author yajun_ren
	 * @sn
	 * 更新从NG出口出来的次数
	 * **/
	void updateUnhealthyCount(String sn);
}
