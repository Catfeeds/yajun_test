package com.wis.basis.common.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeNode {

	private String text;
	private String icon;
	private String selectedIcon;
	private String color;
	private String backColor;
	private String href;
	private Boolean selectable = true;
	private Map<String, Boolean> state = new HashMap<String, Boolean>();
	private List<String> tags = new ArrayList<String>();
	private List<TreeNode> nodes = new ArrayList<TreeNode>();

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

	public void setCheckedState(boolean checked) {
		state.put("checked", checked);
	}

	public void setDisableState(boolean checked) {
		state.put("disabled", checked);
	}

	public void setExpandedState(boolean checked) {
		state.put("expanded", checked);
	}

	public void setSelectedState(boolean checked) {
		state.put("selected", checked);
	}

	public List<String> getTags() {
		return tags;
	}

	public void addTags(String tag) {
		this.tags.add(tag);
	}

	public List<TreeNode> getNodes() {
		return nodes;
	}

	public void addNode(TreeNode node) {
		this.nodes.add(node);
	}
}
