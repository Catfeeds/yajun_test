package com.wis.mes.production.regulate.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.GlobalConfigurationUtils;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.workcalendar.entity.TmWorktime;
import com.wis.mes.production.plan.porder.entity.ToPorderAvi;
import com.wis.mes.production.record.entity.TpRecordUloc;
import com.wis.mes.production.regulate.service.CrossPointService;
import com.wis.mes.production.regulate.service.OnlineService;
import com.wis.mes.production.wip.entity.TpWip;
import com.wis.mes.production.wip.service.TpWipService;
import com.wis.mes.util.StringUtil;

/**
 * 
 * @company:上海西信
 *
 * @ClassName: CrossPointController
 * 
 * @author liuzejun
 *
 * @date 2017年6月27日 下午4:23:21
 * 
 * @Description: 过点Controller
 */
@Controller
@RequestMapping(value = "/crossPoint")
public class CrossPointController extends BaseController {

	@Autowired
	private CrossPointService crossPointService;
	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	@Autowired
	private TmLineService lineService;
	@Autowired
	private OnlineService onlineService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TpWipService tpWipService;

	@RequestMapping("/listInput")
	public ModelAndView listInput(Integer id, ModelMap modelMap) {
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, id);
		modelMap.put("isAutoOnline",
				globalConfigurationService.getValueByKey(GlobalConfigurationUtils.GLOBAL_CONFIGUR_IS_ONLINE_AUTO));
		return new ModelAndView("/production-control/crossPoint/crossPoint_list");
	}

	/**
	 * 初始化页面数据
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/initList")
	public JsonActionResult initList(Integer tmLineId) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		if (tmLineId == null) {
			List<TmLine> linePermissonData = lineService.findAll();
			tmLineId = linePermissonData.size() == 0 ? null : linePermissonData.get(0).getId();
			//产线数据
			returnMap.put("lineData", linePermissonData);
		}
		List<TmUloc> ulocPermissonData = crossPointService.getCrossPointUlocByLineId(tmLineId);
		TmUloc uloc = ulocPermissonData.size() == 0 ? null : ulocPermissonData.get(0);
		//工位数据
		returnMap.put("ulocData", ulocPermissonData);
		TmWorktime workTime = onlineService.getNowWorkTimeByLineId(tmLineId);
		if (uloc != null) {
			returnMap.put("wip", crossPointService.getWaitCrossSNByUlocId(uloc.getId()));
			returnMap.put("qty", onlineService.getCrossQty(workTime, uloc.getId()));
			//已上线数据
			returnMap.put("onLineData", getCrossQueue(uloc.getId()));
		}
		//班次数据
		returnMap.put("shiftNo", workTime == null ? ""
				: entryService.getEntryByCode(ConstantUtils.SHIFT_TYPE, workTime.getShiftno()).getName());
		return ActionUtils.handleResult(returnMap);
	}

	/**
	 * 根据工位ID查询页面数据
	 * 
	 * @param tmUlocId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getListDataByUlocId")
	public JsonActionResult getListDataByUlocId(Integer tmUlocId) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		TmUloc uloc = onlineService.findUlocById(tmUlocId);
		TmWorktime workTime = onlineService.getNowWorkTimeByLineId(uloc.getTmLineId());
		returnMap.put("wip", crossPointService.getWaitCrossSNByUlocId(uloc.getId()));
		returnMap.put("qty", onlineService.getCrossQty(workTime, uloc.getId()));
		//已上线数据
		returnMap.put("onLineData", getCrossQueue(uloc.getId()));
		//班次数据
		returnMap.put("shiftNo", workTime == null ? ""
				: entryService.getEntryByCode(ConstantUtils.SHIFT_TYPE, workTime.getShiftno()).getName());
		return ActionUtils.handleResult(returnMap);
	}

	private List<TpRecordUloc> getCrossQueue(Integer tmUlocId) {
		List<TpRecordUloc> alreadyOnlineData = onlineService.getAlreadyOnlineData(tmUlocId);
		if (alreadyOnlineData != null && alreadyOnlineData.size() > 0) {
			for (TpRecordUloc bean : alreadyOnlineData) {
				if (StringUtil.isNotBlank(bean.getNextUloc())) {
					String[] nextUloc = bean.getNextUloc().split(",");
					StringBuffer buffer = new StringBuffer();
					for (String id : nextUloc) {
						TmUloc uloc = onlineService.findUlocById(Integer.valueOf(id.trim()));
						buffer.append(uloc.getNo()).append(",");
					}
					if (buffer.length() > 0) {
						buffer.deleteCharAt(buffer.length() - 1);
					}
					bean.setNextUloc("[" + buffer + "]");
				}
			}
		}
		return alreadyOnlineData;
	}

	/**
	 * 扫描SN
	 * 
	 * @param tmUlocId
	 * @param SN
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/scanSN")
	public JsonActionResult scanSN(Integer tmUlocId, String SN) {
		Map<String, Object> scanSN = crossPointService.doScanSN(tmUlocId, SN);
		return ActionUtils.handleResult(scanSN);
	}

	/**
	 * 校验SN
	 * 
	 * @param tmUlocId
	 * @param SN
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkSN")
	public JsonActionResult checkSN(Integer tmUlocId, String SN) {
		Map<String, Object> checkSN = crossPointService.checkSN(SN, tmUlocId, false);
		JSONObject obj = new JSONObject();
		ToPorderAvi avi = (ToPorderAvi) checkSN.get("avi");
		obj.put("aviId", avi.getId());
		obj.put("currentUlocSeq", checkSN.get("currentUlocSeq"));
		return ActionUtils.handleResult(obj);
	}

	/**
	 * 确认上线
	 * 
	 * @param toPorderAviId
	 * @param tmUlocId
	 * @param SN
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/confirmCrossPoint")
	public JsonActionResult confirmCrossPoint(Integer toPorderAviId, Integer tmUlocId, String SN) {
		Map<String, Object> doConfirmCrossPoint = crossPointService.doConfirmCrossPoint(toPorderAviId, tmUlocId, SN);
		return ActionUtils.handleResult(doConfirmCrossPoint);
	}
	
	@ResponseBody
	@RequestMapping("/holdCheckSn")
	public JsonActionResult holdCheckSn(String SN,Integer tmUlocId){
		Map<String, Object> obj = crossPointService.holdCheckSn(SN, tmUlocId);
		return ActionUtils.handleResult(obj);
	}
	
	@ResponseBody
	@RequestMapping(value = "/doCancelHold")
	public JsonActionResult doCancelHold(HttpServletResponse response, Integer id) {
		TpWip tpWip = tpWipService.findById(id);
		tpWip.setIsHold(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO);
		tpWip.setNote("");
		tpWipService.doUpdate(tpWip);
		return ActionUtils.handleResult(response);
	}
	
	@ResponseBody
	@RequestMapping(value = "/doHoldInput")
	public ModelAndView doHoldInput(ModelMap modelMap, Integer id) {
		modelMap.addAttribute("id", id);
		return new ModelAndView("/production-control/crossPoint/porder_hold");
	}
	
	@ResponseBody
	@RequestMapping(value = "/doHold")
	public JsonActionResult doHold(HttpServletResponse response, Integer id, String note) {
		TpWip tpWip = tpWipService.findById(id);
		tpWip.setIsHold(ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_YES);
		tpWip.setNote(note);
		tpWipService.doUpdate(tpWip);
		return ActionUtils.handleResult(response);
	}
}
