package com.wis.mes.master.maintenance.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openscada.opc.lib.da.AddFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.LoadUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.master.boardmanage.service.TmBoardManageService;
import com.wis.mes.master.equipment.entity.TmpEquipmentStatus;
import com.wis.mes.master.equipment.service.TmEquipmentService;
import com.wis.mes.master.maintenance.entity.TmDeviceMaintenance;
import com.wis.mes.master.maintenance.service.TmDeviceMaintenanceService;

import net.sf.json.JSONObject;
/**
 * 钣金维修保养
 * @author lenovo
 *
 */
@Controller
@RequestMapping(value = "/maintenance")
public class TmDeviceMaintenanceController extends BaseController {

	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private TmDeviceMaintenanceService deviceMaintenanceService;
	@Resource
	private TmEquipmentService tmEquipmentService;
	@Resource
	private TmBoardManageService tmBoardManageService;
	
	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		getMetalMaterialEntry(modelMap);
		return new ModelAndView("/master/maintenance/maintenance_list");
	}
	
	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] {"deviceName","maintenanceProject" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, deviceMaintenanceService.findBy(criteria));
	}	
	

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		getMetalMaterialEntry(modelMap);
		return ActionUtils.handleEntryResult(modelMap, new ModelAndView("/master/maintenance/maintenance_add"));
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonActionResult add(HttpServletRequest request, HttpServletResponse response, TmDeviceMaintenance bean)
			throws Exception {
		try {
			bean = deviceMaintenanceService.doSave(bean);
		} catch (Exception e) {
			throw e;
		}

		logService.doAddLog("TmDeviceMaintenance", bean);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, String id, ModelMap modelMap) {
		modelMap.addAttribute("bean", deviceMaintenanceService.findById(Integer.valueOf(id)));
		getMetalMaterialEntry(modelMap);
		return new ModelAndView("/master/maintenance/maintenance_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletResponse response, TmDeviceMaintenance bean) {
		TmDeviceMaintenance before = deviceMaintenanceService.findById(bean.getId());
		bean.setOptCounter(before.getOptCounter() + 1);
		bean.setRemoved(before.getRemoved());
		bean.setCurrentValue(before.getCurrentValue());
		bean.setLastCollectTime(before.getLastCollectTime());
		deviceMaintenanceService.doUpdate(bean);
		logService.doUpdateLog("TmDeviceMaintenance", before, bean);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonActionResult delete(HttpServletRequest request, HttpServletResponse response, String ids)
			throws Exception {
		Integer[] deleteIds = getIds(ids);
		List<TmDeviceMaintenance> list = deviceMaintenanceService.findAllInIds(deleteIds);
		try {
			deviceMaintenanceService.doRemoveByBatch(deleteIds);
		} catch (Exception e) {
			throw e;
		}
		logService.doDeleteLog("TmDeviceMaintenance", list);
		return ActionUtils.handleResult(response);
	}
	
	@ResponseBody
	@RequestMapping(value = "/checkMaintenanceCode")
	public void checkMaintenanceCode(HttpServletResponse response, String code, String param) {
		TmDeviceMaintenance bean = new TmDeviceMaintenance();
		bean.setMaintenanceCode(param);
		Integer num = deviceMaintenanceService.findByEg(bean).size();
		JSONObject result = new JSONObject();
		if (num > 0 && !param.equals(code)) {
			result.put("status", "n");
			result.put("info", "保养代码已存在！");
		} else {
			result.put("status", "y");
		}
		ActionUtils.handleResult(response, result);
	}
	/**
	 * 保养重置
	 * @param response
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/resetMaintenance")
	public JsonActionResult resetMaintenance(HttpServletResponse response, String ids) {
		List<TmDeviceMaintenance> beans = deviceMaintenanceService.findAllInIds(this.getIds(ids));
		beans.forEach(bean->{
			bean.setCurrentValue(0);
			deviceMaintenanceService.doUpdate(bean);
		});
		//每次重置保养以后检查是否还有保养项超出了阈值需要报警
		deviceMaintenanceService.maintenanceWarningNotice();
		return ActionUtils.handleResult(response);
	}
	
	/**
	 * 暂时取消关灯
	 * @param response
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/warnCancel")
	public JsonActionResult warnCancel(HttpServletResponse response) {
		deviceMaintenanceService.alarmSetting(false);
		return ActionUtils.handleResult(response);
	}
	
	@ResponseBody
	@RequestMapping(value = "/importData")
	public JsonActionResult importData(final HttpServletRequest request, final HttpServletResponse response,
			final MultipartFile rawFile) {
		Workbook book;
		try {
			book = WorkbookFactory.create(rawFile.getInputStream());
			String msg = deviceMaintenanceService.doImportExcelData(book, request);
			return ActionUtils.handleResult(true, msg);
		} catch (final BusinessException e) {
			throw new BusinessException("IMPORT_DATA_VALID_ERROR_TITLE");
		} catch (final Exception e) {
			logger.error("Error Upload market：" + e.getMessage(), e);
			throw new BusinessException("METALPLATE_IMPORT_FAIL");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/exportData")
	public JsonActionResult ExportData(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) throws IOException {
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] {}));
		final PageResult<TmDeviceMaintenance> findBy = deviceMaintenanceService.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		List<TmDeviceMaintenance> list = findBy == null ? new ArrayList<TmDeviceMaintenance>() : findBy.getContent();
		String[] header = new String[] { "设备编号", "设备名称", "保养项目", "保养代码", "保养单位", "保养值", "保养点检内容","备注" };
		deviceMaintenanceService.exportExcelData(response, list, downName + ".xlsx", header);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/exportTemplate")
	public JsonActionResult exportTemplate(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) {
		try {
			if (downName == null || downName == "") {
				downName = "钣金设备保养模板导出";
			}
			downName = LoadUtils.urlDecoder(downName);
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "maintenance" + File.separator + "maintenance.xlsx";
			final Workbook wb = deviceMaintenanceService.getWorkbookTemp(downName, templatePath, null);
			LoadUtils.setContent(request, response, wb, downName);
		} catch (final IOException e) {
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		}
		return ActionUtils.handleResult(response);
	}
	@ResponseBody
	@RequestMapping(value = "/findNameInfo")
	public JsonActionResult findNameInfo(String no) {
		return ActionUtils.handleResult(tmEquipmentService.findNameByNo(no));
	}
	/**
	 * 查询条件
	 * @param modelMap
	 */
	private void getMetalMaterialEntry(ModelMap modelMap) {
		//保养单位
		modelMap.addAttribute("smMaintenanceUnit", entryService.getEntryByTypeCode(ConstantUtils.SM_MAINTENANCE_UNIT));
		//保养列表查询
		modelMap.addAttribute("optionMaintenances", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
		//设备数据字典展示
		modelMap.addAttribute("equipmentOptions", tmEquipmentService.queryDictItemNo(new QueryCriteria()));
	}

}
