package com.wis.mes.master.workcalendar.service.impl;

import static com.wis.basis.common.utils.GetPropertiesMessageUtils.getMessage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.BaseExcelUtil;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.basis.common.utils.DateUtils;
import com.wis.basis.common.utils.LoadUtils;
import com.wis.basis.common.utils.SpiltUtils;
import com.wis.basis.excel.pojo.ExcelImportPojo;
import com.wis.basis.systemadmin.entity.ImportLog;
import com.wis.basis.systemadmin.service.ImportLogService;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.framework.service.GlobalConfigurationService;
import com.wis.core.framework.service.exception.BusinessException;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.master.plant.entity.TmPlant;
import com.wis.mes.master.plant.service.TmPlantService;
import com.wis.mes.master.workcalendar.dao.TmWorkscheduleDao;
import com.wis.mes.master.workcalendar.entity.TmWorkSpecialDate;
import com.wis.mes.master.workcalendar.entity.TmWorkschedule;
import com.wis.mes.master.workcalendar.entity.TmWorkscheduleRest;
import com.wis.mes.master.workcalendar.entity.TmWorktime;
import com.wis.mes.master.workcalendar.entity.TmWorktimeRest;
import com.wis.mes.master.workcalendar.service.TmWorkSpecialDateService;
import com.wis.mes.master.workcalendar.service.TmWorkscheduleRestService;
import com.wis.mes.master.workcalendar.service.TmWorkscheduleService;
import com.wis.mes.master.workcalendar.service.TmWorktimeRestService;
import com.wis.mes.master.workcalendar.service.TmWorktimeService;
import com.wis.mes.master.workshop.entity.TmWorkshop;
import com.wis.mes.master.workshop.service.TmWorkshopService;

@Service
public class TmWorkscheduleServiceImpl extends BaseServiceImpl<TmWorkschedule> implements TmWorkscheduleService {

	@Autowired
	public void setDao(TmWorkscheduleDao dao) {
		this.dao = dao;
	}

	@Autowired
	private ImportLogService importLogService;
	@Autowired
	private TmWorkSpecialDateService workSpecialDateService;
	@Autowired
	private TmWorkscheduleRestService workscheduleRestService;
	@Autowired
	private TmWorktimeService worktimeService;
	@Autowired
	private TmWorktimeRestService worktimeRestService;
	@Autowired
	private DictEntryService entryService;
	@Autowired
	private GlobalConfigurationService globalConfigurationService;
	@Autowired
	private TmPlantService plantService;
	@Autowired
	private TmWorkshopService workshopService;
	@Autowired
	private TmLineService lineService;

	DateFormat date = DateUtils.getDateFormat(DateUtils.FORMAT_DATE_DEFAULT);
	DateFormat datePlusHour = DateUtils.getDateFormat("yyyy-MM-dd HH:mm");
	SimpleDateFormat timeFormat = new SimpleDateFormat(DateUtils.FORMAT_TIME_HH_MM);

	@Override
	public List<DictEntry> getDictItem(TmWorkschedule tmWorkschedule) {
		List<TmWorkschedule> tmWorkscheduleList = (tmWorkschedule == null ? findAll() : findByEg(tmWorkschedule));
		final List<DictEntry> dictList = new ArrayList<DictEntry>();
		for (final TmWorkschedule e : tmWorkscheduleList) {
			final DictEntry dict = new DictEntry();
			dict.setCode(e.getId().toString());
			dict.setName(e.getNo());
			dictList.add(dict);
		}
		return dictList;
	}

	@Override
	public String generateCurrentWeekTemplete(String ids, String isCoveredValue) {
		String[] tmWorkscheduleIds = ids.split(",");
		Map<String, String> dateWeekMap = new HashMap<String, String>();
		Map<String, String> specialDateMap = new HashMap<String, String>();
		// 要保存的workTimeRestIds
		List<Integer> workTimeRestIds = new ArrayList<Integer>();
		// 当前日期距离本周末的天数
		int distanceWeekendDays = new Long(DateUtils.dateDiff(new Date(), DateUtils.getEndDayOfWeek())).intValue();
		try {
			specialDateMap = getAllEnabledSpecialDate();
			for (int i = 0; i < tmWorkscheduleIds.length; i++) {
				Integer tmWorkscheduleId = Integer.valueOf(tmWorkscheduleIds[i]);
				TmWorkschedule tmWorkschedule = findById(tmWorkscheduleId);
				List<TmWorkscheduleRest> tmWorkscheduleRests = geTmWorkscheduleRestsById(tmWorkscheduleId);
				if (null != tmWorkschedule) {
					if (ConstantUtils.TYPE_CODE_ENABLED_ON.equals(tmWorkschedule.getEnabled())) {
						int plusDay = 0;
						if ("Y".equals(isCoveredValue)) {
							// 覆盖当天则加1
							distanceWeekendDays = distanceWeekendDays + 1;
							plusDay = -1;
						}
						// 星期与值对应，为了通过值获取on的星期
						List<String> weekAvalueList = getWeekAvalueList(tmWorkschedule);
						dateWeekMap = getDateWeekMap(weekAvalueList, distanceWeekendDays, plusDay, specialDateMap);
						workTimeRestIds = getWorkTimeIdsByMapSchedule(dateWeekMap, tmWorkschedule);
						saveWorkTimeRest(workTimeRestIds, tmWorkscheduleRests);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("WORK_SCHEDULE_GENERATE_FAILE");
		}
		return "success";
	}

	@Override
	public String generateNextWeekTemplete(String ids) {
		String[] tmWorkscheduleIds = ids.split(",");
		Map<String, String> dateWeekMap = new HashMap<String, String>();
		Map<String, String> specialDateMap = new HashMap<String, String>();
		// 要保存的workTimeRestIds
		List<Integer> workTimeRestIds = new ArrayList<Integer>();
		// 当前日期距离本周末的天数
		int distanceWeekendDays = new Long(DateUtils.dateDiff(new Date(), DateUtils.getEndDayOfWeek())).intValue();
		try {
			specialDateMap = getAllEnabledSpecialDate();
			for (int i = 0; i < tmWorkscheduleIds.length; i++) {
				Integer tmWorkscheduleId = Integer.valueOf(tmWorkscheduleIds[i]);
				TmWorkschedule tmWorkschedule = findById(tmWorkscheduleId);
				List<TmWorkscheduleRest> tmWorkscheduleRests = geTmWorkscheduleRestsById(tmWorkscheduleId);
				if (null != tmWorkschedule) {
					if (tmWorkschedule.getEnabled().equals(ConstantUtils.TYPE_CODE_ENABLED_ON)) {
						// 星期与值对应，为了通过值获取on的星期
						List<String> weekAvalueList = getWeekAvalueList(tmWorkschedule);
						dateWeekMap = getDateWeekMap(weekAvalueList, 7, distanceWeekendDays, specialDateMap);
						workTimeRestIds = getWorkTimeIdsByMapSchedule(dateWeekMap, tmWorkschedule);
						saveWorkTimeRest(workTimeRestIds, tmWorkscheduleRests);
					}
				}
			}
		} catch (Exception e) {
			throw new BusinessException("WORK_SCHEDULE_GENERATE_FAILE");
		}
		return "success";
	}

	private List<TmWorkscheduleRest> geTmWorkscheduleRestsById(Integer tmWorkScheduleId) {
		TmWorkscheduleRest tmWorkscheduleRest = new TmWorkscheduleRest();
		tmWorkscheduleRest.setTmWorkscheduleId(tmWorkScheduleId);
		List<TmWorkscheduleRest> tmWorkscheduleRests = workscheduleRestService.findByEg(tmWorkscheduleRest);
		return tmWorkscheduleRests;
	}

	/**
	 * 
	 * @Title:getDateWeekMap @Description:获取日期与星期对应的map @param @param
	 *                       weekAvalueList @param @param days @param @param
	 *                       specialDateMap @param @return @return
	 *                       Map<String,String> @throws
	 */
	private Map<String, String> getDateWeekMap(List<String> weekAvalueList, int days, int plusDays,
			Map<String, String> specialDateMap) {
		Map<String, String> dateWeekMap = new HashMap<String, String>();
		for (int j = 6; j > 7 - days - 1; j--) {
			String[] weekAvalueStr = weekAvalueList.get(j).split("-");
			String plusDayDate = date.format(DateUtils.getPlusDay(days + plusDays));
			// 工作日历模板为启用或者在特殊日历状态为工作
			if ((weekAvalueStr[1].equals(ConstantUtils.TYPE_CODE_ENABLED_ON) && specialDateMap.get(plusDayDate) == null)
					|| (weekAvalueStr[1].equals(ConstantUtils.TYPE_CODE_ENABLED_ON)
							&& (specialDateMap.get(plusDayDate) != null
									&& specialDateMap.get(plusDayDate).equals("WORK")))
					|| (weekAvalueStr[1].equals(ConstantUtils.TYPE_CODE_ENABLED_OFF)
							&& (specialDateMap.get(plusDayDate) != null
									&& specialDateMap.get(plusDayDate).equals("WORK")))) {
				dateWeekMap.put(date.format(DateUtils.getPlusDay(days + plusDays)), weekAvalueStr[0]);
			}
			plusDays--;
		}

		return dateWeekMap;
	}

	/**
	 * 
	 * @Title:getAllEnabledSpecialDate @Description:获取所有启用的特殊日期 @param @return @return
	 *                                 List<TmWorkSpecialDate> @throws
	 */
	private Map<String, String> getAllEnabledSpecialDate() {
		Map<String, String> specialDateMap = new HashMap<String, String>();
		TmWorkSpecialDate tmWorkSpecialDate = new TmWorkSpecialDate();
		tmWorkSpecialDate.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		// 查询所有启用的特殊日期
		List<TmWorkSpecialDate> tmWorkSpecialDateList = workSpecialDateService.findByEg(tmWorkSpecialDate);
		// 将所有特殊日期封装成map
		for (int i = 0; i < tmWorkSpecialDateList.size(); i++) {
			TmWorkSpecialDate specialDate = tmWorkSpecialDateList.get(i);
			String specialDateStr = DateUtils.formatDate(specialDate.getSpecialDate());
			specialDateMap.put(specialDateStr, specialDate.getType());
		}
		return specialDateMap;
	}

	/**
	 * 
	 * @Title:saveWorkTimeRest @Description:根据工作日历模板和工作日历id保存工作日历休息时间 @param @param
	 *                         workTimeRestIds @param @param
	 *                         tmWorkscheduleRests @return void @throws
	 */
	private void saveWorkTimeRest(List<Integer> workTimeRestIds, List<TmWorkscheduleRest> tmWorkscheduleRests) {
		List<TmWorktimeRest> lastSaveTimeRest = new ArrayList<TmWorktimeRest>();
		for (int i = 0; i < workTimeRestIds.size(); i++) {
			Integer workTimeId = workTimeRestIds.get(i);
			for (int j = 0; j < tmWorkscheduleRests.size(); j++) {
				TmWorkscheduleRest bean = tmWorkscheduleRests.get(j);
				TmWorktimeRest tmWorktimeRest = new TmWorktimeRest();
				tmWorktimeRest.setTmWorktimeId(workTimeId);
				tmWorktimeRest.setStartTime(bean.getStartTime());
				tmWorktimeRest.setEndTime(bean.getEndTime());
				lastSaveTimeRest.add(tmWorktimeRest);
			}
		}
		worktimeRestService.doSaveBatch(lastSaveTimeRest);
	}

	/**
	 * 
	 * @Title:returnWorkTimeIdsByMapSchedule @Description:通过dateWeekMap,tmWorkschedule获取workTimeIds @param @param
	 *                                       dateWeekMap @param @param
	 *                                       tmWorkschedule @param @return @param @throws
	 *                                       Exception @return
	 *                                       List<String> @throws
	 */
	private List<Integer> getWorkTimeIdsByMapSchedule(Map<String, String> dateWeekMap, TmWorkschedule tmWorkschedule)
			throws Exception {
		// 要保存的workTimeIds
		List<Integer> workTimeRestIds = new ArrayList<Integer>();
		for (String key : dateWeekMap.keySet()) {
			TmWorktime workTime = new TmWorktime();
			workTime.setTmWorkshopId(tmWorkschedule.getTmWorkshopId());
			workTime.setTmPlantId(tmWorkschedule.getTmPlantId());
			workTime.setTmLineId(tmWorkschedule.getTmLineId());
			workTime.setShiftno(tmWorkschedule.getShiftnoId());
			/*workTime.setTmClassManagerId(tmWorkschedule.getTmClassManagerId());*/
			workTime.setWorkDate(date.parse(key));
			workTime.setWeek(dateWeekMap.get(key));
			workTime.setStartTime(tmWorkschedule.getStartTime()); 
			workTime.setEndTime(tmWorkschedule.getEndTime());
			workTime.setPlanOnlineQty(tmWorkschedule.getPlanOnlineQty());
			workTime.setPlanOfflineQty(tmWorkschedule.getPlanOfflineQty());
			workTime.setJphQty(tmWorkschedule.getJph());
			workTime.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_OFF);
			workTime.setTmWorkscheduleId(tmWorkschedule.getId());
			TmWorktime tmWorktime = worktimeService.doSave(workTime);
			workTimeRestIds.add(tmWorktime.getId());
		}
		return workTimeRestIds;
	}

	/**
	 * 
	 * @Title:getWeekAvalueList @Description:通过工作日历模板将星期与值对应并封装成list方便获取值为On的日期 @param @param
	 *                          tmWorkschedule @param @return @return
	 *                          List<String> @throws
	 */
	private List<String> getWeekAvalueList(TmWorkschedule tmWorkschedule) {
		List<String> weekAvalueList = new ArrayList<String>();
		weekAvalueList.add(ConstantUtils.WEEK_MON + "-"
				+ (StringUtils.isBlank(tmWorkschedule.getMon()) ? "OFF" : tmWorkschedule.getMon()));
		weekAvalueList.add(ConstantUtils.WEEK_TUE + "-"
				+ (StringUtils.isBlank(tmWorkschedule.getTue()) ? "OFF" : tmWorkschedule.getTue()));
		weekAvalueList.add(ConstantUtils.WEEK_WED + "-"
				+ (StringUtils.isBlank(tmWorkschedule.getWed()) ? "OFF" : tmWorkschedule.getWed()));
		weekAvalueList.add(ConstantUtils.WEEK_THUR + "-"
				+ (StringUtils.isBlank(tmWorkschedule.getThu()) ? "OFF" : tmWorkschedule.getThu()));
		weekAvalueList.add(ConstantUtils.WEEK_FRI + "-"
				+ (StringUtils.isBlank(tmWorkschedule.getFri()) ? "OFF" : tmWorkschedule.getFri()));
		weekAvalueList.add(ConstantUtils.WEEK_SAT + "-"
				+ (StringUtils.isBlank(tmWorkschedule.getSat()) ? "OFF" : tmWorkschedule.getSat()));
		weekAvalueList.add(ConstantUtils.WEEK_SUN + "-"
				+ (StringUtils.isBlank(tmWorkschedule.getSun()) ? "OFF" : tmWorkschedule.getSun()));
		return weekAvalueList;
	}

	@Override
	public String checkWorkScheduleNo(String code, String param) {
		TmWorkschedule tmWorkschedule = new TmWorkschedule();
		tmWorkschedule.setNo(param);
		List<TmWorkschedule> tmWorkschedules = findByEg(tmWorkschedule);
		String result = "success";
		if (tmWorkschedules.size() > 0 && !param.equals(code)) {
			result = "fail";
		}
		return result;
	}

	@Override
	public Integer verifyUniqueValues(TmWorkschedule tmWorkschedule) {
		List<TmWorkschedule> tmWorkschedules = new ArrayList<TmWorkschedule>();
		Integer workscheduleId = null;
		if (tmWorkschedule.getEnabled().equals(ConstantUtils.TYPE_CODE_ENABLED_ON)) {
			tmWorkschedules = findByEg(tmWorkschedule);
		}
		if (tmWorkschedules != null && tmWorkschedules.size() > 0) {
			workscheduleId = tmWorkschedules.get(0).getId();
		}
		return workscheduleId;
	}

	@Override
	public String checkIsGenerateWorkTime(String ids, String isCoveredValue) {
		String[] tmWorkscheduleIds = ids.split(",");
		String[] dates = null;
		int distanceWeekendDays = new Long(DateUtils.dateDiff(new Date(), DateUtils.getEndDayOfWeek())).intValue();
		StringBuffer finallyWorkTimIds = new StringBuffer();
		for (int i = 0; i < tmWorkscheduleIds.length; i++) {
			TmWorkschedule tmWorkschedule = findById(Integer.valueOf(tmWorkscheduleIds[i]));
			List<String> weekAvalueList = getWeekAvalueList(tmWorkschedule);
			int needPlusDay = 0;
			if (StringUtils.isNotBlank(isCoveredValue)) {
				if ("Y".equals(isCoveredValue)) {
					// 覆盖当天则加1
					// =====>>>>>>>修改bug时添加========
					distanceWeekendDays += 1;
					dates = new String[distanceWeekendDays];
					for (int j = 0; j < distanceWeekendDays; j++) {
						dates[j] = date.format(DateUtils.getPlusDay(j));
					}
				} else {
					dates = new String[distanceWeekendDays];
					for (int j = 1; j <= distanceWeekendDays; j++) {
						dates[j - 1] = date.format(DateUtils.getPlusDay(j));
					}
					// ==========修改bug时添加<<<<<========
				}
			} else {
				
				// ----->>>>>>运行逻辑没看懂，但运行形式是对的-----
				needPlusDay = distanceWeekendDays;
				distanceWeekendDays = 7;
				dates = new String[distanceWeekendDays];
				if (distanceWeekendDays >= 1) {
					for (int j = 6; j > 7 - distanceWeekendDays - 1; j--) {
						dates[j] = date.format(DateUtils.getPlusDay(distanceWeekendDays + needPlusDay));
						needPlusDay--;
					}
				}
				// -----思维逻辑没看懂，但运行形式是对的<<<<<<<-----
			}

			if(null != dates && dates.length > 0) {
				for(int k=0;k<dates.length;k++) {
					String weekSrs = "";
					try {
						weekSrs = DateUtils.getWeekDayNameByDateStr(dates[k],DateUtils.FORMAT_DATE_DEFAULT);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					if(!weekAvalueList.contains(weekSrs+"-"+ConstantUtils.TYPE_CODE_ENABLED_ON)) {
						dates[k] = null;
					}
				}
			}
			
			String workTimeIds = worktimeService.queryWorkTimeByParams(tmWorkschedule.getTmPlantId(),
					tmWorkschedule.getTmWorkshopId(), tmWorkschedule.getTmLineId(), tmWorkschedule.getShiftnoId(),
					dates);
			if (StringUtils.isNotBlank(workTimeIds)) {
				finallyWorkTimIds = finallyWorkTimIds.append(workTimeIds + ",");
			}
		}
		return finallyWorkTimIds.toString();
	}
	
	/**
	 * 可满足不限定全局晚班时间段的情况下校验
	 * 
	 * @author ryy
	 * @param tmWorkschedule
	 * @return
	 */
	public String checkTime(TmWorkschedule tmWorkschedule) {
		TmWorkschedule eg = new TmWorkschedule();
		eg.setTmPlantId(tmWorkschedule.getTmPlantId());
		eg.setTmWorkshopId(tmWorkschedule.getTmWorkshopId());
		eg.setTmLineId(tmWorkschedule.getTmLineId());
		eg.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		List<TmWorkschedule> tmWorkschedules = findByEg(eg);
		Integer mS = null; // 早班开始时间
		Integer mE = null; // 早班结束时间
		Integer nS = null; // 中班开始时间
		Integer nE = null; // 中班结束时间
		Integer niS = null; // 晚班开始时间
		Integer niE = null; // 晚班结束时间
		Integer timeOfDay = 240000;

		// 安排班次逻辑：基于车间的不同班次时间段不能重合
		// 设置不同班次的时间段的起始点和终结点
		for (TmWorkschedule workschedule : tmWorkschedules) {
			Integer startTime = Integer.valueOf(timeFormat.format(workschedule.getStartTime()).replaceAll(":", ""));
			Integer endTime = Integer.valueOf(timeFormat.format(workschedule.getEndTime()).replaceAll(":", ""));
			Date startTimeDate = null;
			Date endTimeDate = null;
			try {
				startTimeDate = timeFormat.parse(workschedule.getStartTime());
				endTimeDate = timeFormat.parse(workschedule.getEndTime());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			if (startTimeDate.after(endTimeDate)) {
				if (ConstantUtils.SHIFT_MORNING.equals(workschedule.getShiftnoId())) {
					mS = startTime - timeOfDay;// 当开始时间晚于结束时间，说明时间段跨天，把开始时间重设为负数后(减24hours)，保持相对坐标的唯一性
					mE = endTime;
				} else if (ConstantUtils.SHIFT_NOON.equals(workschedule.getShiftnoId())) {
					nS = startTime - timeOfDay;
					nE = endTime;
				} else {
					niS = startTime - timeOfDay;
					niE = endTime;
				}
			} else {
				if (ConstantUtils.SHIFT_MORNING.equals(workschedule.getShiftnoId())) {
					mS = startTime;
					mE = endTime;
				} else if (ConstantUtils.SHIFT_NOON.equals(workschedule.getShiftnoId())) {
					nS = startTime;
					nE = endTime;
				} else {
					niS = startTime;
					niE = endTime;
				}
			}
		}
		Integer start = null;
		Date startTimeDate = null;
		Date endTimeDate = null;
		try {
			startTimeDate = timeFormat.parse(tmWorkschedule.getStartTime());
			endTimeDate = timeFormat.parse(tmWorkschedule.getEndTime());
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		if (startTimeDate.after(endTimeDate)) {
			start = Integer.valueOf(timeFormat.format(tmWorkschedule.getStartTime()).replaceAll(":", "")) - timeOfDay;
		} else {
			start = Integer.valueOf(timeFormat.format(tmWorkschedule.getStartTime()).replaceAll(":", ""));
		}
		Integer end = Integer.valueOf(timeFormat.format(tmWorkschedule.getEndTime()).replaceAll(":", ""));
		if (tmWorkschedule.getShiftnoId().equals(ConstantUtils.SHIFT_MORNING)) {// 校验早班时间的合理性
			boolean validateMorning = false;
			// 被之前时间模板设置的班次时间点，判断时间是否重合，没有被设置过的时间点，直接返回false，表示没有限制
			boolean flagNoon = (nS == null) ? false : (start > nS && start < nE) || (end > nS && end < nE);
			boolean flagNight = (niS == null) ? false : (start > niS && start < niE) || (end > niS && end < niE);
			validateMorning = flagNoon || flagNight;// true时表示，此班次时间段至少与其他某个班次时间段重合
			if (validateMorning) {
				throw new BusinessException("MORNING_SHIFT_IN_NOON_NIGHT"); // 早班时间不能在中班和晚班时间内！
			}
		} else if (tmWorkschedule.getShiftnoId().equals(ConstantUtils.SHIFT_NOON)) {// 校验中班时间的合理性
			boolean validateNoon = false;
			boolean flagMorning = (mS == null) ? false : (start > mS && start < mE) || (end > mS && end < mE);
			boolean flagNight = (niS == null) ? false : (start > niS && start < niE) || (end > niS && end < niE);
			validateNoon = flagMorning || flagNight;
			if (validateNoon) {
				throw new BusinessException("NOON_SHIFT_IN_MORNING_NIGHT");// 中班时间不能在早班和晚班时间内！
			}
		} else {// 校验晚班时间的合理性
			boolean validateNight = false;
			boolean flagMorning = (mS == null) ? false : (start > mS && start < mE) || (end > mS && end < mE);
			boolean flagNoon = (nS == null) ? false : (start > nS && start < nE) || (end > nS && end < nE);
			validateNight = flagMorning || flagNoon;
			if (validateNight) {
				throw new BusinessException("NIGHT_SHIFT_IN_MORNING_NOON");// 晚班时间不能在早班和中班时间内！
			}
		}
		return "successs";
	}

	@Override
	public String checkTime(TmWorkschedule tmWorkschedule, String[] errors) {
		TmWorkschedule param = new TmWorkschedule();
		param.setTmPlantId(tmWorkschedule.getTmPlantId());
		param.setTmWorkshopId(tmWorkschedule.getTmWorkshopId());
		param.setTmLineId(tmWorkschedule.getTmLineId());
		param.setEnabled(ConstantUtils.TYPE_CODE_ENABLED_ON);
		List<TmWorkschedule> tmWorkschedules = findByEg(param);
		Pattern timePattern = Pattern.compile("\\d{1,2}:\\d{1,2}");
		Matcher startTimeMatcher = timePattern.matcher(tmWorkschedule.getStartTime());
		Matcher endTimeMatcher = timePattern.matcher(tmWorkschedule.getEndTime());
		StringBuffer startBuffer = new StringBuffer();
		StringBuffer endBuffer = new StringBuffer();
		if (startTimeMatcher.find()) {
			for (String s : startTimeMatcher.group().split(":")) {
				startBuffer.append(s);
			}
		}
		if (endTimeMatcher.find()) {
			for (String s : endTimeMatcher.group().split(":")) {
				endBuffer.append(s);
			}
		}
		// 转化成int型"000000"形式
		int startTime = Integer.parseInt(startBuffer.toString());
		int endTime = Integer.parseInt(endBuffer.toString());
		int morningEndTime = 0; // 早班结束时间
		int noonStartTime = 0;// 中班开始时间
		int nightStartTime = 0;// 晚班开始时间
		int nightEndTime = 0;// 晚班结束时间
		String[] nightTimeGlobalSet = globalConfigurationService
				.getValueByKey(ConstantUtils.SYS_CONFIG_NIGHTSHIFT_TIME_SET).split("-");
		for (int i = 0; i < tmWorkschedules.size(); i++) {
			TmWorkschedule workschedule = tmWorkschedules.get(i);
			if (workschedule.getStartTime() != null && workschedule.getEndTime() != null) {
				if (workschedule.getShiftnoId().equals(ConstantUtils.SHIFT_MORNING)) {
					morningEndTime = Integer.parseInt(workschedule.getEndTime().replaceAll(":", ""));
				} else if (workschedule.getShiftnoId().equals(ConstantUtils.SHIFT_NOON)) {
					noonStartTime = Integer.parseInt(workschedule.getStartTime().replaceAll(":", ""));
				}
			}
		}
		String nightTimeStr = nightTimeGlobalSet[0]; // 晚班开始 23:00:00
		String nightEndStr = nightTimeGlobalSet[1]; // 晚班结束 8:00:00
		nightStartTime = Integer.parseInt(nightTimeStr.replaceAll(":", "")); // 230000
		nightEndTime = Integer.parseInt(nightEndStr.replaceAll(":", "")); // 80000
		if (tmWorkschedule.getShiftnoId().equals(ConstantUtils.SHIFT_MORNING)) {
			if (((endTime > noonStartTime && noonStartTime != 0) || (endTime > nightStartTime)
					|| (startTime < nightEndTime))) {
				throw new BusinessException(errors[0]); // 早班时间不能在中班和晚班时间内！
			}
		} else if (tmWorkschedule.getShiftnoId().equals(ConstantUtils.SHIFT_NOON)) {
			if (((startTime < morningEndTime && morningEndTime != 0) || endTime > nightStartTime)
					|| (startTime < nightEndTime)) {
				throw new BusinessException(errors[1]);// 中班时间不能在早班和晚班时间内！
			}
		} else {
			if ((startTime > endTime && (startTime < nightStartTime || endTime > nightEndTime))) {
				throw new BusinessException(errors[2], nightTimeStr + "-" + nightEndStr);// 晚班时间不在{0}之间！
			}
		}

		return "Success";
	}

	@Override
	public Map<String, Object> exportExcelData(HttpServletResponse response, List<TmWorkschedule> list, String filePath,
			String[] header) {
		List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> enabled = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED)) {
			enabled.put(e.getCode(), e.getName());
		}
		Map<String, String> shiftNo = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.SHIFT_TYPE)) {
			shiftNo.put(e.getCode(), e.getName());
		}
		for (int i = 0; i < list.size(); i++) {
			TmWorkschedule workschedule = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			String enabledValue = enabled.get(workschedule.getEnabled());
			workschedule.setEnabled(enabledValue);
			String shiftValue = shiftNo.get(workschedule.getShiftnoId());
			workschedule.setShiftnoId(shiftValue);
			map.put("模板编号", workschedule.getNo());
			map.put("工厂", workschedule.getPlant().getNo() + "-" + workschedule.getPlant().getNameCn());
			map.put("车间", workschedule.getWorkshop().getNo() + "-" + workschedule.getWorkshop().getNameCn());
			map.put("产线", workschedule.getLine().getNo() + "-" + workschedule.getLine().getNameCn());
			map.put("班次", workschedule.getShiftnoId());
			map.put("开始时间", workschedule.getStartTime());
			map.put("结束时间", workschedule.getEndTime());
			map.put("星期一", ("ON".equals(workschedule.getMon()) ? "√" : "×"));
			map.put("星期二", ("ON".equals(workschedule.getTue()) ? "√" : "×"));
			map.put("星期三", ("ON".equals(workschedule.getWed()) ? "√" : "×"));
			map.put("星期四", ("ON".equals(workschedule.getThu()) ? "√" : "×"));
			map.put("星期五", ("ON".equals(workschedule.getFri()) ? "√" : "×"));
			map.put("星期六", ("ON".equals(workschedule.getSat()) ? "√" : "×"));
			map.put("星期日", ("ON".equals(workschedule.getSun()) ? "√" : "×"));
			map.put("计划上线数", workschedule.getPlanOnlineQty() == null ? "" : workschedule.getPlanOnlineQty());
			map.put("计划下线数", workschedule.getPlanOfflineQty() == null ? "" : workschedule.getPlanOfflineQty());
			map.put("参考JPH", workschedule.getJph() == null ? "" : workschedule.getJph());
			map.put("启用", workschedule.getEnabled());
			exportDataList.add(map);
		}
		result = BaseExcelUtil.exportData(response, exportDataList, filePath, header);
		return result;
	}

	/**
	 * 导出工作日历模板及相关联的休息日期
	 * 
	 * @return Map<String,Object>
	 * @author ryy
	 * @Time 2017/5/10
	 */
	@Override
	public Map<String, Object> exportExcelDataAll(HttpServletResponse response, List<TmWorkschedule> list,
			String parentHeadStr, String childHeadStr, String filePath) {

		String[] parentHead = parentHeadStr.split(",");
		String[] childHead = childHeadStr.split(",");
		List<Map<String, Object>> parentExportList = new ArrayList<Map<String, Object>>();
		Map<Integer, List<Map<String, Object>>> childExportMap = new HashMap<Integer, List<Map<String, Object>>>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> enabled = new HashMap<String, String>();
		Map<String, String> shiftType = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED)) {
			enabled.put(e.getCode(), e.getName());
		}

		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.SHIFT_TYPE)) {
			shiftType.put(e.getCode(), e.getName());
		}
		for (int i = 0; i < list.size(); i++) {
			TmWorkschedule tmWorkschedule = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();

			String enabledValue = enabled.get(tmWorkschedule.getEnabled());
			tmWorkschedule.setEnabled(enabledValue);

			String shiftTypeValue = shiftType.get(tmWorkschedule.getShiftnoId());
			tmWorkschedule.setShiftnoId(shiftTypeValue);

			map.put(parentHead[0], tmWorkschedule.getId());
			map.put(parentHead[1], tmWorkschedule.getNo());
			map.put(parentHead[2], tmWorkschedule.getPlant().getNo() + "-" + tmWorkschedule.getPlant().getNameCn());
			map.put(parentHead[3],
					tmWorkschedule.getWorkshop().getNo() + "-" + tmWorkschedule.getWorkshop().getNameCn());
			map.put(parentHead[4], tmWorkschedule.getLine().getNo() + "-" + tmWorkschedule.getLine().getNameCn());
			map.put(parentHead[5], tmWorkschedule.getShiftnoId());
			map.put(parentHead[6], tmWorkschedule.getStartTime());
			map.put(parentHead[7], tmWorkschedule.getEndTime());
			map.put(parentHead[8], ("ON".equals(tmWorkschedule.getMon()) ? "√" : "×"));
			map.put(parentHead[9], ("ON".equals(tmWorkschedule.getTue()) ? "√" : "×"));
			map.put(parentHead[10], ("ON".equals(tmWorkschedule.getWed()) ? "√" : "×"));
			map.put(parentHead[11], ("ON".equals(tmWorkschedule.getThu()) ? "√" : "×"));
			map.put(parentHead[12], ("ON".equals(tmWorkschedule.getFri()) ? "√" : "×"));
			map.put(parentHead[13], ("ON".equals(tmWorkschedule.getSat()) ? "√" : "×"));
			map.put(parentHead[14], ("ON".equals(tmWorkschedule.getSun()) ? "√" : "×"));
			map.put(parentHead[15], tmWorkschedule.getPlanOnlineQty() == null ? "" : tmWorkschedule.getPlanOnlineQty());
			map.put(parentHead[16],
					tmWorkschedule.getPlanOfflineQty() == null ? "" : tmWorkschedule.getPlanOfflineQty());
			map.put(parentHead[17], tmWorkschedule.getJph() == null ? "" : tmWorkschedule.getJph());
			map.put(parentHead[18], tmWorkschedule.getEnabled());

			TmWorkscheduleRest tmWorkscheduleRest = new TmWorkscheduleRest();
			tmWorkscheduleRest.setTmWorkscheduleId(tmWorkschedule.getId());
			List<TmWorkscheduleRest> tmWorkscheduleRests = workscheduleRestService.findByEg(tmWorkscheduleRest);
			List<Map<String, Object>> childExportList = new ArrayList<Map<String, Object>>();
			for (int j = 0; j < tmWorkscheduleRests.size(); j++) {
				Map<String, Object> childMap = new HashMap<String, Object>();
				TmWorkscheduleRest workscheduleRest = tmWorkscheduleRests.get(j);
				childMap.put(childHead[0], workscheduleRest.getStartTime());
				childMap.put(childHead[1], workscheduleRest.getEndTime());
				childExportList.add(childMap);
			}
			childExportMap.put(tmWorkschedule.getId(), childExportList);
			parentExportList.add(map);
		}
		result = BaseExcelUtil.exportDataAll(response, parentExportList, parentHead, childExportMap, childHead,
				filePath);
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public String doImportExcelData(Workbook workbook, HttpServletRequest req) {
		// 覆盖或更新标识
		String repeatOrUpdateFlag = globalConfigurationService
				.getValueByKey(ConstantUtils.SYS_CONFIG_IMP_EXE_UPDATE_WHEN_REPEAT);
		// 回滚标识
		String isRollBack = globalConfigurationService.getValueByKey(ConstantUtils.EXCEL_IMPORT_IS_ALL_ROLLBACK);
		// 工厂Map
		Map<String, TmPlant> plantMap = new HashMap<String, TmPlant>();
		for (TmPlant e : plantService.findAll()) {
			plantMap.put(e.getNo() + "-" + e.getNameCn(), e);
		}
		// 车间Map
		Map<String, TmWorkshop> workShopMap = new HashMap<String, TmWorkshop>();
		for (final TmWorkshop e : workshopService.findAll()) {
			workShopMap.put(e.getTmPlantId() + e.getNo() + "-" + e.getNameCn(), e);
		}
		// 产线Map
		Map<String, TmLine> lineMap = new HashMap<String, TmLine>();
		for (TmLine e : lineService.findAll()) {
			lineMap.put(e.getTmPlantId() + "-" + e.getTmWorkshopId() + e.getNo() + "-" + e.getNameCn(), e);
		}
		// 已经存在的工作日历模板Map
		Map<String, TmWorkschedule> existTmWorkscheduleMap = new HashMap<String, TmWorkschedule>();
		for (TmWorkschedule workschedule : findAll()) {
			// 模板编号唯一码
			existTmWorkscheduleMap.put(workschedule.getNo(), workschedule);
		}

		// 班次map
		Map<String, String> shiftMap = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.SHIFT_TYPE)) {
			shiftMap.put(e.getName(), e.getCode());
		}
		// 是否启用map
		Map<String, String> enableMap = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED)) {
			enableMap.put(e.getName(), e.getCode());
		}

		// 需要新增的数据集合
		List<TmWorkschedule> addList = new ArrayList<TmWorkschedule>();
		// 需要修改的数据的Map
		Map<Integer, TmWorkschedule> updateMap = new HashMap<Integer, TmWorkschedule>();
		// 格式错误的信息
		List<String> errorInfos = new ArrayList<String>();
		// 发生异常的报错信息集合
		String[] errors = { "MORNING_SHIFT_IN_NOON_NIGHT", "NOON_SHIFT_IN_MORNING_NIGHT",
				"NIGHT_SHIFT_IN_MORNING_NOON" };
		// "第"
		final String DI = getMessage(req, "DI");
		// 读取第一章表格内容
		final Sheet sheet = workbook.getSheetAt(0);
		String value = null;
		boolean isExist;
		TmLine mapVal;
		String weekday = null;
		Row row;
		String eL = "^[\\w/-]+$";
		Pattern p = Pattern.compile(eL);
		Matcher m = null;
		int judgeSize = 18;// 数据表格的列数（字段数）
		int totalInt = 0;
		// 循环输出表格中的内容
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i); // 获得行
			int index = 0;
			// 标记初始化
			isExist = false;
			mapVal = null;
			TmWorkschedule entity = new TmWorkschedule();

			String no = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isBlank(no)) {// 第一格为空
				// 整行为空时，导入操作中断
				if (BaseExcelUtil.isAllLineBlank(row, judgeSize)) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "IMPORT_WHOLE_LINE_BLANK"));
					break;
				}
			}
			totalInt++;
			// 编号校验
			if (StringUtils.isBlank(no)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SCHEDULE_NO_REQUIRED"));
				continue;
			}
			if (!Pattern.compile("^[\\w/-]+$").matcher(no).matches()) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SCHEDULE_NO_FORMAT_ERROR"));
				continue;
			}
			entity.setNo(no);
			// 工厂校验
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SCHEDULE_PLANT_REQUIRED"));
				continue;
			}
			if (!plantMap.containsKey(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SCHEDULE_PLANT_NOT_EXIST"));
				continue;
			}
			// 判断工厂是否已启用
			if (ConstantUtils.TYPE_CODE_ENABLED_OFF.equals(plantMap.get(value).getEnabled())) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SCHEDULE_PLANT_UN_ENABLED"));
				continue;
			}
			entity.setTmPlantId(plantMap.get(value).getId());

			// 车间
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SCHEDULE_WORKSHOP_REQUIRED"));
				continue;
			}
			// 判断该工厂下是否有该车间
			if (!workShopMap.containsKey(entity.getTmPlantId() + value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SCHEDULE_WORKSHOP_NOT_EXIST"));
				continue;
			}
			// 判断车间是否已启用
			if (ConstantUtils.TYPE_CODE_ENABLED_OFF
					.equals(workShopMap.get(entity.getTmPlantId() + value).getEnabled())) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SCHEDULE_WORKSHOP_UN_ENABLED"));
				continue;
			}
			entity.setTmWorkshopId(workShopMap.get(entity.getTmPlantId() + value).getId());

			// 产线编号
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SCHEDULE_LINE_REQUIRED"));
				continue;
			}
			// 判断产线是否存在
			isExist = lineMap.containsKey(entity.getTmPlantId() + "-" + entity.getTmWorkshopId() + value);
			if (isExist) {
				mapVal = lineMap.get(entity.getTmPlantId() + "-" + entity.getTmWorkshopId() + value);
			}

			// 产线不存在
			if (!isExist) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SCHEDULE_LINE_NOT_EXIST"));
				continue;
			}
			// 判断产线是否已启用
			if (ConstantUtils.TYPE_CODE_ENABLED_OFF.equals(mapVal.getEnabled())) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SCHEDULE_LINE_UN_ENABLED"));
				continue;
			}
			entity.setTmLineId(mapVal.getId());

			// ========= 班 次 校 验 ==========
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SCHEDULE_SHIFT_REQUIRED"));
				continue;
			}
			// 判断班次是否符合定义类型
			if (null == shiftMap.get(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SCHEDULE_SHIFT_ERROR"));
				continue;
			}
			entity.setShiftnoId(shiftMap.get(value));

			// =========开始时间==========
			eL = "^(?:[01]\\d|2[0-3])(?::[0-5]\\d){1,2}$";
			index++;
			p = Pattern.compile(eL);
			String startTimeStr = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isBlank(startTimeStr)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SCHEDULE_START_TIME_REQUIRED"));
				continue;
			}

			m = p.matcher(startTimeStr);
			if (!m.matches()) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SCHEDULE_START_TIME_ERROR"));
				continue;
			}
			entity.setStartTime(startTimeStr);

			// =========结束时间=========
			index++;
			String endTimeStr = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isBlank(endTimeStr)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SCHEDULE_END_TIME_REQUIRED"));
				continue;
			}
			m = p.matcher(endTimeStr);
			if (!m.matches()) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SCHEDULE_END_TIME_ERROR"));
				continue;
			}
			entity.setEndTime(endTimeStr);

			// =======周一到周日的验证========
			// 周一
			index++;
			weekday = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(weekday)) {
				if (!"√".equals(weekday) && !"×".equals(weekday)) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SCHEDULE_MON_FORMAT_ERROR"));
					continue;
				}
				entity.setMon("√".equals(weekday) ? "ON" : "OFF");
			} else {
				entity.setMon("OFF");
			}
			// 周二
			index++;
			weekday = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(weekday)) {
				if (!"√".equals(weekday) && !"×".equals(weekday)) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SCHEDULE_TUE_FORMAT_ERROR"));
					continue;
				}
				entity.setTue("√".equals(weekday) ? "ON" : "OFF");
			} else {
				entity.setTue("OFF");
			}
			// 周三
			index++;
			weekday = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(weekday)) {
				if (!"√".equals(weekday) && !"×".equals(weekday)) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SCHEDULE_WED_FORMAT_ERROR"));
					continue;
				}
				entity.setWed("√".equals(weekday) ? "ON" : "OFF");
			} else {
				entity.setWed("OFF");
			}
			// 周四
			index++;
			weekday = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(weekday)) {
				if (!"√".equals(weekday) && !"×".equals(weekday)) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SCHEDULE_THU_FORMAT_ERROR"));
					continue;
				}
				entity.setThu("√".equals(weekday) ? "ON" : "OFF");
			} else {
				entity.setThu("OFF");
			}
			// 周五
			index++;
			weekday = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(weekday)) {
				if (!"√".equals(weekday) && !"×".equals(weekday)) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SCHEDULE_FRI_FORMAT_ERROR"));
					continue;
				}
				entity.setFri("√".equals(weekday) ? "ON" : "OFF");
			} else {
				entity.setFri("OFF");
			}
			// 周六
			index++;
			weekday = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(weekday)) {
				if (!"√".equals(weekday) && !"×".equals(weekday)) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SCHEDULE_SAT_FORMAT_ERROR"));
					continue;
				}
				entity.setSat("√".equals(weekday) ? "ON" : "OFF");
			} else {
				entity.setSat("OFF");
			}
			// 周日
			index++;
			weekday = LoadUtils.getCell(row, index);
			if (StringUtils.isNotBlank(weekday)) {
				if (!"√".equals(weekday) && !"×".equals(weekday)) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SCHEDULE_SUN_FORMAT_ERROR"));
					continue;
				}
				entity.setSun("√".equals(weekday) ? "ON" : "OFF");
			} else {
				entity.setSun("OFF");
			}

			// =========计划上线数=========
			index++;
			value = LoadUtils.getCell(row, index).trim();
			eL = "^\\d+$";
			p = Pattern.compile(eL);
			if (StringUtils.isNotBlank(value)) {
				m = p.matcher(value);
				if (!m.matches()) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SCHEDULE_ONLINE_QTY_DIGITAL"));
					continue;
				}
				entity.setPlanOnlineQty(Integer.parseInt(value));
			}

			// =========计划下线数=========
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isNotBlank(value)) {
				m = p.matcher(value);
				if (!m.matches()) {
					errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SCHEDULE_OFFLINE_QTY_DIGITAL"));
					continue;
				}
				entity.setPlanOfflineQty(Integer.parseInt(value));
			}

			// =========参考JPH=========
			index++;
			value = LoadUtils.getCell(row, index).trim();
			entity.setJph(value);

			// =========是 否 启 用========
			index++;
			value = LoadUtils.getCell(row, index).trim();
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SCHEDULE_ENABLED_REQUIRED"));
				continue;
			}
			if (null == enableMap.get(value)) {
				errorInfos.add(DI + (i + 1) + getMessage(req, "WORK_SCHEDULE_ENABLED_ERROR"));
				continue;
			}
			entity.setEnabled(enableMap.get(value));

			// 校验早、中、晚班时间重叠性
			try {
				checkTime(entity, errors);
			} catch (BusinessException e) {
				if (getMessage(req, e.getMessage()).contains("{0}")) {
					errorInfos.add(DI + (i + 1) + "行：晚班时间不在" + e.getParams()[0] + "之间！");
					continue;
				} else {
					errorInfos.add(DI + (i + 1) + "行:" + getMessage(req, e.getMessage()));
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return getMessage(req, "IMPORT_UNKNOWN_EXCEPTION");
			}
			// 新增还是更新，放入各自集合中
			if (existTmWorkscheduleMap.get(entity.getNo()) == null) {
				addList.add(entity);
			} else {
				updateMap.put(i + 1, entity);
			}
		}

		List<TmWorkschedule> updateList = needUpdateEntitys(updateMap);
		StringBuffer addCount = new StringBuffer();
		StringBuffer updateCount = new StringBuffer();
		try {
			batchImport(addList, ConstantUtils.IMPORT_BATCH_COUNT, ConstantUtils.OPERATION_INSERT, addCount);
			// 修改数据
			if ("true".equals(repeatOrUpdateFlag)) {
				batchImport(updateList, ConstantUtils.IMPORT_BATCH_COUNT, null, updateCount);
			}
		} catch (Exception e) {
			if ("true".equals(isRollBack)) {
				throw new BusinessException("IMPORT_DATA_VALID_ERROR_INFO",
						getMessage(req, "IMPORT_UNKNOWN_EXCEPTION"));
			} else {
				return getMessage(req, "IMPORT_UNKNOWN_EXCEPTION");
			}
		}
		// 导入日志错误信息，页面提醒
		Set<Integer> repeatLindexes = updateMap.keySet();
		Map<String, Object> logsAndMsgTip = BaseExcelUtil.getLogsAndMsgTip(repeatOrUpdateFlag, addCount, updateCount,
				totalInt, repeatLindexes, errorInfos, req, getMessage(req, "WORK_SCHEDULE_MANAGEMENT"));

		importLogService.doSaveBatch((List<ImportLog>) logsAndMsgTip.get("logs"));
		return (String) logsAndMsgTip.get("msgTip");
	}

	private void batchImport(List<TmWorkschedule> list, int num, String insert, StringBuffer countBuffer) {
		int successCount = 0;
		if (list.size() > 0) {
			List<List<TmWorkschedule>> parts = SpiltUtils.averageAssign(list, num);
			try {
				for (int i = 0; i < parts.size(); i++) {
					if ("insert".equals(insert)) {
						doSaveBatch(parts.get(i));
						successCount += parts.get(i).size();
					} else {
						doUpdateBatch(parts.get(i));
						successCount += parts.get(i).size();
					}
				}
				countBuffer.append(successCount);
			} catch (Exception e) {
				countBuffer.append(successCount);
				throw new RuntimeException();
			}
		} else {
			countBuffer.append(successCount);
		}
	}

	private List<TmWorkschedule> needUpdateEntitys(Map<Integer, TmWorkschedule> updateTmWorkscheduleMap) {
		List<TmWorkschedule> updateList = new ArrayList<TmWorkschedule>();
		for (Integer key : updateTmWorkscheduleMap.keySet()) {
			TmWorkschedule insert = updateTmWorkscheduleMap.get(key);
			TmWorkschedule workschedule = new TmWorkschedule();
			workschedule.setNo(insert.getNo());
			TmWorkschedule newData = findUniqueByEg(workschedule);
			newData.setStartTime(insert.getStartTime());
			newData.setEndTime(insert.getEndTime());
			newData.setEnabled(insert.getEnabled());
			newData.setShiftnoId(insert.getShiftnoId());
			newData.setMon(insert.getMon());
			newData.setTue(insert.getTue());
			newData.setWed(insert.getWed());
			newData.setThu(insert.getThu());
			newData.setFri(insert.getFri());
			newData.setSat(insert.getSat());
			newData.setSun(insert.getSun());
			updateList.add(newData);
		}
		return updateList;
	}

	/**
	 * @desc 级联导入工作日历模板及休息时间
	 * @date 17/06/13
	 * @author ryy
	 */
	@Override
	@SuppressWarnings("unchecked")
	public String doImportExcelDataAll(Workbook workbook, ExcelImportPojo excelImportPojo, HttpServletRequest request) {

		Map<String, Object> resultMap = null;
		try {
			resultMap = BaseExcelUtil.readExcelDataAll(workbook, excelImportPojo);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		// 读取excel中的工作日历模板
		Map<Integer, Object> workscheduleMap = (Map<Integer, Object>) resultMap.get("parentMap");
		// 读取excel中的工作日历模板休息时间
		Map<Integer, Object> workscheduleRestMap = (Map<Integer, Object>) resultMap.get("childrenMap");
		// 覆盖或更新标识
		String repeatOrUpdateFlag = globalConfigurationService
				.getValueByKey(ConstantUtils.SYS_CONFIG_IMP_EXE_UPDATE_WHEN_REPEAT);
		// 回滚标识
		String isRollBack = globalConfigurationService.getValueByKey(ConstantUtils.EXCEL_IMPORT_IS_ALL_ROLLBACK);
		// 工厂Map
		Map<String, TmPlant> plantMap = new HashMap<String, TmPlant>();
		for (TmPlant e : plantService.findAll()) {
			plantMap.put(e.getNo() + "-" + e.getNameCn(), e);
		}
		// 车间Map
		Map<String, TmWorkshop> workShopMap = new HashMap<String, TmWorkshop>();
		for (final TmWorkshop e : workshopService.findAll()) {
			workShopMap.put(e.getTmPlantId() + "-" + e.getNo() + "-" + e.getNameCn(), e);
		}
		// 产线Map
		Map<String, TmLine> lineMap = new HashMap<String, TmLine>();
		for (TmLine e : lineService.findAll()) {
			lineMap.put(e.getTmPlantId() + "-" + e.getTmWorkshopId() + "-" + e.getNo() + "-" + e.getNameCn(), e);
		}
		// 错误信息集合
		List<String> errorInfos = new ArrayList<String>();

		// 已经存在的工作日历模板Map
		Map<String, TmWorkschedule> existWorkscheduleMap = new HashMap<String, TmWorkschedule>();
		for (TmWorkschedule workschedule : findAll()) {
			// 将编号作为唯一校验
			existWorkscheduleMap.put(workschedule.getNo(), workschedule);
		}
		// 班次map
		Map<String, String> shiftMap = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.SHIFT_TYPE)) {
			shiftMap.put(e.getName(), e.getCode());
		}
		// 星期map
		Map<String, String> weekMap = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.WEEK_VALUE)) {
			weekMap.put(e.getName(), e.getCode());
		}
		// 是否启用map
		Map<String, String> enableMap = new HashMap<String, String>();
		for (DictEntry e : entryService.getEntryByTypeCode(ConstantUtils.TYPE_CODE_ENABLED)) {
			enableMap.put(e.getName(), e.getCode());
		}
		// 第
		final String DI = getMessage(request, "DI");
		String value = null;
		// 发生异常的报错信息集合
		String[] errors = { "MORNING_SHIFT_IN_NOON_NIGHT", "NOON_SHIFT_IN_MORNING_NIGHT",
				"NIGHT_SHIFT_IN_MORNING_NOON" };
		// 保存成功、更新成功，主表记录总条数的计数器
		int addCountInt = 0;
		int updateCountInt = 0;
		int totalInt = 0;
		// 重复行标记录器
		Set<Integer> repeatLindexes = new TreeSet<Integer>();
		for (Integer key : workscheduleMap.keySet()) {
			totalInt++;
			TmWorkschedule tmWorkschedule = (TmWorkschedule) workscheduleMap.get(key);

			// ========================== 主 表 ===========================

			// ------模板编号校验------
			value = tmWorkschedule.getNo();
			// 是否为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_NO_REQUIRED"));
				continue;
			}

			if (!Pattern.compile("[\\w-/]+").matcher(value).matches()
					|| Pattern.compile("[\u4e00-\u9fa5]").matcher(value).find()) {
				errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_NO_FORMAT_ERROR"));
				continue;
			}
			// ------- 工 厂校 验 ------
			value = tmWorkschedule.getPlant().getNameCn();
			// 工厂为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_PLANT_REQUIRED"));
				continue;
			}
			// 工厂不存在
			if (!plantMap.containsKey(value)) {
				errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_PLANT_NOT_EXIST"));
				continue;
			}

			// 判断工厂是否已启用
			if (ConstantUtils.TYPE_CODE_ENABLED_OFF.equals(plantMap.get(value).getEnabled())) {
				errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_PLANT_UN_ENABLED"));
				continue;
			}
			tmWorkschedule.setTmPlantId(plantMap.get(value).getId());

			// ----------- 车 间 校 验 -------------

			value = tmWorkschedule.getWorkshop().getNameCn();
			// 车间为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_WORKSHOP_REQUIRED"));
				continue;
			}
			// 车间是否存在
			if (workShopMap.get(tmWorkschedule.getTmPlantId() + "-" + value) == null) {
				errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_WORKSHOP_NOT_EXIST"));
				continue;
			}
			// 判断车间是否已启用
			if (ConstantUtils.TYPE_CODE_ENABLED_OFF
					.equals(workShopMap.get(tmWorkschedule.getTmPlantId() + "-" + value).getEnabled())) {
				errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_WORKSHOP_UN_ENABLED"));
				continue;
			}
			tmWorkschedule.setTmWorkshopId(workShopMap.get(tmWorkschedule.getTmPlantId() + "-" + value).getId());

			// ------------ 产 线 校 验 -------------

			value = tmWorkschedule.getLine().getNameCn();
			// 产线为空
			if (StringUtils.isBlank(value)) {
				errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_LINE_REQUIRED"));
				continue;
			}
			// 判断产线是否存在
			if (lineMap.get(
					tmWorkschedule.getTmPlantId() + "-" + tmWorkschedule.getTmWorkshopId() + "-" + value) == null) {
				errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_LINE_NOT_EXIST"));
				continue;
			}
			// 判断产线是否已启用
			if (ConstantUtils.TYPE_CODE_ENABLED_OFF.equals(
					lineMap.get(tmWorkschedule.getTmPlantId() + "-" + tmWorkschedule.getTmWorkshopId() + "-" + value)
							.getEnabled())) {
				errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_LINE_UN_ENABLED"));
				continue;
			}
			tmWorkschedule.setTmLineId(lineMap
					.get(tmWorkschedule.getTmPlantId() + "-" + tmWorkschedule.getTmWorkshopId() + "-" + value).getId());

			// ---------------- 班 次 --------------

			String shiftnoId = tmWorkschedule.getShiftnoId();
			// 当班次有值时判断是否符合定义类型
			if (StringUtils.isNotBlank(shiftnoId)) {
				if (null == shiftMap.get(shiftnoId)) {
					errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_SHIFT_ERROR"));
					continue;
				} else {
					tmWorkschedule.setShiftnoId(shiftMap.get(shiftnoId));
				}
			} else {
				errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_SHIFT_REQUIRED"));
				continue;
			}

			// ---------------- 开始时间、结束时间 --------------
			String startTime = tmWorkschedule.getStartTime();
			String eL = "^(?:[01]\\d|2[0-3])(?::[0-5]\\d){1,2}$";
			Pattern p = Pattern.compile(eL);
			Matcher m = null;
			if (null != startTime) {
				m = p.matcher(startTime);
				if (!m.matches()) {
					errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_START_TIME_ERROR"));
					continue;
				}
			} else {
				errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_START_TIME_REQUIRED"));
				continue;
			}
			String endTime = tmWorkschedule.getEndTime();
			if (null != endTime) {
				m = p.matcher(endTime);
				if (!m.matches()) {
					errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_END_TIME_ERROR"));
					continue;
				}
			} else {
				errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_END_TIME_REQUIRED"));
				continue;
			}
			Date startTimeDate = null;
			Date endTimeDate = null;
			try {
				startTimeDate = timeFormat.parse(startTime);
				endTimeDate = timeFormat.parse(endTime);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			// 校验早班、中班开始时间与结束时间的先后顺序
			if ((ConstantUtils.SHIFT_MORNING.equals(tmWorkschedule.getShiftnoId())
					|| ConstantUtils.SHIFT_NOON.equals(tmWorkschedule.getShiftnoId()))
					&& startTimeDate.after(endTimeDate)) {
				errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_START_TIME_AFTER_END_TIME"));
				continue;
			}

			// ---------------- 星 期 几 --------------
			// 星期一
			String mon = tmWorkschedule.getMon();
			if (StringUtils.isNotBlank(mon)) {
				if (!"√".equals(mon) && !"×".equals(mon)) {
					errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_MON_FORMAT_ERROR"));
					continue;
				}
				tmWorkschedule.setMon("√".equals(mon) ? "ON" : "OFF");
			} else {
				tmWorkschedule.setMon("OFF");
			}
			// 星期二
			String tue = tmWorkschedule.getTue();
			if (StringUtils.isNotBlank(tue)) {
				if (!"√".equals(tue) && !"×".equals(tue)) {
					errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_TUE_FORMAT_ERROR"));
					continue;
				}
				tmWorkschedule.setTue("√".equals(tue) ? "ON" : "OFF");
			} else {
				tmWorkschedule.setTue("OFF");
			}
			// 星期三
			String wed = tmWorkschedule.getWed();
			if (StringUtils.isNotBlank(wed)) {
				if (!"√".equals(wed) && !"×".equals(wed)) {
					errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_WED_FORMAT_ERROR"));
					continue;
				}
				tmWorkschedule.setWed("√".equals(wed) ? "ON" : "OFF");
			} else {
				tmWorkschedule.setWed("OFF");
			}
			String thu = tmWorkschedule.getThu();
			if (StringUtils.isNotBlank(thu)) {
				if (!"√".equals(thu) && !"×".equals(thu)) {
					errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_THU_FORMAT_ERROR"));
					continue;
				}
				tmWorkschedule.setThu("√".equals(thu) ? "ON" : "OFF");
			} else {
				tmWorkschedule.setThu("OFF");
			}
			String fri = tmWorkschedule.getFri();
			if (StringUtils.isNotBlank(fri)) {
				if (!"√".equals(fri) && !"×".equals(fri)) {
					errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_FRI_FORMAT_ERROR"));
					continue;
				}
				tmWorkschedule.setFri("√".equals(fri) ? "ON" : "OFF");
			} else {
				tmWorkschedule.setFri("OFF");
			}
			String sat = tmWorkschedule.getSat();
			if (StringUtils.isNotBlank(sat)) {
				if (!"√".equals(sat) && !"×".equals(sat)) {
					errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_SAT_FORMAT_ERROR"));
					continue;
				}
				tmWorkschedule.setSat("√".equals(sat) ? "ON" : "OFF");
			} else {
				tmWorkschedule.setSat("OFF");
			}
			String sun = tmWorkschedule.getSun();
			if (StringUtils.isNotBlank(sun)) {
				if (!"√".equals(sun) && !"×".equals(sun)) {
					errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_SUN_FORMAT_ERROR"));
					continue;
				}
				tmWorkschedule.setSun("√".equals(sun) ? "ON" : "OFF");
			} else {
				tmWorkschedule.setSun("OFF");
			}
			Integer planOnlineQty = tmWorkschedule.getPlanOnlineQty();
			eL = "^\\d+$";
			p = Pattern.compile(eL);
			if (null != planOnlineQty) {
				m = p.matcher(String.valueOf(planOnlineQty));
				if (!m.matches()) {
					errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_ONLINE_QTY_DIGITAL"));
					continue;
				}
			}
			Integer planOfflineQty = tmWorkschedule.getPlanOfflineQty();
			if (null != planOfflineQty) {
				m = p.matcher(String.valueOf(planOfflineQty));
				if (!m.matches()) {
					errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_OFFLINE_QTY_DIGITAL"));
					continue;
				}
			}
			String enable = tmWorkschedule.getEnabled();
			if (StringUtils.isBlank(enable)) {
				errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_ENABLED_REQUIRED"));
				continue;
			}
			if (null == enableMap.get(enable)) {
				errorInfos.add(DI + (key + 1) + getMessage(request, "WORK_SCHEDULE_ENABLED_ERROR"));
				continue;
			}
			tmWorkschedule.setEnabled(enableMap.get(enable));
			// 校验早、中、晚班时间重叠性
			try {
				checkTime(tmWorkschedule, errors);
			} catch (BusinessException e) {
				if (getMessage(request, e.getMessage()).contains("{0}")) {
					errorInfos.add(DI + (key + 1) + "行：晚班时间不在" + e.getParams()[0] + "之间！");
					continue;
				} else {
					errorInfos.add(DI + (key + 1) + "行:" + getMessage(request, e.getMessage()));
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return getMessage(request, "IMPORT_UNKNOWN_EXCEPTION");
			}

			String checkKey = tmWorkschedule.getNo();
			TmWorkschedule existWorkschedule = existWorkscheduleMap.get(checkKey);
			// repeatOrUpdateFlag为true表示更新
			if ("true".equals(repeatOrUpdateFlag)) {
				String childrenErrMsg = null;
				try {
					String operation = null;
					if (null == existWorkschedule) {
						operation = "insert";
						childrenErrMsg = saveWorkscheduleRest(operation, tmWorkschedule, key, workscheduleRestMap,
								request);
					} else {
						operation = "update";
						childrenErrMsg = saveWorkscheduleRest(operation, tmWorkschedule, key, workscheduleRestMap,
								request);
					}
					if (childrenErrMsg != null) {
						errorInfos.add(childrenErrMsg);
						continue;
					}
					if ("insert".equals(operation)) {
						addCountInt++;
					} else {
						updateCountInt++;
					}

				} catch (Exception e) {
					e.printStackTrace();
					if ("true".equals(isRollBack)) {
						throw new BusinessException("IMPORT_UNKNOWN_EXCEPTION");
					} else {
						return getMessage(request, "IMPORT_UNKNOWN_EXCEPTION");
					}
				}
			} else {
				if (existWorkschedule != null) {
					// 记录重复数据的行号
					repeatLindexes.add(key + 1);
				}
			}
		}
		if (resultMap.get("blankLineInfo") != null) {
			errorInfos.add((String) resultMap.get("blankLineInfo"));
		}
		StringBuffer addCount = new StringBuffer().append(addCountInt);
		StringBuffer updateCount = new StringBuffer().append(updateCountInt);
		Map<String, Object> logsAndMsgTip = BaseExcelUtil.getLogsAndMsgTip(repeatOrUpdateFlag, addCount, updateCount,
				totalInt, repeatLindexes, errorInfos, request, getMessage(request, "TM_WORKSCHEDULE_MANAGEMENT"));

		importLogService.doSaveBatch((List<ImportLog>) logsAndMsgTip.get("logs"));
		return (String) logsAndMsgTip.get("msgTip");
	}

	private String saveWorkscheduleRest(String operation, TmWorkschedule tmWorkschedule, Integer key,
			Map<Integer, Object> workscheduleRestMap, HttpServletRequest request) {

		List<TmWorkscheduleRest> needSaveWorkscheduleRests = new ArrayList<TmWorkscheduleRest>();
		@SuppressWarnings("unchecked")
		List<TmWorkscheduleRest> tmWorkscheduleRestList = (List<TmWorkscheduleRest>) workscheduleRestMap.get(key);
		String startTime = tmWorkschedule.getStartTime();
		String endTime = tmWorkschedule.getEndTime();
		Integer workscheduleStart = Integer.parseInt(startTime.replaceAll(":", ""));
		Integer workscheduleEnd = Integer.parseInt(endTime.replaceAll(":", ""));
		String result = null;

		if (null != tmWorkscheduleRestList && tmWorkscheduleRestList.size() > 0) {
			// 子表记录不为空时
			for (int i = 0; i < tmWorkscheduleRestList.size(); i++) {
				TmWorkscheduleRest tmWorkscheduleRest = tmWorkscheduleRestList.get(i);
				Integer startRestTime = Integer
						.parseInt(timeFormat.format(tmWorkscheduleRest.getStartTime()).replaceAll(":", ""));
				Integer endRestTime = Integer
						.parseInt(timeFormat.format(tmWorkscheduleRest.getEndTime()).replaceAll(":", ""));
				if (null != tmWorkscheduleRest.getStartTime()) {
					if (startRestTime < workscheduleStart || startRestTime > workscheduleEnd) {
						result = "第" + (key + 2 + i) + getMessage(request, "WORK_SCHEDULE_START_REST_ERROR");
						return result;
					}
				}
				if (null != tmWorkscheduleRest.getEndTime()) {
					if (endRestTime < workscheduleStart || endRestTime > workscheduleEnd) {
						result = "第" + (key + 2 + i) + getMessage(request, "WORK_SCHEDULE_END_REST_ERROR");
						return result;
					}
				}
				needSaveWorkscheduleRests.add(tmWorkscheduleRest);
			}
		}

		TmWorkschedule saveOrUpdateWorkschedule = null;
		if ("insert".equals(operation)) {
			saveOrUpdateWorkschedule = doSave(tmWorkschedule);
		} else {
			TmWorkschedule eg = new TmWorkschedule();
			eg.setNo(tmWorkschedule.getNo());
			TmWorkschedule findEg = findUniqueByEg(eg);
			tmWorkschedule.setId(findEg.getId());
			saveOrUpdateWorkschedule = doUpdate(tmWorkschedule);
		}

		for (TmWorkscheduleRest rest : needSaveWorkscheduleRests) {
			rest.setTmWorkscheduleId(saveOrUpdateWorkschedule.getId());
		}
		// 当更新主表数据时，才需要删除以前子表数据并添加新的子表数据
		if ("update".equals(operation)) {
			TmWorkscheduleRest needDeleteWorkscheduleRest = new TmWorkscheduleRest();
			needDeleteWorkscheduleRest.setTmWorkscheduleId(saveOrUpdateWorkschedule.getId());
			List<TmWorkscheduleRest> workscheduleRests = workscheduleRestService.findByEg(needDeleteWorkscheduleRest);
			if (workscheduleRests.size() > 0) {
				Integer[] deleteWorkscheduleRestIds = new Integer[workscheduleRests.size()];
				for (int i = 0; i < workscheduleRests.size(); i++) {
					TmWorkscheduleRest delWorkscheduleRest = workscheduleRests.get(i);
					deleteWorkscheduleRestIds[i] = delWorkscheduleRest.getId();
				}
				workscheduleRestService.doDeleteById(deleteWorkscheduleRestIds);
			}
		}
		workscheduleRestService.doSaveBatch(needSaveWorkscheduleRests);
		return null;
	}

}
