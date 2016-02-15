package com.shava.business.security.entity;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@SessionScoped
public class UserInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3650956304262508849L;
	
	private String name;
	
	private String email;
	
	private Long id;
	
	@PostConstruct
	public void init(){
		System.out.println("postconstructor userInfo");
	}
	
	public String logout(){
		 FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	        return "/login.xhtml?faces-redirect=true";
	}

	public UserInfo() {
		System.out.println("iniciando bean");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
