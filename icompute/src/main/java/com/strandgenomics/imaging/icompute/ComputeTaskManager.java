package com.strandgenomics.imaging.icompute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.strandgenomics.imaging.iclient.impl.ws.worker.Work;
import com.strandgenomics.imaging.iclient.impl.ws.worker.WorkStatus;
import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.app.JobState;
import com.strandgenomics.imaging.icore.app.WorkerState;

/**
 * the implementation of TaskManager on local node
 * 
 * @author Devendra/Anup Kulkarni
 */
public class ComputeTaskManager implements ITaskManager {
	private int poolSize;
	private ThreadPoolExecutor executor;
	private BlockingQueue<Runnable> workQueue;

	private Map<Long, ComputeTask> taskMap;

	private List<WorkStatus> report;

	public ComputeTaskManager()
	{
		this.poolSize = Constants.getComputeThreadPoolSize();
		workQueue = new ArrayBlockingQueue<Runnable>(poolSize * 100);
		executor = new ThreadPoolExecutor(poolSize, poolSize, 0, TimeUnit.MILLISECONDS, workQueue);
		taskMap = new HashMap<Long, ComputeTask>();
	}

	@Override
	public WorkStatus getStatus(Work work)
	{
		synchronized (taskMap)
		{
			ComputeTask computeTask = taskMap.get(work.getTaskID());
			WorkStatus workStatus = new WorkStatus(computeTask.getWork().getTaskID(), computeTask.getJobState().ordinal());

			return workStatus;
		}
	}

	@Override
	public void execute(ComputeApplication computeApplication, Work work)
	{
		ComputeTask computeTask = new ComputeTask(computeApplication, work);
		taskMap.put(work.getTaskID(), computeTask);
		executor.execute(computeTask);
	}

	@Override
	public void terminateTask(Work work)
	{
		ComputeTask computeTask = taskMap.get(work.getTaskID());
		computeTask.terminate();
	}

	@Override
	public WorkerState getWorkerState()
	{
		WorkStatus[] statusReport = report.toArray(new WorkStatus[0]);

		if (statusReport.length == 0)
		{
			return WorkerState.FREE;
		}
		else if (executor.getActiveCount() < poolSize)
		{
			return WorkerState.ENGAGED;
		}
		else
		{
			return WorkerState.BUSY;
		}
	}

	@Override
	public void terminate()
	{
		executor.shutdown();
	}

	private List<WorkStatus> getActiveJobStatus()
	{
		List<WorkStatus> report = new ArrayList<WorkStatus>();

		for (Long taskId : taskMap.keySet())
		{
			ComputeTask computeTask = taskMap.get(taskId);
			WorkStatus workStatus = new WorkStatus(computeTask.getWork().getTaskID(), computeTask.getJobState().ordinal());

			report.add(workStatus);
		}

		return report;
	}

	@Override
	public WorkStatus[] getActiveJobsWorkStatus()
	{
		synchronized (taskMap)
		{
			report = getActiveJobStatus();

			Iterator<Map.Entry<Long, ComputeTask>> iterator = taskMap.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry<Long, ComputeTask> entry = iterator.next();
				JobState jobState = entry.getValue().getJobState();
				if (jobState != JobState.EXECUTING)
				{
					iterator.remove();
				}
			}
		}

		return report.toArray(new WorkStatus[0]);
	}
}
