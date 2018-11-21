package com.wis.mes.rfid.vo;

public class TagVo {
	private String epcId;
	private String signalIntensity;
	private String userTagId;
	private String epcTagInfo;
	private String ant;

	public String getEpcId() {
		return epcId;
	}

	public void setEpcId(String epcId) {
		this.epcId = epcId;
	}

	public String getSignalIntensity() {
		return signalIntensity;
	}

	public void setSignalIntensity(String signalIntensity) {
		this.signalIntensity = signalIntensity;
	}

	public String getUserTagId() {
		return userTagId;
	}

	public void setUserTagId(String userTagId) {
		this.userTagId = userTagId;
	}

	public TagVo() {
	}

	public TagVo(String epcId, String signalIntensity, String epcTagInfo) {
		this.epcId = epcId;
		this.signalIntensity = signalIntensity;
		this.epcTagInfo = epcTagInfo;
	}

	public String getEpcTagInfo() {
		return epcTagInfo;
	}

	public void setEpcTagInfo(String epcTagInfo) {
		this.epcTagInfo = epcTagInfo;
	}

	public String getAnt() {
		return ant;
	}

	public void setAnt(String ant) {
		this.ant = ant;
	}

	public static TagVo createTagVo(String epcId,String ant,String signalIntensity,String epcTagInfo) {
		TagVo tagVo = new TagVo();
		tagVo.setAnt(ant);
		tagVo.setEpcId(epcId);
		tagVo.setSignalIntensity(signalIntensity);
		tagVo.setEpcTagInfo(epcTagInfo);
		return tagVo;
	}
}
