package com.wis.mes.production.plan.porder.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.wis.basis.configuration.entity.TsParameter;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.service.BaseService;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPathSip;

/**
 * 
 * @company:上海西信
 *
 * @ClassName: ToPorderAviPathSipService
 * 
 * @author liuzejun
 *
 * @date 2017年5月27日 下午1:38:30
 * 
 * @Description: 质检项service
 */
public interface ToPorderAviPathSipService extends BaseService<ToPorderAviPathSip> {
	/**
	 * 保存质检项信息
	 * 
	 * @param toPorderAviPathId
	 * @param tmPathUlocId
	 */
	void doSaveAviPathSip(Integer toPorderAviPathId, Integer tmPathUlocId);

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
	 * @param toPorderAviPathId
	 * @return
	 */
	List<TsParameter> getSipParameterExamineAndNotInSip(Integer toPorderAviPathId);

	/**
	 * 质检参数必检项
	 * 
	 * @param toPorderAviPathId
	 * @return
	 */
	List<TsParameter> getSipParameterExamine(Integer toPorderAviPathId);

	/**
	 * 保存质检参数
	 * 
	 * @param tsParameterId
	 * @param note
	 * @param toPorderAviPathId
	 * @return
	 */
	List<ToPorderAviPathSip> doSavePathUlocSip(String[] tsParameterId, String[] notes, Integer toPorderAviPathId);

	/**
	 * 质检项数据导出
	 * 
	 * @param response
	 * @param content
	 * @param string
	 * @param header
	 */
	void exportExcelData(HttpServletResponse response, List<ToPorderAviPathSip> content, String string,
			String[] header);
}
