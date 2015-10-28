package com.strandgenomics.imaging.iengine.compute;

import java.io.Serializable;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;
/**
 * task comparator based on the delay
 *
 * @author Anup Kulkarni
 */
public class TaskDelayComparator implements Comparator<Task>, Serializable{
	@Override
	public int compare(Task t1, Task t2)
	{
		//a negative integer, zero, or a positive integer as the 
		//first argument is less than, equal to, or greater than the second. 
		int value = (int) (t1.getDelay(TimeUnit.NANOSECONDS) - t2.getDelay(TimeUnit.NANOSECONDS));
		//same delay, check the priority
		if(value == 0) 
		{
			value = t1.getPriority().ordinal() - t2.getPriority().ordinal();
		}
		return value;
	}
}
