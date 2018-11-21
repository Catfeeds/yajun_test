package com.wis.mes.master.classmanage.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import com.wis.core.service.BaseService;
import com.wis.mes.master.classmanage.entity.TmClassManagerDetail;


/**
 * @author zhuzw
 * 
 * @company 上海西信信息科技有限公司
 * 
 * @dete 2018-03-25
 * 
 * @desc 班次 Service
 * */
public interface TmClassManagerDetailService extends BaseService<TmClassManagerDetail>{

	void exportExcelData(HttpServletResponse response, List<TmClassManagerDetail> content, String string,
			String[] headers);

	String doImportExcelData(Workbook book, HttpServletRequest request, String tmBomId);
	
	
	
}
