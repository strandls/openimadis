package com.strandgenomics.imaging.iengine.movie;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.strandgenomics.imaging.icore.Channel;
import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.Constants.Property;
import com.strandgenomics.imaging.icore.Storable;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.models.Record;
import com.strandgenomics.imaging.iengine.system.SysManagerFactory;

/**
 * Ticket uniquely identifies movie. 
 * 
 * @author Anup Kulkarni
 */
public class MovieTicket implements Storable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4459604464855419984L;
	public static final long DEFAULT_EXPIRY_TIME = 24 * 60 * 60 * 1000;
	/**
	 * id associated with movie
	 */
	public final long ID;
	/**
	 * guid of record on which movie is played
	 */
	public final long guid;
	/**
	 * frame/slice of record
	 * depending upon onFrames flag this coordinate is decided to be frame or slice
	 * if onFrames is true: otherCoordinate is slice
	 * if onFrames is false: otherCoordinate is frame
	 */
	public final int otherCoordinate;
	/**
	 * movies played on frames or z slices
	 * default: movies are played on frames 
	 */
	public final boolean onFrames;
	/**
	 * site of record on which movie is played
	 */
	public final int site;
	/**
	 * selected channels
	 */
	private Set<Integer> channels = null;
	/**
	 * selected channel objects
	 */
	private Set<Channel> channelObjects = null;
	/**
	 * selected overlays
	 */
	private Set<String> overlays = null;
	/**
	 * true if channel color is used
	 */
	public final boolean useChannelColor;
	/**
	 * true if z stack is used
	 */
	public final boolean useZStack;
	/**
	 * last access time: used to clean up the movie after specified timeout
	 */
	private Long creationTime;
	/**
	 * state of the movie frames
	 */
	private boolean[] movieFrames = null;
	/**
	 * maximum number of movie frames
	 */
	public final int maxCoordinate;
	/**
	 * the user requesting the movie
	 */
	public final String actorLogin;
	/**
	 * type of the movie
	 */
	public final MovieType type;
	/**
	 * expiry time in milis
	 */
	protected long expiryTime; // by default movie will be active for 1 day
	/**
	 * frame rate; if null use elapsed time from record metadata
	 */
	Double fps = null; 
	
	/**
	 * Name of the movie provided by user
	 */
	private String movieName = null;
	
	public MovieTicket(long ID, String actorLogin, long guid, int site, boolean onFrames, int otherCoorinate, boolean useChannelColor, boolean useZStack, List<Integer>channels, List<String>overlays, MovieType type)
	{
		this.actorLogin = actorLogin;
		
		this.guid = guid;
		this.site = site;
		
		this.onFrames = onFrames;
		this.otherCoordinate = otherCoorinate;
		
		this.channels = new HashSet<Integer>(); 
		this.channels.addAll(channels);
		this.channelObjects = readChannelObjects();
		
		this.type = type;
		
		this.overlays = new HashSet<String>();
		if (overlays != null)
		    this.overlays.addAll(overlays);

		this.useChannelColor = useChannelColor;
		this.useZStack = useZStack;

		this.creationTime = System.currentTimeMillis();
		this.expiryTime = creationTime + expiryTime;
		
		this.maxCoordinate = getMaxCoordinate();
		movieFrames = new boolean[maxCoordinate];
		
		this.ID = ID;
	}
	
	/**
	 * sets the name of movie 
	 * @param name
	 */
	public void setMovieName(String name)
	{
		this.movieName = name;
	}	
	
	/**
	 * gets the name for this movie
	 * @return the name this movie
	 */
	public String getMovieName()
	{
		return this.movieName;
	}	
	
	/**
	 * gets the creation date for this movie
	 * @return the String creation date this movie
	 */
	public Long getCreationTime()
	{
		return creationTime;
	}	
		
	
	/**
	 * sets the expiry time of this movie
	 * @param exTime expiry time
	 */
	public void setExpiryTime(long exTime)
	{
		this.expiryTime = exTime;
	}
	
	/**
	 * gets the expiry time for this movie
	 * @return the expirty time for this movie
	 */
	public long getExpiryTime()
	{
		return this.expiryTime;
	}
	
	private Set<Channel> readChannelObjects()
	{
	    Set<Channel> channelObjects = new HashSet<Channel>();
		
		try
		{
		    List<Channel> allChannels = SysManagerFactory.getUserPreference().getChannels(actorLogin, guid);
			for(int channelNo : channels)
			{
				channelObjects.add(allChannels.get(channelNo));
			}
		}
		catch (DataAccessException e)
		{
		}
        return channelObjects;
	}
	
	public synchronized boolean isDone()
	{
		for(boolean b : movieFrames)
		{
			if(!b) return b;
		}
		
		return true;
	}
	
	public synchronized boolean isFrameGenerated(int movieFrame)
	{
		return movieFrames[movieFrame];
	}
	
	public synchronized void setGenerated(int movieFrame)
	{
		 movieFrames[movieFrame] = true;
	}
	
	public synchronized void setNotGenerated(int movieFrame)
	{
		 movieFrames[movieFrame] = false;
	}
	
	public long getMovieid()
	{
		return ID;
	}
	
	public long getGuid()
	{
		return guid;
	}
	
	public int getSite()
	{
		return site;
	}
	
	public List<Integer> getChannels()
	{
	    List<Integer> channels = new ArrayList<Integer>();
	    channels.addAll(this.channels);
		return channels;
	}
	
	public List<String> getOverlays()
	{
	    List<String> overlays = new ArrayList<String>();
	    overlays.addAll(this.overlays);
		return overlays;
	}
	
	public boolean isOnFrames()
	{
		return onFrames;
	}
	
	public int getFixedCoordinate()
	{
		return otherCoordinate;
	}
	
	public boolean isColored()
	{
		return useChannelColor;
	}
	
	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
	
	@Override
	public String toString()
	{
		return this.guid+" "+this.site+" "+this.onFrames+" "+this.channels+" "+this.overlays+" "+this.type;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof MovieTicket)
		{
			MovieTicket that = (MovieTicket) obj;
			if(this == that) return true;
			return (this.actorLogin.equals(that.actorLogin)
					&& this.guid == that.guid
					&& this.site == that.site
					&& this.onFrames == that.onFrames
					&& this.otherCoordinate == that.otherCoordinate 
					&& this.useChannelColor == that.useChannelColor
					&& this.useZStack == that.useZStack
					&& this.channels.equals(that.channels)
					&& this.overlays.equals(that.overlays)
					&& this.type.equals(that.type)
					&& ((this.fps == null && that.fps == null) || (this.fps !=null && this.fps.equals(that.fps)))
					&& this.channelObjects.equals(that.channelObjects));
		}
		return false;
	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub
		
	}
	
	protected int getMaxCoordinate()
	{
		try 
		{
			Record record = SysManagerFactory.getRecordManager().findRecord(actorLogin, guid);
			int max = isOnFrames() ? record.numberOfFrames : record.numberOfSlices;
			return max;
		} 
		catch (DataAccessException e) 
		{
			throw new NullPointerException("error finding record for guid "+guid);
		}
	}
	
	public File createStorageDirectory()
	{
		File f = new File(Constants.getStringProperty(Property.MOVIE_STORAGE_LOCATION, null));
		if(!f.isDirectory())
			f.mkdir();
		
		File movieStorageRoot = new File(f, String.valueOf(ID));
		if(!movieStorageRoot.exists())
			movieStorageRoot.mkdirs();
		
		return movieStorageRoot;
	}

	public boolean isValid(int ithImage)
	{
		return ithImage < maxCoordinate;
	}

	public void setFPS(Double fps)
	{
		this.fps = fps;
	}
	
	public Double getFPS()
	{
		return this.fps;
	}
}
