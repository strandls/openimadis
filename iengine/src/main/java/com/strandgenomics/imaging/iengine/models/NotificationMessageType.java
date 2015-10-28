package com.strandgenomics.imaging.iengine.models;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public enum NotificationMessageType {
	AUTHCODE_GENERATED {
		@Override
		public String toString()
		{
			return "AuthCode Generated";
		}
	},
	AUTHCODE_DELETED {
		@Override
		public String toString()
		{
			return "AuthCode Deleted";
		}
	},
	AUTHCODE_MODIFIED {
		@Override
		public String toString()
		{
			return "AuthCode Modified";
		}
	},
	AUTHCODE_DISABLED {
		@Override
		public String toString()
		{
			return "AuthCode Disabled";
		}
	},
	TASK_COMPLETED {
		@Override
		public String toString()
		{
			return "Task Completed";
		}
	},
	TILING_TASK_COMPLETED {
		@Override
		public String toString()
		{
			return "Tiling Task Completed";
		}
	},
	RECORD_CREATED{
		@Override
		public String toString()
		{
			return "Upload Complete";
		}
	},
	PROJECT_QUOTA_REACHING_LIMIT{
		@Override
		public String toString()
		{
			return "Project Quota Reaching Limit";
		}
	},
	TEAM_QUOTA{
		@Override
		public String toString()
		{
			return "Team quota used";
		}
	};
	
	

	private static final String NOTIFICATION_MESSAGE_RESOURCE_NAME = "ImagingNotificationMessages";
	private static ResourceBundle NOTIFICATION_MESSAGES = null;
    private static boolean isInitialized = false;
    private static Object padLock = new Object();
    protected static Logger logger = Logger.getLogger("com.strandgenomics.imaging.iengine.system");
    
    static ResourceBundle getNotificationMessageBundle()
	{
		logger.logp(Level.WARNING, "NotificationMessageType", "getNotificationMessageBundle", "entered ");

		if (!isInitialized)
		{
			synchronized (padLock)
			{
				try
				{
					NOTIFICATION_MESSAGES = ResourceBundle.getBundle(NOTIFICATION_MESSAGE_RESOURCE_NAME, Locale.getDefault());
				}
				catch (Exception ex)
				{
					logger.logp(Level.WARNING, "NotificationMessageType", "getNotificationMessageBundle", "bundle not found ", ex);
				}
				isInitialized = true;
			}
		}
		return NOTIFICATION_MESSAGES;
	}
    
	
    /**
     * returns well formed description message associated with the NotificationMessageType
	 * method will require information for forming the description message. 
	 * 
     * @param receivers a set of email ids for all the intended receivers 
     * @param sender the email id of the email sender
     * @param args information required for forming the description
     * @return returns well formed description message 
     */
	public String getDescription( String sender, List<String> receivers, String... args)
	{
		try 
		{
			ResourceBundle bundle = getNotificationMessageBundle();
			String unformattedString = bundle.getString(this.name());
			
			List<Object> argList = new ArrayList<Object>();
			argList.add(String.valueOf(sender));
			
			argList.add(String.valueOf(""));// TODO: remove this, unused parameter appropriate changes has to be made in ImagingNotificationMessages.properties
			
			String rec = new String();
			for(String emailid : receivers)
				rec += emailid + " ";
				
			argList.add(rec);
			if(args!=null)
			{
				for(String arg:args)
				{
					argList.add(arg);
				}
			}
			return MessageFormat.format(unformattedString, argList.toArray(new Object[0]));
		}
		catch(NullPointerException e)
		{
			logger.logp(Level.WARNING, "NotificationMesageType", "getDescription", "bundle not found ", e);
			return null;
		}
		catch(MissingResourceException e)
		{
			logger.logp(Level.WARNING, "NotificationMesageType", "getDescription", "resource not found ", e);
			return null;
		}
	}
	
}