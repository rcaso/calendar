package com.shava.business.schedule.boundary;

import java.io.IOException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonProcessingException;
import com.shava.business.schedule.control.PersistenceStore;
import com.shava.business.schedule.entity.User;

/**
 * Session Bean implementation class CalendarManaged
 */
@Stateless
@LocalBean
public class CalendarManaged {
	
	@Inject
	PersistenceStore persistenceStore;

    /**
     * Default constructor. 
     */
    public CalendarManaged() {
        // TODO Auto-generated constructor stub
    }
    
    public void updateCalendar(User user) throws JsonProcessingException{
    	persistenceStore.saveScheduleUser(user);
    }
    
    public User loadSchedule(Long id) throws IOException{
    	return persistenceStore.loadUser(id);
    }

}
