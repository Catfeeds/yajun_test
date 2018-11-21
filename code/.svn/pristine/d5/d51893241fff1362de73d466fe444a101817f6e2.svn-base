package com.wis.mes.master.nc.dao;

import java.util.List;
import java.util.Map;

import com.wis.core.dao.BaseDao;
import com.wis.mes.master.nc.entity.TmNc;

public interface TmNcDao extends BaseDao<TmNc> {

    /**
     * 根据 不合格组主键 查询 不合格代码列表
     * 
     * @param ncGroupId
     *            不合格组主键
     * 
     * @return 不合格代码列表
     */
    List<TmNc> findByNcGroupId(Integer ncGroupId);

    /**
     * 分页查询
     * 
     * @param pageNow
     *            当前页
     * 
     * @return 不合格代码列表
     */
    List<TmNc> findByPage(Integer pageNow);

    /**
     * 获取总条数
     * 
     * @return 总条数
     */
    int getCount();

    /**
     * 根据 不合格集 不合格代码/组 查询 不合格记录
     * 
     * @param ncIds
     *            不合格集
     * 
     * @param type
     *            不合格代码/组
     * 
     * @return 不合格记录
     */
    List<Map<String, Object>> findByType(String ncIds, String type);

}
