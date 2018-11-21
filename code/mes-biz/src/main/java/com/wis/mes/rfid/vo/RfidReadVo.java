package com.wis.mes.rfid.vo;

import com.wis.mes.master.uloc.entity.TmUloc;

public class RfidReadVo {

	private String sn;
	
	private TmUloc tmUloc;
	
	private String message;
	
	private String epcInfo;

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public TmUloc getTmUloc() {
		return tmUloc;
	}

	public void setTmUloc(TmUloc tmUloc) {
		this.tmUloc = tmUloc;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getEpcInfo() {
		return epcInfo;
	}

	public void setEpcInfo(String epcInfo) {
		this.epcInfo = epcInfo;
	}
	
	
}
