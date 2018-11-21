package com.wis.basis.report.highcharts;

import java.util.Map;
import java.util.TreeMap;

public class Title {
	protected Map<String, Object> content = new TreeMap<String, Object>();

	private CSSStyle cssStyle;

	protected Title(String text, Align align, Integer x) {
		if (null != text) {
			content.put("text", text);
		}
		if (null != align) {
			content.put("align", align.name().toLowerCase());
		}
		if (null != x) {
			content.put("x", x);
		}
	}

	public void setAlign(Align align) {
		content.put("align", align.name().toLowerCase());
	}

	public void setX(int x) {
		content.put("x", x);
	}

	public void setFloating(boolean floating) {
		content.put("floating", floating);
	}

	public void setMargin(int margin) {
		content.put("margin", margin);
	}

	public void setUseHTML(boolean useHTML) {
		content.put("useHTML", useHTML);
	}

	public CSSStyle createCSSStyle() {
		if (null == cssStyle) {
			cssStyle = new CSSStyle();
			content.put("style", cssStyle.content);
		}
		return cssStyle;
	}

	public static enum Align {
		LOW, MIDDLE, HIGH, LEFT, CENTER, RIGHT
	}
}
