package com.wis.mes.report;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.DateUtils;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.utils.SerialNumUtil;
import com.wis.mes.basis.BizBaseTestCase;
import com.wis.mes.dakin.report.service.StationDataAnalysisService;
import com.wis.mes.master.plant.service.TmPlantService;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.uloc.service.TmUlocService;
import com.wis.mes.master.workcalendar.entity.TmWorktime;
import com.wis.mes.master.workcalendar.service.TmWorktimeService;
import com.wis.mes.production.producttracing.entity.TbProductTracing;
import com.wis.mes.production.producttracing.service.TbProductTracingService;
import com.wis.mes.production.qualitytracing.service.TbQualityTracingService;
import com.wis.mes.rfid.service.RfidService;
import com.wis.mes.rfid.vo.SnInfoObjVo;

public class StationDataAnalysisTest extends BizBaseTestCase{

	@Autowired
	 private StationDataAnalysisService stationDataAnalysisService;
	@Autowired
	 private RfidService rfidService;
	@Autowired
	private TbProductTracingService productTracingService;
	@Autowired
	private TmWorktimeService tmWorktimeService;
	@Autowired
	private TmPlantService tmPlantService;
	@Autowired
	private TmUlocService ulocService;
	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	@Autowired
	private TbQualityTracingService qualityTracingService;
	
	@Test
	public void stationDataAnalysisDataTest(){
		/*String lineNo = globalConfigurationService.getValueByKey(ConstantUtils.R5_LINE_NO);
		System.out.println(ulocService.getRedisUloc(lineNo,"7"));*/
		qualityTracingService.doOnSnUpdateQualityTracing("70219E031F00015333221T", "5");
	}
	
	@Test
	public void rfidReadWrite(){
		String sn = "702194690F01644433111A";
		JsonActionResult result = new JsonActionResult();
		boolean productTracingStatus = true;
		TbProductTracing bean =  productTracingService.getTbProductTracingSn(sn.toUpperCase());
		if(null != bean){
			if(StringUtils.isNotBlank(bean.getBeginTime()) && StringUtils.isNotBlank(bean.getFinishTime()) ){
				productTracingStatus = false;
				result.setSuccess(false);
				result.setMessage("制品条码已使用,不能重复写入！");
			}
		}
		if(productTracingStatus){
			try {
				//result = rfidService.rfidReadWrite(sn.toUpperCase(),0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println();
	}
	
	@Test
	public void validateRfidTag(){
		/*String[] arrays = {"702194690F01645733111%","70219A003F01549533111.","702194758F000892331110","70219A004F01100733111N","702194629F00569133111%","702194696F066710331112","702194885F00705333111/","702194696F066711331113","70219E016F00165233121-","702194628F00717033111Z","702194828F00024233111U","702194887F00651333111%","702194699F01123233111.","70219E019F00028233111-","702194759F000669331113","70219A004F01100833111O","702194689F020752331110"};
		for(String src:arrays){
			saveProductTracing(SnInfoObjVo.splitSn(src));
		}*/
		TmUloc uloc = ulocService.findUniqueByEg(TmUloc.createUlocNo("7"));
		saveProductTracing(SnInfoObjVo.splitSn("702194690F0164443311.."),uloc);
		//rfidService.validateRfidTag("702194690F01644433111A");
	}
	
	private TbProductTracing saveProductTracing(SnInfoObjVo snInfoObj,TmUloc uloc) {
		Date date = new Date();
		TmWorktime tmWorktime = tmWorktimeService.thisWorkTime(uloc.getTmLineId());
		TbProductTracing bean = new TbProductTracing();
		if (StringUtils.isNotBlank(snInfoObj.getNewSn())) {
			String beginTime = DateUtils.formatFull(date);
			bean.setSn(snInfoObj.getNewSn());
			bean.setMachineName(snInfoObj.getMachineName());
			bean.setBackNumber(snInfoObj.getBackNumber());
			bean.setMachineOfName(productTracingService.getEgModelName(snInfoObj.getBackNumber()));
			bean.setBeginTime(beginTime.substring(11, beginTime.length()));
			bean.setCreateTime(date);
			bean.setLineNo(snInfoObj.getPlantNo());
			bean.setTmPlantId(uloc.getTmPlantId());//事部ID
			bean.setTmLineId(uloc.getTmLineId());// 产线ID
			if (null != tmWorktime) {
				bean.setTmWorktimeId(tmWorktime.getId());// 工作日历ID
				bean.setTmClassManagerId(tmWorktime.getTmClassManagerId());//班组
			}
			/*bean = productTracingService.doSave(bean);*/
		}
		return bean;

	}
}
