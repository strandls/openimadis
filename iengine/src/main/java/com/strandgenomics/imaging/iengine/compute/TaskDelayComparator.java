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
