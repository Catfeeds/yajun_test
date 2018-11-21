package com.wis.mes.master.equipment.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.service.BaseService;
import com.wis.mes.master.equipment.entity.TmEquipment;

public interface TmEquipmentService extends BaseService<TmEquipment> {

	/**
	 * 导出设备信息
	 * @param response
	 * @param content
	 * @param string
	 * @param header
	 */
	public Map<String, Object> exportExcelData(HttpServletResponse response, List<TmEquipment> content, String fileName,
			String[] header);

	/**
	 * 设备信息导入
	 * @param book
	 * @param request
	 * @return
	 */
	public String doImportExcelData(Workbook book, HttpServletRequest request);
	
	/**
	 * 获取设备列表
	 * @param param  返回字典格式
	 * @return
	 */
	public List<DictEntry> getDictItem(TmEquipment param);
	
	public List<DictEntry> queryDictItem(QueryCriteria criteria);
	
	public List<DictEntry> queryDictItemNo(QueryCriteria criteria);
	
	public TmEquipment findByPropertyNumber(String value);

	public TmEquipment findNameByNo(String no);

	public TmEquipment findMouldNameById(String mouldArray);

}
