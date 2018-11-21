package com.wis.mes.rfid.controller;

import org.apache.commons.lang3.StringUtils;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wis.basis.common.controller.BaseController;
import com.wis.mes.production.qualitytracing.service.TbQualityTracingService;
import com.wis.mes.rfid.service.RfidService;
import com.wis.mes.rfid.vo.RfidReadVo;

@Controller
//@RequestMapping(value ="/opcReadWrite")
public class OpcReadWriteController extends BaseController{

	@Autowired 
	private RfidService rfidService;
	@Autowired
	private TbQualityTracingService qualityTracingService;
	
	@RequestMapping(value = "/plcRfidRead")
	public void plcRfidRead(String groupCode,String inOrOut){
		Log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		Log.info("进入方法体：plcRfidRead。");
		if(StringUtils.isNotBlank(groupCode)){
			 String ulocNo = groupCode.replace("STATION_", "");
			 RfidReadVo  rfidReadVo = rfidService.plcRfidRead(ulocNo);
			 if(StringUtils.isNotBlank(rfidReadVo.getSn())){
				 rfidService.stationWrite(groupCode,rfidReadVo,inOrOut);
			 }
		}
		Log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	}
	
	@RequestMapping(value = "/doSaveQualityTracing")
	private void doSaveQualityTracing(String sn,String ulocNo,String infoSources){
		qualityTracingService.doSaveQualityTracing(sn, ulocNo);
	}
}
