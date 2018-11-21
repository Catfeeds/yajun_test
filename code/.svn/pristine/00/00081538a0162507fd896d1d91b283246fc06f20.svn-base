package com.wis.mes.production.plan.porder.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.wis.core.service.BaseService;
import com.wis.mes.production.plan.porder.entity.ToPorder;

public interface ToPorderService extends BaseService<ToPorder> {

	public ToPorder doSavePorder(ToPorder bean);

	/**
	 * 派工
	 * 
	 * @param toPorderId
	 *            工单ID
	 * @param taskQty
	 *            序列数量
	 * @param everyQty
	 *            每个序列的数量
	 * @param tmPathId
	 *            工艺路径ID
	 * @param tmBomId
	 *            BOM ID
	 */
	public void doTask(Integer toPorderId, Integer taskQty, Integer everyQty, Integer tmPathId, Integer tmBomId,
			String note);

	public void doCancelTask(Integer toPorderId);

	/**
	 * 删除工单下面的所有的生产序列信息
	 * 
	 * @param toPorderId
	 */
	public void deleteAviByPorderId(Integer toPorderId);

	/**
	 * 工单关闭
	 * 
	 * @param id
	 * @param closeReason
	 */
	public void doClose(Integer id, String closeReason);

	/**
	 * 工单信息的导出
	 * 
	 * @param response
	 * @param content
	 * @param string
	 * @param header
	 */
	public void exportExcelData(HttpServletResponse response, List<ToPorder> content, String string, String[] header);
}
