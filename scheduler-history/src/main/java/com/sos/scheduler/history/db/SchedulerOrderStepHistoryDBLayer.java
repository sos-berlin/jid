package com.sos.scheduler.history.db;


 
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.sos.hibernate.layer.SOSHibernateDBLayer;
import com.sos.scheduler.history.SchedulerOrderStepHistoryFilter;
 

 

public class SchedulerOrderStepHistoryDBLayer extends SOSHibernateDBLayer{
    protected SchedulerOrderStepHistoryFilter  filter           = null;
    private Logger logger = Logger.getLogger(SchedulerOrderStepHistoryDBLayer.class);

	public SchedulerOrderStepHistoryDBLayer(final String configurationFilename) {
		super();
		this.setConfigurationFileName(configurationFilename);
		this.initConnection(this.getConfigurationFileName());
		resetFilter();
	}
	
    public SchedulerOrderStepHistoryDBLayer(final File configurationFile) {
        super();
        try {
            this.setConfigurationFileName(configurationFile.getCanonicalPath());
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
            this.setConfigurationFileName("");
        }
        this.initConnection(this.getConfigurationFileName());
        resetFilter();
    }
    
	public SchedulerOrderStepHistoryDBItem get(final SchedulerOrderStepHistoryCompoundKey id) {
		if (connection == null){
			initConnection(getConfigurationFileName());
		}
		try {
			connection.connect();
			connection.beginTransaction();
			return (SchedulerOrderStepHistoryDBItem) ((Session)connection.getCurrentSession()).get(SchedulerOrderStepHistoryDBItem.class,id);
		} catch (Exception e) {
			logger.error("Error occurred receiving item: ", e);
		} 
		return null;
	}
	
	public void resetFilter() {
	    filter = new SchedulerOrderStepHistoryFilter();
        filter.setDateFormat("yyyy-MM-dd HH:mm:ss");
        filter.setOrderCriteria("startTime");
        filter.setSortMode("desc");
	}
	
	protected String getWhereFromTo() {
		String where = "";
		String and = "";
		if (filter.getExecutedFromUtc() != null ) {
			where += and + " startTime>= :startTimeFrom";
			and = " and ";
		}
		if (filter.getExecutedToUtc() != null ) {
			where += and + " startTime <= :startTimeTo ";
			and = " and ";
		}
		if (!where.trim().equals("")) {
			where = "where " + where;
		}
		return where;
	}
	
	protected String getWhere() {
		String where = "";
		String and = "";
	    if (filter.getHistoryId() != null) {
            where += and + " id.historyId = :historyId";
            and = " and ";
        }
		if (filter.getStartTime() != null && !filter.getStartTime().equals("")) {
			where += and + " startTime>= :startTime";
			and = " and ";
		}
		if (filter.getEndTime() != null && !filter.getEndTime().equals("")) {
			where += and + " endTime <= :endTime ";
			and = " and ";
		}
		if (filter.getStatus() != null && !filter.getStatus().equals("")) {
			where += and + " state = :state";
			and = " and ";
		}
		if (!where.trim().equals("")) {
			where = "where " + where;
		}
		return where;
	}
	
	public int deleteFromTo() {
		String hql = "delete from SchedulerOrderStepHistoryDBItem " + getWhereFromTo();
		int row = 0;
		if(connection == null){
			initConnection(getConfigurationFileName());
		}
		try {
			connection.connect();
			connection.beginTransaction();
			Query query = connection.createQuery(hql);
			query.setTimestamp("startTimeFrom", filter.getExecutedFromUtc());
			query.setTimestamp("startTimeTo", filter.getExecutedToUtc());
			row = query.executeUpdate();
		} catch (Exception e) {
			logger.error("Error occurred trying to delete Items for the given interval: ", e);
		}
		return row;
	}
	
  
	public void deleteInterval(final int interval){
		GregorianCalendar now = new GregorianCalendar();
		now.add(GregorianCalendar.DAY_OF_YEAR, -interval);
		filter.setExecutedTo(new Date());
		filter.setExecutedFrom(now.getTime());
		this.deleteFromTo();
	}
	
	public List<SchedulerOrderStepHistoryDBItem> getSchedulerOrderStepHistoryListFromTo(final int limit) {
		List<SchedulerOrderStepHistoryDBItem> schedulerHistoryList = null;
		if(connection == null){
			initConnection(getConfigurationFileName());
		}
		try {
			connection.connect();
			connection.beginTransaction();
			Query query = connection.createQuery("from SchedulerOrderStepHistoryDBItem " + getWhereFromTo() + filter.getOrderCriteria() + filter.getSortMode());
			if (filter.getExecutedFromUtc() != null && !filter.getExecutedFromUtc().equals("")) {
				query.setTimestamp("startTimeFrom", filter.getExecutedFromUtc());
			}
			if (filter.getExecutedToUtc() != null && !filter.getExecutedToUtc().equals("")) {
				query.setTimestamp("startTimeTo", filter.getExecutedToUtc());
			}
			if (limit > 0) {
				query.setMaxResults(limit);
			}
			schedulerHistoryList = query.list();
		} catch (Exception e) {
			logger.error("Error occurred receiving Items for the given interval: ", e);
		}
 		return schedulerHistoryList;
	}

   

	public List<SchedulerOrderStepHistoryDBItem> getOrderStepHistoryItems(final int limit, long historyId)  {
		filter.setHistoryId(historyId);
		List<SchedulerOrderStepHistoryDBItem> historyList = null;
		if(connection == null){
			initConnection(getConfigurationFileName());
		}
		try {
			connection.connect();
			connection.beginTransaction();
			Query query = connection.createQuery("from SchedulerOrderStepHistoryDBItem " + getWhere());
			if (filter.getHistoryId() != null ) {
			    query.setLong("historyId", filter.getHistoryId());
			}
        if (filter.getStatus() != null && !filter.getStatus().equals("")) {
        	query.setParameter("state", filter.getStatus());
			}
			if (filter.getStartTime() != null && !filter.getStartTime().equals("")) {
			    query.setTimestamp("startTime", filter.getStartTime());
			}
			if (filter.getStartTime() != null && !filter.getStartTime().equals("")) {
			    query.setTimestamp("startTime", filter.getStartTime());
			}
			if (filter.getEndTime() != null && !filter.getEndTime().equals("")) {
				query.setTimestamp("endTime", filter.getEndTime());
			}
			if (limit != 0) {
			    query.setMaxResults(limit);
			}
			historyList = query.list();
		} catch (Exception e) {
			logger.error("Error occurred receiving Items: ", e);
		}
		return historyList;
	}

    public SchedulerOrderStepHistoryFilter getFilter() {
        return filter;
    }

}
