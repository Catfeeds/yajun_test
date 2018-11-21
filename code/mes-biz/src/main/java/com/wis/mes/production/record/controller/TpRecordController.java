package com.wis.mes.production.record.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.wis.mes.master.part.service.TmPartService;
import com.wis.mes.production.record.entity.TpRecord;
import com.wis.mes.production.record.service.TpRecordService;

@Controller
@RequestMapping(value = "/record")
public class TpRecordController extends BaseController {

	@Autowired
	private TpRecordService service;
	@Autowired
	private TmPartService partService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(HttpServletRequest request, QueryCriteria criteria, ModelMap modelMap) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("TYPE", Arrays.asList(ConstantUtils.ENTRY_CODE_PART_TYPE_FINISHED,
				ConstantUtils.ENTRY_CODE_PART_TYPE_SEMI_FINISHED));
		modelMap.addAttribute("parts", partService.getDictItem(param));
		return new ModelAndView("/production-record/record_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		criteria.setFuzzyQueryFileds(Arrays.asList("porderNo", "aviNo", "sn"));
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		criteria.setOrderField(criteria.getSort());
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		return ActionUtils.handleListResult(response, service.findBy(criteria));
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
		final PageResult<TpRecord> findBy = service.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		List<TpRecord> list = findBy == null ? new ArrayList<TpRecord>() : findBy.getContent();
		String[] header = new String[] { "SN编码", "工单编号", "生产序列编号", "物料", "数量", "下线时间", "上线时间", "原SN号", "入库数量" };
		service.exportExcelData(response, list, downName + ".xlsx", header);
		return ActionUtils.handleResult(response);
	}

	@ResponseBody
	@RequestMapping(value = "/exportDataAll")
	public JsonActionResult exportDataAll(final HttpServletRequest request, final HttpServletResponse response,
			final QueryCriteria criteria, String downName, final String ids) throws IOException {
		if (StringUtils.isNotEmpty(ids)) {
			criteria.getQueryCondition().put("idIN", ids);
		}
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		criteria.setFuzzyQueryFileds(Arrays.asList(new String[] {}));
		final PageResult<TpRecord> findBy = service.findBy(criteria);
		downName = LoadUtils.urlDecoder(downName);
		List<TpRecord> list = findBy == null ? new ArrayList<TpRecord>() : findBy.getContent();
		String parentHeader = getMessage(request, "RECORD_EXPORT_DATE_TITLES");
		String childHeader = getMessage(request, "RECORD_ULOC_EXPORT_DATE_TITLES");
		service.exportExcelDataAll(response, list, parentHeader, childHeader, downName + ".xlsx");
		return ActionUtils.handleResult(response);
	}

}
