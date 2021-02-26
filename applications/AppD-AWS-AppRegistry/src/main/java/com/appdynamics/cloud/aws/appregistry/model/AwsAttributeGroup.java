/**
 * 
 */
package com.appdynamics.cloud.aws.appregistry.model;

/**
 * @author James Schneider
 *
 */
public class AwsAttributeGroup {

	private String arn;
	private String name;
	private String description;
	private String id;
	private String attributes;
	
	
	/**
	 * 
	 */
	public AwsAttributeGroup() {
		
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


	public String getAttributes() {
		return attributes;
	}


	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	
}
