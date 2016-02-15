package com.shava.business.schedule.view;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import com.shava.business.schedule.boundary.CalendarManaged;
import com.shava.business.schedule.control.DateUtils;
import com.shava.business.schedule.entity.CalendarType;
import com.shava.business.schedule.entity.Schedule;
import com.shava.business.schedule.entity.Task;
import com.shava.business.schedule.entity.User;
import com.shava.business.security.entity.UserInfo;

@Named
@ViewScoped
public class CalendarBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3791035391689928447L;

	@Inject
	UserInfo userInfo;

	@Inject
	CalendarManaged calendarManaged;

	private User user;

	private ScheduleModel model;
	
	private ScheduleModel modelTask;

	private DefaultScheduleEvent event = new DefaultScheduleEvent();
	
	private DefaultScheduleEvent task = new DefaultScheduleEvent();

	public void addEvent(ActionEvent actionEvent) {
		LocalDateTime starTime = DateUtils.asLocalDateTime(event.getStartDate());
		LocalDateTime endTime = DateUtils.asLocalDateTime(event.getEndDate());
		if(user.getSchedules()==null){
			user.setSchedules(new ArrayList<>());
		}
		createSchedule(user.getSchedules(), event, starTime.getDayOfWeek().getValue(), starTime, endTime);
	}
	
	public void addTask(ActionEvent event){
		LocalDateTime starTime = DateUtils.asLocalDateTime(task.getStartDate());
		LocalDateTime endTime = DateUtils.asLocalDateTime(task.getEndDate());
		try{
			if(user.getTasks()==null){
				user.setTasks(new ArrayList<>());
			}
			if(task.getId()==null){
				Task taskUser = new Task();
				taskUser.setDescription(task.getTitle());
				taskUser.setCalendarType(CalendarType.TASK.getCode());
				taskUser.setInitialDate(starTime);
				taskUser.setFinalDate(endTime);
				taskUser.setId(starTime.toString());
				user.getTasks().add(taskUser);
				calendarManaged.updateCalendar(user);
				task.setData(starTime.toString());
				task.setDescription(task.getTitle());
				modelTask.addEvent(task);
			} else {
				for(Task taskUser : user.getTasks()){
					if(taskUser.getId().equals(task.getData().toString())){
						taskUser.setInitialDate(starTime);
						taskUser.setFinalDate(endTime);
						taskUser.setId(starTime.toString());
						taskUser.setDescription(task.getTitle());
						calendarManaged.updateCalendar(user);
						task.setData(starTime.toString());
						task.setDescription(task.getTitle());
						modelTask.addEvent(task);
						break;
					}
				}
			}
			task = new DefaultScheduleEvent();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}

	private void createSchedule(List<Schedule> schedules, DefaultScheduleEvent event, int day, LocalDateTime starTime,
			LocalDateTime endTime) {
		try {
			if (event.getId() == null) {
				Schedule schedule = new Schedule();
				schedule.setCourse(event.getTitle());
				schedule.setDay(Integer.valueOf(day));
				schedule.setFromDate(starTime.toLocalTime());
				schedule.setUntilDate(endTime.toLocalTime());
				schedule.setId(day + "-" + schedule.getFromDate().getHour());
				schedule.setCalendarType(CalendarType.SCHEDULE.getCode());
				schedules.add(schedule);
				calendarManaged.updateCalendar(user);
				event.setData(day + "-" + schedule.getFromDate().getHour());
				event.setDescription(schedule.getCourse());
				model.addEvent(event);
			} else {
				for (Schedule schedule : schedules) {
					if (schedule.getId().equals(event.getData().toString())) {
						schedule.setCourse(event.getTitle());
						schedule.setFromDate(starTime.toLocalTime());
						schedule.setUntilDate(endTime.toLocalTime());
						calendarManaged.updateCalendar(user);
						event.setDescription(schedule.getCourse());
						model.updateEvent(event);
						break;
					}
				}
			}
			event = new DefaultScheduleEvent();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void onEventSelect(SelectEvent selectEvent) {
		event = (DefaultScheduleEvent) selectEvent.getObject();
	}

	public void onDateSelect(SelectEvent selectEvent) {
		event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
	}
	
	public void onTaskSelect(SelectEvent selectEvent) {
		task = (DefaultScheduleEvent) selectEvent.getObject();
	}

	public void onDateTaskSelect(SelectEvent selectEvent) {
		task = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
	}

	public void loadSchedule() {
		try {
			user = calendarManaged.loadSchedule(userInfo.getId());
			userInfo.setName(user.getName()+" "+user.getLastName());
			model = createCalendar(user);
			modelTask = createTask(user);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private ScheduleModel createTask(User userTask) {
		DefaultScheduleModel task = new DefaultScheduleModel();
		if(user.getTasks()!=null && !user.getTasks().isEmpty()){
			for(Task taskDay : user.getTasks()){
				DefaultScheduleEvent defaultTask = new DefaultScheduleEvent(taskDay.getDescription()
						, DateUtils.asDate(taskDay.getInitialDate()), DateUtils.asDate(taskDay.getFinalDate()));
				defaultTask.setData(taskDay.getId());
				defaultTask.setDescription(taskDay.getDescription());
				task.addEvent(defaultTask);
			}
		}
		return task;
	}

	private DefaultScheduleModel createCalendar(User user) {
		DefaultScheduleModel schedule = new DefaultScheduleModel();
		LocalDate today = LocalDate.now();
		if(user.getSchedules()!=null && !user.getSchedules().isEmpty()){
			createEvents(schedule, today, user.getSchedules());
		}
		return schedule;
	}

	private void createEvents(DefaultScheduleModel scheduleModel, LocalDate today, List<Schedule> schedules) {
		DayOfWeek numberToDay = today.getDayOfWeek();
		if(numberToDay.getValue() == DayOfWeek.SUNDAY.getValue()){
			for (Schedule schedule : schedules) {
				LocalDate calendarDay = null;
				if (numberToDay.getValue() > schedule.getDay().intValue()) {
					calendarDay = today.plusDays(schedule.getDay().intValue());
				} else {
					// same day
					calendarDay = today;
				}
				LocalDateTime initial = LocalDateTime.of(calendarDay, schedule.getFromDate());
				LocalDateTime end = LocalDateTime.of(calendarDay, schedule.getUntilDate());
				DefaultScheduleEvent defaultEvent = new DefaultScheduleEvent(schedule.getCourse(),
						DateUtils.asDate(initial), DateUtils.asDate(end));
				defaultEvent.setData(schedule.getId());
				defaultEvent.setDescription(schedule.getCourse());
				scheduleModel.addEvent(defaultEvent);
			}
		} else {
			for (Schedule schedule : schedules) {
				LocalDate calendarDay = null;
				if (numberToDay.getValue() > schedule.getDay().intValue()) {
					int days = numberToDay.getValue() - schedule.getDay().intValue();
					calendarDay = today.minusDays(days);
				} else if (numberToDay.getValue() < schedule.getDay().intValue()) {
					int days = schedule.getDay().intValue() - numberToDay.getValue();
					calendarDay = today.plusDays(days);
				} else {
					// same day
					calendarDay = today;
				}
				LocalDateTime initial = LocalDateTime.of(calendarDay, schedule.getFromDate());
				LocalDateTime end = LocalDateTime.of(calendarDay, schedule.getUntilDate());
				DefaultScheduleEvent defaultEvent = new DefaultScheduleEvent(schedule.getCourse(),
						DateUtils.asDate(initial), DateUtils.asDate(end));
				defaultEvent.setData(schedule.getId());
				defaultEvent.setDescription(schedule.getCourse());
				scheduleModel.addEvent(defaultEvent);
			}
		}
		
	}
	
	public TimeZone getTimeZone(){
		return TimeZone.getDefault();
	}

	public CalendarBean() {
		// TODO Auto-generated constructor stub
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ScheduleModel getModel() {
		return model;
	}

	public void setModel(ScheduleModel model) {
		this.model = model;
	}

	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(DefaultScheduleEvent event) {
		this.event = event;
	}

	public ScheduleModel getModelTask() {
		return modelTask;
	}

	public void setModelTask(ScheduleModel modelSchedule) {
		this.modelTask = modelSchedule;
	}

	public DefaultScheduleEvent getTask() {
		return task;
	}

	public void setTask(DefaultScheduleEvent task) {
		this.task = task;
	}

}
