package com.wis.mes.master.workcalendar.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wis.core.dao.BaseDao;
import com.wis.mes.master.workcalendar.entity.TmWorktime;

public interface TmWorktimeDao extends BaseDao<TmWorktime> {
	String queryWorkTimeByParams(Integer tmPlantId, Integer tmWorkshopId, Integer tmLineId, String shiftno,
			String workDate[]);

	/**
	 * 根据 起始时间 结束时间 是否全权限 当前登录用户 查询 班次列表
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
	 * @return 班次列表
	 */
	List<Map<String, Object>> findByCondition(String startTime, String endTime, String isAll, Integer currUser);

	/**
	 * 根据开始／结束工作时间+ 班次id查询工作日历是否存在
	 * 
	 * @param tmWorktime
	 * @return
	 */
	Integer getWorkTime(Map<String, Object> map);

	TmWorktime thisWorkTime(Integer tmLineId);

	/**
	 * @author yajun_ren
	 * @tmWorktimeId 工作日历时间
	 * @rownum 每次查询多少条数据 根据当前班次获取休息时间
	 **/
	String breathingSpace(Integer tmWorktimeId, Integer rownum);

	/**
	 * @author yajun_ren
	 * @shiftno 班次
	 * @tmLineId 产线 获取下个班次的工作日历
	 **/
	TmWorktime getNextFlight(String shiftno, Integer tmLineId);

	TmWorktime getWorkTimeByDayAndShiftno(String queryDay, String lineNo,String shiftNo);

	TmWorktime getDateWorkTime(Date queryDate, String lineNo);
	
	/**
	 * @author yajun_ren
	 * @param shiftno
	 * @param tmLineId
	 * 获取前一天的工作日历
	 * ***/
	TmWorktime lastDayWordkTime(String shiftno, Integer tmLineId);
	
	
	/***
	 * @author yajun_ren
	 * @return List<TmWorktime>
	 * @param tmLineId
	 * 通过产线ID获取当天的工作日历
	 * ***/
	List<TmWorktime> todayWorkTime(Integer tmLineId,String shiftNo);
}
