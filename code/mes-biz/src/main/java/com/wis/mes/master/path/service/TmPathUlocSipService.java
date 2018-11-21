package com.wis.mes.master.path.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.wis.basis.configuration.entity.TsParameter;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.service.BaseService;
import com.wis.mes.master.path.entity.TmPathUlocSip;

public interface TmPathUlocSipService extends BaseService<TmPathUlocSip> {

	/**
	 * 查询质检项信息
	 * 
	 * @param tmPathUlocId
	 * @return
	 */
	List<TmPathUlocSip> getPathUloSips(Integer tmPathUlocId);

	/**
	 * 保存质检项信息
	 * 
	 * @param tsParameterId
	 * @param note
	 * @param tmPathUlocId
	 * @return
	 */
	List<TmPathUlocSip> doSavePathUlocSip(String[] tsParameterId, String[] note, Integer tmPathUlocId);

	/**
	 * 获取质检项信息
	 * 
	 * @param tsParameterId
	 * @param note
	 * @param tmPathUlocId
	 * @return
	 */
	List<TmPathUlocSip> getPathUlocSipData(String[] tsParameterId, String[] note, Integer tmPathUlocId);

	/**
	 * 查询质检参数
	 * 
	 * @param criteria
	 * @return
	 */
	PageResult<TsParameter> getEquipmentAndUlocParameter(QueryCriteria criteria);

	/**
	 * 质检参数必检项 并且不在站点质检表中
	 * 
	 * @param tmPathUlocId
	 * @return
	 */
	List<TsParameter> getSipParameterExamineAndNotInSip(Integer tmPathUlocId, Integer tmUlocId,
			String[] tsParameterIds);

	/**
	 * 质检参数必检项
	 * 
	 * @param tmPathUlocId
	 * @return
	 */
	List<TsParameter> getSipParameterExamine(Integer tmPathUlocId, Integer tmUlocId);

	/**
	 * 数据导出
	 * 
	 * @param response
	 * @param content
	 * @param path
	 * @param header
	 */
	void exportExcelData(HttpServletResponse response, List<TmPathUlocSip> content, String path, String[] header);

}
