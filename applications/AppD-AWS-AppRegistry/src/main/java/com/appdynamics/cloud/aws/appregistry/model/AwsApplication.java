/**
 * 
 */
package com.appdynamics.cloud.aws.appregistry.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author James Schneider
 *
 */
public class AwsApplication {
	
	private String arn;
	private String name;
	private String description;
	private String id;
	private Date creationTime;
	private Date lastUpdateTime;
	private int associatedResourceCount;
	private Map<String,String> tags;
	private List<AwsAttributeGroup> attributeGroups;	
	
	/**
	 * 
	 */
	public AwsApplication() {
		
	}


	public String getArn() {
		return arn;
	}


	public void setArn(String arn) {
		this.arn = arn;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public Date getCreationTime() {
		return creationTime;
	}


	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}


	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}


	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}


	public int getAssociatedResourceCount() {
		return associatedResourceCount;
	}


	public void setAssociatedResourceCount(int associatedResourceCount) {
		this.associatedResourceCount = associatedResourceCount;
	}


	public Map<String, String> getTags() {
		return tags;
	}


	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}


	public List<AwsAttributeGroup> getAttributeGroups() {
		return attributeGroups;
	}


	public void setAttributeGroups(List<AwsAttributeGroup> attributeGroups) {
		this.attributeGroups = attributeGroups;
	}
	
	

}
