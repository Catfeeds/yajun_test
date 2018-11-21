package com.wis.mes.production.plan.porder.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.wis.core.service.BaseService;
import com.wis.mes.production.plan.porder.entity.ToPorderAviPathPart;

/**
 * 
 * @company:上海西信
 *
 * @ClassName: ToPorderAviPathPartService
 * 
 * @author liuzejun
 *
 * @date 2017年5月27日 下午1:38:52
 * 
 * @Description: 关键件Service
 */
public interface ToPorderAviPathPartService extends BaseService<ToPorderAviPathPart> {
	/**
	 * 保存工单下工艺路径关键件信息
	 * 
	 * @param toPorderAviPathId
	 * @param tmPathUlocId
	 */
	void doSaveAviPart(Integer toPorderAviPathId, Integer tmPathUlocId);

	/**
	 * 关键件数据导出
	 * 
	 * @param response
	 * @param content
	 * @param string
	 * @param header
	 */
	void exportExcelData(HttpServletResponse response, List<ToPorderAviPathPart> content, String string,
			String[] header);

}
