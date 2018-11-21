package com.wis.basis.systemadmin.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.systemadmin.dao.MenuSetDao;
import com.wis.basis.systemadmin.service.MenuManagementService;
import com.wis.core.context.WebContextHolder;
import com.wis.core.framework.dao.LanguageDao;
import com.wis.core.framework.dao.MenuDao;
import com.wis.core.framework.entity.Language;
import com.wis.core.framework.entity.Menu;
import com.wis.core.framework.service.LanguageService;
import com.wis.core.service.impl.BaseServiceImpl;

@Service("menuMangmentService")
public class MenuManagementServiceImpl extends BaseServiceImpl<Menu> implements MenuManagementService {
	@Autowired
	private void setDao(MenuDao dao) {
		this.dao = dao;
	}
	@Autowired
	private LanguageService languageService;
	
	@Autowired
	private LanguageDao languageDao;
	
	@Autowired
	private MenuSetDao setDao;
	
	@Override
	public List<Menu> getAllMenus() {
		String lang = WebContextHolder.getUserDetailsImpl().getLang();
		List<Menu> menus = new ArrayList<Menu>();
		List<Menu> parentMenus = ((MenuDao) dao).getAllTopMenu();
		for (int i=0; i<parentMenus.size(); i++) {
			Menu parentMenu = parentMenus.get(i);
			parentMenu.setName(languageService.getDescBy(parentMenu.getName(), lang));
			List<Menu> childMenus = ((MenuDao) dao).getChildMenu(parentMenu.getId());
			for(int j=0; j<childMenus.size(); j++) {
				Menu childMenu = childMenus.get(j);
				childMenu.setName(languageService.getDescBy(childMenu.getName(), lang));
			}
			parentMenu.setChildren(childMenus);
			menus.add(parentMenu);
		}
		return menus;
	}
	public Language addLanguage(Language language) {
		return languageDao.save(language);
	}
	public void addMenu(Menu menu) {
		setDao.addMenu(menu);
	}
}
