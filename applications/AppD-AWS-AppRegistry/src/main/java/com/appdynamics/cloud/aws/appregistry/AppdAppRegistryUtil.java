package com.appdynamics.cloud.aws.appregistry;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.appdynamics.cloud.aws.appregistry.json.Application;
import com.appdynamics.cloud.aws.appregistry.model.AwsApplication;
import com.appdynamics.cloud.aws.appregistry.model.AwsAttributeGroup;
import com.appdynamics.cloud.aws.appregistry.utils.DateUtils;
import com.appdynamics.cloud.aws.appregistry.utils.Logger;
import com.appdynamics.cloud.aws.appregistry.utils.StringUtils;



public class AppdAppRegistryUtil {

	protected static final String APP_CONF_KEY = "appConfigFilePath";
	protected static final String APP_ACTION_KEY = "appAction";
	
	protected static final String APP_ACTION_CREATE = "create";
	protected static final String APP_ACTION_DELETE = "delete";
	protected static final String APP_ACTION_LIST = "list";
	
	public static boolean APP_ADD_APPID2NAME = false;
	
	
	
	protected static ApplicationConfig APP_CONFIG;
	
	protected static Logger lgr = new Logger("");
	
	
	public static void main(String[] args) {
		
		try {
			
			
			lgr.printBanner(true);
			
			lgr.log("#########################################################################################    STARTING APPDYNAMICS AWS APPREGISTRY UTILITIES    ################################################################################");
			lgr.log("");
			
			
			String action = System.getProperty(APP_ACTION_KEY);
			
			if (action == null) {
				lgr.error("Missing startup property -D" + APP_ACTION_KEY);
				lgr.error("Please set this property -D" + APP_ACTION_KEY + "=" + APP_ACTION_CREATE 
						+ "  OR -D" + APP_ACTION_KEY + "=" + APP_ACTION_DELETE + "  OR -D" + APP_ACTION_KEY + "=" + APP_ACTION_LIST);
				lgr.carriageReturn();
				lgr.log("#########################################################################################    FINISHED APPDYNAMICS AWS APPREGISTRY UTILITIES    ################################################################################");

				System.exit(1);
			}

			
			// LOAD CONFIG
			String confPath = System.getProperty(APP_CONF_KEY);
			
			if (confPath == null) {
				lgr.error("Missing startup property -D" + APP_CONF_KEY);
				lgr.error("Please set this property -D" + APP_CONF_KEY + "=<path to application-config.yaml file>");
				lgr.carriageReturn();
				lgr.log("#########################################################################################    FINISHED APPDYNAMICS AWS APPREGISTRY UTILITIES    ################################################################################");

				System.exit(1);
			}

			lgr.info("");
			lgr.info(" - Initializing configuration at: " + confPath);
			
			//lgr.info("");
			//lgr.info(" - Processing the '" + action + "' action" + confPath);

			
			Yaml yaml = new Yaml(new Constructor(ApplicationConfig.class));
			InputStream inputStream = StringUtils.getFileAsStream(confPath);
			APP_CONFIG = yaml.load(inputStream);
			
			
			
			switch (action) {
			
			case APP_ACTION_CREATE:

				lgr.info("");
				lgr.info(" - Found " + APP_CONFIG.getApplicationNames().size() + " Applications configured to '" + action + "'");
				
				create();
				break;

			case APP_ACTION_DELETE:
				
				delete();
				break;

			case APP_ACTION_LIST:
				
				lgr.info("");
				lgr.info(" - Listing ALL Applications within AppRegistry");
				
				list();
				break;
				
			default:
				
				lgr.error("Missing startup property -D" + APP_ACTION_KEY);
				lgr.error("Please set this property -D" + APP_ACTION_KEY + "=" + APP_ACTION_CREATE 
						+ "  OR -D" + APP_ACTION_KEY + "=" + APP_ACTION_DELETE);
				lgr.carriageReturn();
				lgr.log("#########################################################################################    FINISHED APPDYNAMICS AWS APPREGISTRY UTILITIES    ################################################################################");

				System.exit(1);
				
				break;
			}
			
			
			
		} catch (Throwable ex) {
			lgr.log(ex.getMessage());
			ex.printStackTrace();
			
		}
		

		lgr.log("");
		lgr.log("#########################################################################################    FINISHED APPDYNAMICS AWS APPREGISTRY UTILITIES    ################################################################################");
		
		
		
	}

	
	protected static void create() throws Throwable {

		
		lgr.info("");
		lgr.info(" - Initializing connection to AppDynamics Controller");
		
		AppdControllerManager controller = AppdControllerManager.initControllerClient(APP_CONFIG);
		
		List<Application> appsList = controller.getApplicationList();
		
		lgr.info("");
		lgr.info(" - Retrieved " + appsList.size() + " Applications from AppDynamics Controller");
		lgr.info("");
		
		List<Application> appsToPublish = new ArrayList<Application>();
		
		String matchMsg = "   - Searching for matching applications to publish --";
		for (int i = 0; i < appsList.size(); i++) {
			matchMsg = matchMsg + "-";
		}

		
		for (Application app : appsList) {
			
			lgr.info(matchMsg);
			matchMsg = matchMsg.substring(0, matchMsg.length() - 1);
			Thread.currentThread().sleep(500);
			
			for (String appName : APP_CONFIG.getApplicationNames()) {
				if (appName.equals(app.getName())) {
					appsToPublish.add(app);						
				}
			}
			
		}
		
		Thread.currentThread().sleep(500);
		
		lgr.info("");
		lgr.info(" - Found " + appsToPublish.size() + " matching Application(s) out of " + APP_CONFIG.getApplicationNames().size() + " to publish to AppRegistry");
		lgr.info("");
		
		Thread.currentThread().sleep(500);
		
		// find the configured apps that had no match in the controller
		boolean foundApp = false;
		List<String> missingApps = new ArrayList<String>();
		
		for (String appConfigd : APP_CONFIG.getApplicationNames()) {
			foundApp = false;
			for (Application appToPub : appsToPublish) {
				
				if (appToPub.getName().equals(appConfigd)) {
					foundApp = true;
				}
				
			}
			
			if (!foundApp) {
				missingApps.add(appConfigd);
			}
		}
		
		if (missingApps.size() > 0) {
			lgr.info("  -------------------------  Appplications configured but not found in Controller  -------------------------");
			for (String appNotFound : missingApps) {
				lgr.info("  App Name: " + appNotFound);
			}
			lgr.info("");
			
			Thread.currentThread().sleep(1000);			
		}
		
		
		
		lgr.info("  ---------------------------------------  Appplications to Publish  ---------------------------------------");
		
		for (Application app : appsToPublish) {
			
			controller.getTiersAndNodesForApplication(app);
			
			lgr.info("  App Name: " + app.getName());
			lgr.info("  App Id: " + app.getId());
			lgr.info("  App Description: " + app.getDescription());
			lgr.info("  Account Guid: " + app.getAccountGuid());
			
			lgr.info("  Number of Tiers: " + app.getNumberOfTiers());
			lgr.info("  Number of Nodes: " + app.getNumberOfNodes());
			lgr.info("  ----------------------------------------------------------------------------------------------------------");
			
			Thread.currentThread().sleep(500);
		}
		
		
		lgr.info("");
		lgr.info(" - Initializing connection to AWS AppRegistry");
		lgr.info("");
		
		AwsAppRegistryManager appReg = new AwsAppRegistryManager();
		
		com.amazonaws.services.appregistry.model.Application awsApp;
		
		lgr.info("");
		lgr.info("  --------------------------------  Starting publication to AWS AppRegistry  -------------------------------");
		lgr.info("");
		lgr.info("");
		
		for (Application app : appsToPublish) {
			
			awsApp = appReg.createApplication(app, APP_CONFIG);
			
			lgr.info("  ################################### Application Successfully Published ###################################");
			lgr.info("  App Name: " + awsApp.getName());
			lgr.info("  App Id: " + awsApp.getId());
			lgr.info("  App Description: " + awsApp.getDescription());
			lgr.info("  App ARN: " + awsApp.getArn());
			lgr.info("  ----------------------------------------------------------------------------------------------------------");
			lgr.info("");
			lgr.info("");
			
		}
		
		
//		Thread.currentThread().sleep(2000);
//		
//		//String json = controller.getNodesAsJson("AD-Financial-Cloud");
//		Gson gson = new GsonBuilder().setPrettyPrinting().create();
//		String json =gson.toJson(appsToPublish.get(0));
//		
//		lgr.log("");
//		lgr.log("******************************************************************************************************************************************");
//		lgr.log(json);
//		lgr.log("******************************************************************************************************************************************");

		
		
	}
	
	protected static void list() throws Throwable {
		
		lgr.info("");
		lgr.info(" - Initializing connection to AWS AppRegistry");
		
		AwsAppRegistryManager appReg = new AwsAppRegistryManager();
		
		List<AwsApplication> awsApps = appReg.listApplications();
		
		if (awsApps == null || awsApps.size() < 1) {
			lgr.info("");
			lgr.info("  -----------------------------  No Applications were found in AWS AppRegistry  ----------------------------");
			lgr.info("");
			lgr.info("");			
		} else {
			lgr.info("");
			lgr.info("  --------------------------------  Found " + awsApps.size() + " Applications in AWS AppRegistry  -------------------------------");
			lgr.info("");
			lgr.info("");			
			
			for (AwsApplication awsApp : awsApps) {
				
				lgr.info("  ########################################### Application Found ############################################");
				lgr.info("  App Name: " + awsApp.getName());
				lgr.info("  App Id: " + awsApp.getId());
				lgr.info("  App Description: " + awsApp.getDescription());
				lgr.info("  App ARN: " + awsApp.getArn());
				lgr.info("  App Associated Resource Count: " + awsApp.getAssociatedResourceCount());
				lgr.info("  App Creation Time: " + DateUtils.formatDateTime(awsApp.getCreationTime()));
				lgr.info("  App Last Update Time: " + DateUtils.formatDateTime(awsApp.getLastUpdateTime()));
				
				Thread.currentThread().sleep(1000);
				
				Map<String, String> awsTags = awsApp.getTags();
				if (awsTags != null && awsTags.size() > 0) {
					lgr.info("");
					lgr.info("  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Tags Found ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
					
					for (String tagKey : awsTags.keySet()) {
						lgr.info("  " + tagKey + ": " + awsTags.get(tagKey));
					}
					
					Thread.currentThread().sleep(1000);
				}
				
				List<AwsAttributeGroup> attrGroups = awsApp.getAttributeGroups();
				if (attrGroups != null && attrGroups.size() > 0) {
					lgr.info("");
					lgr.info("  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Attribute Groups Found ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
					
					
					for (AwsAttributeGroup agroup : attrGroups) {
						lgr.info("  Group Name: " + agroup.getName());
						lgr.info("  Group Id: " + agroup.getId());
						lgr.info("  Group Description: " + agroup.getDescription());
						lgr.info("  Group ARN: " + agroup.getArn());
						lgr.info("  Group Attributes: ");
						lgr.log(agroup.getAttributes());
						
						lgr.info("  ----------------------------------------------------------------------------------------------------------");
					}
					
					Thread.currentThread().sleep(1000);
					
				}
				
				
				
				lgr.info("  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
				lgr.info("");
				lgr.info("");
				
			}
			
			
		}
		
	}
	
	
	
	protected static void delete() throws Throwable {
		
		lgr.info("");
		
		lgr.info(" - This action has been disabled");
		
		//lgr.info(" - Initializing connection to AWS AppRegistry");
		
		//AwsAppRegistryManager appReg = new AwsAppRegistryManager();
		//appReg.deleteApplications();
		
		
	}
	
	
	
}
