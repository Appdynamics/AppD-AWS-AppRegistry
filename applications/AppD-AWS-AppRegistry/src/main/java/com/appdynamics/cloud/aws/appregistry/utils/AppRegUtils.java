/**
 * 
 */
package com.appdynamics.cloud.aws.appregistry.utils;

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

	public static String getAppRegistryAppName(Application appdApp) throws Throwable {
		
		String baseName = appdApp.getName() + "_" + appdApp.getId();
		String finalName = StringUtils.replaceAll(baseName, " ", "_");
		finalName = StringUtils.replaceAll(finalName, "-", "_");
		return finalName;
		
	}
	
	public static String getAppRegistryAttrGroupName(Application appdApp) throws Throwable {
		return getAppRegistryAppName(appdApp) + "_AG";
		
	}	
	
}
