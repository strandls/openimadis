package com.strandgenomics.imaging.iengine.compute;

import java.io.Serializable;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

/**
 * comparator for task based on task priority
 * 
 * @author Anup Kulkarni
 */
public class TaskPriorityComparator implements Comparator<Task>, Serializable{

	@Override
	public int compare(Task t1, Task t2)
	{
		//a negative integer, zero, or a positive integer as the 
		//first argument is less than, equal to, or greater than the second. 
		int value = t1.getPriority().ordinal() - t2.getPriority().ordinal();
		//same priority, check the delay
		//the task that is waiting the longest is ahead
		if(value == 0) 
		{
			value = (int) (t1.getDelay(TimeUnit.NANOSECONDS) - t2.getDelay(TimeUnit.NANOSECONDS));
		}
		return value;
	}
}
