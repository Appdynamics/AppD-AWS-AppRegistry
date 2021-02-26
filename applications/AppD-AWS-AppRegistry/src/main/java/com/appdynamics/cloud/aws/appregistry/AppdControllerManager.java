/**
 * 
 */
package com.appdynamics.cloud.aws.appregistry;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.appdynamics.cloud.aws.appregistry.json.Application;
import com.appdynamics.cloud.aws.appregistry.json.Node;
import com.appdynamics.cloud.aws.appregistry.json.Tier;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author James Schneider
 *
 */
public class AppdControllerManager {

	private ApplicationConfig appConfig;
	private String controllerUrl;
	private String userAtAccount;
	private String userPwd;
	private  String params;
	
	/**
	 * 
	 */
	private AppdControllerManager(ApplicationConfig config) throws Throwable {
		this.appConfig = config;
		
		String proto = "http://";
		
		if (this.appConfig.getControllerSslEnabled().booleanValue() == true) {
			proto = "https://";
		}
		
		this.controllerUrl = proto + this.appConfig.getControllerHostName() + ":" + this.appConfig.getControllerPort();
		this.userAtAccount = this.appConfig.getControllerUsername() + "@" + this.appConfig.getControllerAccount();
		this.userPwd = this.appConfig.getControllerPassword();
	}

	
	public static AppdControllerManager initControllerClient(ApplicationConfig config) throws Throwable {
		
		AppdControllerManager mgr = new AppdControllerManager(config);
		
		return mgr;
	}
	

	public static void closeControllerClient() throws Throwable {
		
	}
	
	public void getTiersAndNodesForApplication(Application app) throws Throwable {
		
		List<Tier> tiers = this.getTierList(app.getName());
		List<Node> nodes = this.getNodeList(app.getName());
		
		for (Tier tier : tiers) {
			List<Node> tierNodes = new ArrayList<Node>();
			
			for (Node node : nodes) {
				
				if (node.getTierId() == tier.getId()) {
					tierNodes.add(node);
				}
			}
			
			tier.setNodes(tierNodes);
			
		}
		
		app.setTiers(tiers);
		
	}
	
	public List<Application> getApplicationList() throws Throwable {
		
		String json = this.getApplicationsAsJson();
		
		List<Application> apps = new Gson().fromJson(json, new TypeToken<List<Application>>() {}.getType());
		
		return apps;
	}
	
	public List<Tier> getTierList(String appName) throws Throwable {
		
		String json = this.getTiersAsJson(appName);
		
		List<Tier> tiers = new Gson().fromJson(json, new TypeToken<List<Tier>>() {}.getType());
		
		return tiers;
	}

	public List<Node> getNodeList(String appName) throws Throwable {
		
		String json = this.getNodesAsJson(appName);
		
		List<Node> nodes = new Gson().fromJson(json, new TypeToken<List<Node>>() {}.getType());
		
		return nodes;
	}
	
	public String getApplicationsAsJson() throws Throwable {
		
		String json = this.sendGetRequestWithAuth(this.controllerUrl + 
				"/controller/rest/applications", "output=JSON&" + this.params, this.userAtAccount, this.userPwd);
		
		
		return json;
		
	}
	
	public String getTiersAsJson(String appName) throws Throwable {
		
		String json = this.sendGetRequestWithAuth(this.controllerUrl + "/controller/rest/applications/" + appName.trim().replaceAll(" ", "%20")
				+ "/tiers", "output=JSON&" + this.params, this.userAtAccount, this.userPwd);
		
		
		return json;
		
	}

	public String getNodesAsJson(String appName) throws Throwable {
		
		String json = this.sendGetRequestWithAuth(this.controllerUrl + "/controller/rest/applications/" + appName.trim().replaceAll(" ", "%20")
				+ "/nodes", "output=JSON&" + this.params, this.userAtAccount, this.userPwd);
		
		
		return json;
		
	}
	
	private String sendGetRequestWithAuth(String endpoint, String requestParameters, final String userAtAcct, final String password) {
		String result = null;
		
			try {
				// Send data
				String urlStr = endpoint;
				if (requestParameters != null && requestParameters.length() > 0) {
					urlStr += "?" + requestParameters;
				}
				
				// Set default cookie manager
				CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
				
				// Change Default Authentication
				Authenticator.setDefault(new Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(userAtAcct, password.toCharArray());
					}
				});
				
				URLConnection conn = new URL(urlStr).openConnection();
				
				// Get the response
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuffer sb = new StringBuffer();
				String line;
				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}
				rd.close();
				result = sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
//			System.out.println("*********************************************************************");
//			System.out.println(result);
//			System.out.println("*********************************************************************");	
			
		return result;
	}
	
	
	
	
}
