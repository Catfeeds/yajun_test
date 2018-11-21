package com.wis.mes.phone.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wis.basis.common.utils.ConstantUtils;
import com.wis.core.context.WebContextHolder;
import com.wis.core.framework.entity.Menu;
import com.wis.core.framework.entity.Permission;
import com.wis.core.framework.model.ComboTree;
import com.wis.core.framework.service.LanguageService;
import com.wis.core.service.impl.BaseServiceImpl;
import com.wis.mes.phone.dao.PhoneMenuDao;
import com.wis.mes.phone.service.PhoneMenuService;

@Service
public class PhoneMenuServiceImpl extends BaseServiceImpl<Menu> implements PhoneMenuService{

    @Autowired
    public void setDao(PhoneMenuDao dao){
        this.dao = dao;
    }
    @Autowired
    private LanguageService languageService;

    @Override
    public List<Menu> findPhoneMenuBy(Integer userId) {
        List<Menu> userHasMenus = ((PhoneMenuDao) dao).getMenuByParent(userId, 200);
        String lang = ConstantUtils.REQUEST_LANG_ZH_CN;
        Map<Integer, List<Menu>> allChild = findAllMenu(lang);
        return treeableMenus(lang, allChild, userHasMenus);
    }

    @Override
    public List<Permission> findPermissionBy(Integer userId) {
        return ((PhoneMenuDao)dao).findPermissionBy(userId);
    }

    private List<Integer> getMenuIds(List<Menu> menus) {
        List<Integer> reulst = new ArrayList<Integer>();
        for (Menu menu : menus) {
            reulst.add(menu.getId());
        }
        return reulst;
    }

    public List<Menu> findTopMenus() {
        return (List<Menu>) ((PhoneMenuDao) dao).getAllTopMenu();
    }

    @Override
    public List<ComboTree> findMenuAndPermissionBy(Integer roleId) {
        List<Integer> userHasMenuIds = getMenuIds(((PhoneMenuDao) dao).getMenuByRoleId(roleId));
        String lang = WebContextHolder.getUserDetailsImpl().getLang();
        List<ComboTree> result = new ArrayList<ComboTree>();

        List<Menu> topMenus = findTopMenus();
        for (Menu topMenu : topMenus) {

            topMenu.setChildren(new ArrayList<Menu>(((PhoneMenuDao) dao).getChildMenu(topMenu.getId())));

            ComboTree parentNode = new ComboTree();
            parentNode.setText(languageService.getDescBy(topMenu.getName(), lang));
            parentNode.setIconCls(topMenu.getIconCls());
            parentNode.setChecked(false);
            parentNode.setId(topMenu.getId());
            int sonCheckedCount = 0;

            if (topMenu.getChildren().size() > 0) {
                List<ComboTree> sonNodes = new ArrayList<ComboTree>();
                for (Menu sonMenu : topMenu.getChildren()) {

                    sonMenu.setChildren(new ArrayList<Menu>(((PhoneMenuDao) dao).getChildMenu(sonMenu.getId())));

                    ComboTree sonNode = new ComboTree();
                    sonNode.setText(languageService.getDescBy(sonMenu.getName(), lang));
                    sonNode.setIconCls(sonMenu.getIconCls());
                    sonNode.setId(sonMenu.getId());
                    sonNode.setChecked(false);
                    if (userHasMenuIds.contains(sonMenu.getId())) {
                        sonNode.setChecked(true);
                        sonCheckedCount++;
                    }

                    int sonLastCheckedCount = 0;
                    if (sonMenu.getChildren().size()>0){
                        List<ComboTree> sonLastNodes = new ArrayList<ComboTree>();
                        for (Menu sonLastMenu : sonMenu.getChildren()) {
                            ComboTree sonLastNode = new ComboTree();
                            sonLastNode.setText(languageService.getDescBy(sonLastMenu.getName(), lang));
                            sonLastNode.setIconCls(sonLastMenu.getIconCls());
                            sonLastNode.setId(sonLastMenu.getId());
                            sonLastNode.setChecked(false);
                            if (userHasMenuIds.contains(sonLastMenu.getId())) {
                                sonLastNode.setChecked(true);
                                sonLastCheckedCount++;
                            }
                            sonLastNodes.add(sonLastNode);
                        }
                        sonNode.setChildren(sonLastNodes);
                    }
                    sonNodes.add(sonNode);
                }
                parentNode.setChildren(sonNodes);
                parentNode.setState(ComboTree.STATE_OPEN);
            } else {
                parentNode.setState(ComboTree.STATE_CLOSE);
            }
            if (sonCheckedCount >= topMenu.getChildren().size()) {
                parentNode.setChecked(true);
            }
            sonCheckedCount = 0;
            result.add(parentNode);
        }

        Collections.sort(result, new Comparator<ComboTree>() {

            @Override
            public int compare(ComboTree o1, ComboTree o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        return result;
    }

    private Map<Integer, List<Menu>> findAllMenu(String lang){
        Map<Integer, List<Menu>> menus = new HashMap<Integer, List<Menu>>();
        List<Menu> list = dao.findAll();
        for (Menu menu : list){
            if (!menus.containsKey(menu.getParentId())){
                menus.put(menu.getParentId(), new ArrayList<Menu>());
            }
            menu.setName(languageService.getDescBy(menu.getName(), lang));
            menus.get(menu.getParentId()).add(menu);
        }
        return menus;
    }


    private List<Menu> treeableMenus(String lang, Map<Integer, List<Menu>> allChild, List<Menu> menus) {
        for (Menu menu : menus) {
            menu.setName(languageService.getDescBy(menu.getName(), lang));
            if(allChild.containsKey(menu.getId())){
                menu.setChildren(allChild.get(menu.getId()));
            }
        }
        return menus;
    }

    private void treeable(Map<Integer, Menu> parentMenus, Menu parent) {
        for (int i = 0; i < parent.getChildren().size(); i++) {
            Menu child = parent.getChildren().get(i);
            if (null != parentMenus.get(child.getId())) {
                parent.getChildren().set(i, parentMenus.get(child.getId()));
                treeable(parentMenus, parentMenus.get(child.getId()));
            }
        }
    }
}
