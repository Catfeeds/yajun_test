package com.wis.mes.master.equipment.dao.impl;

import org.springframework.stereotype.Repository;

import com.wis.core.dao.impl.BaseDaoImpl;
import com.wis.mes.master.equipment.dao.TmEquipmentDao;
import com.wis.mes.master.equipment.entity.TmEquipment;

@Repository
public class TmEquipmentDaoImpl extends BaseDaoImpl<TmEquipment> implements TmEquipmentDao {

	@Override
	public TmEquipment findByPropertyNumber(String value) {
		return (TmEquipment) getSqlSession().selectOne("EquipmentMapper.findByPropertyNumber", value);
	}

	@Override
	public TmEquipment findNameByNo(String no) {
		return (TmEquipment) getSqlSession().selectOne("EquipmentMapper.findNameByNo", no);
	}

	
	@Override
	public TmEquipment findMouldNameById(String mouldArray) {
		return (TmEquipment) getSqlSession().selectOne("EquipmentMapper.findMouldNameById", mouldArray);

	}
}
