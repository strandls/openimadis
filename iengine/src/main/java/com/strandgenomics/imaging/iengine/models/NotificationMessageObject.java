package com.strandgenomics.imaging.iengine.models;

import java.util.List;
import java.util.Set;

public class NotificationMessageObject {
	/**
	 * email receivers' email ids
	 */
	private Set<String> receiverLogins ;
	
	/**
	 * email receivers' names
	 */
	
	private Set<String> receivers ;
	
	/**
	 *type of email message 
	 */
	private NotificationMessageType type ;
	 
	/**
	 * email sender
	 */
	private String sender ;
	
	/**
	 * email senderLogin
	 */
	private String senderLogin ;
	
	/**
	 * description of the modification
	 */
	private String description;
	/**
	 * list of arguments
	 */
	private List<String> arguments;
	
	

	 public NotificationMessageObject( String sender,String senderLogin , Set<String> receivers, Set<String> receiverLogins ,  NotificationMessageType type, String description, List<String>args){
		// TODO Auto-generated constructor stub
		this.sender = sender;
		this.setSenderLogin(senderLogin);
		this.receivers = receivers;
		this.setReceiverLogins(receiverLogins);
		this.description = description;;
		this.type = type;
		this.arguments = args;
	}
	
	
	
	

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @param sender the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * @return the type
	 */
	public NotificationMessageType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(NotificationMessageType type) {
		this.type = type;
	}

	/**
	 * @return the receivers
	 */
	public Set<String> getReceivers() {
		return receivers;
	}

	/**
	 * @param receivers the receivers to set
	 */
	public void setReceivers(Set<String> receivers) {
		this.receivers = receivers;
	}

	/**
	 * @return the arguments
	 */
	public List<String> getArguments() {
		return arguments;
	}

	/**
	 * @param arguments the arguments to set
	 */
	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}

	/**
	 * @param args4:57:51 PM
	
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}





	/**
	 * @return the receiverLogins
	 */
	public Set<String> getReceiverLogins() {
		return receiverLogins;
	}





	/**
	 * @param receiverLogins the receiverLogins to set
	 */
	public void setReceiverLogins(Set<String> receiverLogins) {
		this.receiverLogins = receiverLogins;
	}





	/**
	 * @return the senderLogin
	 */
	public String getSenderLogin() {
		return senderLogin;
	}





	/**
	 * @param senderLogin the senderLogin to set
	 */
	public void setSenderLogin(String senderLogin) {
		this.senderLogin = senderLogin;
	}
}
