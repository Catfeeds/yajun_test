package com.wis.mes.rfid.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alien.enterpriseRFID.reader.AlienClass1Reader;
import com.alien.enterpriseRFID.reader.AlienReaderCommandErrorException;
import com.alien.enterpriseRFID.reader.AlienReaderConnectionException;
import com.alien.enterpriseRFID.reader.AlienReaderTimeoutException;
import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.context.WebContextHolder;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.uloc.service.TmUlocService;
import com.wis.mes.opc.util.OpcPropertiesConfig;
import com.wis.mes.production.producttracing.entity.TbProductTracing;
import com.wis.mes.production.producttracing.service.TbProductTracingService;
import com.wis.mes.rfid.callback.IRfidCallBack;
import com.wis.mes.rfid.service.RfidService;
import com.wis.mes.rfid.util.RfidUtil;

@Controller
@RequestMapping(value = "/rfid")
public class RfidController extends BaseController {
	
	@Autowired
	private RfidService rfidService;
	@Autowired
	private TbProductTracingService productTracingService;
	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	@Autowired
	private TmUlocService tmUlocService;
	
	@RequestMapping(value = "/listInput")
	public ModelAndView equipmentstatusRtListInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
//		modelMap.addAttribute("WEB_SOCKET_KEY","DAIKIN_7");
		return new ModelAndView("rfid/rfid_read_write");
	}
	
	@ResponseBody
	@RequestMapping(value = "/rfidConnectStatus")
	public Map<String,Object> rfidConnectStatus(){
		Map<String,Object> map = new HashMap<String,Object>();
		String ulocNo = globalConfigurationService.getValueByKey(ConstantUtils.RFID_WRITE_ENTRY);
		Map<String,Object> connectStatusMap = rfidService.getRfidConnectStatus(ulocNo);
		if(null != connectStatusMap){
			if(connectStatusMap.containsKey("connectStatus")){
				map.put("rfidStatus",connectStatusMap.get("connectStatus") );
			}
		}
		return map;
	}
	
	/*@ResponseBody
	@RequestMapping(value = "/bePresentOrNot")
	public Map<String,Object> bePresentOrNot(){
		Map<String,Object> map = new HashMap<String,Object>();
		String ulocNo = globalConfigurationService.getValueByKey(ConstantUtils.RFID_WRITE_ENTRY);
		RfidReadVo rfidReadVo = rfidService.plcRfidRead(ulocNo);
		if(StringUtils.isNotBlank(rfidReadVo.getSn())){
			boolean tagStatus = rfidService.validateRfidTag(rfidReadVo.getSn());
			if(!tagStatus){
				rfidService.rfidStationWriteFlag(rfidReadVo.getSn());
				map.put("fruitMsg", "失败：返修状态。");
			}
			map.put("tagStatus", tagStatus);
		}else{
			map.put("fruitMsg", rfidReadVo.getMessage());
		}
		return map;
	}*/
	
	@ResponseBody
	@RequestMapping(value = "/rfidReadWrite")
	public JsonActionResult rfidReadWrite(String sn) throws Exception{
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
			result = rfidService.doRfidReadWrite(sn.toUpperCase());
		}
		return result;
	}
	
	@RequestMapping(value = "/rfidTestInput")
	public ModelAndView rfidTestInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		return new ModelAndView("rfid/rfid_test");
	}
	
	@ResponseBody
	@RequestMapping(value = "/rfidTest")
	public String rfidTest(String ulocNo,String command){
		boolean rfidTestFlag = new Boolean(OpcPropertiesConfig.getPropertyByKey("rfid.test.enabled"));
		String doReaderCommand="";
		if(rfidTestFlag) {
			String lineNo = globalConfigurationService.getValueByKey(ConstantUtils.R5_LINE_NO);
			TmUloc uloc = tmUlocService.getRedisUloc(lineNo,ulocNo);
			AlienClass1Reader reader = null;
			try {
				Object bean = WebContextHolder.getWebAppContext().getBean(uloc.getServiceName());
				if(null != bean) {
					IRfidCallBack callback = (IRfidCallBack)bean;
					reader = callback.createConnection(uloc);
				}
				doReaderCommand = reader.doReaderCommand(RfidUtil.readLine(command));
			}catch (BusinessException e) {
				doReaderCommand = e.getMessage();
			} catch (AlienReaderTimeoutException e) {
				doReaderCommand="RFID连接超时";
			} catch (AlienReaderConnectionException e) {
				doReaderCommand="RFID连接异常";
			} catch (AlienReaderCommandErrorException e) {
				doReaderCommand="RFID命令错误OR程序错误";
			} catch (Exception e) {
				doReaderCommand="未知异常";
			}
		}else {
			doReaderCommand="RFID-TEST功能已关闭。";
		}
		return doReaderCommand;
	}
}
