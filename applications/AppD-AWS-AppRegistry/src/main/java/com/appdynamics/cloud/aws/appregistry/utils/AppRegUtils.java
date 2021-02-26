/**
 * 
 */
package com.appdynamics.cloud.aws.appregistry.utils;

import com.appdynamics.cloud.aws.appregistry.ApplicationConfig;
import com.appdynamics.cloud.aws.appregistry.json.Application;

/**
 * @author James Schneider
 *
 */
public class AppRegUtils {

	/**
	 * 
	 */
	public AppRegUtils() {
		
	}

	public static String getAppRegistryAppName(ApplicationConfig config, Application appdApp) throws Throwable {
		
		String baseName = "";
		
		if (config.getAddAppIdToAppName() != null && config.getAddAppIdToAppName()) {
			baseName = appdApp.getName() + "_" + appdApp.getId();
		} else {
			baseName = appdApp.getName();
		}
		
		
		String finalName = StringUtils.replaceAll(baseName, " ", "_");
		finalName = StringUtils.replaceAll(finalName, "-", "_");
		return finalName;
		
	}
	
	public static String getAppRegistryAttrGroupName(ApplicationConfig config, Application appdApp) throws Throwable {
		return getAppRegistryAppName(config, appdApp) + "_AG";
		
	}	
	
}
