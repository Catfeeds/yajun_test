package com.wis.basis.common.bizlog;

public enum BizLogOperationType {

	ADD,UPDATE,DELETE,CUSTOM,APP,RELEASE;

	public String getName() {
		return this.name();
	}
}
