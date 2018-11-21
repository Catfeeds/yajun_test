package com.wis.mes.master.path.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.wis.core.service.BaseService;
import com.wis.mes.master.path.entity.TmPathUlocPart;

/**
 * 
 * @company:上海西信
 *
 * @ClassName: TmPathUlocPartService
 * 
 * @author liuzejun
 *
 * @date 2017年5月27日 下午1:58:58
 * 
 * @Description: 工艺路径站点 关键件Service
 */
public interface TmPathUlocPartService extends BaseService<TmPathUlocPart> {

	/**
	 * 关键件信息导出
	 * 
	 * @param response
	 * @param content
	 * @param filePath
	 * @param header
	 */
	void exportExcelData(HttpServletResponse response, List<TmPathUlocPart> content, String filePath, String[] header);

	/**
	 * 查询工艺路径关键件信息
	 * 
	 * @param tmPathUlocId
	 * @return
	 */
	List<TmPathUlocPart> getPathUlocParts(Integer tmPathUlocId);

}
