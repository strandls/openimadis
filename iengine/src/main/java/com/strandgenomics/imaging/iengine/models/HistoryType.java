/**
 * openImaDis - Open Image Discovery: Image Life Cycle Management Software
 * Copyright (C) 2011-2016  Strand Life Sciences
 *   
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.strandgenomics.imaging.iengine.models;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * different type of history items
 * 
 * @author Anup Kulkarni 
 */
public enum HistoryType {
	RECORD_CREATED {
		@Override
		public String toString()
		{
			return "Record Created";
		}
	},
	RECORD_DELETED {
		@Override
		public String toString()
		{
			return "Record Deleted";
		}
	},
	ADDED_CHILD_RECORD {
		@Override
		public String toString()
		{
			return "Child Record Added";
		}
	},
	ADDED_PARENT_RECORD {
		@Override
		public String toString()
		{
			return "Parent Record Added";
		}
	},
	ATTACHMENT_ADDED {
		@Override
		public String toString()
		{
			return "Attachment Added";
		}
	},
	ATTACHMENT_DELETED {
		@Override
		public String toString()
		{
			return "Attchment Deleted";
		}
	},
	USER_ANNOTATION_ADDED {
		@Override
		public String toString()
		{
			return "User Annotation Added";
		}
	},
	USER_ANNOTATION_DELETED {
		@Override
		public String toString()
		{
			return "User Annotation Deleted";
		}
	},
	USER_ANNOTATION_MODIFIED {
		@Override
		public String toString()
		{
			return "User Annotation Modified";
		}
	},
	USER_COMMENT_ADDED {
		@Override
		public String toString()
		{
			return "Comment Added";
		}
	},
	USER_COMMENT_DELETED {
		@Override
		public String toString()
		{
			return "Comment Deleted";
		}
	},
	VISUAL_OVERLAY_ADDED {
		@Override
		public String toString()
		{
			return "Visual Overlay Added";
		}
	},
	VISUAL_OVERLAY_DELETED {
		@Override
		public String toString()
		{
			return "Visual Overlay Deleted";
		}
	},
	VISUAL_ANNOTATION_ADDED {
		@Override
		public String toString()
		{
			return "Visual Annotation Added";
		}
	},
	VISUAL_ANNOTATION_DELETED {
		@Override
		public String toString()
		{
			return "Visual Annotation Deleted";
		}
	},
	TASK_EXECUTED {
		@Override
		public String toString()
		{
			return "Task Executed";
		}
	}, 
	TASK_TERMINATED {
		@Override
		public String toString()
		{
			return "Task Terminated";
		}
	}, 
	TASK_FAILED {
		@Override
		public String toString()
		{
			return "Task Failed";
		}
	}, 
	TASK_SUCCESSFUL {
		@Override
		public String toString()
		{
			return "Task Successful";
		}
	}, 
	SHORTCUT_ADDED {
		@Override
		public String toString()
		{
			return "Shortcut Added";
		}
	},
	CUSTOM_HISTORY {
		@Override
		public String toString()
		{
			return "Custom History";
		}
	}, HAS_SHORTCUT {
		@Override
		public String toString()
		{
			return "Has Shortcut";
		}
	}, RECORD_TRANSFERED {
		@Override
		public String toString()
		{
			return "Record Transfered";
		}
	}, PROFILE_APPLIED {
		@Override
		public String toString()
		{
			return "Acquisition Profile Applied";
		}
	};
	
	private static final String HISTORY_MESSAGE_RESOURCE_NAME = "ImagingHistoryMessages";
	private static ResourceBundle History_MESSAGES = null;
    private static boolean isInitialized = false;
    private static Object padLock = new Object();
    protected static Logger logger = Logger.getLogger("com.strandgenomics.imaging.iengine.system");
    
    static ResourceBundle getHistoryMessageBundle()
	{
		if (!isInitialized)
		{
			synchronized (padLock)
			{
				try
				{
					History_MESSAGES = ResourceBundle.getBundle(HISTORY_MESSAGE_RESOURCE_NAME, Locale.getDefault());
				}
				catch (Exception ex)
				{
				}
				isInitialized = true;
			}
		}
		return History_MESSAGES;
	}
    
	/**
	 * returns well formed description message associated with the HistoryType
	 * method will require information for forming the description message. 
	 * 
	 * @param guid specified record
	 * @param user login of user who modified the record
	 * @param args information required for forming the descrition
	 * @return returns well formed description message
	 */
	public String getDescription(long guid, String user, String... args)
	{
		try 
		{
			ResourceBundle bundle = getHistoryMessageBundle();
			String unformattedString = bundle.getString(this.name());
			
			List<Object> argList = new ArrayList<Object>();
			argList.add(String.valueOf(guid));
			argList.add(String.valueOf(user));
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
			logger.logp(Level.WARNING, "HistoryType", "getDescription", "bundle not found ", e);
			return null;
		}
		catch(MissingResourceException e)
		{
			logger.logp(Level.WARNING, "HistoryType", "getDescription", "resource not found ", e);
			return null;
		}
	}
}
