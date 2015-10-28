package com.strandgenomics.imaging.iengine.system;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.worker.Service;
import com.strandgenomics.imaging.iengine.worker.ServiceAnnotation;
import com.strandgenomics.imaging.iengine.worker.ServiceParameter;
import com.strandgenomics.imaging.iengine.worker.ServiceType;

/**
 * manage worker related functionalities
 * get status of workers and services running on those workers
 * @author navneet
 *
 */
public class WorkerManager {
	
	/**
	 * get status of all services running across all workers
	 * @return
	 * @throws DataAccessException
	 */
	public List<Service> getAllServiceStatus() throws DataAccessException{
		
		List<Service> allServiceStatus = ImageSpaceDAOFactory.getDAOFactory().getWorkerDAO().getAllServiceStatus();
		
		return allServiceStatus;
	}
	
	/**
	 * get the parameters in a service status
	 * these parameters define the status of that service
	 * @param type
	 * @return
	 */
	public List<String> getServiceParameters(ServiceType type){
		
		List<String> serviceParameters = new ArrayList<String>();
		
		Reflections reflection=new Reflections("com.strandgenomics.imaging.iengine.worker");
		Set<Class<?>> classes = reflection.getTypesAnnotatedWith(ServiceAnnotation.class);
		
		for(Class<?> c : classes){
			
			if(c.getAnnotation(ServiceAnnotation.class).type().equals(type)){
				Field fields[] = c.getDeclaredFields();
				
				if(fields!=null){
					for(Field field : fields){
						if(field.isAnnotationPresent(ServiceParameter.class))
							serviceParameters.add(field.getAnnotation(ServiceParameter.class).name());
					}
				}
			}
		}
		
		return serviceParameters;
	}
	
	/**
	 * restart the specified service for given worker
	 * @param workerId
	 * @param serviceType
	 * @throws DataAccessException 
	 */
	public void restartService(int workerId, ServiceType serviceType) throws DataAccessException{
		
		ImageSpaceDAOFactory.getDAOFactory().getWorkerDAO().setToBeRestarted(workerId, serviceType, true);
	}
	
}
