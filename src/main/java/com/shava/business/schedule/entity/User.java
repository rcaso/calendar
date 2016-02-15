package com.shava.business.schedule.entity;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2450952495889835667L;
	
	private Long id;
	
	@NotNull
	private String email;
	
	@NotNull
	private String name;
	
	@NotNull
	private String lastName;
	
	@NotNull
	private String userPassword;
	
	private Integer scheduleType;
	
	private List<String> actividades;
	
	private List<Task> tasks;
	
	private List<Schedule> schedules;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String password) {
		this.userPassword = password;
	}

	public Integer getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(Integer scheduleType) {
		this.scheduleType = scheduleType;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<String> getActividades() {
		return actividades;
	}

	public void setActividades(List<String> actividades) {
		this.actividades = actividades;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> task) {
		this.tasks = task;
	}

	public List<Schedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<Schedule> schedules) {
		this.schedules = schedules;
	}

}
