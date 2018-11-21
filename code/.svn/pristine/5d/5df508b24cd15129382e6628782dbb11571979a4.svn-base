package com.wis.mes.master.bom.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import com.wis.basis.excel.pojo.ExcelImportPojo;
import com.wis.basis.numRule.entity.NumRuleValue;
import com.wis.mes.master.base.service.MasterBaseService;
import com.wis.mes.master.bom.entity.TmBom;

/**
 * @author liuzejun
 * 
 * @company 上海西信信息科技有限公司
 * 
 * @dete 2017-04-18
 * 
 * @desc BOM Service
 */
public interface TmBomService extends MasterBaseService<TmBom> {

	/**
	 * BOM拷贝
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public TmBom doDuplicate(TmBom bom, List<String> errorList) throws Exception;

	/**
	 * 获取 BOM版本号
	 * 
	 * @param bom
	 * @return
	 */
	public NumRuleValue getSeqRuleNo(TmBom bom);

	/**
	 * 导出
	 * 
	 * @param response
	 * @param content
	 * @param string
	 * @param header
	 * @return
	 */
	public Map<String, Object> exportExcelData(HttpServletResponse response, List<TmBom> content, String string,
			String[] header);

	/**
	 * 保存BOM
	 * 
	 * @param bean
	 * @param errorMsg
	 * @return
	 * @throws Exception 
	 */
	public TmBom doSaveBom(TmBom bean, String errorMsg) throws Exception;
	
	/**
	 * 主表单表导入
	 * @author ryy
	 */
	public String doImportExcelData(Workbook book,HttpServletRequest request);

	/**
	 * 模板下载
	 * @param templatePath
	 * @return
	 */
	public Workbook getWorkbookTemp(String templatePath);
	/**
	 * 主表及关联表全部导出
	 * @param response
	 * @param list
	 * @param parentHeader
	 * @param childHeader
	 * @param string
	 */
	public Map<String, Object> exportExcelDataAll(HttpServletResponse response, List<TmBom> list, String parentHeader,
			String childHeader, String filePath);

	/**
	 * BOM&BOM明细联表导入
	 * @param book
	 * @param pojo
	 * @param request
	 * @return
	 */
	public String doImportExcelDataAll(Workbook book, ExcelImportPojo pojo, HttpServletRequest request);

}
