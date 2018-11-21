package com.wis.mes.production.record.service.impl; 

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.wis.basis.common.utils.ConstantUtils;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.part.entity.TmPart;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.production.record.dao.OnePassRateDao;
import com.wis.mes.production.record.entity.TpRecord;
import com.wis.mes.production.record.service.OnPassRateService;

/**   
 * @author Jixueyuan   
 * @date 2017年6月25日
 * @Description: TODO 
 */
@Service
public class OnePassRateServiceImpl extends BaseServiceImpl<TpRecord> implements OnPassRateService {
	@Autowired
	private OnePassRateDao dao;

	@Override
	public Map<String, String> findData(String type, String viewGroup,String startDateStr, String endDateStr, ModelMap modelMap) throws Exception {
		//根据类型查询一次通过率报表的数据(type:日/周/月/班次)
				JSONArray  xData = new JSONArray();//存放X轴的数据
				Map<String,String> map = new HashMap<String, String>();
				//根据开始和结束时间查询涉及的种类
				JSONArray legends = new JSONArray();
				String legend = "";
				if("PART".equals(viewGroup)){
					List<TmPart> partTypeSum = dao.findPartTypeSum(startDateStr,endDateStr);//查询物料的种类
					legend =  getPartTypeID(partTypeSum);
					legends.add(getPartTypeName(partTypeSum));
					map.put("legend", legends.toString());
				}else if("LINE".equals(viewGroup)){
					List<TmLine> lineTypeSum = dao.findLineTypeSum(startDateStr,endDateStr);//查询产线的种类
					legend = getLineTypeID(lineTypeSum);
					legends.add(getLineTypeName(lineTypeSum));
					map.put("legend",legends.toString());
				}else if("PORDER".equals(viewGroup)){
					List<TpRecord> porderTypeSum = dao.findPorderTypeSum(startDateStr,endDateStr);//统计订单的种类
					legend = getPorderTypeName(porderTypeSum);
					legends.add(legend);
					map.put("legend",legends.toString());
				}else if("ULOC".equals(viewGroup)){
					List<TmUloc> ulocTypeSum = dao.finUlocTypeSum(startDateStr,endDateStr);//统计工位的种类
					legend = getUlocTypeID(ulocTypeSum);
					legends.add(getUlocTypeName(ulocTypeSum));
					map.put("legend", legends.toString());
				}
				
				String[] legendArr = legend.split(",");
				if("DAY".equals(type)){  //天
					JSONArray yData = new JSONArray();
					//获取区间的天
					List<String> dayList = new ArrayList<String>();
					 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			         Date end = sdf.parse(endDateStr);
			         Calendar temp = Calendar.getInstance();
			         temp.setTime(sdf.parse(startDateStr));
			         while (temp.getTime().before(end)) {
			        	 dayList.add(sdf.format(temp.getTime()));
			             temp.add(Calendar.DAY_OF_MONTH, 1);
			         }
			         dayList.add(sdf.format(end));
			       //订单
					if(viewGroup.equalsIgnoreCase("PORDER")){
						JSONArray rate = new JSONArray();
						for(int i=0;i<legendArr.length;i++){
							xData.clear();
							for (String day : dayList) {
								 xData.add(day);
								 rate.add(dao.getOnepassRateByPorderAndMonth(day,legendArr[i])); 
							}	
							 yData.add(rate);
					}
					}
					//产品
					if(viewGroup.equalsIgnoreCase("PART")){
						JSONArray rate = new JSONArray();
						for(int i=0;i<legendArr.length;i++){
							xData.clear();
							for (String day : dayList) {
								 xData.add(day);
								 rate.add(dao.getOnepassRateByPartAndMonth(day,legendArr[i])); 
							}	
							 yData.add(rate);
					}
					}
					//产线
					if(viewGroup.equalsIgnoreCase("LINE")){
						JSONArray rate = new JSONArray();
						for(int i=0;i<legendArr.length;i++){
							xData.clear();
							for (String day : dayList) {
								 xData.add(day);
								 rate.add(dao.getOnepassRateByLineAndMonth(day,legendArr[i])); 
							}	
							 yData.add(rate);
					}
					}
					//工位
					if(viewGroup.equalsIgnoreCase("ULOC")){
						JSONArray rate = new JSONArray();
						for(int i=0;i<legendArr.length;i++){
							xData.clear();
							for (String day : dayList) {
								 xData.add(day);
								 rate.add(dao.getOnepassRateByUlocAndMonth(day,legendArr[i])); 
							}	
							 yData.add(rate);
					}
				  }
					map.put("yData", yData.toString());
				}else if("WEEK".equals(type)){//周
					JSONArray yData = new JSONArray();
					//获取区间的周
					List<Map<String, String>> list = getWeek(startDateStr,endDateStr);
					//订单
					if(viewGroup.equalsIgnoreCase("PORDER")){
						JSONArray rate = new JSONArray();
						for(int i=0;i<legendArr.length;i++){
							xData.clear();
							for (Map<String, String> e : list) {
								 xData.add(e.get("COL"));
								 rate.add(dao.getOnepassRateByPorderAndWeek(e.get("START_TIME"),e.get("END_TIME"),legendArr[i])); 
							 }
							 yData.add(rate);
						}
					}
					//产品
					if(viewGroup.equalsIgnoreCase("PART")){
						JSONArray rate = new JSONArray();
						for(int i=0;i<legendArr.length;i++){
							xData.clear();
							for (Map<String, String> e : list) {
								xData.add(e.get("COL"));
								rate.add(dao.getOnepassRateByPartAndWeek(e.get("START_TIME"),e.get("END_TIME"),legendArr[i])); 
							}
							yData.add(rate);
						}
					}
					//产线
					if(viewGroup.equalsIgnoreCase("LINE")){
						JSONArray rate = new JSONArray();
						for(int i=0;i<legendArr.length;i++){
							xData.clear();
							for (Map<String, String> e : list) {
								xData.add(e.get("COL"));
								rate.add(dao.getOnepassRateByLineAndWeek(e.get("START_TIME"),e.get("END_TIME"),legendArr[i])); 
							}
							yData.add(rate);
						}
					}
					//工位
					if(viewGroup.equalsIgnoreCase("ULOC")){
						JSONArray rate = new JSONArray();
						for(int i=0;i<legendArr.length;i++){
							xData.clear();
							for (Map<String, String> e : list) {
								xData.add(e.get("COL"));
								rate.add(dao.getOnepassRateByUlocAndWeek(e.get("START_TIME"),e.get("END_TIME"),legendArr[i])); 
							}
							yData.add(rate);
						}
					}
					
					map.put("yData", yData.toString());
				}else if("MONTH".equals(type)){ //月
					JSONArray yData = new JSONArray();
					//获取区间的月份
					List<String> monthList = new ArrayList<String>();
			        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			        Date end = sdf.parse(endDateStr);
			        Calendar temp = Calendar.getInstance();
			        temp.setTime(sdf.parse(startDateStr));
			        while (temp.getTime().before(end)) {
			        	monthList.add(sdf.format(temp.getTime())+",");
			            temp.add(Calendar.MONTH, 1);
			        }
			        monthList.add(sdf.format(end));
					//订单
					if(viewGroup.equalsIgnoreCase("PORDER")){
						JSONArray rate = new JSONArray();
						for(int i=0;i<legendArr.length;i++){
							xData.clear();
							 for (String month : monthList) {
								 xData.add(month);
								 rate.add(dao.getOnepassRateByPorderAndMonth(month,legendArr[i])); 
							 }
							 yData.add(rate);
						}
					}
					
					//产品
					if(viewGroup.equalsIgnoreCase("PART")){
						JSONArray rate = new JSONArray();
						for(int i=0;i<legendArr.length;i++){
							xData.clear();
							 for (String month : monthList) {
								 xData.add(month);
								 rate.add(dao.getOnepassRateByPartAndMonth(month,legendArr[i])); 
							 }
							 yData.add(rate);
						}
						
					}
					
					//产线
					if(viewGroup.equalsIgnoreCase("LINE")){
						JSONArray rate = new JSONArray();
						for(int i=0;i<legendArr.length;i++){
							xData.clear();
							 for (String month : monthList) {
								 xData.add(month);
								 rate.add(dao.getOnepassRateByLineAndMonth(month,legendArr[i])); 
							 }
							 yData.add(rate);
						}
						
					}
					
					//工位
					if(viewGroup.equalsIgnoreCase("ULOC")){
						JSONArray rate = new JSONArray();
						for(int i=0;i<legendArr.length;i++){
							xData.clear();
							 for (String month : monthList) {
								 xData.add(month);
								 rate.add(dao.getOnepassRateByUlocAndMonth(month,legendArr[i])); 
							 }
							 yData.add(rate);
						}
						
					}
					map.put("yData", yData.toString());
					
				}else if("SHIFT".equals(type)){
					startDateStr = startDateStr +" 00:00:00";
					endDateStr = endDateStr + " 23:59:59";
					JSONArray  yData = new JSONArray();
					xData.add("早班");
					xData.add("中班");
					xData.add("晚班");
						//订单
						if(viewGroup.equalsIgnoreCase("PORDER")){
							for(int i=0;i<legendArr.length;i++){
								JSONArray rate = new JSONArray();
							//查询早班订单的数据
							rate.add(dao.getOnepassRateByPorderAndShift(startDateStr,endDateStr,ConstantUtils.SHIFT_MORNING,legendArr[i]));
							//查询中班订单的数据
							rate.add(dao.getOnepassRateByPorderAndShift(startDateStr,endDateStr,ConstantUtils.SHIFT_NOON,legendArr[i]));
							//查询晚班订单的数据
							rate.add(dao.getOnepassRateByPorderAndShift(startDateStr,endDateStr,ConstantUtils.SHIFT_NIGHT,legendArr[i]));
							yData.add(rate);
							map.put("yData", yData.toString());
						 }
						}
						//产品
						if(viewGroup.equalsIgnoreCase("PART")){
							for(int i=0;i<legendArr.length;i++){
								JSONArray rate = new JSONArray();
							//查询早班产品的数据
							rate.add(dao.getOnepassRateByPartAndShift(startDateStr, endDateStr, ConstantUtils.SHIFT_MORNING,legendArr[i]));
							//查询中班产品的数据
							rate.add(dao.getOnepassRateByPartAndShift(startDateStr, endDateStr, ConstantUtils.SHIFT_NOON,legendArr[i]));	
							//查询晚班产品的数据
							rate.add(dao.getOnepassRateByPartAndShift(startDateStr, endDateStr, ConstantUtils.SHIFT_NIGHT,legendArr[i]));	
							yData.add(rate);
							map.put("yData", yData.toString());
						  }
						}
						//产线
						if(viewGroup.equalsIgnoreCase("LINE")){
							for(int i=0;i<legendArr.length;i++){
								JSONArray rate = new JSONArray();
							//查询早班产线的数据
							rate.add(dao.getOnepassRateByLineAndShift(startDateStr, endDateStr, ConstantUtils.SHIFT_MORNING,legendArr[i]));
							//查询中班产线的数据
							rate.add(dao.getOnepassRateByLineAndShift(startDateStr, endDateStr, ConstantUtils.SHIFT_NOON,legendArr[i]));
							//查询晚班产线的数据
							rate.add(dao.getOnepassRateByLineAndShift(startDateStr, endDateStr, ConstantUtils.SHIFT_NIGHT,legendArr[i]));
							yData.add(rate);
							map.put("yData", yData.toString());
						  }
						}		
						//工位
						if(viewGroup.equalsIgnoreCase("ULOC")){
							for(int i=0;i<legendArr.length;i++){
								JSONArray rate = new JSONArray();
							//查询早班工位的数据
							rate.add(dao.getOnepassRateByUlocAndShift(startDateStr, endDateStr, ConstantUtils.SHIFT_MORNING,legendArr[i]));
							//查询中班工位的数据
							rate.add(dao.getOnepassRateByUlocAndShift(startDateStr, endDateStr, ConstantUtils.SHIFT_NOON,legendArr[i]));
							//查询晚班工位的数据
							rate.add(dao.getOnepassRateByUlocAndShift(startDateStr, endDateStr, ConstantUtils.SHIFT_NIGHT,legendArr[i]));
							yData.add(rate);
							map.put("yData", yData.toString());
						}
					}
				}
				map.put("xData",xData.toString());
				return map;
				
				
			}	
			
			

			//获取时间段内的周,以及这周的开始时间和结束时间
			private List<Map<String, String>> getWeek(String startDateStr,String endDateStr) throws ParseException {
				 final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
			        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			        final Date end = sdf.parse(endDateStr);
			        final Calendar temp = Calendar.getInstance();
			        temp.setTime(sdf.parse(startDateStr));
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
					return list;
			}




			//返回工单名称
			private String getPorderTypeName(List<TpRecord> porderTypeSum) {
				if(porderTypeSum.size()>0&&porderTypeSum!=null){
					StringBuffer buff = new StringBuffer();
					for (TpRecord tpRecord : porderTypeSum) {
						buff.append(tpRecord.getPorderNo()+",");
					}
					return buff.deleteCharAt(buff.length()-1).toString();
				}
				return "";
			}


			//返回产线名称
			private String getLineTypeName(List<TmLine> lineTypeSum) {
				if(lineTypeSum.size()>0&&lineTypeSum!=null){
					StringBuffer buff = new StringBuffer();
					for (TmLine tmLine : lineTypeSum) {
						buff.append(tmLine.getNameCn()+",");
					}
					return buff.deleteCharAt(buff.length()-1).toString();
				}
				return "";
			}
			//返回产线的ID
			private String getLineTypeID(List<TmLine> lineTypeSum) {
				if(lineTypeSum.size()>0&&lineTypeSum!=null){
					StringBuffer buff = new StringBuffer();
					for (TmLine tmLine : lineTypeSum) {
						buff.append(tmLine.getId()+",");
					}
					return buff.deleteCharAt(buff.length()-1).toString();
				}
				return "";
			}


			//返回物料名称
			private String getPartTypeName(List<TmPart> partTypeSum) {
				if(partTypeSum.size()>0&&partTypeSum!=null){
					StringBuffer buff = new StringBuffer();
					for (TmPart tmPart : partTypeSum) {
						buff.append(tmPart.getNameCn()+",");
					}
					return buff.deleteCharAt(buff.length()-1).toString();
				}
				return "";
			}
			//返回物料的ID
			private String getPartTypeID(List<TmPart> partTypeSum) {
				if(partTypeSum.size()>0&&partTypeSum!=null){
					StringBuffer buff = new StringBuffer();
					for (TmPart tmPart : partTypeSum) {
						buff.append(tmPart.getId()+",");
					}
					return buff.deleteCharAt(buff.length()-1).toString();
				}
				return "";
			}
			
			//返回工位的名称
			private String getUlocTypeName(List<TmUloc> ulocTypeSum) {
				if(ulocTypeSum.size()>0&&ulocTypeSum!=null){
					StringBuffer buff = new StringBuffer();
					for (TmUloc tmUloc : ulocTypeSum) {
						buff.append(tmUloc.getName()+",");
					}
					return buff.deleteCharAt(buff.length()-1).toString();
				}
				return "";
			}
			//返回工位的ID
			private String getUlocTypeID(List<TmUloc> ulocTypeSum) {
				if(ulocTypeSum.size()>0&&ulocTypeSum!=null){
					StringBuffer buff = new StringBuffer();
					for (TmUloc tmUloc : ulocTypeSum) {
						buff.append(tmUloc.getId()+",");
					}
					return buff.deleteCharAt(buff.length()-1).toString();
				}
				return "";
			}

			
			//给定日期往后加一天
			public Date addOneDay(Date time){
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(time);
				calendar.add(Calendar.DATE,1);
				return calendar.getTime();
			}
			
	}
	


