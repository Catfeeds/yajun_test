package com.wis.mes.master.nc.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import com.wis.core.dao.QueryCriteria;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.service.BaseService;
import com.wis.mes.master.nc.entity.TmNc;
import com.wis.mes.master.nc.vo.NGVo;

public interface TmNcService extends BaseService<TmNc> {

    /**
     * 根据 不合格组主键 查询 不合格代码列表
     * 
     * @param ncGroupId
     *            不合格组主键
     * 
     * @return 不合格代码列表
     */
    List<TmNc> findByNcGroupId(Integer ncGroupId);

    /**
     * 将数据导出成Excel文件
     * 
     * @param response
     *            响应信息
     * 
     * @param list
     *            要导出的数据列表
     * 
     * @param filePath
     *            文件路径
     * 
     * @param header
     *            标题
     * 
     * @return 操作结果
     */
    Map<String, Object> exportExcelData(HttpServletResponse response, List<TmNc> list, String filePath,
            String[] header);

    /**
     * 将数据导出成Excel文件
     * 
     * @param workbook
     *            req
     * 
     * @param errorList
     *            错误提示信息列表
     */
	String doImportExcelData(Workbook workbook, HttpServletRequest req, String tmNcGroupId);

    /**
     * 记录日志
     * 
     * @param logType
     *            日志实体类型
     * 
     * @param operationType
     *            操作类型
     * 
     * @param before
     *            操作前描述
     * 
     * @param after
     *            操作后描述
     */
    void doLog(String logType, String operationType, String before, String after);

    /**
     * 模板下载
     * 
     * @param templatePath
     * @return
     */
    Workbook getWorkbookTemp(final String downName, final String templatePath, final List<TmNc> list);

    /**
     * 字典列表
     * 
     * @author zhaoxianquan
     * @param tmNc
     * @return
     */
    public List<NGVo> getDictItem(TmNc tmNc);
    
    /**
     * @author yajun_ren
     * @param tmNcGroupId
     * @return String
     *  生成模板编号
     * **/
    public String createTemplateNo(String src);
    
    /**
	 *
	 *封装数据字典公共接口 
	 * @return List<DictEntry>
	 * @author yajun_ren
	 */
	List<DictEntry> queryDictItem(QueryCriteria criteria);
}
