/**
 * 
 */
package com.appdynamics.cloud.aws.appregistry;

import java.util.List;

/**
 * @author James Schneider
 *
 */
public class ApplicationConfig {

	private String controllerAccount;
	private String controllerUsername;
	private String controllerPassword;
	private String controllerHostName;
	private Integer controllerPort;
	private Boolean controllerSslEnabled;
	private Boolean addAppIdToAppName;
	private String jsonFilesDirectory;
	
	private List<String> applicationNames;
	
	
	/**
	 * 
	 */
	public ApplicationConfig() {
		
	}


	public String getControllerAccount() {
		return controllerAccount;
	}


	public void setControllerAccount(String controllerAccount) {
		this.controllerAccount = controllerAccount;
	}


	public String getControllerUsername() {
		return controllerUsername;
	}


	public void setControllerUsername(String controllerUsername) {
		this.controllerUsername = controllerUsername;
	}


	public String getControllerPassword() {
		return controllerPassword;
	}


	public void setControllerPassword(String controllerPassword) {
		this.controllerPassword = controllerPassword;
	}


	public String getControllerHostName() {
		return controllerHostName;
	}


	public void setControllerHostName(String controllerHostName) {
		this.controllerHostName = controllerHostName;
	}


	public Integer getControllerPort() {
		return controllerPort;
	}


	public void setControllerPort(Integer controllerPort) {
		this.controllerPort = controllerPort;
	}


	public Boolean getControllerSslEnabled() {
		return controllerSslEnabled;
	}


	public void setControllerSslEnabled(Boolean controllerSslEnabled) {
		this.controllerSslEnabled = controllerSslEnabled;
	}


	public Boolean getAddAppIdToAppName() {
		return addAppIdToAppName;
	}


	public void setAddAppIdToAppName(Boolean addAppIdToAppName) {
		this.addAppIdToAppName = addAppIdToAppName;
	}


	public String getJsonFilesDirectory() {
		return jsonFilesDirectory;
	}


	public void setJsonFilesDirectory(String jsonFilesDirectory) {
		this.jsonFilesDirectory = jsonFilesDirectory;
	}


	public List<String> getApplicationNames() {
		return applicationNames;
	}


	public void setApplicationNames(List<String> applicationNames) {
		this.applicationNames = applicationNames;
	}

	
	
}
