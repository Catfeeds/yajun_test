package com.wis.basis.systemadmin.entity;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wis.bpm.core.interaction.IBPMUser;
import com.wis.core.entity.Removable;
import com.wis.core.framework.entity.Role;
import com.wis.core.framework.model.IUser;

@Entity
@Table(name = "TS_USER")
public class User extends Employee implements Removable, IUser, IBPMUser {

	private static final long serialVersionUID = -8510470937107612052L;
	private String password;
	private String historyPwd;
	private String phone;
	private String email;
	private String gender;
	private Date pwdUpdateTime;
	private Date lastLoginDate;
	/**产线ID**/
	private String tmLineIds;

	private Set<Role> roles;
	private List<String> orgnizationCodes;

	@Column(name = "PASSWORD")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name = "HISTORY_PWD")
	public String getHistoryPwd() {
		return historyPwd;
	}

	public void setHistoryPwd(String historyPwd) {
		this.historyPwd = historyPwd;
	}
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PWD_UPDATE_TIME")
	public Date getPwdUpdateTime() {
		return pwdUpdateTime;
	}

	public void setPwdUpdateTime(Date pwdUpdateTime) {
		this.pwdUpdateTime = pwdUpdateTime;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_LOGIN_DATE")
	public Date getLastLoginDate() {
		return this.lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	@Column(name = "PHONE")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public static User createUserByAccount(String account) {
		User user = new User();
		user.setAccount(account);
		return user;
	}

	@Column(name = "GENDER")
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	public List<String> getOrgnizationCodes() {
		return orgnizationCodes;
	}

	@Override
	public void setOrgnizationCode(List<String> orgnizationCodes) {
		this.orgnizationCodes = orgnizationCodes;
	}

	@Column(name = "tm_line_ids")
	public String getTmLineIds() {
		return tmLineIds;
	}

	public void setTmLineIds(String tmLineIds) {
		this.tmLineIds = tmLineIds;
	}
	
	
}
