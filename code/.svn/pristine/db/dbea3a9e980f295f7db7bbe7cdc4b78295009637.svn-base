package com.wis.mes.master.classmanage.service;

import java.util.List;

import com.wis.core.framework.entity.DictEntry;
import com.wis.core.service.BaseService;
import com.wis.mes.master.classmanage.entity.TmClassManager;


/**
 * @author zhuzw
 * 
 * @company 上海西信信息科技有限公司
 * 
 * @dete 2018-03-25
 * 
 * @desc 班次 Service
 * */
public interface TmClassManagerService extends BaseService<TmClassManager>{
	
	List<DictEntry> getDictItem(TmClassManager eg);
	
	List<DictEntry> getDictItemEntry(TmClassManager eg);
	
	/**
	 * 事部  产线 班组 三级联动 ajax处理
	 * @plantId
	 * @author ryj
	 * @return List<TmClassManager>
	 */
	List<TmClassManager> plantTolineToClassManager(Integer plantId,Integer lineId);
}
