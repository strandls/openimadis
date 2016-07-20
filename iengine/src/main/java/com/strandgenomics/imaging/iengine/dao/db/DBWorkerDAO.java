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

package com.strandgenomics.imaging.iengine.dao.db;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.logging.Level;

import javax.activation.DataSource;

import com.strandgenomics.imaging.icore.Constants;
import com.strandgenomics.imaging.icore.db.ConnectionProvider;
import com.strandgenomics.imaging.icore.db.DataAccessException;
import com.strandgenomics.imaging.icore.db.RowSet;
import com.strandgenomics.imaging.icore.db.SQLQuery;
import com.strandgenomics.imaging.icore.util.Util;
import com.strandgenomics.imaging.iengine.dao.ImageSpaceDAOFactory;
import com.strandgenomics.imaging.iengine.dao.WorkerDAO;
import com.strandgenomics.imaging.iengine.worker.Service;
import com.strandgenomics.imaging.iengine.worker.ServiceStatus;
import com.strandgenomics.imaging.iengine.worker.ServiceType;

/**
 * access to worker related functionalities
 * @author navneet
 *
 */
public class DBWorkerDAO extends ImageSpaceDAO<Service> implements WorkerDAO{

	DBWorkerDAO(ImageSpaceDAOFactory factory,
			ConnectionProvider connectionProvider) {
		super(factory, connectionProvider, "WorkerDAO.xml");
	}

	@Override
	public void insertServieStatus(ServiceType serviceType,
			ServiceStatus serviceStatus) throws DataAccessException {
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("INSERT_SERVICE_STATUS");
        sqlQuery.setValue("workerID", Constants.getWorkerIdentifier(), Types.INTEGER);
        sqlQuery.setValue("serviceType", serviceType, Types.VARCHAR);
        sqlQuery.setValue("serviceStatus", toByteArray(serviceStatus), Types.BLOB);
        
		updateDatabase(sqlQuery);
		
	}

	@Override
	public void updateServiceStatus(ServiceType serviceType,
			ServiceStatus serviceStatus) throws DataAccessException {
		
		if(getServiceStatus(serviceType) == null){
			logger.logp(Level.INFO, "DBWorkerDAO"," updateServiceStatus", "insert type="+serviceType);
			insertServieStatus(serviceType, serviceStatus);
		}		
		else{
			logger.logp(Level.INFO, "DBWorkerDAO"," updateServiceStatus", "type="+serviceType);
	        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("UPDATE_SERVICE_STATUS");
	        sqlQuery.setValue("workerID", Constants.getWorkerIdentifier(), Types.INTEGER);
	        sqlQuery.setValue("serviceType", serviceType, Types.VARCHAR);
	        sqlQuery.setValue("serviceStatus", toByteArray(serviceStatus), Types.BLOB);
	        sqlQuery.setValue("lastModificationTime", new Timestamp(System.currentTimeMillis()), Types.TIMESTAMP);
	        
			updateDatabase(sqlQuery);
		}
		
	}

	@Override
	public Service getServiceStatus(ServiceType serviceType) throws DataAccessException {

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_SERVICE_STATUS");
        sqlQuery.setValue("workerID", Constants.getWorkerIdentifier(), Types.INTEGER);
        sqlQuery.setValue("serviceType", serviceType, Types.VARCHAR);
		
        return fetchInstance(sqlQuery);
	}

	@Override
	public List<Service> getAllServiceStatus() throws DataAccessException {

        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_ALL_SERVICE_STATUS");
		
        RowSet<Service> result = find(sqlQuery);
        
        return result == null ? null : result.getRows();
	}
	
	@Override
	public Service createObject(Object[] columnValues) {
		
		int workerId = Util.getInteger(columnValues[0]);
		ServiceType type = ServiceType.valueOf(columnValues[1].toString());
		ServiceStatus status = (ServiceStatus) toJavaObject((DataSource) columnValues[2]);
		
		return new Service(workerId, type, status);
	}

	@Override
	public long getLastModificationTime(ServiceType serviceType) throws DataAccessException {
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_LAST_MODIFICATION_TIME");
        sqlQuery.setValue("workerID", Constants.getWorkerIdentifier(), Types.INTEGER);
        sqlQuery.setValue("serviceType", serviceType, Types.VARCHAR);
		
        return getLong(sqlQuery);
	}

	@Override
	public boolean getToBeRestarted(ServiceType serviceType) throws DataAccessException {
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("GET_TO_BE_RESTARTED_VALUE");
        sqlQuery.setValue("workerID", Constants.getWorkerIdentifier(), Types.INTEGER);
        sqlQuery.setValue("serviceType", serviceType, Types.VARCHAR);
        
        return getBoolean(sqlQuery);
	}

	@Override
	public void setToBeRestarted(ServiceType serviceType, boolean toBeRestarted) throws DataAccessException{
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("SET_TO_BE_RESTARTED");
        sqlQuery.setValue("toBeRestarted", toBeRestarted , Types.BOOLEAN);
        sqlQuery.setValue("workerID", Constants.getWorkerIdentifier(), Types.INTEGER);
        sqlQuery.setValue("serviceType", serviceType, Types.VARCHAR);
        
        updateDatabase(sqlQuery);
	}
	
	@Override
	public void setToBeRestarted(int workerId, ServiceType serviceType, boolean toBeRestarted) throws DataAccessException{
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("SET_TO_BE_RESTARTED");
        sqlQuery.setValue("toBeRestarted", toBeRestarted , Types.BOOLEAN);
        sqlQuery.setValue("workerID", workerId, Types.INTEGER);
        sqlQuery.setValue("serviceType", serviceType, Types.VARCHAR);
        
        updateDatabase(sqlQuery);
	}

	@Override
	public void removeService(ServiceType serviceType)
			throws DataAccessException {
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("REMOVE_SERVICE");
        sqlQuery.setValue("workerID", Constants.getWorkerIdentifier(), Types.INTEGER);
        sqlQuery.setValue("serviceType", serviceType, Types.VARCHAR);
        
        updateDatabase(sqlQuery);
	}
	
	@Override
	public void removeService(int workerId, ServiceType serviceType)
			throws DataAccessException {
		
        SQLQuery sqlQuery = queryDictionary.createQueryGenerator("REMOVE_SERVICE");
        sqlQuery.setValue("workerID", Constants.getWorkerIdentifier(), Types.INTEGER);
        sqlQuery.setValue("serviceType", serviceType, Types.VARCHAR);
        
        updateDatabase(sqlQuery);
	}

}
