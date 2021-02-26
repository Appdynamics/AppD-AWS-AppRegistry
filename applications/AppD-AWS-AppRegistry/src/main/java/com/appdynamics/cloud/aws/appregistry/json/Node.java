/**
 * 
 */
package com.appdynamics.cloud.aws.appregistry.json;

/**
 * @author James Schneider
 *
 */
public class Node {

	private String appAgentVersion;
	private String machineAgentVersion;
	private String agentType;
	private String type;
	private String machineName;
	private boolean appAgentPresent;
	private String nodeUniqueLocalId;
	private int machineId;
	private String machineOSType;
	private int tierId;
	private String tierName;
	private boolean machineAgentPresent;
	private String name;
	private String ipAddresses = null;
	private int id;	
	  
	/**
	 * 
	 */
	public Node() {
		
	}

	public String getAppAgentVersion() {
		return appAgentVersion;
	}

	public void setAppAgentVersion(String appAgentVersion) {
		this.appAgentVersion = appAgentVersion;
	}

	public String getMachineAgentVersion() {
		return machineAgentVersion;
	}

	public void setMachineAgentVersion(String machineAgentVersion) {
		this.machineAgentVersion = machineAgentVersion;
	}

	public String getAgentType() {
		return agentType;
	}

	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	public boolean isAppAgentPresent() {
		return appAgentPresent;
	}

	public void setAppAgentPresent(boolean appAgentPresent) {
		this.appAgentPresent = appAgentPresent;
	}

	public String getNodeUniqueLocalId() {
		return nodeUniqueLocalId;
	}

	public void setNodeUniqueLocalId(String nodeUniqueLocalId) {
		this.nodeUniqueLocalId = nodeUniqueLocalId;
	}

	public int getMachineId() {
		return machineId;
	}

	public void setMachineId(int machineId) {
		this.machineId = machineId;
	}

	public String getMachineOSType() {
		return machineOSType;
	}

	public void setMachineOSType(String machineOSType) {
		this.machineOSType = machineOSType;
	}

	public int getTierId() {
		return tierId;
	}

	public void setTierId(int tierId) {
		this.tierId = tierId;
	}

	public String getTierName() {
		return tierName;
	}

	public void setTierName(String tierName) {
		this.tierName = tierName;
	}

	public boolean isMachineAgentPresent() {
		return machineAgentPresent;
	}

	public void setMachineAgentPresent(boolean machineAgentPresent) {
		this.machineAgentPresent = machineAgentPresent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIpAddresses() {
		return ipAddresses;
	}

	public void setIpAddresses(String ipAddresses) {
		this.ipAddresses = ipAddresses;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	
}
