package com.wis.mes.master.rolemaster.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.wis.mes.master.base.service.impl.MasterBaseServiceImpl;
import com.wis.mes.master.line.entity.TmLine;
import com.wis.mes.master.line.service.TmLineService;
import com.wis.mes.master.rolemaster.dao.TsRoleMasterSetDao;
import com.wis.mes.master.rolemaster.entity.TsRoleMasterSet;
import com.wis.mes.master.rolemaster.service.TsRoleMasterSetService;
import com.wis.mes.master.uloc.entity.TmUloc;
import com.wis.mes.master.uloc.service.TmUlocService;
import com.wis.mes.master.workshop.entity.TmWorkshop;
import com.wis.mes.master.workshop.service.TmWorkshopService;
import com.wis.mes.util.StringUtil;

@Service
public class TsRoleMasterSetServiceImpl extends MasterBaseServiceImpl<TsRoleMasterSet> implements TsRoleMasterSetService {

	@Autowired
	public void setDao(TsRoleMasterSetDao dao) {
		this.dao = dao;
	}
	@Autowired
	private TmWorkshopService workshopService;
	@Autowired
	private TmLineService lineService;
	@Autowired
	private TmUlocService ulocService;
	
	
    public List<Integer> getPlantIds(Integer roleId) {
    	TsRoleMasterSet roleMasterSet = new TsRoleMasterSet();
    	roleMasterSet.setRoleId(roleId);
    	List<TsRoleMasterSet> tsRoleMasterSets = findByEg(roleMasterSet);
    	List<Integer> plantIdList = new ArrayList<Integer>();
    	if(tsRoleMasterSets.size()>0){
    		if(tsRoleMasterSets.get(0).getTmPlantId()!=null){
    			String[] plantIdStr = tsRoleMasterSets.get(0).getTmPlantId().split(",");
    			for(int i=0;i<plantIdStr.length;i++){
    				plantIdList.add(Integer.valueOf(plantIdStr[i]));
    			}
    		}
    		
    	}
    	return plantIdList;
    }
    
    
	public List<Integer> getWorkshopIds(Integer roleId) {
		TsRoleMasterSet roleMasterSet = new TsRoleMasterSet();
    	roleMasterSet.setRoleId(roleId);
    	List<TsRoleMasterSet> tsRoleMasterSets = findByEg(roleMasterSet);
    	List<Integer> workshopIdList = new ArrayList<Integer>();
    	if(tsRoleMasterSets.size()>0){
    		if(tsRoleMasterSets.get(0).getTmWorkshopId()!=null){
    			String[] workshopIdStr = tsRoleMasterSets.get(0).getTmWorkshopId().split(",");
    			for(int i=0;i<workshopIdStr.length;i++){
    				workshopIdList.add(Integer.valueOf(workshopIdStr[i]));
    			}
    		}
    		
    	}
    	return workshopIdList;
	}
	
	public List<Integer> getLineIds(Integer roleId) {
		TsRoleMasterSet roleMasterSet = new TsRoleMasterSet();
    	roleMasterSet.setRoleId(roleId);
    	List<TsRoleMasterSet> tsRoleMasterSets = findByEg(roleMasterSet);
    	List<Integer> lineIdList = new ArrayList<Integer>();
    	if(tsRoleMasterSets.size()>0){
    		if(tsRoleMasterSets.get(0).getTmLineId()!=null){
    			String[] lineIdStr = tsRoleMasterSets.get(0).getTmLineId().split(",");
    			for(int i=0;i<lineIdStr.length;i++){
    				lineIdList.add(Integer.valueOf(lineIdStr[i]));
    			}
    		}
    	}
    	return lineIdList;
	}
    
	public List<Integer> getUlocIds(Integer roleId) {
		TsRoleMasterSet roleMasterSet = new TsRoleMasterSet();
    	roleMasterSet.setRoleId(roleId);
    	List<TsRoleMasterSet> tsRoleMasterSets = findByEg(roleMasterSet);
    	List<Integer> ulocIdList = new ArrayList<Integer>();
    	if(tsRoleMasterSets.size()>0){
    		if(tsRoleMasterSets.get(0).getTmUlocId()!=null){
    			String[] ulocIdStr = tsRoleMasterSets.get(0).getTmUlocId().split(",");
    			for(int i=0;i<ulocIdStr.length;i++){
    				ulocIdList.add(Integer.valueOf(ulocIdStr[i]));
    			}
    		}
    	}
    	return ulocIdList;
	}
	
	public void doSaveRoleMenu(Integer[] plantIds, Integer[] workshopIds,Integer[] lineIds,
			Integer[] ulocIds,Integer roleId) {
		//根据roleId查询已经存在的权限数据
		TsRoleMasterSet tsRoleMasterSet = ((TsRoleMasterSetDao)dao).queryRoleMasterSet(roleId);
		//删除之前的数据
		if(tsRoleMasterSet!=null){
			((TsRoleMasterSetDao)dao).deleteByRoleId(roleId);
		}
		if(plantIds.length>0||workshopIds.length>0||lineIds.length>0||ulocIds.length>0){
		String plantId = "";
		String workshopId = "";
		String lineId = "";
		TsRoleMasterSet roleMasterSet = new TsRoleMasterSet();
		roleMasterSet.setRoleId(roleId);
		//工位
		if(ulocIds.length>0){
			roleMasterSet.setTmUlocId(changeArrayToString(ulocIds));
		}
		//产线
		StringBuffer lineBuffer = new StringBuffer();
		if(ulocIds.length>0){//如果选择了工位,则根据工位ID查找产线ID
			List<TmUloc> ulocList = ulocService.findAllInIds(ulocIds);
			for (int i=0;i<ulocList.size();i++) {
				if(i!=ulocList.size()-1){
					lineBuffer.append(ulocList.get(i).getTmLineId()+",");
				}else{
					lineBuffer.append(ulocList.get(i).getTmLineId());
				}
			}
			lineId =lineBuffer.toString();
			roleMasterSet.setTmLineId(removeRepeatValue(lineId));
		}
	   if(StringUtil.isNotBlank(changeArrayToString(lineIds))){
			if(lineBuffer.length()>0){
				lineBuffer.append(","+changeArrayToString(lineIds));
			}else{
				lineBuffer.append(changeArrayToString(lineIds));
			}
			lineId = lineBuffer.toString();
			roleMasterSet.setTmLineId(removeRepeatValue(lineId));
		}
		//车间
		StringBuffer workshopBuffer = new StringBuffer();
		if(!"".equals(lineId)){
			List<TmLine> lineList = lineService.findAllInIds(getIds("["+lineId+"]"));
			for (TmLine tmLine : lineList) {
				workshopBuffer.append(tmLine.getTmWorkshopId()+",");
			}
			workshopId = workshopBuffer.deleteCharAt(workshopBuffer.length()-1).toString();
			roleMasterSet.setTmWorkshopId(removeRepeatValue(workshopId));
		}
		if(StringUtil.isNotBlank(changeArrayToString(workshopIds))){//选择了车间
			if(workshopBuffer.length()>0){
				workshopBuffer.append(","+changeArrayToString(workshopIds));
			}else{
				workshopBuffer.append(changeArrayToString(workshopIds));
			}
			workshopId = workshopBuffer.toString();
			roleMasterSet.setTmWorkshopId(removeRepeatValue(workshopId));
		}
		//工厂
		StringBuffer plantBuffer = new StringBuffer();
		if(!"".equals(workshopId)){
			List<TmWorkshop> workshopList = workshopService.findAllInIds(getIds("["+workshopId+"]"));
			for (TmWorkshop tmWorkshop : workshopList) {
				plantBuffer.append(tmWorkshop.getTmPlantId()+",");
			}
			plantId = plantBuffer.deleteCharAt(plantBuffer.length()-1).toString();
			roleMasterSet.setTmPlantId(removeRepeatValue(plantId));
		}
		if(StringUtil.isNotBlank(changeArrayToString(plantIds))){
			if(plantBuffer.length()>0){
				plantBuffer.append(","+changeArrayToString(plantIds));
				roleMasterSet.setTmPlantId(removeRepeatValue(plantBuffer.toString()));
			}else{
				roleMasterSet.setTmPlantId(changeArrayToString(plantIds));
			}
		}
		//保存
		((TsRoleMasterSetDao)dao).save(roleMasterSet);
	
		
		
		}
		
	}
	
	public void deletePlantRelationById(Integer userId, Integer roleId, Integer[] plantIds) {
    	((TsRoleMasterSetDao)dao).deletePlantRelationById(userId, roleId, plantIds);
    }
	
	public String queryRoleIdsByUserId(Integer userId) {
		return ((TsRoleMasterSetDao)dao).queryRoleIdsByUserId(userId);
	}
	public String queryPlantIdsByUserIdAndRoleIds(Integer userId, Integer[] roleIds) {
		return ((TsRoleMasterSetDao)dao).queryPlantIdsByUserIdAndRoleIds(userId, roleIds);
	}
	
	
	//将Integer[]转化为String
	public String changeArrayToString(Integer[] id){
		StringBuffer buff = new StringBuffer();
		for(int i=0;i<id.length;i++){
			buff.append(id[i]+",");
		}
		return buff.length()>0?buff.deleteCharAt(buff.length() - 1).toString():"";
		
	}
	
	//去除字符串中的重复值
	public String removeRepeatValue(String ids){
		 String[] idArray = ids.split(","); 
         List<String> list = new ArrayList<String>(); 
         String idStr = "";
         for(int i=0;i<idArray.length;i++){ 
             if(!list.contains(idArray[i])) 
             list.add(idArray[i]);       
         } 
         for(int i=0;i<list.size();i++){
        	 if(i!=list.size()-1){
        		 idStr += list.get(i)+",";
        	 }else{
        		 idStr += list.get(i);
        	 }
         }
		return idStr;
	}
	
	
	
	protected Integer[] getIds(String ids) {
		if (StringUtils.isEmpty(ids)) {
			return new Integer[0];
		}
		JSONArray array = JSONArray.fromObject(ids);
		Integer[] result = new Integer[array.size()];
		for (int i = 0; i < array.size(); i++) {
			result[i] = array.getInt(i);
		}
		return result;
	}


	
	
}
