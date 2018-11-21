package com.wis.mes.production.plan.porder.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.wis.basis.configuration.entity.TsParameter;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.service.BaseService;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPathParameter;

/**
 * 
 * @company:上海西信
 *
 * @ClassName: ToPorderAviPathParameterService
 * 
 * @author liuzejun
 *
 * @date 2017年5月27日 下午1:39:08
 * 
 * @Description: 参数service
 */
public interface ToPorderAviPathParameterService extends BaseService<ToPorderAviPathParameter> {

	/**
	 * 保存生产序列下的工艺路径的参数信息
	 * 
	 * @param toPorderAviPathId
	 * @param tmPathUlocId
	 */
	void doSaveAviPathParameter(Integer toPorderAviPathId, Integer tmPathUlocId);

	/**
	 * 查询参数列表
	 * 
	 * @param queryCriteria
	 * @return
	 */
	PageResult<TsParameter> getParameterPageResult(QueryCriteria queryCriteria);

	/**
	 * 保存参数信息
	 * 
	 * @param tmPathUlocId
	 * @param tsParameterId
	 * @param note
	 * @param isRequeired
	 * @param enabled
	 * @return
	 */
	List<ToPorderAviPathParameter> doSaveAviPathParameter(Integer toPorderAviPathId, String[] tsParameterIds,
			String[] notes, String[] isRequeired, String[] enabled);

	/**
	 * 参数信息导出
	 * 
	 * @param response
	 * @param content
	 * @param string
	 * @param header
	 */
	void exportExcelData(HttpServletResponse response, List<ToPorderAviPathParameter> content, String string,
			String[] header);

}
