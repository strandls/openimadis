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

package com.strandgenomics.imaging.icore.system;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is to be used by the server as well as the client components to
 * format appropriate error messages as well as to extract appropriate error
 * codes from such formated messages
 */
public class ErrorCode implements Serializable {

	private static final long serialVersionUID = -6205166811712938949L;
	
	public static final String ARGUMENT_DELIMITER          = "#@$";
    public static final String ERROR_MESSAGE_RESOURCE_NAME = "ImagingErrorMessages";
    protected static Logger logger = Logger.getLogger("com.strandgenomics.imaging.icore.system");

    private static ResourceBundle ERROR_MESSAGES = null;
    private static boolean isInitialized = false;
    private static Object padLock = new Object();
    
    static ResourceBundle getErrorMessageBundle()
    {
        if(!isInitialized)
        { //load lazily when required
            synchronized(padLock){
                try {
                    // Gets a resource bundle using the specified 
                    // base name, (name of the java properties file (without .properties extn)
                    // locale, for example (en, US)
                    // and class loader. the properties files must be in the class path
                    // or embedded inside a JAR file which is in the class path
                    // to be loaded by the application class loader
                    // typical convension is 
                    // baseName + "_" + language1 + "_" + country1 + "_" + variant1
                    // for our case, it would be
                    // ImagingErrorMessage_en.properties

                    ERROR_MESSAGES = ResourceBundle.getBundle( ERROR_MESSAGE_RESOURCE_NAME, 
                                                               Locale.getDefault(), 
                                                               ErrorCode.class.getClassLoader());
                }
                catch(Exception ex)
                {
                    logger.logp(Level.WARNING, "ErrorCode", "<cinit>", "unable to load resource bundle "+ERROR_MESSAGE_RESOURCE_NAME, ex);
                }
                isInitialized = true;
            }
        }
        return ERROR_MESSAGES;
    }

    /**
     * every error has an unique identifier
     */
    protected int code;
    /**
     * an error can be qualifier by parameters (java primitive types)
     */
    protected Object[] messageArgs;
    
    public ErrorCode(int code, Object ... args)
    {
        this.code        = code;
        this.messageArgs = args;
    }
    
    public int getCode()
    {
        return code;
    }
    
    public Object[] getMessageArguments()
    {
        return messageArgs;
    }
    
    /**
     * Returns the error message represented by the given ErrorCode object.
     * @param errorCode
     * @return String the error message represented by the ErrorCode object
     */
    public String getErrorMessage(){
        String formattedMsg = null;
        try {
            String errorMsgPattern = getErrorMessageBundle().getString(""+code);
            formattedMsg = MessageFormat.format(errorMsgPattern, messageArgs);
        }
        catch(MissingResourceException mex){
        }
        
        return formattedMsg;
    }
    
    /**
     * @see extractErrorCode
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("ErrorCode[");
        buffer.append(code);
        buffer.append("]:");
        
        if(messageArgs != null){
            for(int i = 0; i < messageArgs.length; i++){
                buffer.append(ARGUMENT_DELIMITER);
                buffer.append(messageArgs[i]);
            }
        }
        
        return buffer.toString();
    }
    
    /**
     * The application server will send appropriate exception messages on soap faults
     * and/or java.rmi exceptions. The server side application code will format
     * all exceptions and errors as
     * <code>ErrorCode[xyx]: error-message</code>
     * where xyz is the error code (a whole number)
     */
    public static ErrorCode extractErrorCode(String serverErrorMsg){

        //"ErrorCode[xxx]: $ arg1 $ arg2 $ arg3", where xxx is a whole number.
        //This is how an error msg looks. Now we try to extract ErrorCode components out of this and
        //construct an ErrorCode object.
    	ErrorCode errData = null;
        try {
            if(serverErrorMsg != null){

                int startIndex = serverErrorMsg.indexOf("ErrorCode[");
                if(startIndex != -1){

                    startIndex = startIndex + "ErrorCode[".length();
                    int endIndex = serverErrorMsg.indexOf("]:", startIndex);

                    if(endIndex != -1){
                        int errorCode = Integer.parseInt(serverErrorMsg.substring(startIndex, endIndex));
                        errData = new ErrorCode(errorCode);
                        
                        //the rest is the error message
                        String errorMsg  = serverErrorMsg.substring(endIndex + "]:".length());
                        Object[] errorMsgArgs = getObjectTokens(errorMsg, ARGUMENT_DELIMITER);
                        
                        errData = new ErrorCode(errorCode, errorMsgArgs);
                    }
                }
            }
        }
        catch(Exception ex){
            errData = null;
        }

        return errData;
    }
    
    public static Object[] getObjectTokens(String messageText, String strDelimiter)
    {
        String[] tokens = getTokens(messageText, strDelimiter);
        if(tokens == null || tokens.length == 0){
            return null;
        }
        
        Object[] arguments = new Object[tokens.length];
        for(int i = 0;i < tokens.length; i++){
            
            if(tokens[i].equals("true") || tokens[i].equals("false")) {
                arguments[i] = Boolean.valueOf(tokens[i]);
            }
            else {
                try {
                    Long value = Long.valueOf(tokens[i]);
                    arguments[i] = value;
                }
                catch(Exception ex){
                    try {
                        Double value = Double.valueOf(tokens[i]);
                        arguments[i] = value;
                    }
                    catch(Exception exx){
                        arguments[i] = tokens[i];
                    }
                }
            }
        }
        
        return arguments;
    }
    
    public static String[] getTokens(String text, String strDelimiter){
        
        if(text == null || strDelimiter == null) return null;
                
        List<String> tokens = new ArrayList<String>();
        
        int startIndex = text.indexOf(strDelimiter);
        int endIndex   = 0;

        if(startIndex == -1){
            tokens.add(text);
        }
        else if(startIndex > 0){
            String token = text.substring(0, startIndex);
            tokens.add(token);
        }
        
        int delimiterLength = strDelimiter.length();
        int textLength = text.length() - 1;
        
        while(startIndex != -1){
            
            startIndex = startIndex + delimiterLength;
            endIndex = text.indexOf(strDelimiter, startIndex);
            
            if(endIndex == -1){
                if(startIndex < textLength){
                    String token = text.substring(startIndex);
                    tokens.add(token);
                }
            }
            else {
                if(endIndex != startIndex) {
                    String token = text.substring(startIndex, endIndex);
                    tokens.add(token);
                 }
            }
            
            startIndex = endIndex;
        }
        
        return tokens.isEmpty() ? null : tokens.toArray(new String[0]);
    }
    
	public static class ImageSpace 
	{
		/**
		 * internal server error
		 */
		public static final int INTERNAL_SERVER_ERROR = 1000;
		public static final int UNAUTHORIZED_ACCESS   = 1001;
		public static final int IMAGE_READER_UNAVAILABLE = 1002;
		public static final int NOT_VALID_FILEPATH = 1003;
		public static final int NOT_ENOUGH_ACQ_LICENSES = 1004;
		public static final int ACQ_PROFILE_NAME_EXISTS = 1005;
		public static final int LICENSE_EXCEPTION = 1006;
		
        public static final int INVALID_SESSION     = 999;
        /* no error msg argument required */
        public static final int SESSION_EXPIRED     = 998;
        /* no error msg argument required */
        public static final int SESSION_ENDED       = 997;
        
        /** illegal login */
        public static final int ILLEGAL_LOGIN       = 900;
        /* no error msg argument required */
        public static final int INVALID_CREDENTIALS = 901;
        /* no error msg argument required */
        public static final int LOGIN_DISABLED      = 902;
        /* no error msg argument required */
        public static final int LOGIN_DELETED       = 903;
        /* no error msg argument required */
        public static final int LOGIN_UNAUTHORIZED  = 904;
        /* external user not in valid ldap group */
        public static final int NOT_IN_VALID_LDAP_GROUP  = 905;
        /* max no. of active users is exceeded for the system*/
        public static final int MAX_USER_LIMIT_EXCEEDED = 906;
        /* max no. of microscopes exceeded */
        public static final int MAX_MICROSCOPE_LIMIT_EXCEEDED = 907;
        
        /**
         * management errors
         */
        public static final int USER_ALREADY_EXIT  = 500;
        /**
         * management errors
         */
        public static final int USER_DO_NOT_EXIT  = 501;
        /**
         * management errors
         */
        public static final int CANT_CHANGE_LDAP_ATTR = 502;
        
        public static final int POOR_PASSWORD_STRENGTH = 503;
        /**
         * project already exist
         */
        public static final int PROJECT_ALREADY_EXIT = 400;
        /**
         * project do not exist
         */
        public static final int PROJECT_DO_NOT_EXIT = 401;
        
        public static final int PROJECT_IS_ARCHIVED = 402;
        
        public static final int PROJECT_NOT_ACTIVE = 403;
        
		public static final short QUOTA_EXCEEDED   = 800;
		
		public static final short DISK_SPACE_ERROR = 810;
		
		/**
		 * This code is thrown when server is busy with too many tickets
		 */
		public static final short TICKET_NOT_AVAILABLE = 666;
		/**
		 * This code is used when there already exist a ticket which is uploading same stuff
		 */
		public static final short TICKET_ALREADY_EXIST = 660;
		/**
		 * This code is used when the archive exist within the system
		 */
		public static final short ARCHIVE_ALREADY_EXIST = 650;
		
        /**
         * management errors
         */
        public static final int ANNOTATION_DO_NOT_EXIT  = 300;
        
        /**
         * compute errors
         */
        public static final int PUBLISHER_ALREADY_EXIT  = 700;
        
        public static final int TASK_IS_RUNNING = 701;
        
        public static final int TASK_IS_NOT_RUNNING = 702;
        
        public static final int PUBLISHER_ALREADY_REGISTERED = 750;
        
		public static final int PUBLISHER_DOESNT_EXIST = 751;
		
		/**
		 * record creation errors
		 */
		/**
		 * image height/width/depth do not match
		 */
		public static final int IMAGE_DIMENSIONS_DO_NOT_MATCH = 1101;
		/**
		 * record z,c,t do not match
		 */
		public static final int RECORD_DIMENSIONS_DO_NOT_MATCH = 1102;
		/**
		 * record already commited
		 */
		public static final int RECORD_COMMITTED_OR_DO_NOT_EXIST = 1103;
		/**
		 * record builder does not exist
		 */
		public static final int RECORD_CREATION_REQUEST_DOES_NOT_EXIST = 1104;
		/**
		 * data is missing
		 */
		public static final int ALL_PIXEL_DATA_NOT_RECEIVED = 1105;
		
		/**
		 * record export errors
		 */
		/**
		 * record already exported
		 */
		public static final int RECORD_ALREADY_EXPORTED = 1201;
		/**
		 * record already exported
		 */
		public static final int RECORD_NOT_READY_TO_DOWNLOAD = 1202;
		/**
		 * no space left on cache
		 */
		public static final int NO_SPACE_LEFT = 1203;
		/**
		 * export request does not exists
		 */
		public static final int INVALID_EXPORT_REQUEST = 1204;
		/**
		 * export request expired
		 */
		public static final int EXPORT_VALIDITY_EXPIRED = 1205;
		/**
		 * unit-project association errors
		 */
		/**
		 * project space is violated
		 */
		public static final int PROJECT_SPACE_VIOLATION = 1301;
		/**
		 * disk space not available
		 */
		public static final int INSUFFICIENT_DISK_SPACE = 1302;
		/**
		 * HUGE IMAGE
		 */
		public static final int IMAGE_SIZE_EXCEPTION = 1303;
	}
	
	/**
	 * Error codes to represent authorization related errors.
	 * 
	 * @author santhosh
	 *
	 */
	public static class Authorization {
	    
	    /**
	     * auth code is not valid
	     */
	    public static final short INVALID_AUTH_CODE = 200;
	    
	    /**
	     * access token is not valid
	     */
	    public static final short INVALID_ACCESS_TOKEN = 201;
	
	    /**
	     * auth code has expired
	     */
	    public static final short AUTH_CODE_EXPIRED = 202;
	    
	    /**
	     * access token has expired
	     */
	    public static final short ACCESS_TOKEN_EXPIRED = 203;
	    
	    /**
	     * not a valid client
	     */
	    public static final short INVALID_CLIENT = 204;
	    
	    /**
	     * invalid pair of auth code and client id
	     */
	    public static final short INVALID_KEYPAIR = 205;
	    
	    /**
	     * Client exists with the provided name and version
	     */
	    public static final short CLIENT_EXISTS = 206;
	}
	
    public static void main(String[] args) throws Exception {
        
        Object[] tokens = getObjectTokens(args[0], args[1]);
        if(tokens != null){
            for(int i = 0; i < tokens.length; i++){
                System.out.println("token["+i+"]="+tokens[i] +", class="+tokens[i].getClass());    
            }
        }
    }
}
