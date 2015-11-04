/**
 * DOIterator.java
 * 
 * Project imaging
 * Module com.strandgenomics.imaging.framework.dataobjects
 *
 * Copyright 2009-2010 by Strand Life Sciences
 * 237, Sir C.V.Raman Avenue
 * RajMahal Vilas
 * Bangalore 560080
 * India
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Strand Life Sciences., ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Strand Life Sciences.


 */
package com.strandgenomics.imaging.iviewer.dataobjects;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.apache.log4j.Logger;

/**
 * 
 * Make use of help
 * http://langexplr.blogspot.com/2007/12/combining-iterators-in-java.html
 * 
 * @author nishu
 * 
 */
public class DOIterator implements Iterator<Triplet> {

	private Object dataObject;
	private Iterator<Method> fieldsIterator;
	private Iterator<Object> collectionFieldIterator;
	private Method currentField;
	private Object currentFieldValue;
	
	private Logger logger = Logger.getRootLogger();

	/**
	 * @param dataObject
	 */
	public DOIterator(Object dataObject) {
		this(dataObject, null);
	}

	public DOIterator(Object dataObject, String[] fieldsToSkip) {
		super();
		this.dataObject = dataObject;
		if (dataObject != null) {
			Class aClass = dataObject.getClass();
			List<Method> methods = Arrays.asList(aClass.getMethods());
			List<Method> getterMethods = getAllGetMethods(methods);
			List<Method> fieldGetters = removeFieldsToSkip(getterMethods,
					fieldsToSkip);
			fieldsIterator = fieldGetters.iterator();
		}
	}

	private List<Method> getAllGetMethods(List<Method> methods) {
		List<Method> getterMethods = new ArrayList<Method>();
		for (Method m : methods) {
			if (isGetter(m)) {
				getterMethods.add(m);
			}
		}
		return getterMethods;
	}

	public static boolean isGetter(Method method) {
		if (!method.getName().startsWith("get"))
			return false;
		if (method.getParameterTypes().length != 0)
			return false;
		if (void.class.equals(method.getReturnType()))
			return false;
		return true;
	}

	private List<Method> removeFieldsToSkip(List<Method> methods,
			String[] fieldsToSkip) {
		removeFieldToSkip(methods, "class");
		if (fieldsToSkip != null) {
			for (String skippedfieldName : fieldsToSkip) {
				methods=removeFieldToSkip(methods, skippedfieldName);
			}
		}
		return methods;
	}

	private List<Method> removeFieldToSkip(List<Method> methods,
			String fieldToSkip) {
		if (fieldToSkip != null) {
			for (Method method : methods) {
				if (method.getName().toLowerCase()
						.equals("get"+fieldToSkip.toLowerCase())) {
					methods.remove(method);
					break;
				}
			}

		}
		return methods;
	}

	@Override
	public boolean hasNext() {
		if (dataObject != null) {
			try {
				if (currentField == null) {
					if (fieldsIterator.hasNext()) {
						// has some more fields
						currentField = fieldsIterator.next();

						currentFieldValue = currentField.invoke(dataObject);
						Class<?> interfaces[] =currentField.getReturnType().getInterfaces();
						
						if ( Arrays.asList(interfaces).contains(Collection.class)) {
							// if field is iterator then initialize collection
							// iterator
							Collection c = (Collection) currentFieldValue;
							if(c != null){
								collectionFieldIterator = c.iterator();
							}
							else{
								//if collection is null move to next field
								collectionFieldIterator = null;
								currentField = null;
								return this.hasNext();
							}
						} else {
							return true;
						}

					} else {
						// finished with all fields
						return false;
					}

				}

				// current field is collection
				if (collectionFieldIterator.hasNext()) {
					// hasnext is true if collectionFieldIterator is has more
					// elements
					return true;
				} else {
					// if collectionfielditerator finished with all elements
					// check
					// for the next field in the fieldsiterator
					collectionFieldIterator = null;
					currentField = null;
					return this.hasNext();
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				logger.debug(e);
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				logger.debug(e);
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				logger.debug(e.getTargetException()+" "+currentField);
			} catch (Exception e) {
				logger.debug(e);
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public Triplet next() {
		if (dataObject != null) {
			if (collectionFieldIterator != null) {
				Object obj = collectionFieldIterator.next();
				return createTriplet(obj, getFieldName(currentField));
			} else {
				Triplet t = createTriplet(currentFieldValue, getFieldName(currentField));
				currentField = null;
				return t;
			}
		}
		return null;
	}

	private String getFieldName(Method currentField) {
		String fieldName=currentField.getName().substring(3);
		char firstChar=fieldName.charAt(0);			
		return Character.toLowerCase(firstChar)+fieldName.substring(1);
	}
	
	@Override
	public void remove() {
		try {
			throw new OperationNotSupportedException("Remove is not supported!");
		} catch (OperationNotSupportedException e) {
			e.printStackTrace();
		}
	}

	public static Triplet createTriplet(Object obj, String fieldName) {
//		if (obj instanceof UserAnnotation) {
//			UserAnnotation userAnnotation = (UserAnnotation) obj;
//			return new Triplet(userAnnotation.getKey(),
//					userAnnotation.getValue(), Triplet.TYPE_USER_ANNOTATION);
//		} else if (obj instanceof Pair) {
//			Pair pair = (Pair) obj;
//			return new Triplet(pair.getKey(), pair.getValue(),
//					Triplet.TYPE_UNINTERPRETED_FIELD);
//		} else {
			
			if (obj instanceof Integer) {
				return new Triplet(fieldName, obj,
						Triplet.TYPE_INT);
			} else if (obj instanceof Float) {
				return new Triplet(fieldName, obj,
						Triplet.TYPE_FLOAT);
			} else if (obj instanceof Long) {
				return new Triplet(fieldName, obj,
						Triplet.TYPE_LONG);
			} else if (obj instanceof String) {
				return new Triplet(fieldName, obj,
						Triplet.TYPE_STRING);
			} else if (obj instanceof URI) {
				return new Triplet(fieldName, ((URI)obj).getPath(),
						Triplet.TYPE_STRING);
			} else {
				return new Triplet(fieldName, obj,
						Triplet.TYPE_OBJECT);
			}
//		}
	}
}
