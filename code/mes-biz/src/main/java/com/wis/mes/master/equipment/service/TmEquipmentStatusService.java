package com.wis.mes.master.equipment.service;

import java.util.Map;

import com.wis.basis.common.web.model.BootstrapTableQueryCriteria;
import com.wis.core.dao.PageResult;
import com.wis.core.service.BaseService;
import com.wis.mes.master.equipment.entity.TmpEquipmentStatus;
import com.wis.mes.util.ExportPageResult;

public interface TmEquipmentStatusService extends BaseService<TmpEquipmentStatus>,ExportPageResult<TmpEquipmentStatus> {

    /**
     * 设备报警报表
     * @param criteria
     * @return
     */
    PageResult<Map<String,Object>> warmReport(BootstrapTableQueryCriteria criteria);

	PageResult<TmpEquipmentStatus> queryPageTbEquipmentStatus(BootstrapTableQueryCriteria criteria);
}
