package com.wis.mes.production.record.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.context.WebContextHolder;
import com.wis.core.dao.PageResult;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.nc.dao.TmNcDao;
import com.wis.mes.master.nc.dao.TmNcGroupDao;
import com.wis.mes.master.rolemaster.dao.TsRoleMasterSetDao;
import com.wis.mes.master.workcalendar.dao.TmWorktimeDao;
import com.wis.mes.production.record.dao.TpRecordUlocNcDao;
import com.wis.mes.production.record.entity.TpRecordUlocNc;
import com.wis.mes.production.record.service.TpRecordUlocNcService;

@Service
public class TpRecordUlocNcServiceImpl extends BaseServiceImpl<TpRecordUlocNc> implements TpRecordUlocNcService {

    @Autowired
    public void setDao(final TpRecordUlocNcDao dao) {
        this.dao = dao;
    }

    @Autowired
    private TsRoleMasterSetDao roleMasterSetDao;
    @Autowired
    private TmNcDao ncDao;
    @Autowired
    private TmNcGroupDao ncGroupDao;
    @Autowired
    private TmWorktimeDao worktimeDao;

    private static final String ERROR = "ERROR";

    @Override
    public PageResult<TpRecordUlocNc> getNcPageResult(final BootstrapTableQueryCriteria criteria) {
        return ((TpRecordUlocNcDao) dao).getNcPageResult(criteria);
    }

    @Override
    public void doSaveNc(final Integer ncCode, final Integer ncGroup, final String note, final Integer tpRecordUlocId,
            final Integer tpRecordId) {
        final TpRecordUlocNc bean = new TpRecordUlocNc();
        bean.setTmNcId(ncCode);
        bean.setTmNcGroupId(ncGroup);
        bean.setNote(note);
        bean.setTpRecordUlocId(tpRecordUlocId);
        bean.setTpRecordId(tpRecordId);
        doSave(bean);
    }

    @Override
    public List<Map<String, Object>> findByCondition(final String sn, final Integer ncId, final Integer ncGroupId,
            final String startTime, final String endTime) {
        final Integer currUser = WebContextHolder.getCurrentUser().getId();
        return ((TpRecordUlocNcDao) dao).findByCondition(sn, ncId, ncGroupId, startTime, endTime,
                (roleMasterSetDao.getCountByCurrUser(currUser) == 0 ? "1" : "0"), currUser);
    }

    @Override
    public String getNext(final String type, final Integer pageNow) {
        final Gson json = new Gson();
        try {
            if ("nc".equals(type)) {
                return json.toJson(ncDao.findByPage(pageNow));
            }
            return json.toJson(ncGroupDao.findByPage(pageNow));
        } catch (final Exception e) {
            return json.toJson(ERROR);
        }
    }

    @Override
    public String getReportData(final String sn, final String type, final String time, final String startTime,
            final String endTime, final String ncIds) {
        final Gson json = new GsonBuilder().disableHtmlEscaping().create();
        try {
            String str = "";
            final StringBuffer data = new StringBuffer("[");
            final StringBuffer ncData = new StringBuffer("[");
            final List<String> list = new ArrayList<String>();
            final Map<String, Object> map = new HashMap<String, Object>();
            final Integer currUser = WebContextHolder.getCurrentUser().getId();
            final String isAll = roleMasterSetDao.getCountByCurrUser(currUser) == 0 ? "1" : "0";
            final String[] ncArray = ncIds.split(";");
            // 获取不合格
            final Map<Integer, String> nc = new HashMap<Integer, String>();
            for (final Map<String, Object> e : ncDao.findByType(ncIds.replaceAll(";", ","), type)) {
                nc.put(Integer.valueOf(e.get("ID").toString()), e.get("NAME").toString());
            }
            final Map<String, Integer> result = new HashMap<String, Integer>();
            // 月统计
            if ("month".equals(time)) {
                // 获取区间的月份
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                final Date end = sdf.parse(endTime);
                final Calendar temp = Calendar.getInstance();
                temp.setTime(sdf.parse(startTime));
                while (temp.getTime().before(end)) {
                    str = str + "'" + sdf.format(temp.getTime()) + "',";
                    list.add(sdf.format(temp.getTime()));
                    temp.add(Calendar.MONTH, 1);
                }
                str = str + "'" + sdf.format(end) + "',";
                list.add(sdf.format(end));

                // 组装数据
                int i = 1;
                for (final String ncId : ncArray) {
                    for (final Map<String, Object> e : ((TpRecordUlocNcDao) dao).StatisticByCondition(sn,
                            Integer.valueOf(ncId), startTime, endTime, isAll, currUser, type, time)) {
                        result.put(e.get("COL").toString(), Integer.valueOf(e.get("VAL").toString()));
                    }
                    data.append("{'name':'").append(nc.get(Integer.valueOf(ncId))).append("','type':'bar','data':[");
                    ncData.append("{'name':'").append(nc.get(Integer.valueOf(ncId)))
                            .append("','textStyle':{color:'#25c36c'}}");
                    int j = 1;
                    for (final String s : list) {
                        data.append(result.get(s) == null ? 0 : result.get(s));
                        if (j < list.size()) {
                            data.append(",");
                        }
                        j = j + 1;
                    }
                    data.append(
                            "],'markPoint' : {'data' : [{'type' : 'max', 'name': '最大值'},{'type' : 'min', 'name': '最小值'}]},'markLine' : {'data' : [{'type' : 'average', 'name': '平均值'}]}}");
                    if (i < ncArray.length) {
                        data.append(",");
                        ncData.append(",");
                    }
                    i = i + 1;
                    result.clear();
                }
            }
            // 日统计
            if ("day".equals(time)) {
                // 获取区间的日
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                final Date end = sdf.parse(endTime);
                final Calendar temp = Calendar.getInstance();
                temp.setTime(sdf.parse(startTime));
                while (temp.getTime().before(end)) {
                    str = str + "'" + sdf.format(temp.getTime()) + "',";
                    list.add(sdf.format(temp.getTime()));
                    temp.add(Calendar.DAY_OF_MONTH, 1);
                }
                str = str + "'" + sdf.format(end) + "',";
                list.add(sdf.format(end));

                // 组装数据
                int i = 1;
                for (final String ncId : ncArray) {
                    for (final Map<String, Object> e : ((TpRecordUlocNcDao) dao).StatisticByCondition(sn,
                            Integer.valueOf(ncId), startTime, endTime, isAll, currUser, type, time)) {
                        result.put(e.get("COL").toString(), Integer.valueOf(e.get("VAL").toString()));
                    }
                    data.append("{'name':'").append(nc.get(Integer.valueOf(ncId))).append("','type':'bar','data':[");
                    ncData.append("{'name':'").append(nc.get(Integer.valueOf(ncId)))
                            .append("','textStyle':{color:'#25c36c'}}");
                    int j = 1;
                    for (final String s : list) {
                        data.append(result.get(s) == null ? 0 : result.get(s));
                        if (j < list.size()) {
                            data.append(",");
                        }
                        j = j + 1;
                    }
                    data.append(
                            "],'markPoint' : {'data' : [{'type' : 'max', 'name': '最大值'},{'type' : 'min', 'name': '最小值'}]},'markLine' : {'data' : [{'type' : 'average', 'name': '平均值'}]}}");
                    if (i < ncArray.length) {
                        data.append(",");
                        ncData.append(",");
                    }
                    i = i + 1;
                    result.clear();
                }
            }
            // 周统计
            if ("week".equals(time)) {
                // 获取周
                final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                final Date end = sdf.parse(endTime);
                final Calendar temp = Calendar.getInstance();
                temp.setTime(sdf.parse(startTime));
                final List<Map<String, String>> weekList = new ArrayList<Map<String, String>>();
                int co = 0;
                while (temp.getTime().before(end)) {
                    final Map<String, String> week = new HashMap<String, String>();
                    week.put("COL", sdf1.format(temp.getTime()) + " 第" + temp.get(Calendar.WEEK_OF_MONTH) + "周");
                    week.put("START_TIME", sdf.format(temp.getTime()));
                    if (temp.get(Calendar.DAY_OF_WEEK) == 1) {
                        week.put("COL",
                                sdf1.format(temp.getTime()) + " 第" + (temp.get(Calendar.WEEK_OF_MONTH) - 1) + "周");
                        week.put("END_TIME", sdf.format(temp.getTime()));
                        temp.add(Calendar.DATE, 1);
                        weekList.add(week);
                        continue;
                    }
                    if (temp.getActualMaximum(Calendar.DAY_OF_MONTH) - temp.get(Calendar.DAY_OF_MONTH) < 8
                            - temp.get(Calendar.DAY_OF_WEEK)) {
                        co = temp.getActualMaximum(Calendar.DAY_OF_MONTH) - temp.get(Calendar.DAY_OF_MONTH);
                    } else {
                        co = 8 - temp.get(Calendar.DAY_OF_WEEK);
                    }
                    temp.add(Calendar.DATE, co);
                    week.put("END_TIME", sdf.format(temp.getTime()));
                    weekList.add(week);
                    temp.add(Calendar.DATE, 1);
                }
                if (sdf.format(end).equals(sdf.format(temp.getTime()))) {
                    final Map<String, String> week = new HashMap<String, String>();
                    week.put("COL", sdf1.format(temp.getTime()) + " 第" + temp.get(Calendar.WEEK_OF_MONTH) + "周");
                    if (temp.get(Calendar.DAY_OF_WEEK) == 1) {
                        week.put("COL",
                                sdf1.format(temp.getTime()) + " 第" + (temp.get(Calendar.WEEK_OF_MONTH) - 1) + "周");
                    }
                    week.put("START_TIME", sdf.format(temp.getTime()));
                    week.put("END_TIME", sdf.format(temp.getTime()));
                    weekList.add(week);
                }
                // 组装数据
                int i = 1;
                for (final String ncId : ncArray) {
                    data.append("{'name':'").append(nc.get(Integer.valueOf(ncId))).append("','type':'bar','data':[");
                    ncData.append("{'name':'").append(nc.get(Integer.valueOf(ncId)))
                            .append("','textStyle':{color:'#25c36c'}}");
                    int j = 1;
                    for (final Map<String, String> e : weekList) {
                        str = str + "'" + e.get("COL") + "',";
                        data.append(
                                Integer.valueOf(
                                        ((TpRecordUlocNcDao) dao)
                                                .StatisticByWeek(sn, Integer.valueOf(ncId), startTime, endTime, isAll,
                                                        currUser, e.get("START_TIME"), e.get("END_TIME"), type)
                                                .get("VAL").toString()));
                        if (j < weekList.size()) {
                            data.append(",");
                        }
                        j = j + 1;
                    }
                    data.append(
                            "],'markPoint' : {'data' : [{'type' : 'max', 'name': '最大值'},{'type' : 'min', 'name': '最小值'}]},'markLine' : {'data' : [{'type' : 'average', 'name': '平均值'}]}}");
                    if (i < ncArray.length) {
                        data.append(",");
                        ncData.append(",");
                    }
                    i = i + 1;
                    result.clear();
                }

            }
            // 班次统计
            if ("shift".equals(time)) {
                final List<Map<String, Object>> shiftList = worktimeDao.findByCondition(startTime, endTime, isAll,
                        currUser);
                // 组装数据
                int i = 1;
                for (final String ncId : ncArray) {
                    data.append("{'name':'").append(nc.get(Integer.valueOf(ncId))).append("','type':'bar','data':[");
                    ncData.append("{'name':'").append(nc.get(Integer.valueOf(ncId)))
                            .append("','textStyle':{color:'#25c36c'}}");
                    int j = 1;
                    for (final Map<String, Object> e : shiftList) {
                        str = str + "'" + e.get("COL") + "',";
                        data.append(Integer.valueOf(((TpRecordUlocNcDao) dao)
                                .StatisticByShift(sn, Integer.valueOf(ncId), startTime, endTime, isAll, currUser,
                                        e.get("START_TIME").toString(), e.get("END_TIME").toString(), type)
                                .get("VAL").toString()));
                        if (j < shiftList.size()) {
                            data.append(",");
                        }
                        j = j + 1;
                    }
                    data.append(
                            "],'markPoint' : {'data' : [{'type' : 'max', 'name': '最大值'},{'type' : 'min', 'name': '最小值'}]},'markLine' : {'data' : [{'type' : 'average', 'name': '平均值'}]}}");
                    if (i < ncArray.length) {
                        data.append(",");
                        ncData.append(",");
                    }
                    i = i + 1;
                    result.clear();
                }
            }
            data.append("]");
            ncData.append("]");
            if ("".equals(str)) {
                return "NULL";
            }
            map.put("xData", "[" + str.substring(0, str.length() - 1) + "]");
            map.put("ncData", ncData.toString());
            map.put("data", data.toString());
            return "[" + str.substring(0, str.length() - 1) + "]" + "-----------" + ncData.toString() + "-----------"
                    + data.toString();
        } catch (

        final Exception e) {
            return json.toJson(ERROR);
        }
    }

    public static void main(final String[] args) throws ParseException {
        final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final Date end = sdf.parse("2017-02-12");
        final Calendar temp = Calendar.getInstance();
        temp.setTime(sdf.parse("2017-02-12"));
        final List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        int co = 0;
        while (temp.getTime().before(end)) {
            final Map<String, String> map = new HashMap<String, String>();
            map.put("COL", sdf1.format(temp.getTime()) + " 第" + temp.get(Calendar.WEEK_OF_MONTH) + "周");
            map.put("START_TIME", sdf.format(temp.getTime()));
            if (temp.get(Calendar.DAY_OF_WEEK) == 1) {
                map.put("COL", sdf1.format(temp.getTime()) + " 第" + (temp.get(Calendar.WEEK_OF_MONTH) - 1) + "周");
                map.put("END_TIME", sdf.format(temp.getTime()));
                temp.add(Calendar.DATE, 1);
                list.add(map);
                continue;
            }
            if (temp.getActualMaximum(Calendar.DAY_OF_MONTH) - temp.get(Calendar.DAY_OF_MONTH) < 8
                    - temp.get(Calendar.DAY_OF_WEEK)) {
                co = temp.getActualMaximum(Calendar.DAY_OF_MONTH) - temp.get(Calendar.DAY_OF_MONTH);
            } else {
                co = 8 - temp.get(Calendar.DAY_OF_WEEK);
            }
            temp.add(Calendar.DATE, co);
            map.put("END_TIME", sdf.format(temp.getTime()));
            list.add(map);
            temp.add(Calendar.DATE, 1);
        }
        if (sdf.format(end).equals(sdf.format(temp.getTime()))) {
            final Map<String, String> week = new HashMap<String, String>();
            week.put("COL", sdf1.format(temp.getTime()) + " 第" + temp.get(Calendar.WEEK_OF_MONTH) + "周");
            if (temp.get(Calendar.DAY_OF_WEEK) == 1) {
                week.put("COL", sdf1.format(temp.getTime()) + " 第" + (temp.get(Calendar.WEEK_OF_MONTH) - 1) + "周");
            }
            week.put("START_TIME", sdf.format(temp.getTime()));
            week.put("END_TIME", sdf.format(temp.getTime()));
            list.add(week);
        }
        for (final Map<String, String> e : list) {
            System.out.println(e.get("COL") + "," + e.get("START_TIME") + "," + e.get("END_TIME"));
        }
    }

}
