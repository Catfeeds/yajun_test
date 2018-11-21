package com.wis.basis.configuration.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.LoadUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.basis.configuration.entity.TsParameter;
import com.wis.basis.configuration.service.TsParameterService;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.AuditLogService;
import com.wis.core.framework.service.exception.BusinessException;

@Controller
@RequestMapping(value = "/parameter")
public class TsParameterController extends BaseController {

	@Autowired
	private TsParameterService parameterService;
	@Autowired
	private AuditLogService logService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(ModelMap modelMap) {
		modelMap.addAttribute("parameters", parameterService.findAll());
		return new ModelAndView("/core/configuration/parameter/parameter_list");
	}

	@RequestMapping(value = "/list")
	@ResponseBody
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "code", "name", "variuableName", "note" }));
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, parameterService.findBy(criteria));
	}

	@RequestMapping(value = "/addInput")
	public ModelAndView addInput(ModelMap modelMap) {
		return new ModelAndView("/core/configuration/parameter/parameter_add");
	}

	@RequestMapping(value = "/add")
	@ResponseBody
	public JsonActionResult add(HttpServletResponse response, TsParameter parameter,String min,String max) {
		// 判断code是否重复
		TsParameter eg = new TsParameter();
		eg.setCode(parameter.getCode());
		List<TsParameter> codeList = parameterService.findByEg(eg);
		if (codeList.size() > 0) {
			throw new BusinessException("PARAMETER_CODE_EXCEPTION");
		} 
/*		else {
			// 判断variuableName是否重复
			TsParameter eeg = new TsParameter();
			eeg.setVariuableName(parameter.getVariuableName());
			List<TsParameter> variuableList = parameterService.findByEg(eeg);
			if (variuableList.size() > 0) {
				throw new BusinessException("PARAMETER_VARIUABLE_NAME_EXCEPTION");
			}
		}*/
		//获取自定义数据生成达式
		/*if (StringUtils.isNoneBlank(min)&&StringUtils.isNotBlank(max)) {
			StringBuffer regExp = parameterService.generatorExpression(max,min);
			parameter.setRegularExpression(regExp.toString());
		}*/
		parameter = parameterService.doSave(parameter);
		logService.doAddLog("TsParameter", parameter);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/updateInput")
	public ModelAndView updateInput(String id, ModelMap modelMap) {
		modelMap.addAttribute("parameter", parameterService.findById(Integer.valueOf(id)));
		return new ModelAndView("/core/configuration/parameter/parameter_update");
	}

	@RequestMapping(value = "/update")
	@ResponseBody
	public JsonActionResult update(HttpServletResponse response, TsParameter parameter) {
		TsParameter before = parameterService.findById(parameter.getId());
		parameterService.doUpdate(parameter);
		logService.doUpdateLog("TsParameter", before, parameter);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/delete")
	@ResponseBody
	public JsonActionResult delete(HttpServletRequest request,HttpServletResponse response, String ids) {
		Integer[] deleteIds = getIds(ids);
		List<TsParameter> list = parameterService.findAllInIds(deleteIds);
		try {
			parameterService.doDeleteById(deleteIds);
		} catch (Exception e) {
			String fks = getMessage(request, "PARAMETER_FKS");
			String[] fkArr = fks.split(",");
			for (int i = 0; i < fkArr.length; i++) {
				if (e.getMessage().contains(fkArr[i])) {
					throw new BusinessException(fkArr[i]);
				}
			}
		}
		logService.doDeleteLog("TsParameter", list);
		return ActionUtils.handleResult(response);
	}

	@RequestMapping(value = "/exportData")
	@ResponseBody
	public JsonActionResult ExportData(final HttpServletResponse response, final QueryCriteria criteria,
			String downName, final String ids) throws IOException {
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		criteria.setQueryRelationEntity(true);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "no" }));
		criteria.setQueryPage(false);
		downName = LoadUtils.urlDecoder(downName);
		final String[] header = new String[] { "编码", "名称", "表达式", "变量名", "默认值", "备注","单位" };
		parameterService.exportExcelData(response, parameterService.findBy(criteria).getContent(), downName + ".xlsx",
				header);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/exportTemplate")
	public JsonActionResult exportTemplate(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) {
		try {
			if (downName == null || downName == "") {
				downName = "参数模板导出";
			}
			downName = LoadUtils.urlDecoder(downName);
			final String templatePath = this.getBasePath(request) + File.separator + "templates" + File.separator
					+ "parameter" + File.separator + "parameter.xlsx";
			final Workbook wb = parameterService.getWorkbookTemp(downName, templatePath, null);
			LoadUtils.setContent(request, response, wb, downName);
		} catch (final IOException e) {
			logger.error(e.getMessage(), e);
			return ActionUtils.handleResult(false, ExceptionUtils.getStackTrace(e));
		}
		return ActionUtils.handleResult(response);
	}


	@ResponseBody
	@RequestMapping(value = "/importData")
	public JsonActionResult importDate(final HttpServletRequest request, final HttpServletResponse response,
			final MultipartFile rawFile) {
		Workbook book;
		try {
			book = WorkbookFactory.create(rawFile.getInputStream());
			String msg = parameterService.doImportExcelData(book,request);
			return ActionUtils.handleResult(true,msg);
		}catch (final BusinessException e) {
			throw new BusinessException("IMPORT_DATA_VALID_ERROR_TITLE");
		} catch (final Exception e) {
			logger.error("Error Upload market：" + e.getMessage(), e);
			throw new BusinessException("PARAMETER_IMPORT_FAIL");
		}
	}

	@RequestMapping(value = "/validationExpression")
	public ModelAndView validationExpression(ModelMap modelMap,final String regularExpression,final String id){
		modelMap.addAttribute("regularExpression", regularExpression);
		modelMap.addAttribute("id", id);
		return new ModelAndView("/core/configuration/parameter/parameter_validation_expression");
	}

	@RequestMapping(value = "/updateExpression")
	public boolean updateExpression(HttpServletRequest request,final String id,final String regularExpression) {
		boolean state = true;
		TsParameter before = parameterService.findById(Integer.valueOf(id));
		TsParameter parameter = before;
		parameter.setRegularExpression(regularExpression);
		try{
			parameterService.doUpdate(parameter);
		}catch(Exception e){
			state = false;
			return state;
		}
		logService.doUpdateLog("TsParameter", before, parameter);
		return state;
	}
}
