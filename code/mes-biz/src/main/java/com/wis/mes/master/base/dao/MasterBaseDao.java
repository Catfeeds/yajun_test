package com.wis.mes.master.base.dao; 

import java.util.List;

import com.wis.core.dao.BaseDao;
import com.wis.core.dao.OrderBy;
import com.wis.core.dao.PageResult;
import com.wis.core.dao.QueryCriteria;
import com.wis.core.entity.AbstractEntity;

/**   
 * @author Jixueyuan   
 * @date 2017年6月14日
 * @Description: 主数据BaseDao 
 */
public interface MasterBaseDao<T extends AbstractEntity> extends BaseDao<T> {
	public T get(Integer id);
	
	public List<T> findAll(OrderBy... orderBies);
	
	public List<T> findByEg(T eg);
	
	public T findUniqueByEg(T eg);
	
	public List<T> findAllInIds(Integer[] ids);
	
	public PageResult<T> findBy(QueryCriteria queryCriteria);

}
