package com.shava.business.mobile.boundary;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.couchbase.client.java.document.JsonDocument;
import com.shava.business.schedule.control.PersistenceStore;
import com.shava.business.schedule.entity.CalendarType;
import com.shava.business.schedule.entity.Schedule;
import com.shava.business.schedule.entity.User;

@RequestScoped
@Path("calendar")
@Produces({  "application/json" })
@Consumes({  "application/json" })
public class CalendarResource {

	@Inject
	PersistenceStore persistenceStore;
	
	@GET
	@Path("register")
	public String registerUser(){
		User user = new User();
		String data = null;
		user.setName("Raul");
		user.setLastName("Caso");
		user.setUserPassword("1234");
		user.setEmail("feve18@gmail.com");
		user.setScheduleType(CalendarType.SCHEDULE.getCode());
		try{
			data = persistenceStore.registerUser(user).content().toString();
		} catch(Exception ex){
			ex.printStackTrace();
			data = ex.getMessage();
		}
		return data;
	}
	
	@GET
	@Path("login")
	public String loginUser(){
		String userId = null;
		try{
			userId = persistenceStore.loginUser("feve18@gmail.com", "1234").toString();
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return userId;
	}
	
	@GET
	@Path("update")
	public String updateCalendar(){
		String data = null;
		try{
			User user = persistenceStore.loadUser(4L);
			Schedule lunes = new Schedule();
			lunes.setCourse("Gestion de proyectos");
			lunes.setFromDate(LocalTime.of(15,0));
			lunes.setUntilDate(LocalTime.of(17, 0));
			lunes.setDay(DayOfWeek.MONDAY.getValue());
			lunes.setId("1-15");
			lunes.setCalendarType(CalendarType.SCHEDULE.getCode());
			user.setSchedules(new ArrayList<>());
			user.getSchedules().add(lunes);
			JsonDocument document = persistenceStore.saveScheduleUser(user);
			data = document.content().toString();
		} catch(Exception ex){
			ex.printStackTrace();
			data = ex.getMessage();
		}
		return data;
	}
}
