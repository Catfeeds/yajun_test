package com.wis.basis.systemadmin.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class JobInstanceVo implements Comparable<JobInstanceVo> {

	private String name;
	private Date startTime;
	private Date lastExecuteTime;
	private Date nextExecuteTime;
	private int state;
	private long timeConsuming;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getLastExecuteTime() {
		return lastExecuteTime;
	}

	public void setLastExecuteTime(Date lastExecuteTime) {
		this.lastExecuteTime = lastExecuteTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getNextExecuteTime() {
		return nextExecuteTime;
	}

	public void setNextExecuteTime(Date nextExecuteTime) {
		this.nextExecuteTime = nextExecuteTime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getTimeConsuming() {
		return timeConsuming;
	}

	public void setTimeConsuming(long timeConsuming) {
		this.timeConsuming = timeConsuming;
	}

	@Override
	public int compareTo(JobInstanceVo o) {
		return this.name.compareTo(o.name);
	}
}
