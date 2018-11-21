package com.wis.mes.master.part.dao;

import java.util.List;
import java.util.Map;

import com.wis.core.dao.BaseDao;
import com.wis.mes.master.part.entity.TmPart;

/**
 *
 * @author 赵吉超<jc_zhao@westinfosoft.com>
 *
 * @company 上海西信信息科技有限公司
 *
 * @date 2017年4月17日
 *
 * @desc 物料数据持久层接口
 *
 */
public interface TmPartDao extends BaseDao<TmPart> {

    /**
     * 根据 条件实体 生成 字典列表
     * 
     * @param param
     *            查询条件
     * 
     *            注：key必须为字段名，如：NAME_CN
     * 
     *            注：value必须为 String、List以及其他基本类型的包装类
     * 
     * @return 字典列表
     */
    List<TmPart> getDictItem(Map<String, Object> param);

    /**
     * 分页查询
     * 
     * @param pageNow
     *            当前页
     * 
     * @return 成品列表
     */
    List<Map<String, Object>> findByPage(Integer pageNow);

    /**
     * 根据 主键集合 查询 物料
     * 
     * @param ids
     *            主键集合
     * 
     * @return 物料
     */
    List<Map<String, Object>> findByIds(String ids);

}
