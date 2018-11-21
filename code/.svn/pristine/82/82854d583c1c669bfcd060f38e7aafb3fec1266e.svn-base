package com.wis.mes.production.record.dao;

import java.util.List;
import java.util.Map;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.BaseDao;
import com.wis.core.dao.PageResult;
import com.wis.mes.production.record.entity.TpRecordUlocNc;

public interface TpRecordUlocNcDao extends BaseDao<TpRecordUlocNc> {

    /**
     * 产品档案不合格信息
     * 
     * @author zhaoxianquan
     * @param criteria
     * @return
     */
    public PageResult<TpRecordUlocNc> getNcPageResult(BootstrapTableQueryCriteria criteria);

    /**
     * 根据 SN 不合格代码主键 不合格组主键 起始时间 结束时间 是否全权限 当前登录用户 查询 不合格记录
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
     * @param isAll
     *            是否全权限
     * 
     * @param currUser
     *            当前登录用户
     * 
     * @return 不合格记录
     */
    List<Map<String, Object>> findByCondition(String sn, Integer ncId, Integer ncGroupId, String startTime,
            String endTime, String isAll, Integer currUser);

    /**
     * 根据 SN 不合格主键 起始时间 结束时间 是否全权限 当前登录用户 不合格代码/组 时间单位 统计 不合格记录
     * 
     * @param sn
     *            SN
     * 
     * @param ncId
     *            不合格主键
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
     * @param type
     *            不合格代码/组
     * 
     * @param time
     *            时间单位
     * 
     * @return 不合格记录
     */
    List<Map<String, Object>> StatisticByCondition(String sn, Integer ncId, String startTime, String endTime,
            String isAll, Integer currUser, String type, String time);

    /**
     * 根据 SN 不合格主键 起始时间 结束时间 是否全权限 当前登录用户 班次开始时间 班次结束时间 不合格代码/组 统计 不合格记录
     * 
     * @param sn
     *            SN
     * 
     * @param ncId
     *            不合格主键
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
     * @param type
     *            不合格代码/组
     * 
     * @return 不合格记录
     */
    Map<String, Object> StatisticByShift(String sn, Integer ncId, String startTime, String endTime, String isAll,
            Integer currUser, String shiftStart, String shiftEnd, String type);

    /**
     * 根据 SN 不合格主键 起始时间 结束时间 是否全权限 当前登录用户 周开始时间 周结束时间 不合格代码/组 统计 不合格记录
     * 
     * @param sn
     *            SN
     * 
     * @param ncId
     *            不合格主键
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
     * @param type
     *            不合格代码/组
     * 
     * @return 不合格记录
     */
    Map<String, Object> StatisticByWeek(String sn, Integer ncId, String startTime, String endTime, String isAll,
            Integer currUser, String weekStart, String weekEnd, String type);

}
