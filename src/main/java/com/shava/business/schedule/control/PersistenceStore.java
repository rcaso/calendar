package com.shava.business.schedule.control;

import java.io.IOException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonProcessingException;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.JsonLongDocument;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.shava.business.schedule.entity.User;
import com.shava.business.store.control.Database;

/**
 * Session Bean implementation class PersistenceStore
 */
@Stateless
@LocalBean
public class PersistenceStore {
	
	@Inject
	Database dataBase;
	
	@Inject
	JsonTransformer jsonTransformer;

    /**
     * Default constructor. 
     */
    public PersistenceStore() {
        // TODO Auto-generated constructor stub
    }
    
    public JsonDocument registerUser(User user) throws JsonProcessingException{
          JsonLongDocument id = dataBase.getBucket().counter("user_sequence", 1);
          user.setId(id.content().longValue());
          JsonDocument document = dataBase.getBucket().insert( jsonTransformer.convertoToJson(user));
          return document;
    }
    
    public Long loginUser(String email,String password){
    	 N1qlQuery query = N1qlQuery.simple("SELECT id from `default` where email = \"" + email + "\" and userPassword = \""+password+"\"");
         System.out.println(query.statement().toString());   
         N1qlQueryResult result = dataBase.getBucket().query(query);
         if (result.finalSuccess() && !result.allRows().isEmpty()) {
             return result.allRows().get(0).value().getLong("id");
         }
    	return null;
    }
    
    public User loadUser(Long id) throws IOException{
    	JsonDocument document = dataBase.getBucket().get("user_"+id);
    	return jsonTransformer.convertFromJson(document);
    }
    
    public JsonDocument saveScheduleUser(User user) throws JsonProcessingException{
    	JsonDocument document = dataBase.getBucket().replace(jsonTransformer.convertoToJson(user));
    	return document;
    }
    
}
