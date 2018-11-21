package com.wis.mes.production.record.dao;

import java.util.List;
import java.util.Map;

import com.wis.core.dao.BaseDao;
import com.wis.mes.production.record.entity.TpRecord;

public interface TpRecordDao extends BaseDao<TpRecord> {

    /**
     * 根据 SN 产品编码 产品名称 工单号 是否全权限 当前登录用户 查询 产品记录
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
     * @param isAll
     *            是否全权限
     * 
     * @param currUser
     *            当前登录用户
     * 
     * @return 产品记录
     */
    List<Map<String, Object>> findByCondition(String sn, String partNo, String partName, String porder, String isAll,
            Integer currUser);

    /**
     * 根据 SN 产品主键 起始时间 结束时间 是否全权限 当前登录用户 时间单位 统计 产品下线记录
     * 
     * @param sn
     *            SN
     * 
     * @param partId
     *            产品主键
     * 
     * @param startTime
     *            起始时间
     * 
     * @param endTime
     *            结束时间
     * 
     * @param isAll
     *            是否全权限
     * 
     * @param currUser
     *            当前登录用户
     * 
     * @param time
     *            时间单位
     * 
     * @return 产品下线记录
     */
    List<Map<String, Object>> StatisticByCondition(String sn, Integer partId, String startTime, String endTime,
            String isAll, Integer currUser, String time);

    /**
     * 根据 SN 产品主键 起始时间 结束时间 是否全权限 当前登录用户 班次开始时间 班次结束时间 统计 产品下线记录
     * 
     * @param sn
     *            SN
     * 
     * @param partId
     *            产品主键
     * 
     * @param startTime
     *            起始时间
     * 
     * @param endTime
     *            结束时间
     * 
     * @param isAll
     *            是否全权限
     * 
     * @param currUser
     *            当前登录用户
     * 
     * @param shiftStart
     *            班次开始时间
     * 
     * @param shiftEnd
     *            班次结束时间
     * 
     * @return 产品下线记录
     */
    Map<String, Object> StatisticByShift(String sn, Integer partId, String startTime, String endTime, String isAll,
            Integer currUser, String shiftStart, String shiftEnd);

    /**
     * 根据 SN 产品主键 起始时间 结束时间 是否全权限 当前登录用户 周开始时间 周结束时间 统计 产品下线记录
     * 
     * @param sn
     *            SN
     * 
     * @param partId
     *            产品主键
     * 
     * @param startTime
     *            起始时间
     * 
     * @param endTime
     *            结束时间
     * 
     * @param isAll
     *            是否全权限
     * 
     * @param currUser
     *            当前登录用户
     * 
     * @param weekStart
     *            周开始时间
     * 
     * @param weekEnd
     *            周结束时间
     * 
     * @return 产品下线记录
     */
    Map<String, Object> StatisticByWeek(String sn, Integer partId, String startTime, String endTime, String isAll,
            Integer currUser, String weekStart, String weekEnd);

}
