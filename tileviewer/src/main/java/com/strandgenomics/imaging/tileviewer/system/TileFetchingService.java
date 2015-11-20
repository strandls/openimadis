package com.strandgenomics.imaging.tileviewer.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import com.strandgenomics.imaging.iclient.ImageSpaceSystem;

/**
 * Executor service for executing TileFetchingTask
 *  
 * @author Anup Kulkarni
 */
public class TileFetchingService {

	private static Object padLock = new Object();
	
	/**
	 * timer used to check if submitted tile fetching tasks are completed 
	 * checks at regular intervals
	 */
	private Timer timer = null;
	
	/**
	 * 
	 */
	private static TileFetchingService service = null;
	/**
	 * executor service which will execute TilePrefetchingTask 
	 */
	private ExecutorService tileFetchers = null;
	
	/**
	 * Set of tasks currently in execution
	 * uniquely identified by hashcode
	 * key-value pair here is hashcode and task state
	 * task state is true if its in execution and false if task is cancelled
	 * if tasks gets completed its removed from this map
	 */
	private Map<Integer, Boolean> tasksInExecution =null;
	
	/**
	 * the list of futures of tasks in execution
	 */
	private List< Future<Integer> > futureTasks=null;
	
	private TileFetchingService(int nThreads)
	{
		tileFetchers = Executors.newFixedThreadPool(nThreads);
		futureTasks = new ArrayList< Future<Integer> >();
		tasksInExecution = new HashMap<Integer, Boolean>();
		
		timer = new Timer();
		timer.schedule(new CheckTileFetchingTaskCompletion(),10);
	}
	
	/**
	 * returns singleton instance of TileFetchingService
	 * @param nThreads no of tasks that can be executed parallely 
	 * @return singleton instance of TileFetchingService
	 */
	static TileFetchingService getInstance(int nThreads)
	{
		if(service==null)
		{
			service = new TileFetchingService(nThreads);
		}
		return service;
	}
	
	/**
	 * Submits request for fetching tile from the server. The tile is described by TileParameters, relevant user session is captured in ImageSpaceSystem
	 * @param ispace client side service end point capturing relevant user session
	 * @param params parameters uniquely describing the tile
	 * @throws TileNotReadyException 
	 * @throws TileRequestCancelledException 
	 */
	public void submitRequest(ImageSpaceSystem ispace, TileParameters params) throws TileNotReadyException, TileRequestCancelledException
	{
		// TODO:
		// check if tile is already fetched, if yes return immediately 
		// check if same request is already in queue, if yes return immediately
		// create and submit TileFetchingTask for specified parameters
		
		synchronized(padLock){
			if(doesRequestExist(params)){
				throw new TileNotReadyException("Tile not ready");
			}
			else{
				System.out.println("submited"+params.getX()+" "+params.getY()+" "+params.getZ());
				TileFetchingTask task = new TileFetchingTask(ispace, params);
				Future<Integer> future = tileFetchers.submit(task);
				futureTasks.add(future);
				tasksInExecution.put(params.hashCode(),true);
			}
		}
	}
	
	/**
	 * returns true if request for same TileParameters is already in queue
	 * @param params parameters uniquely describing tile
	 * @return true if request for same TileParameters is already in queue, false otherwise
	 * @throws TileRequestCancelledException if the tasks has been cancelled
	 */
	public boolean doesRequestExist(TileParameters params) throws TileRequestCancelledException
	{
		// TODO:
		// return true/false depending on if request for the same tile is already in queue
		boolean state=false;
		
		synchronized(padLock){
			if(tasksInExecution.containsKey(params.hashCode())){
				
				if(!tasksInExecution.get(params.hashCode())){
					tasksInExecution.remove(params.hashCode());
					throw new TileRequestCancelledException("Tile request cancelled");
				}
				
				state=true;
			}
		}
		return state;
	}
	
	/**
	 * return true if tasks is Cancelled
	 * @param params
	 * @return
	 */
	public boolean isTaskCancelled(TileParameters params){
		
		boolean state= false;
		
		synchronized(padLock){
			if(tasksInExecution.containsKey(params.hashCode())){
				state=!tasksInExecution.get(params.hashCode());
			}			
		}
		
		return state;
	}
	
	/**
	 * Object of this class is scheduled using Timer
	 * It checks if a submitted task got completed
	 * @author navneet
	 *
	 */
	class CheckTileFetchingTaskCompletion extends TimerTask{
		
		public void run(){
			synchronized(padLock){
				
				Iterator<Future<Integer> > iterator = futureTasks.iterator();
				
				while(iterator.hasNext()){
					
					Future<Integer> future = iterator.next();
					
					  if(future.isDone()){
						try {
							tasksInExecution.remove(future.get());
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						iterator.remove();
					}
				}
			}
		}
	}
	
	/**
	 * cancels all the tasks which are in queue of executor service
	 * tasks already in progress are allowed to complete
	 */
	public void cancelSubmitedTasks(){
		synchronized(padLock){
			Iterator<Future<Integer> > iterator = futureTasks.iterator();
			while(iterator.hasNext()){
				Future<Integer> future = iterator.next();
				
				future.cancel(false);
				
				iterator.remove();
			}
			
			for (Map.Entry<Integer, Boolean> entry : tasksInExecution.entrySet()) {
				entry.setValue(false);
			}
		}
	}
}
