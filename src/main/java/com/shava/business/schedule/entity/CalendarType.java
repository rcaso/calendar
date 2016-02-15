package com.shava.business.schedule.entity;

public enum CalendarType {

	SCHEDULE(Integer.valueOf(1)),
	
	TASK(Integer.valueOf(2));
	
	private Integer code;

	private CalendarType(Integer code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}
	
	
}
