package com.shava.business.security.boundary;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonProcessingException;
import com.shava.business.schedule.control.PersistenceStore;
import com.shava.business.schedule.entity.User;
import com.shava.business.security.control.AESEncrypt;

/**
 * Session Bean implementation class UserSecurity
 */
@Stateless
@LocalBean
public class UserSecurity {
	
	@Inject
	PersistenceStore persistenceStore;

    /**
     * Default constructor. 
     */
    public UserSecurity() {
        // TODO Auto-generated constructor stub
    }
    
    public void registerUser(User user) throws JsonProcessingException{
    	persistenceStore.registerUser(user);
    }
    
    public Long loginUser(User user){
    	Long idUser = null;
    	try{
    		idUser=  persistenceStore.loginUser(user.getEmail(),AESEncrypt.encrypt(user.getUserPassword()));
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	return idUser;
    }

}
