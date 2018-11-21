/**
 * 2013-5-17 下午2:16:14
 */
package com.wis.basis.systemadmin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.SysJob;
import com.wis.core.framework.model.AuditConfigurationType;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.SysJobLogService;
import com.wis.core.framework.service.SysJobManagerService;
import com.wis.core.framework.service.SysJobService;

@Controller
@RequestMapping(value = "/job")
public class JobController extends BaseController {

	@Autowired
	private SysJobLogService sysJobLogService;
	@Autowired
	private AuditLogService logService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private SysJobService sysJobService;
	@Autowired
	private SysJobManagerService sysJobManagerService;

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria, ModelMap modelMap) {
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, sysJobService.findBy(criteria));
	}

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletResponse response, QueryCriteria criteria, ModelMap modelMap) {
		modelMap.addAttribute("options", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLE_STATUS));
		return new ModelAndView("/core/job/job_list");
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(HttpServletResponse response, Integer id, ModelMap modelMap) {
		modelMap.addAttribute("job", sysJobService.findById(id));
		modelMap.addAttribute("options", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLE_STATUS));
		return new ModelAndView("/core/job/job_update");
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonActionResult update(HttpServletRequest request, HttpServletResponse response, SysJob job) {
		SysJob before = sysJobService.findById(job.getId());
		copy(before, job, "enableStatus", "jobMemo", "cronExpression");
		sysJobManagerService.doUpdateAndNotify(job);
		logService.doUpdateLog(AuditConfigurationType.AUDIT_TYPE_SYSJOB, before, job);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/viewInput")
	public ModelAndView viewInput(Integer id, ModelMap modelMap) {
		modelMap.addAttribute("jobId", id);
		return new ModelAndView("/core/job/job_log_list_view");
	}

	@ResponseBody
	@RequestMapping(value = "/logList")
	public JsonActionResult logList(HttpServletResponse response, BootstrapTableQueryCriteria criteria, ModelMap modelMap) {
		criteria.setSort("\"exeTime\"");
		criteria.setOrder("desc");
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		return ActionUtils.handleListResult(response, sysJobLogService.findBy(criteria));
	}

	@ResponseBody
	@RequestMapping(value = "/pause")
	public JsonActionResult pause(HttpServletRequest request, HttpServletResponse response, Integer id) {
		sysJobManagerService.doPause(id);
		SysJob job = sysJobService.findById(id);
		logService.doLog(AuditConfigurationType.AUDIT_TYPE_SYSJOB, "PAUSE", job, null);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/resume")
	public JsonActionResult resume(HttpServletRequest request, HttpServletResponse response, Integer id) {
		sysJobManagerService.doResume(id);
		SysJob job = sysJobService.findById(id);
		logService.doLog(AuditConfigurationType.AUDIT_TYPE_SYSJOB, "AWAITING", job, null);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/runImmediately")
	public JsonActionResult runImmediately(HttpServletRequest request, HttpServletResponse response, Integer id) {
		sysJobManagerService.runImmediately(id);
		SysJob job = sysJobService.findById(id);
		logService.doLog(AuditConfigurationType.AUDIT_TYPE_SYSJOB, "RUN", job, null);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/checkCronExp")
	public void checkCronExp(HttpServletRequest request, HttpServletResponse response, String param) {
		JSONObject result = new JSONObject();
		if (CronExpression.isValidExpression(param)) {
			result.put("status", "y");
		} else {
			result.put("status", "n");
			result.put("info", getMessage(request, "JOB_EXP_INVALID"));
		}
		ActionUtils.handleResult(response, result);
	}

}
