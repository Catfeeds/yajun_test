package com.wis.basis.systemadmin.vo;

public class OrgnizationVo {

	private Integer id;
	private String dimension;
	private String name;
	private String code;
	// private OrgnizationVo parent;
	private String parentSystemCode;
	private String type;
	private String description;
	private Integer leaderId;
	private Integer parentId;
	// private User leader;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	// public OrgnizationVo getParent() {
	// return parent;
	// }
	//
	// public void setParent(OrgnizationVo parent) {
	// this.parent = parent;
	// }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(Integer leaderId) {
		this.leaderId = leaderId;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getParentSystemCode() {
		return parentSystemCode;
	}

	public void setParentSystemCode(String parentSystemCode) {
		this.parentSystemCode = parentSystemCode;
	}

	// public User getLeader() {
	// return leader;
	// }
	//
	// public void setLeader(User leader) {
	// this.leader = leader;
	// }

}
