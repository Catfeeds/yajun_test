package com.wis.basis.systemadmin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.systemadmin.dao.OrgnizationDao;
import com.wis.basis.systemadmin.dao.PositionDao;
import com.wis.basis.systemadmin.entity.Orgnization;
import com.wis.basis.systemadmin.entity.Position;
import com.wis.basis.systemadmin.service.OrgnizationService;
import com.wis.basis.systemadmin.service.PositionService;
import com.wis.basis.systemadmin.service.UserService;
import com.wis.core.service.impl.BaseServiceImpl;

@Service("positionService")
public class PositionServiceImpl extends BaseServiceImpl<Position> implements PositionService {

	@Autowired
	public void setDao(PositionDao dao) {
		this.dao = dao;
	}
	@Autowired
	private UserService userService;
	@Autowired
	private OrgnizationService orgnizationService;
	@Autowired
	private OrgnizationDao orgnizationDao;
	
	@Override
	public List<Position> findBy(Integer userId) {
		return ((PositionDao) dao).findBy(userId);
	}

	@Override
	public void doUpdateUserPosition(Integer userId, Integer... positionIds) {
		for (Integer positionId : positionIds) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);
			map.put("positionId", positionId);
			((PositionDao) dao).deleteUserPositionById(map);
			((PositionDao) dao).addUser(map);
		}
	}

	@Override
	public void doUpdatePositionUser(Integer positionId, Integer... userIds) {
		Position position = this.findById(positionId);
		orgnizationService.doAddUser(position.getOrgnizationId(), userIds);
		for (Integer userId : userIds) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);
			map.put("positionId", positionId);
			((PositionDao) dao).deleteUserPositionById(map);
			((PositionDao) dao).addUser(map);
		}
	}

	@Override
	public void doDeletePosition(Integer userId, Integer... positionIds) {
		((PositionDao) dao).deletePositionById(userId, positionIds);
	}

	@Override
	public void doDeleteUser(Integer positionId, Integer... userIds) {
		((PositionDao) dao).deleteUserById(positionId, userIds);
	}

	@Override
	public void doDeleteRelationUser(Integer positionId, Integer... userIds) {
		((PositionDao) dao).deleteUserById(positionId, userIds);
		//add by huanglong 2017/8/29    操作:该用户不在其他岗位时,删除 用户组织 关系表 (岗位设置用户时,会往 用户组织 关系表 插入数据)  
		Position position = this.findById(positionId);
		Map<String, Object> paramMap = null;
		for(Integer userId : userIds){
			if(this.findBy(userId).size() == 0 && position != null){
				paramMap = new HashMap<String, Object>();
				paramMap.put("userId", userId);
				paramMap.put("orgnizationId", position.getOrgnizationId());
				orgnizationDao.doDeleteUserOrgById(paramMap);
			}
		}
	}
	
	@Override
	public List<Position> findByAccount(String account) {
		return ((PositionDao) dao).findByUserAccount(account);
	}

	@Override
	public List<String> findUpPositionByAccount(String account) {

		List<Integer> temp = ((PositionDao) dao).findUpByUserAccount(account);
		List<String> result = new ArrayList<String>();
		if (temp != null && temp.size() > 0) {
			for (Integer id : temp) {
				if (id != null) {
					result.add(id.toString());
				}
			}
		}
		return result;
	}

	@Override
	public Integer checkCode(String code) {
		Position position = new Position();
		position.setCode(code);
		List<Position> rList = dao.findByEg(position);
		if (rList.size() > 0) {
			return rList.get(0).getId();
		}
		return null;
	}

	private List<Position> getSonBy(Integer parentId) {
		Position position = new Position();
		position.setParentId(parentId);
		return dao.findByEg(position);
	}

	@Override
	public void doDeleteById(Integer[] ids) {
		for (Integer id : ids) {
			super.doDeleteById(id);
			List<Position> list = getSonBy(id);
			for (Position position : list) {
				//super.doDeleteById(position.getId());
				//删除上级岗位时,其下级岗位不删除,清除 parentId
				position.setParentId(null);
				super.doUpdate(position);
			}
		}
	}

	@Override
	public List<Integer> findUserBy(Integer... positionIds) {
		return ((PositionDao) dao).findUserBy(positionIds);
	}

	@Override
	public List<String> findUserAccountsByPositionCode(String code) {
		List<String> accounts = ((PositionDao) dao).findUserAccountByPositionCode(code);
		if(accounts!=null && accounts.size()>0){
			return accounts;
		}else {
			return null;
		}
	}
	/**
	 * 根据岗位查找用户，如果该岗位只有一个用户，返回用户账户，大于一个用户，返回空
	 * */
	public String findUserAccountByPositionCode(String code){
		List<String> accounts = ((PositionDao) dao).findUserAccountByPositionCode(code);
		if(accounts!=null && accounts.size()==1){
			return accounts.get(0);
		}else{
			return null;
		}
	}

	@Override
	public String findUpByAccount(String account) {
		List<Integer> temp = ((PositionDao) dao).findUpByUserAccount(account);
		if (temp != null && temp.size() > 0 && temp.get(0) != null) {
			List<Integer> userList = ((PositionDao) dao).findUserBy(temp.toArray(new Integer[temp.size()]));
			if (userList != null && userList.size() == 1) {
				return userService.findById(userList.get(0)).getAccount();
			}
		}
		return null;
	}

	@Override
	public List<String> findUpListByAccount(String account) {
		List<String> list = new ArrayList<String>();
		List<Integer> temp = ((PositionDao) dao).findUpByUserAccount(account);
		if (temp != null && temp.size() > 0) {
			List<Integer> userList = ((PositionDao) dao).findUserBy(temp.toArray(new Integer[temp.size()]));
			for (Integer userId : userList) {
				list.add(userService.findById(userId).getAccount());
			}
		}
		return list;
	}

	@Override
	public List<Integer> findPositionIdsByOrgCode(String orgCode) {
		return ((PositionDao) dao).findPositionIdsByOrgCode(new String[]{orgCode});
	}

	@Override
	public List<Integer> findPositionIdsByOrgCodes(String[] orgCodes) {
		return ((PositionDao) dao).findPositionIdsByOrgCode(orgCodes);
	}
	
	@Override
	public List<String> findPositiIdsByOrgCode(String orgCode){
		List<String> list = new ArrayList<String>();
		Orgnization org = new Orgnization();
		org.setCode(orgCode);
		org = orgnizationService.findUniqueByEg(org);
		Position position = new Position();
		position.setOrgnizationId(org.getId());
		List<Position> positions = dao.findByEg(position);
		if(positions!=null && positions.size()>0){
			for(Position pos:positions){
				list.add(String.valueOf(pos.getId()));
			}
		}else{
			return null;
		}
		
		return list;
	}

	
}
