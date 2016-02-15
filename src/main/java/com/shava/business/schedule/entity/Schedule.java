package com.shava.business.schedule.entity;

import java.io.Serializable;
import java.time.LocalTime;

public class Schedule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7224462686781946786L;
	
	private String id;
	
	private Integer day;
	
	private String course;
	
	private LocalTime fromDate;
	
	private LocalTime untilDate;
	
	private Integer calendarType;

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public LocalTime getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalTime fromDate) {
		this.fromDate = fromDate;
	}

	public LocalTime getUntilDate() {
		return untilDate;
	}

	public void setUntilDate(LocalTime untilDate) {
		this.untilDate = untilDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getCalendarType() {
		return calendarType;
	}

	public void setCalendarType(Integer calendarType) {
		this.calendarType = calendarType;
	}	

}
