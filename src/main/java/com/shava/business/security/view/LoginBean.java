package com.shava.business.security.view;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.shava.business.schedule.entity.User;
import com.shava.business.security.boundary.UserSecurity;
import com.shava.business.security.control.AESEncrypt;
import com.shava.business.security.entity.UserInfo;
import com.shava.business.security.entity.UserRegistryInformation;

@Named
@ViewScoped
public class LoginBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7231866902347498561L;
	
	private User user;
	
	private UserRegistryInformation userRegistry;
	
	@Inject
	UserSecurity userSecurity;
	
	@Inject
	Instance<UserInfo> userInfo;
	
	@PostConstruct
	public void init(){
		user = new User();
		userRegistry = new UserRegistryInformation();
	}
	
	public String loginUser(){
		String result = null;
		try{
			Long id = userSecurity.loginUser(user);
			if(id != null){
				//success so
				UserInfo userInfoNew = userInfo.get();
				userInfoNew.setId(id);
				userInfoNew.setEmail(user.getEmail());
				result="pages/calendarview.xhtml?faces-redirect=true";
			} else {
				FacesContext.getCurrentInstance().addMessage("errorLogin", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario o password no valido","Usuario o password no valido"));
			}
		}catch(Exception ex){
			ex.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("errorLogin", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario o password no valido","Usuario o password no valido"));
		}
		return result;
	}
	
	public String registerUser(){
		String result = null;
		try{
			User userNew = new User();
			userNew.setEmail(userRegistry.getUserEmail());
			userNew.setName(userRegistry.getUserName());
			userNew.setLastName(userRegistry.getUserLastName());
			userNew.setUserPassword(AESEncrypt.encrypt(userRegistry.getUserPassword()));
			userSecurity.registerUser(userNew);
			//is ok
			user.setEmail(userNew.getEmail());
			user.setUserPassword(userRegistry.getUserPassword());
			Long id = userSecurity.loginUser(user);
			UserInfo userInfoNew = userInfo.get();
			userInfoNew.setId(id);
			userInfoNew.setEmail(user.getEmail());
			result="pages/calendarview.xhtml?faces-redirect=true";
		}catch(Exception ex){
			ex.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("errorRegister", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al registrar usuario","Error al registrar usuario"));
		}
		return result;
	}

	public LoginBean() {
		// TODO Auto-generated constructor stub
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserRegistryInformation getUserRegistry() {
		return userRegistry;
	}

	public void setUserRegistry(UserRegistryInformation userRegistry) {
		this.userRegistry = userRegistry;
	}

}
