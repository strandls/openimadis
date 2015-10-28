/*
 * QueryFormat.java
 *
 * AVADIS Image Management System
 * Core Engine Database Module
 *
 * Copyright 2011-2012 by Strand Life Sciences
 * 5th Floor, Kirloskar Business Park, 
 * Bellary Road, Hebbal
 * Bangalore 560024
 * Karnataka, India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.
 */

package com.strandgenomics.imaging.icore.db;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.strandgenomics.imaging.icore.util.Util;

/**
 * A convenient class to serialize a SQLQuery instance into XML
 * @author arunabha
 *
 */
public class QueryFormat {

    protected Logger logger = Logger.getLogger("com.strandgenomics.imaging.iengine.db");
    /**
     * the sequence of query fragments that form the completed parametric SQL query
     */
    protected List<QueryFragment> queryFragments = null;

    /**
     * default constructor for XML Bean Serializer
     */
    public QueryFormat()
    {
        queryFragments  = new ArrayList<QueryFragment>();
    }

    /**
     * typically query string will be like
     * use the braces as parameter delimiter
     *
     * SELECT R.ResourceID, R.Name, U.Name Owner, R.IsDirectory,
     * R.DirectoryID, R.IsAlias, R.isHidden, R.CreationDate
     * FROM Resource R, User U
     * WHERE R.Owner=U.UserID
     * AND R.Name {resourceName}
     * AND R.DirectoryID {directoryID}
     */
    public QueryFormat(String pattern)
    {
        setPattern(pattern);
    }

    /**
     * Get method for XML Bean Serializer
     * recreates the pattern associated with this format
     */
    public String getPattern(){

        StringBuffer buffer = new StringBuffer(128);

        for(int counter = 0;counter < queryFragments.size(); counter++)
        {
            QueryFragment fragment = queryFragments.get(counter);
            buffer.append( fragment.toString() );
        }

        String pattern = buffer.toString();
        buffer = null;

        return pattern;
    }

    /**
     * Set method for XML Bean Serializer
     */
    public void setPattern(String pattern){

        queryFragments  = new ArrayList<QueryFragment>();

        char[] data = pattern.toCharArray();

        StringBuilder paramBuffer = new StringBuilder();
        StringBuilder constBuffer = new StringBuilder();
        StringBuilder tagBuffer   = new StringBuilder();

        boolean insideParam   = false; //is true if we are reading prepared statement parameters
        boolean insideDynamic = false; //is true if we are reading an optional fragment
        boolean insideTagged  = false; //is true if we are reading a tagged fragment
        
        //dynamic or optional fragments
        List<QueryFragment> dyFragments = new ArrayList<QueryFragment>();

        for(int i = 0; i < data.length; ++i)
        {
            if(data[i] == '%')
            {
                if(insideParam)
                {
                    throw new IllegalArgumentException("parameter is not supported inside variable fragments");
                }
                
                if(insideTagged) //pair found => closing the tagged fragment
                {
                    insideTagged = false;
                    String tagName = tagBuffer.toString();
                    tagBuffer.setLength(0);//reset the buffer
                    
                    if(tagName.length() > 0)
                    {
                        TaggedFragment fragment = new TaggedFragment(tagName);
                        //simply add as a normal variable fragment in the sequence
                        logger.logp(Level.FINEST, "QueryFormat", "setPattern", "adding tagged fragment "+fragment);
                        
                        if(insideDynamic)
                        	dyFragments.add(fragment);
                        else
                        	queryFragments.add(fragment);
                    }
                }
                else //a new tagged fragment
                {
                    insideTagged = true;
                    
                    //handle existing const buffer
                    String constStr = constBuffer.toString();
                    if(constStr.length() > 0) 
                    {
                        constBuffer.setLength(0);//reset the buffer
                        ConstantFragment fragment = new ConstantFragment(constStr);
                        
                        logger.logp(Level.FINEST, "QueryFormat", "setPattern", "adding constant fragment "+fragment);
                       
                        if(insideDynamic)
                        	dyFragments.add(fragment);
                        else
                        	queryFragments.add(fragment);
                    }
                }
                continue;
            }
            
            if(insideTagged)
            {
                tagBuffer.append(data[i]);
                continue;
            }
                        
            if(data[i] == '[')
            {
                if(insideDynamic)
                {
                    throw new IllegalArgumentException("nested [ is not supported");
                }
                insideDynamic = true;
                
                String constStr = constBuffer.toString();
                if(constStr.length() > 0) 
                {
                    constBuffer.setLength(0);//reset the buffer
                    ConstantFragment fragment = new ConstantFragment(constStr);
                    logger.logp(Level.FINEST, "QueryFormat", "setPattern", "adding constant fragment "+fragment);
                    //simply add as a global sequence of fragments
                    queryFragments.add(fragment); 
                }
                continue;
            }
            
            if(data[i] == ']')
            {
                if(!insideDynamic)
                {
                    throw new IllegalArgumentException("unbalanced ] is not supported");
                }
                insideDynamic = false;
                
                if(constBuffer.length() > 0) 
                {
                	ConstantFragment fragment = new ConstantFragment(constBuffer.toString());
                    dyFragments.add( fragment );
                    constBuffer.setLength(0);//reset the buffer
                    logger.logp(Level.FINEST, "QueryFormat", "setPattern", "adding dynamic post constant fragment "+fragment);
                }

                OptionalFragment fragment = new OptionalFragment(dyFragments);
                //reset this buffer
                dyFragments.clear();
                //simply add the fragment instance in the global sequence
                queryFragments.add(fragment);
                logger.logp(Level.FINEST, "QueryFormat", "setPattern", "adding dynamic variable fragment "+fragment);

                continue;
            }
            
            if(data[i] == '{')
            {
                if(insideParam){
                    throw new IllegalArgumentException("nested { is not supported");
                }
                insideParam = true;

                String constStr = constBuffer.toString();
                
                if(constStr.length() > 0) 
                {
                    constBuffer.setLength(0);//reset the buffer
                    ConstantFragment fragment = new ConstantFragment(constStr);
                    logger.logp(Level.FINEST, "QueryFormat", "setPattern", "adding constant fragment "+fragment);
                    
                    if(insideDynamic)
                    	dyFragments.add(fragment);
                    else
                    	queryFragments.add(fragment);
                }
                continue;
            }

            if(data[i] == '}')
            {
                if(!insideParam)
                {
                    throw new IllegalArgumentException("unbalanced } is not supported");
                }
                insideParam = false;
                
                String value = paramBuffer.toString();
                paramBuffer.setLength(0);//reset the buffer

                StringTokenizer tokenizer = new StringTokenizer(value, ",");

                //must field, the first parameter is the name of the variable fragment
                String name = tokenizer.nextToken().trim();
                //optional field,the 2nd parameter
                boolean setNullIsValid = false;
                //optional 3rd parameter, the operator, default is empty string
                String operator = "";
                try 
                {
                    setNullIsValid = Boolean.parseBoolean(tokenizer.nextToken().trim());
                    //3rd token is valid if 2nd token is there
                    if(tokenizer.hasMoreTokens())
                    {
                        operator = tokenizer.nextToken().trim();
                    }
                }
                catch(Exception ex){
                }

                VariableFragment fragment = new VariableFragment(name, setNullIsValid, operator);
                logger.logp(Level.FINEST, "QueryFormat", "setPattern", "adding variable fragment "+fragment);
                
                if(insideDynamic)
                	dyFragments.add(fragment);
                else
                	queryFragments.add(fragment);
                
                continue;//look for the next char
            }

            if(insideParam)
            {
                paramBuffer.append(data[i]);
            }
            else 
            {
                constBuffer.append(data[i]);
            }
            
        } //end of for loop

        if(insideDynamic)
        {
            throw new IllegalArgumentException("unbalanced [ is not supported");
        }
        
        //end of string
        if(insideParam)
        {
            throw new IllegalArgumentException("unbalanced { is not supported");
        }

        String constStr = constBuffer.toString();
        if(constStr.length() > 0)
        {
            constBuffer.setLength(0);//reset the buffer
            ConstantFragment fragment = new ConstantFragment(constStr);
            
            queryFragments.add(fragment); //simply add as a sequence
            logger.logp(Level.FINEST, "QueryFormat", "setPattern", "adding constant fragment "+fragment);
        }

        logger.logp(Level.INFO, "QueryFormat", "setPattern", "successfully parsed the query string");
    }

    /**
     * SQLQuery is basically a clone of QueryFormat as far as the encapsulated data
     * is concerned
     */
    public SQLQuery createQueryGenerator()
    {
        List<QueryFragment> fragmentSequence = new ArrayList<QueryFragment>();

        for(Iterator<QueryFragment> e = queryFragments.iterator(); e.hasNext(); ){

            QueryFragment fragment = e.next();

            if(fragment instanceof ConstantFragment)
            {
                //constant fragments are immutable
                fragmentSequence.add(fragment);
            }
            else 
            {
            	//otherwise clone and add
            	fragmentSequence.add( (QueryFragment) fragment.clone() );
            }
        }

        return new SQLQuery(fragmentSequence);
    }
    
    public static void main(String[] args) throws IOException {
		
    	Util.unmarshallFromXML(new File("D:\\svn\\rep\\imaging\\iengine\\trunk\\config\\imagingdao\\mysql\\ProjectDAO.xml"), "UTF-8");
	}
}