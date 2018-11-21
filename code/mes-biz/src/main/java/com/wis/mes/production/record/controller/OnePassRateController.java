package com.wis.mes.production.record.controller; 

import java.text.ParseException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wis.basis.common.controller.BaseController;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.mes.production.record.service.OnPassRateService;

/**   
 * @author Jixueyuan   
 * @date 2017年6月25日
 * @Description: 一次通过率报表Controller 
 */
@Controller
@RequestMapping(value="/onepassRate")
public class OnePassRateController extends BaseController{
	
	@Autowired
	private OnPassRateService service;
	
	@RequestMapping("/listInput")
	public ModelAndView input(){
		return new ModelAndView("/production-record/report/one_pass_rate");
	}
	
	
	@ResponseBody
	@RequestMapping("/findDataByType")
	public Map<String, String> onePassRate(String type,String viewGroup ,String startDateStr,String endDateStr,ModelMap modelMap) throws ParseException{
		try {
			return service.findData(type,viewGroup,startDateStr,endDateStr,modelMap);
		} catch (Exception e) {
			throw new BusinessException("ONEPASS_RATE_QUERY_ERROR");
			}
		}
	
}


