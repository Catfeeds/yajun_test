package com.wis.mes.dakin.report.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.utils.ActionUtils;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.DateUtils;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.basis.common.web.model.JsonActionResult;
import com.wis.core.dao.OrderEnum;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.service.DictEntryService;
import com.wis.mes.master.classmanage.service.TmClassManagerService;
import com.wis.mes.master.equipment.service.TmEquipmentService;
import com.wis.mes.master.equipment.service.TmEquipmentStatusService;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.master.workcalendar.entity.TmWorktime;
import com.wis.mes.master.workcalendar.service.TmWorktimeService;
import com.wis.mes.production.metalplate.service.TbMetalPlateSchedulService;
import com.wis.mes.util.StringUtil;

import net.sf.json.JSONArray;

@Controller
@RequestMapping(value = "/maintenance_report")
public class MaintenanceReportController {

	@Autowired
	private TmWorktimeService worktimeService;

    @Autowired
    private TmEquipmentStatusService tmpEquipmentStatusService;

    @Autowired
    private TbMetalPlateSchedulService tbMetalPlateSchedulService;
    
    @Autowired
    private DictEntryService entryService;
    @Autowired
    private TmLineService lineService;
    @Autowired
    private TmClassManagerService classManagerService;
    @Autowired
    private TmEquipmentService tmEquipmentService;

	private void setDate(ModelMap modelMap){
        Date now = DateUtils.getCurrentDate();
        String toDay = DateUtils.formatDate(DateUtils.getCurrentDate());
        String lastMonthDate = DateUtils.formatDate(DateUtils.addDays(now, -30));
        modelMap.addAttribute("from",lastMonthDate);
        modelMap.addAttribute("to",toDay);
        modelMap.addAttribute("equipmentOptions", tmEquipmentService.queryDictItemNo(new QueryCriteria()));
        modelMap.addAttribute("optionMaintenances", entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_YES_OR_NO));
    }
	
	@RequestMapping(value = "/listInput")
	public ModelAndView maintenanceReport(ModelMap modelMap) {
        setDate(modelMap);
		return new ModelAndView("/report/maintenance_report");
	}

    @RequestMapping(value = "/warn_report")
    public ModelAndView energyReport(ModelMap modelMap) {
        setDate(modelMap);
        return new ModelAndView("/report/maintenance_warm_report");
    }

    @ResponseBody
    @RequestMapping(value = "/warn_report_list")
    public JsonActionResult list(HttpServletResponse response,
                                 BootstrapTableQueryCriteria criteria, ModelMap modelMap) {
        criteria.setQueryRelationEntity(true);
        criteria.setFuzzyQueryFileds(Arrays.asList(new String[] { "deviceCode",
                "deviceName", "remark" }));
        criteria.setRows(criteria.getLimit());
        criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
        criteria.setOrderField(criteria.getSort());
        criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC
                : OrderEnum.DESC);

       return ActionUtils.handleListResult(response,
                tmpEquipmentStatusService.warmReport(criteria));
    }

    @RequestMapping(value = "/pmc_report")
    public ModelAndView pmcReport(ModelMap modelMap) {
        setDate(modelMap);
        modelMap.addAttribute("lines", JSONArray.fromObject(lineService.getDictItem(null)).toString());
        modelMap.addAttribute("classManagers", JSONArray.fromObject(classManagerService.getDictItemEntry(null)).toString());
        modelMap.addAttribute("classGroupOptions",classManagerService.getDictItemEntry(null));
        modelMap.addAttribute("shiftnoOptions",entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_CLASS_ORDER));
        return new ModelAndView("/report/pmc_report");
    }

    @ResponseBody
    @RequestMapping(value = "/pmc_report_list")
    public JsonActionResult pmclist(HttpServletResponse response,
                                    BootstrapTableQueryCriteria criteria, ModelMap modelMap) {
        criteria.setRows(criteria.getLimit());
        criteria.setPage(criteria.getOffset() / criteria.getLimit() + 1);
        criteria.setOrderField(criteria.getSort());
        criteria.setOrderDirection("desc".equalsIgnoreCase(criteria.getOrder()) ? OrderEnum.ASC
                : OrderEnum.DESC);

        long now = new Date().getTime();
        PageResult<Map<String, Object>> pageResult = tbMetalPlateSchedulService.pmcReport(criteria);
        List<Map<String, Object>> contents = pageResult.getContent();
        for(Map<String,Object> row : contents){
            String dispatchNumber = StringUtil.getString(row.get("DISPATCH_NUMBER"));
            String finishNumber = StringUtil.getString(row.get("FINISH_NUMBER"));
            String workTimeId = StringUtil.getString(row.get("WORK_TIME_ID"));
            if(StringUtils.isNotEmpty(workTimeId)) {
                TmWorktime worktime = worktimeService.findById(Integer.valueOf(workTimeId));
                Long min = worktimeService.calcMinutes(worktime);
                long end = worktimeService.getEndTime(worktime).getTime();
                long start = worktimeService.getStartTime(worktime).getTime();
                if(end<now){
                    //如果已经超过时间,现时计划 = 计划数
                    row.put("realityPlan",dispatchNumber);
                }else if(start>now){
                    //如果还没到工作日历时间
                    row.put("realityPlan",0);
                }else {
                    ////计算每分钟数量
                    long plan = Integer.valueOf(dispatchNumber).longValue();
                    BigDecimal bg = new BigDecimal( (float)plan / min);
                    //计划: 每分钟应该生成多少个
                    double ava = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    //过去了多少分钟
                    double passTime =( now - start)/(1000*60);
                    bg = new BigDecimal(passTime*ava);
                    int realityPlan = bg.setScale(0,BigDecimal.ROUND_HALF_UP).intValue();
                    Integer complete = Integer.valueOf(finishNumber);
                    row.put("realityPlan",realityPlan);
                    if(complete>realityPlan){
                        row.put("delayTime",0);
                    }else{
                        int tmp = new BigDecimal( ava * (realityPlan - complete)).intValue();
                        row.put("delayTime",(tmp/60)+"H"+(tmp%60)+"min");
                    }
                }
            }else{
                row.put("realityPlan","0");
                row.put("realityCompletion","0");
                row.put("delayTime","0");
            }
        }
        return ActionUtils.handleListResult(response,pageResult);
    }



}
