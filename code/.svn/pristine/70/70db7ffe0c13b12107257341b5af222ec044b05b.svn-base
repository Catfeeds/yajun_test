package com.wis.mes.production.metalplate.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.NumberUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.context.WebContextHolder;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.classmanage.entity.TmClassManagerDetail;
import com.wis.mes.master.classmanage.service.TmClassManagerDetailService;
import com.wis.mes.master.employeeno.entity.TmEmployeeNo;
import com.wis.mes.master.employeeno.service.TmEmployeeNoService;
import com.wis.mes.master.equipment.entity.TmEquipment;
import com.wis.mes.master.equipment.service.TmEquipmentService;
import com.wis.mes.master.metalPlate.entity.TmSheetMetalMaterial;
import com.wis.mes.master.metalPlate.service.TmSheetMetalMaterialService;
import com.wis.mes.master.workcalendar.entity.TmWorktime;
import com.wis.mes.master.workcalendar.service.TmWorktimeService;
import com.wis.mes.opc.util.metalplate.OpcMetalPlateHelper;
import com.wis.mes.production.metalplate.entity.TbMetalPlateProductionRecord;
import com.wis.mes.production.metalplate.entity.TbMetalPlateSchedul;
import com.wis.mes.production.metalplate.entity.TbMetalPlateSourceData;
import com.wis.mes.production.metalplate.entity.TbSchedulingRecorLog;
import com.wis.mes.production.metalplate.service.TbMetalPlateProductionRecordService;
import com.wis.mes.production.metalplate.service.TbMetalPlateSchedulService;
import com.wis.mes.production.metalplate.service.TbMetalPlateSourceDataService;
import com.wis.mes.production.metalplate.service.TbSchedulingRecorLogService;
import com.wis.mes.util.NewSerialNumUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/mpSchedul")
public class TbMetalPlateSchedulController extends BaseController {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	@Autowired
	private AuditLogService logService;
	@Autowired
	private TbMetalPlateSchedulService schedulService;
	@Autowired
	private TbMetalPlateSourceDataService sourceDataService;
	@Autowired
	private TmSheetMetalMaterialService sheetMetalMaterialService;
	@Autowired
	private TbMetalPlateProductionRecordService productionRecordService;
	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmWorktimeService worktimeService;
	@Autowired
	private TmClassManagerDetailService managerDetailService;
	@Autowired
	private TmEquipmentService tmEquipmentService;
	@Autowired
	private TbSchedulingRecorLogService tbSchedulingRecorLogService;
	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] {}));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, schedulService.findBy(criteria));
	}

	/**
	 * 单次派工
	 * 
	 * @param response
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/single")
	public JsonActionResult single(HttpServletResponse response, TbMetalPlateSchedul bean) {
		TbMetalPlateSourceData sourceData = sourceDataService.findById(bean.getId());// 钣金生产管理，排产列表信息<-源列表信息
		doSchedul(bean.getDispatchNumber(), sourceData, null, bean.getWorkSchedule(), bean.getTmWorktimeId());
		return ActionUtils.handleResult(response);
	}

	// 派工数，源列表信息，钣金原材料，班次，工作日历
	private TbMetalPlateSchedul doSchedul(Integer dispatchNumber, TbMetalPlateSourceData sourceData,
			TmSheetMetalMaterial eg, String workSchedule, Integer workTimeId) {
		if (eg == null) {
			eg = new TmSheetMetalMaterial();
			eg.setMachiningSurface(sourceData.getModel());
			eg = sheetMetalMaterialService.findUniqueByEg(eg);// 通过加工图面查询钣金原材料
		}
		TbMetalPlateSchedul schedul = new TbMetalPlateSchedul();// 通过源列表信息修改排产列表信息
		schedul.setSourceDataId(sourceData.getId());
		schedul.setSchedulDate(sourceData.getTaskDate());
		schedul.setWorkSchedule(workSchedule);
		schedul.setModel(sourceData.getModel());
		schedul.setBatchNumber(
				getSchedulBatchNumber(sourceData.getTaskDate(), workSchedule, sourceData.getModel(), dispatchNumber));
		schedul.setPress(eg.getMachineTagNames());
		schedul.setMoulds(eg.getToolNumber());
		schedul.setDispatchNumber(dispatchNumber);
		schedul.setFinishNumber(0);
		schedul.setSendWorkers(WebContextHolder.getCurrentUser().getName());
		schedul.setStatus(0);
		schedul.setDispatchTime(new Date());
		schedul.setRegionMark(eg.getRegionMark());
		schedul.setTmWorktimeId(workTimeId);
		Integer consumeApcTotal = sourceData.getApcTotalNumber() > dispatchNumber ? dispatchNumber
				: sourceData.getApcTotalNumber();// 计算消耗APC汇总数
		schedul.setConsumeApcTotal(consumeApcTotal);
		schedul.setSort(String.valueOf(new Date().getTime()));// 当前时间作为排序标识
		schedul.setSynPosition(getSynPosition(sourceData.getTaskDate(), workSchedule, eg.getRegionMark()));
		schedul.setMaterialId(eg.getId());// 关联号
		schedulService.doSave(schedul);
		// 修改原列表信息 apc会总数 计划数
		sourceData.setApcTotalNumber(
				sourceData.getApcTotalNumber() > dispatchNumber ? sourceData.getApcTotalNumber() - dispatchNumber : 0);
		sourceData.setPlannedProduction(sourceData.getPlannedProduction() > dispatchNumber
				? sourceData.getPlannedProduction() - dispatchNumber : 0);
		sourceData.setStatus(1);/** 数据状态 0初始新增，1部分派工，2派工完成 */
		if (sourceData.getPlannedProduction() == 0) {
			sourceData.setStatus(2);
		}
		sourceDataService.doUpdate(sourceData);
		return schedul;
	}

	// 获得生产批次
	private String getSchedulBatchNumber(String date, String work, String model, Integer dispatchNumber) {
		date = date.replaceAll("-", "");
		if (ConstantUtils.SHIFT_MORNING.equals(work)) {
			work = "A";
		} else if(ConstantUtils.SHIFT_NOON.equals(work)) {
			work = "B";
		}else {
			work = "C";
		}
		String sbnStr = date + work + model;
		int nextVal = NewSerialNumUtil.getInstance().nextInt(sbnStr);
		sbnStr += NumberUtils.frontCompWithZore(nextVal,
				String.valueOf(nextVal).length() <= 2 ? 2 : String.valueOf(nextVal).length());
		sbnStr += NumberUtils.frontCompWithZore(dispatchNumber, 4);
		return sbnStr;
	}

	// 获得生产顺位
	private String getSynPosition(String date, String work, String regionMark) {
		date = date.replaceAll("-", "");
		if (ConstantUtils.SHIFT_MORNING.equals(work)) {
			work = "A";
		} else if(ConstantUtils.SHIFT_NOON.equals(work)) {
			work = "B";
		}else {
			work = "C";
		}
		String sbnStr = date + work + regionMark;
		int nextVal = NewSerialNumUtil.getInstance().nextInt(sbnStr);
		sbnStr += NumberUtils.frontCompWithZore(nextVal, 3);
		return sbnStr;
	}

	@ResponseBody
	@RequestMapping(value = "/singleMulti") // 单次派多个
	public JsonActionResult singleMulti(HttpServletResponse response, String selectIds, Integer dispatchNumber,
			String workSchedule, Integer tmWorktimeId) {
		Integer[] ids = getIds("[" + selectIds + "]");
		List<TbMetalPlateSchedul> list = new ArrayList<TbMetalPlateSchedul>();
		for (Integer id : ids) {
			TbMetalPlateSourceData sourceData = sourceDataService.findById(id);
			// 派工数，源列表信息，钣金原材料，班次，工作日历
			list.add(doSchedul(dispatchNumber, sourceData, null, workSchedule, tmWorktimeId));
		}
		if (list.size() >= 2) {
			TbMetalPlateSchedul mps1 = list.get(0);
			TbMetalPlateSchedul mps2 = list.get(1);
			// 关联号
			Integer materialId = queryTmSheetMetalMaterialId(mps1.getModel(), mps2.getModel());
			mps1.setRelationId(mps2.getId());
			mps1.setMaterialId(materialId);
			mps2.setMaterialId(materialId);
			mps2.setSort(mps1.getSort());
			mps2.setRelationId(mps1.getId());
			/**
			 * 状态 0待产 1在产 2完成 3已下发 4已校验 9左关联右的右数据,不做显示派发等操作，生产完成后直接进生产记录表
			 */
			mps2.setStatus(9);
			schedulService.doUpdate(mps1);
			schedulService.doUpdate(mps2);
		}
		return ActionUtils.handleResult(response);
	}

	// 获取关联号
	private Integer queryTmSheetMetalMaterialId(String model, String model2) {
		TmSheetMetalMaterial eg = new TmSheetMetalMaterial();
		eg.setProgramNumber(model + "," + model2);
		eg = sheetMetalMaterialService.findUniqueByEg(eg);
		if (eg.getId() == null) {
			eg.setProgramNumber(model2 + "," + model);
			eg = sheetMetalMaterialService.findUniqueByEg(eg);
		}
		if (eg.getId() == null) {
			eg.setProgramNumber(model2 + "," + model);
			eg = sheetMetalMaterialService.findUniqueByEg(eg);
		}
		if (eg.getId() == null) {
			return 0;
		}
		return eg.getId();
	}

	@ResponseBody
	@RequestMapping(value = "/checkDispatchNumber")
	public void checkDispatchNumber(HttpServletResponse response, String id, String param) {
		JSONObject result = new JSONObject();
		result.put("status", "y");
		TbMetalPlateSourceData sourceData = sourceDataService.findById(Integer.parseInt(id));
		int dispatchNumber = Integer.parseInt(param);
		if (dispatchNumber <= 0) {
			result.put("status", "n");
			result.put("info", "派工数不能小于等于0！");
		} else {
			if (dispatchNumber > sourceData.getPlannedProduction()) {
				result.put("status", "n");
				result.put("info", "派工数不能大于计划数量[" + sourceData.getPlannedProduction() + "]！");
			} else {
				TmSheetMetalMaterial eg = new TmSheetMetalMaterial();
				eg.setMachiningSurface(sourceData.getModel());
				eg = sheetMetalMaterialService.findUniqueByEg(eg);
				if (dispatchNumber > eg.getBatchCap()) {
					result.put("status", "n");
					result.put("info", "派工数不能大于批次上限[" + eg.getBatchCap() + "]数量！");
				} else {
					if (!ConstantUtils.MP_SOURCE_DATA_TYPE_2.equals(sourceData.getDataType()) && dispatchNumber % eg.getBatchNumber() != 0) {
						result.put("status", "n");
						result.put("info", "派工数需要为批次数[" + eg.getBatchNumber() + "]的倍数！");
					}
				}
			}
		}
		ActionUtils.handleResult(response, result);
	}

	@ResponseBody
	@RequestMapping(value = "/checkMultiDispatchNumber")
	public void checkMultiDispatchNumber(HttpServletResponse response, String selectIds, Integer dispatchNumber) {
		JSONObject result = new JSONObject();
		result.put("status", "y");
		try {
			Integer[] ids = getIds(selectIds);
			List<TbMetalPlateSourceData> sourceDatas = sourceDataService.findAllInIds(ids);
			String leftMs = globalConfigurationService.getValueByKey(ConstantUtils.MP_LEFT_MACHINING_SURFACE);
			String rightMs = globalConfigurationService.getValueByKey(ConstantUtils.MP_RIGHT_MACHINING_SURFACE);
			List<String> leftList = Arrays.asList(leftMs.split("/"));
			List<String> rightList = Arrays.asList(rightMs.split("/"));
			if (dispatchNumber <= 0) {
				throw new Exception("派工数不能小于等于0！");
			} else {
				int leftNum = 0;
				int rightNum = 0;
				for (TbMetalPlateSourceData sourceData : sourceDatas) {
					if (leftList.contains(sourceData.getModel())) {
						leftNum++;
					}
					if (rightList.contains(sourceData.getModel())) {
						rightNum++;
					}
					if (dispatchNumber > sourceData.getPlannedProduction()) {
						throw new Exception("派工数不能大于型号[" + sourceData.getModel() + "]计划数量["
								+ sourceData.getPlannedProduction() + "]！");
					} else {
						TmSheetMetalMaterial eg = new TmSheetMetalMaterial();
						eg.setMachiningSurface(sourceData.getModel());
						eg = sheetMetalMaterialService.findUniqueByEg(eg);
						if (dispatchNumber > eg.getBatchCap()) {
							throw new Exception(
									"派工数不能大于型号[" + sourceData.getModel() + "]批次上限[" + eg.getBatchCap() + "]数量！");
						} else {
							if (dispatchNumber % eg.getBatchNumber() != 0) {
								throw new Exception(
										"派工数需要为型号[" + sourceData.getModel() + "]批次数[" + eg.getBatchNumber() + "]的倍数！");
							}
						}
					}
				}
				if (leftNum != rightNum) {
					throw new Exception("选择错误，请选择左右各一个加工図面进行排产");
				}
			}
		} catch (Exception ex) {
			result.put("status", "n");
			result.put("info", ex.getMessage());
		}
		ActionUtils.handleResult(response, result);
	}

	/**
	 * 取消派工
	 * 
	 * @param request
	 * @param response
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/cancelSchedul")
	public JsonActionResult cancelSchedul(HttpServletRequest request, HttpServletResponse response, String ids)
			throws Exception {
		Integer[] cancelIds = getIds(ids);
		List<TbMetalPlateSchedul> list = schedulService.findAllInIds(cancelIds);
		for (TbMetalPlateSchedul mps : list) {
			doCancelSchedul(mps);
			if (mps.getRelationId() != null && mps.getRelationId() > 0) {
				doCancelSchedul(schedulService.findById(mps.getRelationId()));
			}
		}
		logService.doDeleteLog("TbMetalPlateSchedul", list);
		return ActionUtils.handleResult(response);
	}

	private void doCancelSchedul(TbMetalPlateSchedul mps) throws Exception {
		if (mps == null || schedulService.findById(mps.getId()) == null) {
			return;
		}
		TbMetalPlateSchedul beforeBean = schedulService.findById(mps.getId());
		TbSchedulingRecorLog schedulingRecorLog = new TbSchedulingRecorLog();
		schedulingRecorLog.setMachiningSurface(beforeBean.getModel());
		schedulingRecorLog.setTaskTime(beforeBean.getSchedulDate());
		schedulingRecorLog.setStatus(String.valueOf(ConstantUtils.SM_PRODUCTION_STATE_6));
		schedulingRecorLog.setDispatchNumber(beforeBean.getDispatchNumber());
		schedulingRecorLog.setFinishNumber(beforeBean.getFinishNumber());
		schedulingRecorLog.setSynPosition(beforeBean.getSynPosition());
		schedulingRecorLog.setCreateTime(new Date());
		schedulingRecorLog.setCreateUser(WebContextHolder.getCurrentUser().getId());
		tbSchedulingRecorLogService.doSave(schedulingRecorLog);

		schedulService.doDeleteById(mps.getId());
		TbMetalPlateSourceData sourceData = sourceDataService.findById(mps.getSourceDataId());
		sourceData.setApcTotalNumber(sourceData.getApcTotalNumber() + mps.getConsumeApcTotal());
		sourceData.setPlannedProduction(sourceData.getPlannedProduction() + mps.getDispatchNumber());
		if (sourceData.getStatus() == 2) {
			sourceData.setStatus(1);
		}
		TbMetalPlateSchedul eg = new TbMetalPlateSchedul();
		eg.setModel(sourceData.getModel());
		eg.setSchedulDate(sourceData.getTaskDate());
		// eg.setWorkSchedule(sourceData.getWorkSchedule());
		List<TbMetalPlateSchedul> allMps = schedulService.findByEg(eg);
		if (allMps.size() == 1 && allMps.get(0).getId().intValue() == mps.getId().intValue()) {
			sourceData.setStatus(0);
		} else {
			boolean isequ = true;
			for (TbMetalPlateSchedul amps : allMps) {
				if (amps.getId().intValue() != mps.getId().intValue()) {
					isequ = false;
					break;
				}
			}
			if (isequ) {
				sourceData.setStatus(0);
			}
		}
		sourceDataService.doUpdate(sourceData);
	}

	@ResponseBody
	@RequestMapping(value = "/batchDispatching")
	public JsonActionResult batchDispatching(HttpServletRequest request, HttpServletResponse response, String ids)
			throws Exception {
		try {
			Integer[] queryIds = getIds(ids);
			List<TbMetalPlateSourceData> list = sourceDataService.findAllInIds(queryIds);
			String lineNo = globalConfigurationService.getValueByKey(ConstantUtils.MP_LINE_NO);
			TmWorktime mWorkTime = worktimeService.getWorkTimeByDayAndShiftno(list.get(0).getTaskDate(), lineNo,
					ConstantUtils.SHIFT_MORNING);
			TmWorktime nWorkTime = worktimeService.getWorkTimeByDayAndShiftno(list.get(0).getTaskDate(), lineNo,
					ConstantUtils.SHIFT_NIGHT);
			if (mWorkTime == null || nWorkTime == null) {
				throw new BusinessException("ERROR_KEY", "未维护" + list.get(0).getTaskDate() + "[钣金冲压线]工作时段,请维护后进行批量派工");
			}
			for (TbMetalPlateSourceData sourceData : list) {
				TmSheetMetalMaterial metalMaterial = new TmSheetMetalMaterial();
				metalMaterial.setMachiningSurface(sourceData.getModel());
				metalMaterial = sheetMetalMaterialService.findUniqueByEg(metalMaterial);
				List<Integer> dispatchNumbers = splitPlannedProduction(sourceData.getPlannedProduction(),
						metalMaterial.getBatchNumber(), metalMaterial.getBatchCap());
				for (Integer dispatchNumber : dispatchNumbers) {
					// 早班
					doSchedul(dispatchNumber, sourceData, metalMaterial, ConstantUtils.SHIFT_MORNING,
							mWorkTime.getId());
					// 晚班
					doSchedul(dispatchNumber, sourceData, metalMaterial, ConstantUtils.SHIFT_NIGHT, nWorkTime.getId());
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/positionSwitch") // 数据移动
	public JsonActionResult positionSwitch(HttpServletRequest request, HttpServletResponse response, String sign,
			Integer id) throws Exception {
		try {
			TbMetalPlateSchedul selectSchedul = schedulService.findById(id);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("sign", sign);
			params.put("id", id);
			// 置顶
			if ("top".equals(sign)) {
				schedulService.doWorkOrderStick(params);
				//selectSchedul.setSort(String.valueOf(new Date().getTime()));
			} else {
				// 上移下移
				TbMetalPlateSchedul releSchedul = schedulService.findRelevanceById(params);
				if (releSchedul != null) {
					if (selectSchedul.getRelationId() != null
							&& releSchedul.getId().intValue() == selectSchedul.getRelationId().intValue()) {
						params.put("id", selectSchedul.getRelationId());
						releSchedul = schedulService.findRelevanceById(params);
					}
					if (releSchedul != null) {
						String rSort = releSchedul.getSort();
						releSchedul.setSort(selectSchedul.getSort());
						releSchedul.setRemoved(0);
						schedulService.doUpdate(releSchedul);
						if (releSchedul.getRelationId() != null && releSchedul.getRelationId() != 0) {
							releSchedul = schedulService.findById(releSchedul.getRelationId());
							releSchedul.setSort(selectSchedul.getSort());
							schedulService.doUpdate(releSchedul);
						}
						selectSchedul.setSort(rSort);
					}
				}
				schedulService.doUpdate(selectSchedul);
				TbMetalPlateSchedul relationSchedul = null;
				if (selectSchedul.getRelationId() != null && selectSchedul.getRelationId() != 0) {
					relationSchedul = schedulService.findById(selectSchedul.getRelationId());
					if (relationSchedul != null) {
						relationSchedul.setSort(selectSchedul.getSort());
						schedulService.doUpdate(relationSchedul);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return ActionUtils.handleResult(response);
	}

	// 分割计划数
	private static List<Integer> splitPlannedProduction(Integer plannedProduction, Integer batchNumber,
			Integer batchCap) {
		List<Integer> dispatchNumbers = new ArrayList<Integer>();
		while (plannedProduction != 0) {
			int dispatchNumber = batchNumber
					* (int) Math.ceil(plannedProduction.doubleValue() / batchNumber.doubleValue() / 2D);
			if (dispatchNumber >= batchCap) {
				dispatchNumbers.add(batchCap);
				plannedProduction -= batchCap;
				if (plannedProduction < 0) {
					plannedProduction = 0;
				}
			} else {
				dispatchNumbers.add(dispatchNumber);
				plannedProduction = 0;
			}
		}
		return dispatchNumbers;
	}

	/**
	 * 临时触发生产完成事件
	 */
	@ResponseBody
	@RequestMapping(value = "/productionCompletion")
	public JsonActionResult productionCompletion(HttpServletRequest request, HttpServletResponse response, String ids)
			throws Exception {
		QueryCriteria criteria = new QueryCriteria();
		criteria.setQueryRelationEntity(true);
		if (!StringUtils.isEmpty(ids)) {
			criteria.addQueryCondition("idIN", ids.substring(1, ids.length() - 1));
		}
		criteria.setQueryPage(false);
		List<TbMetalPlateSchedul> list = schedulService.findBy(criteria).getContent();
		TbMetalPlateProductionRecord precord = null;
		for (TbMetalPlateSchedul mps : list) {
			if (mps.getStatus() != 2) {
				mps.setFinishNumber(mps.getDispatchNumber());
				mps.setStatus(2);
				mps.setRemoved(0);
				schedulService.doUpdate(mps);
				precord = new TbMetalPlateProductionRecord();
				precord.setSchedulId(mps.getId());
				precord.setProductionDate(mps.getSchedulDate());
				precord.setModel(mps.getModel());
				precord.setBatchNumber(mps.getBatchNumber());
				precord.setWorkPeople(getClassManagerDetail(mps.getTmWorkTime().getTmClassManagerId()));
				precord.setWorkSchedule(mps.getWorkSchedule());
				precord.setPress(mps.getPress());
				precord.setMoulds(mps.getMoulds());
				precord.setPlannedCycles(mps.getDispatchNumber());
				precord.setPracticalCycles(mps.getFinishNumber());
				precord.setDefectiveNumber(0);
				precord.setInventoryConsumption(0);
				precord.setProductionTime("");
				precord.setPeriodTakeTime("");
				precord.setPlaceTime("");
				precord.setAlarmNumber("");
				precord.setStartTime("");
				precord.setEndTime("");
				precord.setRegionMark(mps.getRegionMark());
				precord.setStatus(ConstantUtils.BJ_BAD_STATUS_0);
				productionRecordService.doSave(precord);
			}
			if (mps.getRelationId() != null && mps.getRelationId() != 0) {
				TbMetalPlateSchedul r = schedulService.findById(mps.getRelationId());
				if (r != null && r.getStatus() != 2) {
					r.setFinishNumber(r.getDispatchNumber());
					r.setStatus(2);
					r.setRemoved(0);
					schedulService.doUpdate(r);
					precord = new TbMetalPlateProductionRecord();
					precord.setSchedulId(r.getId());
					precord.setProductionDate(r.getSchedulDate());
					precord.setModel(r.getModel());
					precord.setBatchNumber(r.getBatchNumber());
					precord.setWorkPeople(getClassManagerDetail(r.getTmWorkTime().getTmClassManagerId()));
					precord.setWorkSchedule(r.getWorkSchedule());
					precord.setPress(r.getPress());
					precord.setMoulds(r.getMoulds());
					precord.setPlannedCycles(r.getDispatchNumber());
					precord.setPracticalCycles(r.getFinishNumber());
					precord.setDefectiveNumber(0);
					precord.setInventoryConsumption(0);
					precord.setProductionTime("");
					precord.setPeriodTakeTime("");
					precord.setPlaceTime("");
					precord.setAlarmNumber("");
					precord.setStartTime("");
					precord.setEndTime("");
					precord.setRegionMark(r.getRegionMark());
					precord.setStatus("0");
					productionRecordService.doSave(precord);
				}
			}
		}
		return ActionUtils.handleResult(response);
	}

	@Autowired
	private TmEmployeeNoService employeeNoService;

	private String getClassManagerDetail(Integer id) {
		TmClassManagerDetail eg = new TmClassManagerDetail();
		eg.setTmClassManagerId(id);
		List<TmClassManagerDetail> findByEg = managerDetailService.findByEg(eg);
		String returnStr = "";
		for (TmClassManagerDetail bean : findByEg) {
			TmEmployeeNo en = employeeNoService.findById(bean.getTmEmployeeNoId());
			returnStr += en.getNo() + "_" + en.getName() + ",";
		}
		if (returnStr.length() > 0) {
			returnStr = returnStr.substring(0, returnStr.length() - 1);
		}
		return returnStr;
	}

	/**
	 * 暂停,锁定
	 * 
	 * @param response
	 * @param bean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/lockout")
	public JsonActionResult lockout(HttpServletResponse response, TbMetalPlateSchedul bean) {
		TbMetalPlateSourceData sourceData = sourceDataService.findById(bean.getId());// 钣金生产管理，排产列表信息<-源列表信息
		doSchedul(bean.getDispatchNumber(), sourceData, null, bean.getWorkSchedule(), bean.getTmWorktimeId());
		return ActionUtils.handleResult(response);
	}

	/**
	 * 模具校验
	 * 
	 * @param type
	 *            PRESS MOULD
	 * @param value
	 *            机台 模具
	 * @param press
	 *            机台
	 * @param id
	 *            type 类型 是模具还是压机 value 扫描的值 就是那个固定资产编号 id是扫了压机 查出排产id
	 */
	@ResponseBody
	@RequestMapping(value = "/mouldVerify")
	public JsonActionResult mouldVerify(HttpServletResponse response, String type, String value, String press, String id) {
		TbMetalPlateSchedul schedul = new TbMetalPlateSchedul();
		try {
			// 扫描机台事件
			if ("PRESS".equals(type)) {
				TmEquipment equipment = tmEquipmentService.findByPropertyNumber(value);
				if (null == equipment) {
					return ActionUtils.handleResult(false,"扫描的机台信息不存在！");
				}
				Map<String, Object> schedulMap = schedulService.findSchedulEgByPress(equipment.getName());
				if (schedulMap == null || schedulMap.isEmpty()) {
					return ActionUtils.handleResult(false,"扫描的机台无待产信息！");
				}
				schedul.setSendWorkers(getToString(schedulMap.get("ID")));
				schedul.setRegionMark(getToString(schedulMap.get("REGION_MARK")));//区域标识
				schedul.setBatchNumber(getToString(schedulMap.get("BATCH_NUMBER")));//批次
				schedul.setModel(getToString(schedulMap.get("MODEL")));//加工图面
				schedul.setPress(equipment.getName());//机台
				String moulds = getMouldsNameByCode(getToString(schedulMap.get("MOULDS")),String.valueOf(equipment.getId()));//模具
				if(StringUtils.isNotBlank(moulds)) {
					List<TmEquipment> findByMoulds = findByMouldIds(moulds);
					StringBuffer propertyNumbers = new StringBuffer();
					StringBuffer mouldNames = new StringBuffer();
					if(null != findByMoulds && findByMoulds.size()>0) {
						for(TmEquipment mould:findByMoulds) {
							propertyNumbers.append(mould.getPropertyNumber()+",");
							mouldNames.append(mould.getName()+",");
						}
						if(propertyNumbers.length()>0) {
							propertyNumbers = propertyNumbers.deleteCharAt(propertyNumbers.length()-1);
						}
						if(mouldNames.length()>0) {
							mouldNames = mouldNames.deleteCharAt(mouldNames.length()-1);
						}
					}
					schedul.setMoulds(mouldNames.toString());
					schedul.setPropertyNumbers(propertyNumbers.toString());
				}
				// 扫描模具事件,处理成功后修改数据库该模具状态 verify 为模具校验状态
			} else if ("MOULD".equals(type)) {
				schedul = schedulService.findById(Integer.parseInt(id));
				if (schedul != null) {
					schedul = schedulService.findById(Integer.parseInt(id));
					if (schedul != null) {
						boolean isVerify = true;
						TmEquipment equipment = tmEquipmentService.findByPropertyNumber(press);
						TmEquipment equipmentModel = getModelBean(value);
						String verify = schedul.getVerify();
						if(StringUtils.isEmpty(verify)) {
							verify = equipment.getName()+":"+equipmentModel.getName();
						}else {
							if(!verify.contains(equipment.getName())) {
								verify+=","+equipment.getName()+":"+equipmentModel.getName();
							}
						}
						for(String schedulPress : schedul.getPress().split(",")) {
							if(!verify.contains(schedulPress)) {
								isVerify = false;
							}
						}
						schedul.setVerify(verify);
					// 模具全部校验完成
					if (schedul.getStatus() == 3 && isVerify) {
						schedul.setStatus(4);
						// 全部校验完成，修改PLC的status=3 模具已校验
						String[] ORDERCOMM_ROWS = new String[] {
								"Banjin1.1756-L72_2.ORDERCOMM.MFG AREA " + schedul.getRegionMark() + ".BJ_PLC_01_"
										+ schedul.getRegionMark() + "_ODRINFO_STATUS" };
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("BJ_PLC_01_" + schedul.getRegionMark() + "_ODRINFO_STATUS", 3);
						OpcMetalPlateHelper.sendDataToOpc(ORDERCOMM_ROWS, map);
					}
					schedulService.doUpdate(schedul);
				}
			}
		  }
		} catch (Exception e) {
			return ActionUtils.handleResult(false,"请求异常。");
		}
		return ActionUtils.handleResult(schedul);
	}
	
	private TmEquipment getModelBean(String value) {
		TmEquipment bean = new TmEquipment();
		TmEquipment eg = new TmEquipment();
		eg.setPropertyNumber(value);
		List<TmEquipment> equipments = tmEquipmentService.findByEg(eg);
		if(null != equipments && equipments.size() > 0) {
			bean = equipments.get(0);
		}
		return bean;
	}
	
	/***
	 * @author yajun_ren
	 * @param codes 模具编码 
	 * 
	 * **/
	public List<TmEquipment> findByMouldIds(String codes) {
		QueryCriteria queryCriteria = new QueryCriteria();
		queryCriteria.setQueryPage(false);
		queryCriteria.getQueryCondition().put("idIN", codes);
		return tmEquipmentService.findBy(queryCriteria).getContent();
	}

	/***
	 * @author yajun_ren
	 * @param mouldStr 模具编码 equipmentId 设备ID(机台)
	 * 
	 * **/
	public String getMouldsNameByCode(String mouldStr,String equipmentId) {
		String moulds = "";
		if(StringUtils.isNotBlank(mouldStr)) {
			String[] mouldArrays = mouldStr.split("~");
			for(String mouldSrc:mouldArrays) {
				String[] split = mouldSrc.split(":");
				if(equipmentId.equals(split[0])) {
					moulds = split[1];
				}
			}
		}
		return moulds;
	}

	private String getToString(Object val) {
		if (val == null) {
			return "";
		}
		return val.toString();
	}

	@ResponseBody
	@RequestMapping("/unLock")
	public JsonActionResult unLock(HttpServletRequest request, HttpServletResponse response, String ids) {
		Integer[] cancelIds = getIds(ids);
		List<TbMetalPlateSchedul> list = schedulService.findAllInIds(cancelIds);
//		TbMetalPlateSchedul tmps = new TbMetalPlateSchedul();

		try {
			for (TbMetalPlateSchedul mps : list) {
				/*tmps.setStatus(ConstantUtils.SM_PRODUCTION_STATE_7);// 状态7为解锁
				tmps.setId(mps.getId());*/
				mps.setStatus(ConstantUtils.SM_PRODUCTION_STATE_7);
				schedulService.doUnLock(mps);
//				tmps.setStatus(ConstantUtils.SM_PRODUCTION_STATE_0);//状态0为待产
				mps.setStatus(ConstantUtils.SM_PRODUCTION_STATE_0);	
				schedulService.doUnLock(mps);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping("/lock")
	public JsonActionResult lock(HttpServletRequest request, HttpServletResponse response, String ids)
			throws ParseException {

		Integer[] cancelIds = getIds(ids);
		List<TbMetalPlateSchedul> list = schedulService.findAllInIds(cancelIds);
//		TbMetalPlateSchedul tmps = new TbMetalPlateSchedul();
		try {
			for (TbMetalPlateSchedul mps : list) {
				mps.setStatus(ConstantUtils.SM_PRODUCTION_STATE_5);// 状态5为锁定
//				tmps.setStatus(ConstantUtils.SM_PRODUCTION_STATE_5);
//				tmps.setId(mps.getId());
				schedulService.doLock(mps);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ActionUtils.handleResult(response);
	}
/**
 * 强制结束
 * @param request
 * @param response
 * @param ids
 * @return
 * @throws ParseException
 */
	@ResponseBody
	@RequestMapping("/mandatoryEnd")
	public JsonActionResult mandatoryEnd(HttpServletRequest request, HttpServletResponse response, String ids)
			throws ParseException {

		Integer[] cancelIds = getIds(ids);
		List<TbMetalPlateSchedul> list = schedulService.findAllInIds(cancelIds);
		for (TbMetalPlateSchedul mps : list) {
			schedulService.doDeleteById(mps.getId());
			TbMetalPlateSourceData sourceData = sourceDataService.findById(mps.getSourceDataId());
			
			  Integer apcTotalNumber = sourceData.getApcTotalNumber();//计划需求量
			  Integer dispatchNumber = mps.getDispatchNumber();//派工数量 
			  Integer finishNumber = mps.getFinishNumber();//成品数
			  Integer consumeApcTotal = mps.getConsumeApcTotal();//已消耗apc汇总数(最大值 = 计划需求量,最小值 = 派工数)
			
			  int plannedProduction = 200*((dispatchNumber-finishNumber)/200+1)+sourceData.getPlannedProduction();
				if((dispatchNumber-finishNumber)%200==0){
					plannedProduction = dispatchNumber-finishNumber+sourceData.getPlannedProduction();
				}
			sourceData.setPlannedProduction(plannedProduction);
			int apcTotalNumbers = apcTotalNumber + consumeApcTotal-finishNumber;//重新计算计划需求量
			sourceData.setApcTotalNumber(apcTotalNumbers<0?0:apcTotalNumbers);
			TbMetalPlateSchedul eg = new TbMetalPlateSchedul();
			eg.setModel(sourceData.getModel());
			eg.setSchedulDate(sourceData.getTaskDate());
			List<TbMetalPlateSchedul> allMps = schedulService.findByEg(eg);
			if (allMps.size() == 1 && allMps.get(0).getId().intValue() == mps.getId().intValue()) {
				sourceData.setStatus(0);
			} else {
				boolean isequ = true;
				for (TbMetalPlateSchedul amps : allMps) {
					if (amps.getId().intValue() != mps.getId().intValue()) {
						isequ = false;
						break;
					}
				}
				if (isequ) {
					sourceData.setStatus(0);
				}
			}
			sourceDataService.doUpdate(sourceData);
		}
		return ActionUtils.handleResult(response);
	}
}
