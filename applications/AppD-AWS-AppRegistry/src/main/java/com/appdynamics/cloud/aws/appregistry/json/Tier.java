/**
 * 
 */
package com.appdynamics.cloud.aws.appregistry.json;

import java.util.List;

/**
 * @author James Schneider
 *
 */
public class Tier {

	private String agentType;
	private String name;
	private String description;
	private int id;
	private int numberOfNodes;
	private String type;
	 
	private List<Node> nodes;
	
	/**
	 * 
	 */
	public Tier() {
		
	}

	public String getAgentType() {
		return agentType;
	}

	public void setAgentType(String agentType) {
		this.agentType = agentType;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumberOfNodes() {
		return numberOfNodes;
	}

	public void setNumberOfNodes(int numberOfNodes) {
		this.numberOfNodes = numberOfNodes;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	
	
}
