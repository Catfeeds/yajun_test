package com.wis.basis.systemadmin.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrgnizationTree {
	public static String TREE_TYPE_COMPANY = "COMPANY";
	public static String TREE_TYPE_PART = "PART";
	public static String TREE_TYPE_SECTION = "SECTION";
	public static String TREE_STATE_OPEN = "open";
	public static String TREE_STATE_CLOSED = "closed";
	public static String TREE_TYPE_FOLDER = "folder";
	public static String TREE_TYPE_ITEM = "item";

	private Integer id;
	private String code;
	
	private String text;
	private String icon = "icon-sitemap";
	private String selectedIcon = "icon-sitemap";
	private String color;
	private String backColor;
	private String href;
	private Boolean selectable = true;
	private Map<String, Boolean> state = new HashMap<String, Boolean>();
	private List<String> tags = new ArrayList<String>();
	private List<OrgnizationTree> nodes = new ArrayList<OrgnizationTree>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<OrgnizationTree> getNodes() {
		return nodes;
	}

	public void setNodes(List<OrgnizationTree> nodes) {
		this.nodes = nodes;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getSelectedIcon() {
		return selectedIcon;
	}

	public void setSelectedIcon(String selectedIcon) {
		this.selectedIcon = selectedIcon;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getBackColor() {
		return backColor;
	}

	public void setBackColor(String backColor) {
		this.backColor = backColor;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public Boolean getSelectable() {
		return selectable;
	}

	public void setSelectable(Boolean selectable) {
		this.selectable = selectable;
	}

	public Map<String, Boolean> getState() {
		return state;
	}

	public void setState(Map<String, Boolean> state) {
		this.state = state;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
}
