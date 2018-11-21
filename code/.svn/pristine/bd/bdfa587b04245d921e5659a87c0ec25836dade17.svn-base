package com.wis.mes.production.record.service;

import java.util.List;
import java.util.Map;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.core.service.BaseService;
import com.wis.mes.production.record.entity.TpRecordUlocNc;

public interface TpRecordUlocNcService extends BaseService<TpRecordUlocNc> {

    /**
     * 产品档案不合格信息
     * 
     * @author zhaoxianquan
     * @param criteria
     * @return
     */
    public PageResult<TpRecordUlocNc> getNcPageResult(BootstrapTableQueryCriteria criteria);

    /**
     * 保存不合格代码
     * 
     * @param ncCode
     * @param ncGroup
     * @param note
     * @param tpRecordUlocId
     */
    void doSaveNc(Integer ncCode, Integer ncGroup, String note, Integer tpRecordUlocId, Integer tpRecordId);

    /**
     * 根据 SN 不合格代码主键 不合格组主键 起始时间 结束时间 查询 不合格记录
     * 
     * @param sn
     *            SN
     * 
     * @param ncId
     *            不合格代码主键
     * 
     * @param ncGroupId
     *            不合格组主键
     * 
     * @param startTime
     *            起始时间
     * 
     * @param endTime
     *            结束时间
     * 
     * @return 不合格记录
     */
    List<Map<String, Object>> findByCondition(String sn, Integer ncId, Integer ncGroupId, String startTime,
            String endTime);

    /**
     * 分页查询 不合格代码/组 列表
     * 
     * @param type
     *            代码/组
     * 
     * @param pageNow
     *            当前页
     * 
     * @return 不合格代码/组 列表
     */
    String getNext(String type, Integer pageNow);

    /**
     * 根据 SN 不合格代码/组 时间单位 起始时间 结束时间 不合格集合 查询 不合格记录
     * 
     * @param sn
     *            SN
     * 
     * @param type
     *            不合格代码/组
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
     * @param ncIds
     *            不合格集合
     * 
     * @return 不合格记录
     */
    String getReportData(String sn, String type, String time, String startTime, String endTime, String ncIds);

}
