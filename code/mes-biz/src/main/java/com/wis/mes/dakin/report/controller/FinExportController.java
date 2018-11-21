package com.wis.mes.dakin.report.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
import com.wis.basis.common.utils.DateUtils;
import com.wis.basis.common.utils.LoadUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.PageResult;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.DictEntryService;
import com.wis.mes.dakin.report.service.FinExportService;
import com.wis.mes.dakin.report.vo.FinExportVo;
import com.wis.mes.util.StringUtil;

@Controller
@RequestMapping(value = "/finExport")
public class FinExportController extends BaseController {
	@Autowired
	private FinExportService finExportService;
	@Autowired
	private DictEntryService entryService;

	@RequestMapping(value = "/listInput")
	public ModelAndView listInput(ModelMap modelMap) {
		modelMap.addAttribute("currentPageId", "FIN_EXPORT_MAIN");
		return new ModelAndView("report/fin/fin_export_main");
	}

	@RequestMapping(value = "/exportListInput")
	public ModelAndView exportListInput(ModelMap modelMap) {
		modelMap.addAttribute("currentPageId", "FIN_EXPORT");
		List<DictEntry> enryList = new ArrayList<DictEntry>();
		DictEntry entry = new DictEntry();
		entry.setCode("1");
		entry.setName("白班");
		enryList.add(entry);
		entry = new DictEntry();
		entry.setCode("2");
		entry.setName("晚班");
		enryList.add(entry);
		modelMap.addAttribute("deviceStatus", entryService.getEntryByTypeCode("FIN_DEVICE_STATUS"));
		modelMap.addAttribute("shiftNo", enryList);
		modelMap.addAttribute("oneLivelStatusContent", entryService.getEntryByTypeCode("FIN_ONE_LIVEL_STATUSCONTENT"));
		return new ModelAndView("report/fin/fin_export_list");
	}

	@ResponseBody
	@RequestMapping(value = "/list")
	public JsonActionResult list(HttpServletResponse response, BootstrapTableQueryCriteria criteria,
			ModelMap modelMap) {
		Map<String, Object> queryCondition = criteria.getQueryCondition();
		queryCondition.put("isRest", "YES");
		if (queryCondition.get("createTimeStart") == null || queryCondition.get("createTimeEnd") == null) {
			queryCondition.put("createTimeStart", DateUtils.formatDate(DateUtils.getPlusDay(0)));
			queryCondition.put("createTimeEnd", DateUtils.formatDate(DateUtils.getPlusDay(1)));
		}else {
			long dateDiff = DateUtils.dateDiff(DateUtils.parseDate(queryCondition.get("createTimeStart").toString()),
					DateUtils.parseDate(queryCondition.get("createTimeEnd").toString()));
			if (dateDiff > 7) {
				return ActionUtils.handleResult(false, "日期间隔请不要超出一个周");
			}
		}
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		if (StringUtil.isEmpty(criteria.getSort())) {
			criteria.setOrderField("beginTimeAllShow");
		} else {
			criteria.setOrderField(criteria.getSort());
		}
		criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		PageResult<FinExportVo> pageResult = new PageResult<FinExportVo>();
		List<FinExportVo> finExportInfo = finExportService.getFinExportInfo(criteria).getContent();

		if (queryCondition.get("oneLivelStatusContent") != null) {
			String string = queryCondition.get("oneLivelStatusContent").toString();
			for (Iterator<FinExportVo> iterator = finExportInfo.iterator(); iterator.hasNext();) {
				FinExportVo finExportVo = (FinExportVo) iterator.next();
				if (finExportVo.getOenLevelStatusContent() == null
						|| !finExportVo.getOenLevelStatusContent().equals(string)) {
					iterator.remove();
				}
			}
		}
		if (queryCondition.get("twoLivelStatusContent") != null) {
			String string = queryCondition.get("twoLivelStatusContent").toString();
			for (Iterator<FinExportVo> iterator = finExportInfo.iterator(); iterator.hasNext();) {
				FinExportVo finExportVo = (FinExportVo) iterator.next();
				if (finExportVo.getTwoLevelStatusContent() == null
						|| !finExportVo.getTwoLevelStatusContent().contains(string)) {
					iterator.remove();
				}
			}
		}
		int totalSize = finExportInfo.size();
		int totalPageNum = totalSize % criteria.getRows() == 0 ? totalSize / criteria.getRows()
				: totalSize / criteria.getRows() + 1;
		List<FinExportVo> pageList = getPageList(finExportInfo, criteria.getPage(), criteria.getRows(), totalPageNum);
		pageResult.setContent(pageList);
		pageResult.setTotalCount(totalSize);
		return ActionUtils.handleListResult(response, pageResult);
	}

	private List<FinExportVo> getPageList(List<FinExportVo> list, int currentPage, int pageSize, int totalPageNum) {
		int fromIndex = 0; // 从哪里开始截取
		int toIndex = 0; // 截取几个
		if (list == null || list.size() == 0)
			return new ArrayList<FinExportVo>();
		// 当前页小于或等于总页数时执行
		if (currentPage <= totalPageNum && currentPage != 0) {
			fromIndex = (currentPage - 1) * pageSize;
			if (currentPage == totalPageNum) {
				toIndex = list.size();
			} else {
				toIndex = currentPage * pageSize;
			}
		}
		return list.subList(fromIndex, toIndex);
	}

	@ResponseBody
	@RequestMapping(value = "/exportData")
	public JsonActionResult ExportData(final HttpServletRequest request, final HttpServletResponse response,
			final BootstrapTableQueryCriteria criteria, String downName) throws IOException {
		criteria.setQueryRelationEntity(true);
		criteria.setQueryPage(false);
		/// finExportService.getFinExportInfo(criteria).getContent();
		Map<String, Object> queryCondition = criteria.getQueryCondition();
		queryCondition.put("isRest", "NO");
		if (queryCondition.get("createTimeStart") == null || queryCondition.get("createTimeEnd") == null) {
			queryCondition.put("createTimeStart", DateUtils.formatDate(DateUtils.getPlusDay(0)));
			queryCondition.put("createTimeEnd", DateUtils.formatDate(DateUtils.getPlusDay(1)));
		} 
		criteria.setQueryRelationEntity(true);
		criteria.setRows(criteria.getLimit());
		criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
		if (StringUtil.isEmpty(criteria.getSort())) {
			criteria.setOrderField("beginTimeAllShow");
			criteria.setOrderDirection(OrderEnum.ASC);
		} else {
			criteria.setOrderField(criteria.getSort());
			criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC : OrderEnum.DESC);
		}
		List<FinExportVo> finExportInfo = finExportService.getFinExportInfo(criteria).getContent();

		if (queryCondition.get("oneLivelStatusContent") != null) {
			String string = queryCondition.get("oneLivelStatusContent").toString();
			for (Iterator<FinExportVo> iterator = finExportInfo.iterator(); iterator.hasNext();) {
				FinExportVo finExportVo = (FinExportVo) iterator.next();
				if (finExportVo.getOenLevelStatusContent() == null
						|| !finExportVo.getOenLevelStatusContent().equals(string)) {
					iterator.remove();
				}
			}
		}
		if (queryCondition.get("twoLivelStatusContent") != null) {
			String string = queryCondition.get("twoLivelStatusContent").toString();
			for (Iterator<FinExportVo> iterator = finExportInfo.iterator(); iterator.hasNext();) {
				FinExportVo finExportVo = (FinExportVo) iterator.next();
				if (finExportVo.getTwoLevelStatusContent() == null
						|| !finExportVo.getTwoLevelStatusContent().contains(string)) {
					iterator.remove();
				}
			}
		}
		List<FinExportVo> finExportVos = finExportInfo;
		downName = LoadUtils.urlDecoder(downName);
		List<FinExportVo> list = finExportVos == null ? new ArrayList<FinExportVo>() : finExportVos;
		String[] header = new String[] { "日期", "班次", "编号", "列", "段", "片数", "设备状态", "开始时间", "结束时间", "时长", "一级状态内容",
				"二级状态内容" };
		finExportService.exportExcelData(response, list, downName + ".xlsx", header);
		return ActionUtils.handleResult(response);
	}
}
