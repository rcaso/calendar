package com.shava.business.schedule.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Task implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7360928907800211755L;
	
	private String id;
	
	private String description;
	
	private LocalDateTime initialDate;
	
	private LocalDateTime finalDate;
	
	private String doctorName;
	
	private Integer calendarType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getInitialDate() {
		return initialDate;
	}

	public void setInitialDate(LocalDateTime initialDate) {
		this.initialDate = initialDate;
	}

	public LocalDateTime getFinalDate() {
		return finalDate;
	}

	public void setFinalDate(LocalDateTime finalDate) {
		this.finalDate = finalDate;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public Integer getCalendarType() {
		return calendarType;
	}

	public void setCalendarType(Integer calendarType) {
		this.calendarType = calendarType;
	}

}
