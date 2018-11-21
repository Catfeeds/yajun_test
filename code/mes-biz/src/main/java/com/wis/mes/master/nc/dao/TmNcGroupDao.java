package com.wis.mes.master.nc.dao;

import java.util.List;

import com.wis.core.dao.BaseDao;
import com.wis.mes.master.nc.entity.TmNcGroup;

public interface TmNcGroupDao extends BaseDao<TmNcGroup> {

    /**
     * 分页查询
     * 
     * @param pageNow
     *            当前页
     * 
     * @return 不合格代码列表
     */
    List<TmNcGroup> findByPage(Integer pageNow);

    /**
     * 获取总条数
     * 
     * @return 总条数
     */
    int getCount();

}
