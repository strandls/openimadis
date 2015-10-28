package com.strandgenomics.imaging.iengine.worker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be used by fields of ServiceStatus class
 * The name with which fields will be annotated
 * @author navneet
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceParameter {
	String name();
}
