package com.wis.basis.menu.vo;

import com.wis.core.framework.entity.Language;
import com.wis.core.framework.entity.Menu;

public class MenuVo {
	private Integer menuId;
	private Menu menu;
	private Language language;
	
	public Integer getMenuId() {
		return menuId;
	}
	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}
	public Menu getMenu() {
		return menu;
	}
	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	public Language getLanguage() {
		return language;
	}
	public void setLanguage(Language language) {
		this.language = language;
	}
	
}
