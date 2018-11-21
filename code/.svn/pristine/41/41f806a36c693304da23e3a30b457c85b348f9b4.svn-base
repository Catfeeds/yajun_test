package com.wis.mes.production.record.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.wis.core.service.BaseService;
import com.wis.mes.production.record.entity.TpRecord;

public interface TpRecordService extends BaseService<TpRecord> {

    /**
     * 根据SN查询
     * 
     * @param SN
     * @return
     */
    public TpRecord getTpRecordBySN(String SN);

    /**
     * 导出数据
     * 
     * @author zhaoxianquan
     * @param response
     * @param list
     * @param filePath
     * @param header
     */
    public Map<String, Object> exportExcelData(HttpServletResponse response, List<TpRecord> list, String filePath,
            String[] header);

    /**
     * 级联数据导出
     * 
     * @author zhaoxianquan
     * @param response
     * @param list
     * @param parentHeader
     * @param childHeader
     * @param filePath
     */
    public Map<String, Object> exportExcelDataAll(HttpServletResponse response, List<TpRecord> list,
            String parentHeader, String childHeader, String filePath);

    /**
     * 根据 SN 产品编码 产品名称 工单号 查询 产品记录
     * 
     * @param sn
     *            SN
     * 
     * @param partNo
     *            产品编码
     * 
     * @param partName
     *            产品名称
     * 
     * @param porder
     *            工单号
     * 
     * @return 产品记录
     */
    List<Map<String, Object>> findByCondition(String sn, String partNo, String partName, String porder);

    /**
     * 根据 SN 时间单位 起始时间 结束时间 产品集合 下线统计
     * 
     * @param sn
     *            SN
     * 
     * @param time
     *            时间单位
     * 
     * @param startTime
     *            起始时间
     * 
     * @param endTime
     *            结束时间
     * 
     * @param partIds
     *            产品集合
     * 
     * @return 下线统计
     */
    String getReportData(String sn, String time, String startTime, String endTime, String partIds);

    /**
     * 分页查询 成品列表
     * 
     * @param pageNow
     *            当前页
     * 
     * @return 成品列表
     */
    String getNext(Integer pageNow);

}
