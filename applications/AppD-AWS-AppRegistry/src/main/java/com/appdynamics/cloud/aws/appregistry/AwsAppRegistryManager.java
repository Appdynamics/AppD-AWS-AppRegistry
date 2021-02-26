/**
 * 
 */
package com.appdynamics.cloud.aws.appregistry;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.appregistry.AWSAppRegistry;
import com.amazonaws.services.appregistry.AWSAppRegistryClientBuilder;
import com.amazonaws.services.appregistry.model.Application;
import com.amazonaws.services.appregistry.model.ApplicationSummary;
import com.amazonaws.services.appregistry.model.AssociateAttributeGroupRequest;
import com.amazonaws.services.appregistry.model.AttributeGroup;
import com.amazonaws.services.appregistry.model.CreateApplicationRequest;
import com.amazonaws.services.appregistry.model.CreateApplicationResult;
import com.amazonaws.services.appregistry.model.CreateAttributeGroupRequest;
import com.amazonaws.services.appregistry.model.CreateAttributeGroupResult;
import com.amazonaws.services.appregistry.model.DeleteApplicationRequest;
import com.amazonaws.services.appregistry.model.DeleteApplicationResult;
import com.amazonaws.services.appregistry.model.DeleteAttributeGroupRequest;
import com.amazonaws.services.appregistry.model.DeleteAttributeGroupResult;
import com.amazonaws.services.appregistry.model.DisassociateAttributeGroupRequest;
import com.amazonaws.services.appregistry.model.DisassociateAttributeGroupResult;
import com.amazonaws.services.appregistry.model.GetApplicationRequest;
import com.amazonaws.services.appregistry.model.GetApplicationResult;
import com.amazonaws.services.appregistry.model.GetAttributeGroupRequest;
import com.amazonaws.services.appregistry.model.GetAttributeGroupResult;
import com.amazonaws.services.appregistry.model.ListApplicationsRequest;
import com.amazonaws.services.appregistry.model.ListApplicationsResult;
import com.amazonaws.services.appregistry.model.ListAssociatedAttributeGroupsRequest;
import com.amazonaws.services.appregistry.model.ListAssociatedAttributeGroupsResult;
import com.appdynamics.cloud.aws.appregistry.model.AwsApplication;
import com.appdynamics.cloud.aws.appregistry.model.AwsAttributeGroup;
import com.appdynamics.cloud.aws.appregistry.utils.AppRegUtils;
import com.appdynamics.cloud.aws.appregistry.utils.Logger;
import com.appdynamics.cloud.aws.appregistry.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author James Schneider
 *
 */
public class AwsAppRegistryManager {

	protected static Logger lgr = new Logger("");
	
	private static final String ADTAGKEY_APP_ID = "APPD_APP_ID";
	private static final String ADTAGKEY_APP_NAME = "APPD_APP_NAME";
	private static final String ADTAGKEY_APP_NUM_TIERS = "APPD_APP_NUMBER_OF_TIERS";
	private static final String ADTAGKEY_APP_NUM_NODES = "APPD_APP_NUMBER_OF_NODES";
	private static final String ADTAGKEY_APP_CONTROLLER_HOST = "APPD_APP_CONTROLLER_HOST";
	
	private AWSAppRegistry appRegistry;
	
	/**
	 * 
	 */
	public AwsAppRegistryManager() throws Throwable {
		
		this.appRegistry = AWSAppRegistryClientBuilder.standard().withRegion(Regions.fromName(Regions.getCurrentRegion().getName())).build();
		
	}
	
	public Application createApplication(com.appdynamics.cloud.aws.appregistry.json.Application appdApp, ApplicationConfig config) throws Throwable {
		
		CreateApplicationRequest caReq = new CreateApplicationRequest();
		
		caReq.setName(AppRegUtils.getAppRegistryAppName(appdApp));
		caReq.setDescription(appdApp.getDescription());
		caReq.addTagsEntry(ADTAGKEY_APP_ID, appdApp.getId() + "");
		caReq.addTagsEntry(ADTAGKEY_APP_NAME, appdApp.getName());
		caReq.addTagsEntry(ADTAGKEY_APP_NUM_TIERS, appdApp.getNumberOfTiers() + "");
		caReq.addTagsEntry(ADTAGKEY_APP_NUM_NODES, appdApp.getNumberOfNodes() + "");
		caReq.addTagsEntry(ADTAGKEY_APP_CONTROLLER_HOST, config.getControllerHostName());

		CreateApplicationResult caRes = this.appRegistry.createApplication(caReq);
		Application awsApp = caRes.getApplication();
		
		CreateAttributeGroupRequest cagReq = new CreateAttributeGroupRequest();
		cagReq.setName(AppRegUtils.getAppRegistryAttrGroupName(appdApp));
		cagReq.setDescription("Json model for Application : " + AppRegUtils.getAppRegistryAppName(appdApp));
		
		//lgr.info("    - AttrGroup Name: " + cagReq.getName());
		//lgr.info("    - AttrGroup Description: " + cagReq.getDescription());
		//cagReq.addTagsEntry(key, value)
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(appdApp);
		cagReq.setAttributes(json);
		
		StringUtils.saveStringAsFile(config.getJsonFilesDirectory() + "/" + caReq.getName() + ".json", json);
		
		CreateAttributeGroupResult cagRes = this.appRegistry.createAttributeGroup(cagReq);
		AttributeGroup attrGroup = cagRes.getAttributeGroup();
		
		AssociateAttributeGroupRequest aagReq = new AssociateAttributeGroupRequest();
		aagReq.setApplication(awsApp.getId());
		aagReq.setAttributeGroup(attrGroup.getId());
		this.appRegistry.associateAttributeGroup(aagReq);
		
		
		return awsApp;
	}
	
	public void deleteApplications() throws Throwable {
		List<AwsApplication> awsApps = this.listApplications();
		
		if (awsApps != null && awsApps.size() > 0) {
			
			for (AwsApplication awsApp : awsApps) {
				
				List<AwsAttributeGroup> attrGroups = awsApp.getAttributeGroups();
				if (attrGroups != null && attrGroups.size() > 0) {
					
					for (AwsAttributeGroup attrGroup : attrGroups) {
						
						DisassociateAttributeGroupRequest diagReq = new DisassociateAttributeGroupRequest();
						diagReq.setApplication(awsApp.getId());
						diagReq.setAttributeGroup(attrGroup.getId());
						DisassociateAttributeGroupResult diagRes =  this.appRegistry.disassociateAttributeGroup(diagReq);
						
						
						DeleteAttributeGroupRequest deagReq = new DeleteAttributeGroupRequest();
						deagReq.setAttributeGroup(attrGroup.getId());
						DeleteAttributeGroupResult deagRes = this.appRegistry.deleteAttributeGroup(deagReq);
						
						
					}
				}
				
				// delete app
				DeleteApplicationRequest daReq = new DeleteApplicationRequest();
				daReq.setApplication(awsApp.getId());
				DeleteApplicationResult daRes = this.appRegistry.deleteApplication(daReq);
				
				
			}
			
			
			
		}
		
		
	}
	
	public List<AwsApplication> listApplications() throws Throwable {
	
		List<AwsApplication> awsAppsList = new ArrayList<AwsApplication>();
		
		List<ApplicationSummary> appSummaryList = null;
		
		ListApplicationsRequest laReq = new ListApplicationsRequest();
		
		laReq.setMaxResults(25);
		
		ListApplicationsResult laRes = this.appRegistry.listApplications(laReq);
		
		if (laRes != null) {
			appSummaryList = laRes.getApplications();
			
			GetApplicationRequest gaReq;
			GetApplicationResult qaRes;
			
			ListAssociatedAttributeGroupsRequest laagReq;
			ListAssociatedAttributeGroupsResult laagRes;
			
			List<String> attrGroupIds;
			
			GetAttributeGroupRequest gagReq;
			GetAttributeGroupResult gagRes;
			
			AwsApplication awsApp;
			AwsAttributeGroup awsAttrGroup;
			
			if (appSummaryList!= null) {
				
				for (ApplicationSummary appSum : appSummaryList) {
					
					awsApp = new AwsApplication();
					awsApp.setId(appSum.getId());
					awsApp.setName(appSum.getName());
					awsApp.setDescription(appSum.getDescription());
					awsApp.setArn(appSum.getArn());
					awsApp.setCreationTime(appSum.getCreationTime());
					awsApp.setLastUpdateTime(appSum.getLastUpdateTime());
					
					gaReq = new GetApplicationRequest();
					gaReq.setApplication(appSum.getId());
					qaRes = this.appRegistry.getApplication(gaReq);
					
					awsApp.setAssociatedResourceCount(qaRes.getAssociatedResourceCount());
					awsApp.setTags(qaRes.getTags());
					
					
					// get attribute groups for application
					laagReq = new ListAssociatedAttributeGroupsRequest();
					laagReq.setApplication(awsApp.getId());
					laagReq.setMaxResults(25);
					
					laagRes = this.appRegistry.listAssociatedAttributeGroups(laagReq);
					attrGroupIds = laagRes.getAttributeGroups();
					
					if (attrGroupIds != null) {
						
						List<AwsAttributeGroup> attrGroupList = new ArrayList<AwsAttributeGroup>();
						
						for (String agId : attrGroupIds) {
							
							gagReq = new GetAttributeGroupRequest();
							gagReq.setAttributeGroup(agId);
							gagRes = this.appRegistry.getAttributeGroup(gagReq);
							
							awsAttrGroup = new AwsAttributeGroup();
							
							awsAttrGroup.setId(gagRes.getId());
							awsAttrGroup.setName(gagRes.getName());
							awsAttrGroup.setDescription(gagRes.getDescription());
							awsAttrGroup.setArn(gagRes.getArn());
							awsAttrGroup.setAttributes(gagRes.getAttributes());
							
							attrGroupList.add(awsAttrGroup);
							
						}
						
						awsApp.setAttributeGroups(attrGroupList);
					}
					

					awsAppsList.add(awsApp);
				}
				
			}
			
			
			

		}
		
		
		return awsAppsList;
		
	}
	
	
	
	
}
