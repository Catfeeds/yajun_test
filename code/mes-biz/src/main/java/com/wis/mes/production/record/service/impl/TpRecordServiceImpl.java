package com.wis.mes.production.record.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.wis.basis.common.utils.BaseExcelUtil;
import com.wis.basis.common.utils.ConstantUtils;
import com.wis.core.context.WebContextHolder;
import com.wis.core.framework.entity.DictEntry;
import com.wis.core.framework.service.DictEntryService;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.part.dao.TmPartDao;
import com.wis.mes.master.rolemaster.dao.TsRoleMasterSetDao;
import com.wis.mes.master.workcalendar.dao.TmWorktimeDao;
import com.wis.mes.production.record.dao.TpRecordDao;
import com.wis.mes.production.record.entity.TpRecord;
import com.wis.mes.production.record.entity.TpRecordUloc;
import com.wis.mes.production.record.service.TpRecordService;
import com.wis.mes.production.record.service.TpRecordUlocService;
import com.wis.mes.util.StringUtil;

@Service
public class TpRecordServiceImpl extends BaseServiceImpl<TpRecord> implements TpRecordService {

    @Autowired
    public void setDao(final TpRecordDao dao) {
        this.dao = dao;
    }

    @Autowired
    private TpRecordUlocService recordUlocService;
    @Autowired
    private DictEntryService entryService;
    @Autowired
    private TsRoleMasterSetDao roleMasterSetDao;
    @Autowired
    private TmWorktimeDao worktimeDao;
    @Autowired
    private TmPartDao partDao;

    private static final String ERROR = "ERROR";

    @Override
    public TpRecord getTpRecordBySN(final String SN) {
        final TpRecord eg = new TpRecord();
        eg.setSn(SN);
        final List<TpRecord> record = findByEg(eg);
        return record.size() > 0 ? record.get(0) : null;
    }

    @Override
    public Map<String, Object> exportExcelData(final HttpServletResponse response, final List<TpRecord> list,
            final String filePath, final String[] header) {
        final List<Map<String, Object>> exportDataList = new ArrayList<Map<String, Object>>();
        Map<String, Object> result = new HashMap<String, Object>();
        for (int i = 0; i < list.size(); i++) {
            final TpRecord tpRecord = list.get(i);
            final Map<String, Object> map = new HashMap<String, Object>();
            map.put("SN编码", StringUtil.getString(tpRecord.getSn()));
            map.put("工单编号", StringUtil.getString(tpRecord.getPorderNo()));
            map.put("生产序列编号", StringUtil.getString(tpRecord.getAviNo()));
            map.put("物料", StringUtil.getString(tpRecord.getPart().getNo() + "-" + tpRecord.getPart().getNameCn()));
            map.put("数量", StringUtil.getString(tpRecord.getQty()));
            map.put("下线时间", StringUtil.getString(tpRecord.getOfflineTime()));
            map.put("上线时间", StringUtil.getString(tpRecord.getOnlineTime()));
            map.put("原SN号", StringUtil.getString(tpRecord.getParentSn()));
            map.put("入库数量", StringUtil.getString(tpRecord.getInWareQty()));
            exportDataList.add(map);
        }
        result = BaseExcelUtil.exportData(response, exportDataList, filePath, header);
        return result;
    }

    @Override
    public Map<String, Object> exportExcelDataAll(final HttpServletResponse response, final List<TpRecord> list,
            final String parentHeader, final String childHeader, final String filePath) {
        final String[] parentHead = parentHeader.split(",");
        final String[] childHead = childHeader.split(",");
        // 操作类型数据字典
        final Map<String, DictEntry> opertionTypes = entryService.getMapTypeCode(ConstantUtils.PRODUCT_OPERTION_TYPE);
        // 班次数据字典
        final Map<String, DictEntry> shiftTypes = entryService.getMapTypeCode(ConstantUtils.SHIFT_TYPE);
        final List<Map<String, Object>> parentExportList = new ArrayList<Map<String, Object>>();
        final Map<Integer, List<Map<String, Object>>> childExportMap = new HashMap<Integer, List<Map<String, Object>>>();
        Map<String, Object> result = new HashMap<String, Object>();
        for (int i = 0; i < list.size(); i++) {
            final TpRecord record = list.get(i);
            final Map<String, Object> map = new HashMap<String, Object>();
            map.put(parentHead[0], record.getId());
            map.put(parentHead[1], StringUtil.getString(record.getSn()));
            map.put(parentHead[2], StringUtil.getString(record.getPorderNo()));
            map.put(parentHead[3], StringUtil.getString(record.getAviNo()));
            map.put(parentHead[4], StringUtil.getString(record.getPart().getNo() + "-" + record.getPart().getNameCn()));
            map.put(parentHead[5], StringUtil.getString(record.getQty()));
            map.put(parentHead[6], StringUtil.getString(record.getOfflineTime()));
            map.put(parentHead[7], StringUtil.getString(record.getOnlineTime()));
            map.put(parentHead[8], StringUtil.getString(record.getParentSn()));
            map.put(parentHead[9], StringUtil.getString(record.getInWareQty()));
            // 查询产品档案站点信息
            final TpRecordUloc eg = new TpRecordUloc();
            eg.setTpRecordId(record.getId());
            final List<TpRecordUloc> recordUlocList = recordUlocService.findByEg(eg);
            final List<Map<String, Object>> childExportList = new ArrayList<Map<String, Object>>();
            for (int j = 0; j < recordUlocList.size(); j++) {
                final TpRecordUloc recordUloc = recordUlocList.get(j);
                final Map<String, Object> childMap = new HashMap<String, Object>();
                childMap.put(childHead[0], StringUtil.getString(recordUloc.getUlocNo()));
                childMap.put(childHead[1], StringUtil.getString(recordUloc.getUlocName()));
                childMap.put(childHead[2], StringUtil.getString(recordUloc.getNextUloc()));
                childMap.put(childHead[3], shiftTypes.containsKey(recordUloc.getShiftNo())
                        ? shiftTypes.get(recordUloc.getShiftNo()).getName() : "");
                childMap.put(childHead[4], opertionTypes.containsKey(recordUloc.getOperationType())
                        ? opertionTypes.get(recordUloc.getOperationType()).getName() : "");
                childMap.put(childHead[5], StringUtil.getString(recordUloc.getRoutingSeq()));
                childMap.put(childHead[6], StringUtil.getString(recordUloc.getNote()));
                childExportList.add(childMap);
            }
            childExportMap.put(record.getId(), childExportList);
            parentExportList.add(map);
        }
        result = BaseExcelUtil.exportDataAll(response, parentExportList, parentHead, childExportMap, childHead,
                filePath);
        return result;
    }

    @Override
    public List<Map<String, Object>> findByCondition(final String sn, final String partNo, final String partName,
            final String porder) {
        final Integer currUser = WebContextHolder.getCurrentUser().getId();
        return ((TpRecordDao) dao).findByCondition(sn, partNo, partName, porder,
                (roleMasterSetDao.getCountByCurrUser(currUser) == 0 ? "1" : "0"), currUser);
    }

    @Override
    public String getReportData(final String sn, final String time, final String startTime, final String endTime,
            final String partIds) {
        try {
            String str = "";
            final StringBuffer data = new StringBuffer("[");
            final StringBuffer ncData = new StringBuffer("[");
            final List<String> list = new ArrayList<String>();
            final Map<String, Object> map = new HashMap<String, Object>();
            final Integer currUser = WebContextHolder.getCurrentUser().getId();
            final String isAll = roleMasterSetDao.getCountByCurrUser(currUser) == 0 ? "1" : "0";
            final String[] ncArray = partIds.split(";");
            // 获取产品
            final Map<Integer, String> nc = new HashMap<Integer, String>();
            for (final Map<String, Object> e : partDao.findByIds(partIds.replaceAll(";", ","))) {
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
                    for (final Map<String, Object> e : ((TpRecordDao) dao).StatisticByCondition(sn,
                            Integer.valueOf(ncId), startTime, endTime, isAll, currUser, time)) {
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
                    for (final Map<String, Object> e : ((TpRecordDao) dao).StatisticByCondition(sn,
                            Integer.valueOf(ncId), startTime, endTime, isAll, currUser, time)) {
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
                                        ((TpRecordDao) dao)
                                                .StatisticByWeek(sn, Integer.valueOf(ncId), startTime, endTime, isAll,
                                                        currUser, e.get("START_TIME"), e.get("END_TIME"))
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
                        data.append(Integer.valueOf(((TpRecordDao) dao)
                                .StatisticByShift(sn, Integer.valueOf(ncId), startTime, endTime, isAll, currUser,
                                        e.get("START_TIME").toString(), e.get("END_TIME").toString())
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
        } catch (final Exception e) {
            return ERROR;
        }
    }

    @Override
    public String getNext(final Integer pageNow) {
        final Gson json = new Gson();
        try {
            return json.toJson(partDao.findByPage(pageNow));
        } catch (final Exception e) {
            return json.toJson("ERROR");
        }
    }

}
