/**
 * 
 */
package com.appdynamics.cloud.aws.appregistry.json;

import java.util.List;

/**
 * @author James Schneider
 *
 */
public class Application {

	private String name;
	private String description;
	private int id;
	private String accountGuid;
	private int numberOfTiers;
	private int numberOfNodes;
	
	private List<Tier> tiers;
	
	/**
	 * 
	 */
	public Application() {
		
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

	public String getAccountGuid() {
		return accountGuid;
	}

	public void setAccountGuid(String accountGuid) {
		this.accountGuid = accountGuid;
	}

	public List<Tier> getTiers() {
		return tiers;
	}

	public void setTiers(List<Tier> tiers) {
		this.tiers = tiers;
	}

	public int getNumberOfTiers() {
		if (this.getTiers() != null && this.getTiers().size() > 0) {
			this.numberOfTiers = this.getTiers().size(); 
			return this.getTiers().size();
		} else {
			this.numberOfTiers = 0;
			return 0;
		}
	}
	
	public int getNumberOfNodes() {
		
		if (this.getTiers() != null && this.getTiers().size() > 0) {
			int nodeCntr = 0;
			
			for (Tier tier : this.getTiers()) {
				
				if (tier.getNodes() != null && tier.getNodes().size() > 0) {
					
					//for (Node node : tier.getNodes()) {
						//if (node.isAppAgentPresent()) {
							//nodeCntr = nodeCntr + 1;
						//}
					//}
					
					
					nodeCntr = nodeCntr + tier.getNodes().size();
				}
			}
			
			this.numberOfNodes = nodeCntr;
			return nodeCntr;
			
		} else {
			this.numberOfNodes = 0;
			return 0;
		}
		
	}
}
