package com.wis.mes.production.regulate.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.DateUtils;
import com.wis.basis.common.utils.GlobalConfigurationUtils;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.master.nc.entity.TmNc;
import com.wis.mes.master.nc.entity.TmNcGroup;
import com.wis.mes.master.nc.service.TmNcGroupService;
import com.wis.mes.master.nc.service.TmNcService;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.uloc.entity.TmUlocSip;
import com.wis.mes.master.uloc.entity.TmUlocSipNc;
import com.wis.mes.master.uloc.service.TmUlocSipNcService;
import com.wis.mes.master.workcalendar.entity.TmWorktime;
import com.wis.mes.production.plan.porder.entity.ToPorder;
import com.wis.mes.production.plan.porder.entity.ToPorderAviBom;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPathParameter;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPathSip;
import com.wis.mes.production.plan.porder.service.ToPorderService;
import com.wis.mes.production.record.entity.TpRecord;
import com.wis.mes.production.record.entity.TpRecordUloc;
import com.wis.mes.production.record.entity.TpRecordUlocPart;
import com.wis.mes.production.record.service.TpRecordService;
import com.wis.mes.production.record.service.TpRecordUlocNcService;
import com.wis.mes.production.record.service.TpRecordUlocParameterService;
import com.wis.mes.production.record.service.TpRecordUlocPartService;
import com.wis.mes.production.record.service.TpRecordUlocQualityService;
import com.wis.mes.production.record.service.TpRecordUlocService;
import com.wis.mes.production.regulate.service.OnlineService;
import com.wis.mes.production.untreated.service.TpUntreatedNcService;
import com.wis.mes.production.wip.entity.TpWip;
import com.wis.mes.production.wip.service.TpWipService;
import com.wis.mes.util.StringUtil;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/onLine")
public class OnlineController extends BaseController {

	@Autowired
	private OnlineService onlineService;
	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	@Autowired
	private TmLineService lineService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private ToPorderService porderService;
	@Autowired
	private TpRecordUlocService recordUlocService;
	@Autowired
	private TpRecordService recordService;
	@Autowired
	private TpRecordUlocParameterService recordUlocParameterService;
	@Autowired
	private TpRecordUlocQualityService recordUlocQualityService;
	@Autowired
	private TmNcGroupService ncGroupService;
	@Autowired
	private TmNcService ncService;
	@Autowired
	private TpRecordUlocNcService recordUlocNcService;
	@Autowired
	private TpWipService wipService;
	@Autowired
	private TpUntreatedNcService untreatedNcService;
	@Autowired
	private TmUlocSipNcService ulocSipNcService;
	@Autowired
	private TpRecordUlocPartService recordUlocPartService;

	/**
	 * 上线页面
	 * 
	 * @param modelMap
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/listInput")
	public ModelAndView listInput(ModelMap modelMap, Integer id) {
		modelMap.addAttribute(ConstantUtils.REQUEST_CURRENT_PAGE_ID, id);
		modelMap.put("isAutoOnline",
				globalConfigurationService.getValueByKey(GlobalConfigurationUtils.GLOBAL_CONFIGUR_IS_ONLINE_AUTO));
		return new ModelAndView("/production-control/online/online_list");
	}

	/**
	 * 页面数据初始化
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/initList")
	public JsonActionResult initList() {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<TmLine> linePermissonData = lineService.findAll();
		TmLine line = linePermissonData.size() == 0 ? null : linePermissonData.get(0);
		//产线数据
		modelMap.put("lineData", linePermissonData);
		List<TmUloc> ulocPermissonData = onlineService.getOnlineUlocByLineId(line == null ? null : line.getId());
		TmUloc uloc = ulocPermissonData.size() == 0 ? null : ulocPermissonData.get(0);
		//工位数据
		modelMap.put("ulocData", ulocPermissonData);
		List<ToPorder> porder = onlineService.getPorderByUlocId(uloc == null ? null : uloc.getId());
		//工单数据
		modelMap.put("porderData", porder);
		//生产序列数据
		if (porder != null && porder.size() > 0) {
			modelMap.put("aviData",
					onlineService.getAviByPorderId("'FINSH','CLOSE','ALL_ONLINE'", porder.get(0).getId()));
		}
		TmWorktime workTime = onlineService.getNowWorkTimeByLineId(line == null ? null : line.getId());
		//班次数据
		modelMap.put("shiftNo", workTime == null ? ""
				: entryService.getEntryByCode(ConstantUtils.SHIFT_TYPE, workTime.getShiftno()).getName());
		//合格数、不合格数
		modelMap.put("qty", onlineService.getCrossQty(workTime, (uloc == null ? null : uloc.getId())));
		//已上线数据
		modelMap.put("onLineData", getCrossQueue(uloc == null ? null : uloc.getId()));
		return ActionUtils.handleResult(modelMap);
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
	 * 根据产线查询工位数据
	 * 
	 * @param tmLineId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getUlocDataByLineId")
	public JsonActionResult getUlocDataByLineId(Integer tmLineId) {
		List<TmUloc> ulocData = onlineService.getOnlineUlocByLineId(tmLineId);
		return ActionUtils.handleResult(ulocData);
	}

	/**
	 * 根据产线、工位查询上线页面数据
	 * 
	 * @param tmLineId
	 * @param tmUlocId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getOnlineListDataByLineAndUloc")
	public JsonActionResult getOnlineListDataByLineAndUloc(Integer tmLineId, Integer tmUlocId) {
		TmUloc uloc = onlineService.findUlocById(tmUlocId);
		Map<String, Object> map = new HashMap<String, Object>();
		List<ToPorder> porder = onlineService.getPorderByUlocId(tmUlocId);
		//工单数据
		map.put("porderData", porder);
		//生产序列数据
		if (porder != null && porder.size() > 0) {
			map.put("aviData", onlineService.getAviByPorderId("'FINSH','CLOSE','ALL_ONLINE'", porder.get(0).getId()));
		}
		TmWorktime workTime = onlineService.getNowWorkTimeByLineId(tmLineId);
		//班次数据
		map.put("shiftNo", workTime == null ? ""
				: entryService.getEntryByCode(ConstantUtils.SHIFT_TYPE, workTime.getShiftno()).getName());
		//合格数、不合格数
		map.put("qty", onlineService.getCrossQty(workTime, uloc.getId()));
		/*		//待上线SN
				if (porder != null && porder.size() > 0) {
					map.put("waitSN", onlineService.getWaitOnlineSn(porder.get(0).getNo(), null));
				}*/
		//已上线数据
		map.put("onLineData", getCrossQueue(uloc.getId()));
		return ActionUtils.handleResult(map);
	}

	/**
	 * 根据工单、产线、工位查询页面数据
	 * 
	 * @param toPorderId
	 * @param tmLineId
	 * @param tmUlocId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/changedPorderSelect")
	public JsonActionResult changedPorderSelect(Integer toPorderId, Integer tmLineId, Integer tmUlocId) {
		ToPorder porder = porderService.findById(toPorderId, true);
		TmUloc uloc = onlineService.findUlocById(tmUlocId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("porder", porder);
		//生产序列数据
		map.put("aviData", onlineService.getAviByPorderId("'FINSH','CLOSE','ALL_ONLINE'", toPorderId));
		TmWorktime workTime = onlineService.getNowWorkTimeByLineId(tmLineId);
		//班次数据
		map.put("shiftNo", workTime == null ? ""
				: entryService.getEntryByCode(ConstantUtils.SHIFT_TYPE, workTime.getShiftno()).getName());
		//合格数、不合格数
		map.put("qty", onlineService.getCrossQty(workTime, uloc.getId()));
		//已上线数据
		map.put("onLineData", getCrossQueue(uloc.getId()));
		return ActionUtils.handleResult(map);
	}

	@ResponseBody
	@RequestMapping("/getWaitOnlineSn")
	public JsonActionResult getWaitOnlineSn(Integer aviId, Integer ulocId) {
		return ActionUtils.handleResult(onlineService.getWaitOnlineSn(aviId, ulocId));
	}

	/**
	 * 生成SN
	 * 
	 * @param toPorderAviId
	 * @param tmUlocId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/generateSN")
	public JsonActionResult generateSN(Integer toPorderAviId, Integer tmUlocId) {
		String sn = onlineService.doGenerateSN(toPorderAviId, tmUlocId);
		return ActionUtils.handleResult(sn);
	}

	/**
	 * 确认上线
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/confirmOnline")
	public JsonActionResult confirmOnline(Integer toPorderAviId, Integer tmUlocId, String SN) {
		Map<String, Object> doConfirmOnline = onlineService.doConfirmOnline(toPorderAviId, tmUlocId, SN);
		return ActionUtils.handleResult(doConfirmOnline);
	}

	/**
	 * 扫描SN
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/scanSN")
	public JsonActionResult scanSN(HttpServletRequest request, Integer toPorderAviId, Integer tmUlocId, String SN) {
		Map<String, Object> doScanSN = onlineService.doScanSN(toPorderAviId, tmUlocId, SN);
		if (doScanSN == null) {
			return ActionUtils.handleResult(true, getMessage(request, "ONLINE_NOT_HAVE_BIND"));
		}
		return ActionUtils.handleResult(doScanSN);
	}

	/**
	 * 绑定参数页面展示
	 * 
	 * @param modelMap
	 * @param data
	 * @param SN
	 * @param tmUlocId
	 * @param toPorderAviId
	 * @param currentUlocSeq
	 * @return
	 */
	@RequestMapping("/onlineNeedBindParameter")
	public ModelAndView onlineNeedBindParameter(ModelMap modelMap, String data, String SN, Integer tmUlocId,
			Integer toPorderAviId, String currentUlocSeq) {
		bindDataToList(modelMap, SN, tmUlocId, toPorderAviId, currentUlocSeq,
				ConstantUtils.PRODUCT_OPERTION_TYPE_BIND_PARAMETER_START);
		try {
			if (data == null) {
				List<ToPorderAviPathParameter> needBindParameter = onlineService.getNeedBindParameter(SN, toPorderAviId,
						tmUlocId, currentUlocSeq);
				modelMap.addAttribute("parameter", needBindParameter);
			} else {
				JSONArray array = JSONArray.fromObject(data);
				@SuppressWarnings("unchecked")
				List<ToPorderAviPathParameter> needBindParameter = (List<ToPorderAviPathParameter>) JSONArray
						.toCollection(array, ToPorderAviPathParameter.class);
				modelMap.addAttribute("parameter", needBindParameter);
			}
		} catch (Exception e) {
			List<ToPorderAviPathParameter> needBindParameter = onlineService.getNeedBindParameter(SN, toPorderAviId,
					tmUlocId, currentUlocSeq);
			modelMap.addAttribute("parameter", needBindParameter);
		}
		return new ModelAndView("/production-control/online/online_bind_parameter");
	}

	/**
	 * 绑定参数返回按钮
	 * 
	 * @param response
	 * @param recordId
	 * @param shiftNo
	 * @param tmUlocId
	 * @param currentUlocSeq
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/bindParameterGoBack")
	public JsonActionResult bindParameterGoBack(HttpServletResponse response, Integer recordId, String shiftNo,
			Integer tmUlocId, String currentUlocSeq) {
		TmUloc uloc = onlineService.findUlocById(tmUlocId);
		recordUlocService.doSaveTpRecordUloc(recordId, tmUlocId, uloc.getNo(), uloc.getName(), "", shiftNo,
				ConstantUtils.PRODUCT_OPERTION_TYPE_BIND_PARAMETER_END, currentUlocSeq, "点击返回!");
		return ActionUtils.handleResult(response);
	}

	/**
	 * 绑定参数确定按钮
	 * 
	 * @param response
	 * @param recordId
	 * @param tmUlocId
	 * @param shiftNo
	 * @param currentUlocSeq
	 * @param parameterCode
	 * @param parameterValue
	 * @param note
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/bindParameter")
	public JsonActionResult bindParameter(HttpServletResponse response, Integer recordId, Integer tmUlocId,
			String shiftNo, String currentUlocSeq, String[] parameterCode, String[] parameterValue, String[] note) {
		TmUloc uloc = onlineService.findUlocById(tmUlocId);
		TpRecordUloc doSaveTpRecordUloc = recordUlocService.doSaveTpRecordUloc(recordId, tmUlocId, uloc.getNo(),
				uloc.getName(), "", shiftNo, ConstantUtils.PRODUCT_OPERTION_TYPE_BIND_PARAMETER_END, currentUlocSeq,
				"点击确定!");
		recordUlocParameterService.doSaveRecordUlocParameter(parameterCode, ConstantUtils.PRODUCT_PARAMETER,
				parameterValue, doSaveTpRecordUloc.getId(), note, recordId, ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO);
		return ActionUtils.handleResult(response);
	}

	/**
	 * 绑定关键件
	 * 
	 * @param modelMap
	 * @param data
	 * @param SN
	 * @param tmUlocId
	 * @param toPorderAviId
	 * @param currentUlocSeq
	 * @return
	 */
	@RequestMapping("/onlineNeedBindPart")
	public ModelAndView onlineNeedBindPart(ModelMap modelMap, String data, String SN, Integer tmUlocId,
			Integer toPorderAviId, String currentUlocSeq) {
		bindDataToList(modelMap, SN, tmUlocId, toPorderAviId, currentUlocSeq,
				ConstantUtils.PRODUCT_OPERTION_TYPE_BIND_PART_START);
		/*		try {
					JSONArray array = JSONArray.fromObject(data);
					@SuppressWarnings("unchecked")
					List<ToPorderAviBom> needBindBom = (List<ToPorderAviBom>) JSONArray.toCollection(array,
							ToPorderAviBom.class);
					modelMap.addAttribute("part", needBindBom);
				} catch (Exception e) {
					List<ToPorderAviBom> needBindPart = onlineService.getNeedBindPart(SN, toPorderAviId, tmUlocId,
							currentUlocSeq);
					modelMap.addAttribute("part", needBindPart);
				}*/
		return new ModelAndView("/production-control/online/online_bind_part");
	}

	/**
	 * 获取需要绑定哪些产品
	 * 
	 * @param SN
	 * @param toPorderAviId
	 * @param tmUlocId
	 * @param currentUlocSeq
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getNeedAndAreadyBindPart")
	public Map<String, Object> getNeedBindPart(String SN, Integer toPorderAviId, Integer tmUlocId,
			String currentUlocSeq) {
		List<TpRecordUlocPart> ulocAlreadyBindPart = recordUlocPartService.getUlocAlreadyBindPart(SN, tmUlocId,
				currentUlocSeq);
		List<ToPorderAviBom> needBindPart = onlineService.getNeedBindPart(SN, toPorderAviId, tmUlocId, currentUlocSeq);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("needBind", needBindPart);
		returnMap.put("alreadyBind", ulocAlreadyBindPart);
		return returnMap;
	}

	/**
	 * 扫描物料条码
	 * 
	 * @param response
	 * @param SN
	 * @param toPorderAviId
	 * @param tmUlocId
	 * @param currentUlocSeq
	 * @param partBarCode
	 * @param recordId
	 * @param tpRecordUlocId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/scanPartBarCode")
	public JsonActionResult scanPartBarCode(HttpServletResponse response, String SN, Integer toPorderAviId,
			Integer tmUlocId, String currentUlocSeq, String partBarCode, Integer recordId, Integer tpRecordUlocId) {
		TmUloc uloc = onlineService.findUlocById(tmUlocId);
		List<ToPorderAviBom> needBindPart = onlineService.getNeedBindPart(SN, toPorderAviId, tmUlocId, currentUlocSeq);
		Map<String, String> partMap = onlineService.checkPartBarCode(needBindPart, uloc.getTmPlantId(),
				uloc.getTmWorkshopId(), uloc.getTmLineId(), partBarCode);
		recordUlocPartService.doSaveTpRecordUlocPart(recordId, tpRecordUlocId, Integer.valueOf(partMap.get("tmPartId")),
				1, partBarCode, partMap.get("partNo"), partMap.get("partName"),
				ConstantUtils.ENTRY_CODE_CONFIRM_STATUS_NO, null);
		return ActionUtils.handleResult(response);
	}

	/**
	 * 取消绑定条码
	 * 
	 * @param response
	 * @param tpRecordUlocPartId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/cancelBindPart")
	public JsonActionResult cancelBindPart(HttpServletResponse response, Integer tpRecordUlocPartId) {
		recordUlocPartService.doDeleteById(tpRecordUlocPartId);
		return ActionUtils.handleResult(response);
	}

	/**
	 * 绑定物料条码点击取消
	 * 
	 * @param response
	 * @param tpRecordUlocPartId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/bindPartGoBack")
	public JsonActionResult bindPartGoBack(HttpServletResponse response, Integer recordId, String shiftNo,
			Integer tmUlocId, String currentUlocSeq) {
		TmUloc uloc = onlineService.findUlocById(tmUlocId);
		recordUlocService.doSaveTpRecordUloc(recordId, tmUlocId, uloc.getNo(), uloc.getName(), "", shiftNo,
				ConstantUtils.PRODUCT_OPERTION_TYPE_BIND_PART_END, currentUlocSeq, "点击返回!");
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping("/bindPartEnd")
	public JsonActionResult bindPartEnd(HttpServletResponse response, Integer recordId, String shiftNo,
			Integer tmUlocId, String currentUlocSeq) {
		TmUloc uloc = onlineService.findUlocById(tmUlocId);
		recordUlocService.doSaveTpRecordUloc(recordId, tmUlocId, uloc.getNo(), uloc.getName(), "", shiftNo,
				ConstantUtils.PRODUCT_OPERTION_TYPE_BIND_PART_END, currentUlocSeq, "点击返回!");
		return ActionUtils.handleResult(response);

	}

	private void bindDataToList(ModelMap modelMap, String SN, Integer tmUlocId, Integer toPorderAviId,
			String currentUlocSeq, String operationType) {
		TpRecord record = recordService.getTpRecordBySN(SN);
		TmUloc uloc = onlineService.findUlocById(tmUlocId);
		TmWorktime worktime = onlineService.getNowWorkTimeByLineId(uloc.getTmLineId());
		TpRecordUloc recordUloc = recordUlocService.doSaveTpRecordUloc(record.getId(), tmUlocId, uloc.getNo(),
				uloc.getName(), "", worktime == null ? "" : worktime.getShiftno(), operationType, currentUlocSeq, "");
		modelMap.addAttribute("recordId", record.getId());
		modelMap.addAttribute("shiftNo", recordUloc.getShiftNo());
		modelMap.addAttribute("tmUlocId", tmUlocId);
		modelMap.addAttribute("currentUlocSeq", currentUlocSeq);
		modelMap.addAttribute("toPorderAviId", toPorderAviId);
		modelMap.addAttribute("tpRecordUlocId", recordUloc.getId());
		modelMap.addAttribute("SN", SN);
	}

	/**
	 * 记录质量信息
	 * 
	 * @param modelMap
	 * @param data
	 * @param SN
	 * @param tmUlocId
	 * @param toPorderAviId
	 * @param currentUlocSeq
	 * @return
	 */
	@RequestMapping("/onlineNeedBindQuality")
	public ModelAndView onlineNeedBindQuality(ModelMap modelMap, String data, String SN, Integer tmUlocId,
			Integer toPorderAviId, String currentUlocSeq) {
		bindDataToList(modelMap, SN, tmUlocId, toPorderAviId, currentUlocSeq,
				ConstantUtils.PRODUCT_OPERTION_TYPE_RECORD_QUALITY_START);
		try {
			if (data == null) {
				List<ToPorderAviPathSip> needBindQuality = onlineService.getNeedBindQuality(SN, toPorderAviId, tmUlocId,
						currentUlocSeq);
				modelMap.addAttribute("quality", needBindQuality);
			} else {
				JSONArray array = JSONArray.fromObject(data);
				@SuppressWarnings("unchecked")
				List<ToPorderAviPathSip> needBindQuality = (List<ToPorderAviPathSip>) JSONArray.toCollection(array,
						ToPorderAviPathSip.class);
				modelMap.addAttribute("quality", needBindQuality);
			}
		} catch (Exception e) {
			List<ToPorderAviPathSip> needBindQuality = onlineService.getNeedBindQuality(SN, toPorderAviId, tmUlocId,
					currentUlocSeq);
			modelMap.addAttribute("quality", needBindQuality);
		}
		return new ModelAndView("/production-control/online/online_bind_quality");
	}

	@ResponseBody
	@RequestMapping("/recordQuality")
	public JsonActionResult recordQuality(HttpServletResponse response, Integer recordId, Integer tmUlocId,
			String shiftNo, String currentUlocSeq, String[] qualityCode, String[] checkResult, String[] qualityNote) {
		TmUloc uloc = onlineService.findUlocById(tmUlocId);
		TpRecordUloc recordUloc = recordUlocService.doSaveTpRecordUloc(recordId, tmUlocId, uloc.getNo(), uloc.getName(),
				"", shiftNo, ConstantUtils.PRODUCT_OPERTION_TYPE_RECORD_QUALITY_END, currentUlocSeq, "点击确定!");
		recordUlocQualityService.doSaveTpRecordUlocQuality(recordUloc.getId(), qualityCode, checkResult, qualityNote,
				recordId);
		return ActionUtils.handleResult(response);
	}

	/**
	 * 记录质量信息返回按钮
	 * 
	 * @param response
	 * @param recordId
	 * @param shiftNo
	 * @param tmUlocId
	 * @param currentUlocSeq
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/recordQualityGoBack")
	public JsonActionResult recordQualityGoBack(HttpServletResponse response, Integer recordId, String shiftNo,
			Integer tmUlocId, String currentUlocSeq) {
		TmUloc uloc = onlineService.findUlocById(tmUlocId);
		recordUlocService.doSaveTpRecordUloc(recordId, tmUlocId, uloc.getNo(), uloc.getName(), "", shiftNo,
				ConstantUtils.PRODUCT_OPERTION_TYPE_RECORD_QUALITY_END, currentUlocSeq, "点击返回!");
		return ActionUtils.handleResult(response);
	}

	/**
	 * 记录不合格页面
	 * 
	 * @param modelMap
	 * @param toPorderAviId
	 * @param tmUlocId
	 * @param SN
	 * @return
	 */
	@RequestMapping("/recordNC")
	public ModelAndView recordNC(ModelMap modelMap, Integer toPorderAviId, String currentUlocSeq, Integer tmUlocId,
			String SN) {
		bindDataToList(modelMap, SN, tmUlocId, toPorderAviId, currentUlocSeq,
				ConstantUtils.PRODUCT_OPERTION_TYPE_RECORD_NC_START);
		List<TmNcGroup> findAll = ncGroupService.findAll();
		modelMap.addAttribute("ncGroup", findAll);
		modelMap.addAttribute("ncType", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_NC_TYPE));
		return new ModelAndView("/production-control/online/online_record_nc");
	}

	/**
	 * 通过 ncGroup查找 nc
	 * 
	 * @param tmNcGroupId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getNcData")
	public List<TmNc> getNcData(Integer tmNcGroupId) {
		TmNc eg = new TmNc();
		eg.setTmNcGroupId(tmNcGroupId);
		return ncService.findByEg(eg);
	}

	@ResponseBody
	@RequestMapping("/getNcDataFromUlocNc")
	public List<TmUlocSipNc> getNcDataFromUlocNc(Integer tmNcGroupId, Integer qualityId, Integer ulocId) {
		QueryCriteria criteria = new QueryCriteria();
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.getQueryCondition().put("tmUlocSip.tmUlocId", ulocId.toString());
		criteria.getQueryCondition().put("tmUlocSip.tsParameterId", qualityId.toString());
		criteria.getQueryCondition().put("TmNcGroupId", tmNcGroupId.toString());
		return ulocSipNcService.findBy(criteria).getContent();
	}

	/**
	 * 记录nc返回按钮
	 * 
	 * @param response
	 * @param recordId
	 * @param shiftNo
	 * @param tmUlocId
	 * @param currentUlocSeq
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/recordNcGoBack")
	public JsonActionResult recordNcGoBack(HttpServletResponse response, Integer recordId, String shiftNo,
			Integer tmUlocId, String currentUlocSeq) {
		TmUloc uloc = onlineService.findUlocById(tmUlocId);
		recordUlocService.doSaveTpRecordUloc(recordId, tmUlocId, uloc.getNo(), uloc.getName(), "", shiftNo,
				ConstantUtils.PRODUCT_OPERTION_TYPE_RECORD_NC_END, currentUlocSeq, "点击返回!");
		return ActionUtils.handleResult(response);
	}

	/**
	 * 记录nc确认按钮
	 * 
	 * @param response
	 * @param recordId
	 * @param shiftNo
	 * @param tmUlocId
	 * @param currentUlocSeq
	 * @param ncCode
	 * @param ncGroup
	 * @param note
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/recordNcAndGroup")
	public JsonActionResult recordNcAndGroup(HttpServletResponse response, Integer recordId, String shiftNo,
			Integer tmUlocId, String currentUlocSeq, Integer ncCode, Integer ncGroup, String note, String ncTypeInput,
			String SN) {
		TmUloc uloc = onlineService.findUlocById(tmUlocId);
		TpRecordUloc recordUloc = recordUlocService.doSaveTpRecordUloc(recordId, tmUlocId, uloc.getNo(), uloc.getName(),
				"", shiftNo, ConstantUtils.PRODUCT_OPERTION_TYPE_RECORD_NC_END, currentUlocSeq, "点击确认!");
		recordUlocNcService.doSaveNc(ncCode, ncGroup, note, recordUloc.getId(), recordId);
		TpWip wip = wipService.getTpWip(SN, tmUlocId, currentUlocSeq);
		if (ConstantUtils.ENTRY_NC_TYPE_REPAIRED.equals(ncTypeInput)) {
			wip.setStatus(ConstantUtils.PRODUCT_WIP_STATUS_WAIT_REPAIR);
			wipService.doUpdate(wip);
		}
		untreatedNcService.doSaveUnTratedNc(SN, ncGroup, ncCode, ncTypeInput, note, tmUlocId, currentUlocSeq);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping("/checkSN")
	public JsonActionResult checkSN(HttpServletResponse response, Integer toPorderAviId, Integer tmUlocId, String SN) {
		Map<String, String> doCheckSN = onlineService.doCheckSN(toPorderAviId, tmUlocId, SN);
		return ActionUtils.handleResult(doCheckSN);
	}

	private JSONObject getUlocSipNc(Integer tmUlocId) {
		JSONObject map = new JSONObject();
		List<TmUlocSipNc> list = onlineService.findTmUlocSipNcByTmUlocId(tmUlocId);
		JSONArray array = null;
		for (TmUlocSipNc sipNc : list) {
			array = new JSONArray();
			TmUlocSip ulocSip = sipNc.getTmUlocSip();
			JSONObject ncMap = new JSONObject();
			TmNc nc = ncService.findById(sipNc.getTmNcId(), true);
			ncMap.put("ncGroupId", nc.getTmNcGroupId().toString());
			ncMap.put("ncGoup", nc.getNcGroup().getNo() + "-" + nc.getNcGroup().getName());
			ncMap.put("nc", nc.getNo() + "-" + nc.getName());
			ncMap.put("ncId", nc.getId().toString());
			ncMap.put("type", nc.getType());
			//array.add(ncMap);
			if(map.containsKey(ulocSip.getTsParameterId().toString())){
				array = map.getJSONArray(ulocSip.getTsParameterId().toString());
				array.add(ncMap);
			}else{
				array.add(ncMap);
			}
			map.put(ulocSip.getTsParameterId().toString(), array);
		}
		return map;
	}

	@RequestMapping("/unQuallityRecordNc")
	public ModelAndView unQuallityRecordNc(ModelMap modelMap, String quality, String SN) {
		JSONObject fromObject = JSONObject.fromObject(quality);
		Integer recordId = Integer.valueOf(fromObject.getInt("recordId"));
		String shiftNo = fromObject.getString("shiftNo");
		Integer tmUlocId = Integer.valueOf(fromObject.getInt("tmUlocId"));
		String currentUlocSeq = fromObject.getString("currentUlocSeq");

		JSONArray ncGroupArray = getNcMap();//取得所有的不合格group
		JSONObject ulocSipNc = getUlocSipNc(tmUlocId);//取得该工位下配置的 不合格组合不合格代码

		JSONArray array = getQualityArray(fromObject);

		JSONArray qualityArray = new JSONArray();
		for (Object object : array) {
			JSONObject jsonObject = JSONObject.fromObject(object);
			String checkResult = jsonObject.get("checkResult").toString();
			String qualityId = jsonObject.get("qualityId").toString();
			if ("NO".equals(checkResult)) {
				jsonObject.put("ncGroup", ncGroupArray);
				if(ulocSipNc.containsKey(qualityId)){
					Object obj = JSONArray.fromObject(ulocSipNc.get(qualityId)).get(0);
					jsonObject.put("tmUlocSipnc",obj);
					TmNc tmNc = new TmNc();
					tmNc.setTmNcGroupId(Integer.parseInt(JSONObject.fromObject(obj).get("ncGroupId").toString()));
					jsonObject.put("tmNcs",ncService.findByEg(tmNc));
				}
				/*if (!ulocSipNc.containsKey(qualityId)) {
					jsonObject.put("ncGroup", ncGroupArray);
					jsonObject.put("ncType", "NC_GROUP");
				} else {
					jsonObject.put("ncGroup", JSONArray.fromObject(ulocSipNc.get(qualityId)));
					jsonObject.put("ncType", "SIP_NC");
				}*/
			}
			qualityArray.add(jsonObject);
		}
		modelMap.addAttribute("quality", qualityArray);
		modelMap.addAttribute("recordId", recordId);
		modelMap.addAttribute("shiftNo", shiftNo);
		modelMap.addAttribute("tmUlocId", tmUlocId);
		modelMap.addAttribute("currentUlocSeq", currentUlocSeq);
		modelMap.addAttribute("SN", SN);
		modelMap.addAttribute("ncType", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_NC_TYPE));
		return new ModelAndView("/production-control/online/online_unquality_record_nc");
	}

	private JSONArray getQualityArray(JSONObject fromObject) {
		JSONArray array = new JSONArray();
		JSONArray qualityCodeArray = new JSONArray();
		JSONArray qualityIdArray = new JSONArray();
		JSONArray qualityNameArray = new JSONArray();
		JSONArray checkResultArray = new JSONArray();
		JSONArray qualityNoteArray = new JSONArray();
		String string = fromObject.get("qualityCode").toString();
		if (string.startsWith("[")) {
			qualityCodeArray.addAll(JSONArray.fromObject(fromObject.get("qualityCode")));
			qualityIdArray.addAll(JSONArray.fromObject(fromObject.get("qualityId")));
			qualityNameArray.addAll(JSONArray.fromObject(fromObject.get("qualityName")));
			checkResultArray.addAll(JSONArray.fromObject(fromObject.get("checkResult")));
			qualityNoteArray.addAll(JSONArray.fromObject(fromObject.get("qualityNote")));
		} else {
			qualityCodeArray.add(fromObject.get("qualityCode"));
			qualityIdArray.add(fromObject.get("qualityId"));
			qualityNameArray.add(fromObject.get("qualityName"));
			checkResultArray.add(fromObject.get("checkResult"));
			qualityNoteArray.add(fromObject.get("qualityNote"));
		}
		for (int i = 0; i < qualityIdArray.size(); i++) {
			JSONObject obj = new JSONObject();
			obj.put("qualityId", qualityIdArray.get(i));
			obj.put("qualityCode", qualityCodeArray.get(i));
			obj.put("qualityName", qualityNameArray.get(i));
			obj.put("checkResult", checkResultArray.get(i));
			obj.put("qualityNote", qualityNoteArray.get(i));
			array.add(obj);
		}
		return array;
	}

	private JSONArray getNcMap() {
		JSONArray array = new JSONArray();
		List<TmNcGroup> findAll = ncGroupService.findAll();
		for (TmNcGroup bean : findAll) {
			JSONObject map = new JSONObject();
			map.put("ncGroupId", bean.getId().toString());
			map.put("ncGoup", bean.getNo() + "- " + bean.getName());
			array.add(map);
		}
		return array;
	}

	@ResponseBody
	@RequestMapping("/doSaveNcGroupAndUntreated")
	public JsonActionResult doSaveNcGroupAndUntreated(HttpServletResponse response, Integer recordId, String shiftNo,
			Integer tmUlocId, String currentUlocSeq, String[] qualityCode, String[] checkResult, String[] qualityNote,
			Integer[] ncGroup, Integer ncCode[], String[] ncTypeInput, String SN) {
		onlineService.doSaveNcGroupAndUntreated(recordId, shiftNo, tmUlocId, currentUlocSeq, qualityCode, checkResult,
				qualityNote, ncGroup, ncCode, ncTypeInput, SN);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping("/printSn")
	public String printSn(ModelMap model, String SN) {
		TpRecord record = recordService.getTpRecordBySN(SN);
		model.addAttribute("url", "/WEB-INF/jasper/snPrint.jasper");// 指定报表模板url
		model.addAttribute("format", "pdf"); // 报表格式
		model.addAttribute("snTxt", SN);
		model.addAttribute("SN", SN);
		model.addAttribute("currDate", DateUtils.formatDateTime(record.getCreateTime()));
		model.addAttribute("jrMainDataSource", new JREmptyDataSource());
		return "iReportView";
	}
}
