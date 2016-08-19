package com.sos.scheduler.history.db;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import com.sos.hibernate.classes.DbItem;
import com.sos.hibernate.layer.SOSHibernateIntervalDBLayer;
import com.sos.scheduler.history.SchedulerTaskHistoryFilter;

/** @author Uwe Risse */
public class SchedulerTaskHistoryDBLayer extends SOSHibernateIntervalDBLayer {

    protected SchedulerTaskHistoryFilter filter = null;
    private static final Logger LOGGER = Logger.getLogger(SchedulerTaskHistoryDBLayer.class);
    private String lastQuery = "";

    public SchedulerTaskHistoryDBLayer(String configurationFilename) {
        super();
        this.setConfigurationFileName(configurationFilename);
        this.initConnection(this.getConfigurationFileName());
        this.resetFilter();
    }

    public SchedulerTaskHistoryDBLayer(File configurationFile) {
        super();
        try {
            this.setConfigurationFileName(configurationFile.getCanonicalPath());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            this.setConfigurationFileName("");
        }
        this.initConnection(this.getConfigurationFileName());
        this.resetFilter();
    }

    public SchedulerTaskHistoryDBItem get(Long id) throws Exception {
        if (connection == null) {
            initConnection(getConfigurationFileName());
        }
        SchedulerTaskHistoryDBItem schedulerHistoryDBItem = null;
        connection.beginTransaction();
        schedulerHistoryDBItem = (SchedulerTaskHistoryDBItem) ((Session) connection.getCurrentSession()).get(SchedulerTaskHistoryDBItem.class, id);
        return schedulerHistoryDBItem;
    }

    public void resetFilter() {
        this.filter = new SchedulerTaskHistoryFilter();
        this.filter.setDateFormat("yyyy-MM-dd HH:mm:ss");
        this.filter.setOrderCriteria("startTime");
        this.filter.setSortMode("desc");
    }

    public SchedulerTaskHistoryFilter getFilter() {
        return filter;
    }

    public void setFilter(SchedulerTaskHistoryFilter filter_) {
        filter = filter_;
    }

    protected String getWhere() {
        String where = "";
        String and = "";
        if (filter.getSchedulerId() != null && !"".equals(filter.getSchedulerId())) {
            where += and + " spoolerId=:schedulerId";
            and = " and ";
        }
        if (filter.getJobname() != null && !"".equals(filter.getJobname())) {
            where += and + " jobName=:jobName";
            and = " and ";
        }
        if (filter.getSeverity() != null && filter.getSeverity().hasValue()) {
            where += and + " error=:severity";
            and = " and ";
        }
        if (filter.getStartTime() != null) {
            where += and + " startTime>= :startTime";
            and = " and ";
        }
        if (filter.getEndTime() != null) {
            where += and + " endTime <= :endTime ";
            and = " and ";
        }
        if (!"".equals(where.trim())) {
            where = "where " + where;
        }
        return where;
    }

    protected String getWhereFromTo() {
        return getWhereFromToStart();
    }

    protected String getWhereFromToStart() {
        return getWhereFromTo("startTime");
    }

    protected String getWhereFromToEnd() {
        return getWhereFromTo("endTime");
    }

    protected String getWhereFromTo(String fieldname_date_field) {
        String where = "";
        String and = "";
        if (filter.getSchedulerId() != null && !"".equals(filter.getSchedulerId())) {
            where += and + " spoolerId=:schedulerId";
            and = " and ";
        }
        if (filter.getJobname() != null && !"".equals(filter.getJobname())) {
            if (filter.getJobname().contains("%")) {
                where += and + " jobName like :jobName";
            } else {
                where += and + " jobName=:jobName";
            }
            and = " and ";
        }
        if (filter.getExecutedUtcFrom() != null) {
            where += and + fieldname_date_field + " >= :startTimeFrom";
            and = " and ";
        }
        if (filter.getExecutedUtcTo() != null) {
            where += and + fieldname_date_field + " <= :startTimeTo ";
            and = " and ";
        }
        if (!filter.isShowJobs()) {
            where += and + " 1=0";
            and = " and ";
        }
        if (filter.isShowSuccessfull()) {
            where += and + " exitCode=0";
            and = " and ";
        }
        if (!"".equals(where.trim())) {
            where = "where " + where;
        }
        return where;
    }

    public long deleteInterval() throws Exception {
        int row = 0;
        if (connection == null) {
            initConnection(getConfigurationFileName());
        }
        String hql = "delete from SchedulerTaskHistoryDBItem " + getWhereFromTo();
        connection.beginTransaction();
        Query query = connection.createQuery(hql);
        if (filter.getExecutedUtcFrom() != null) {
            query.setTimestamp("startTimeFrom", filter.getExecutedUtcFrom());
        }
        if (filter.getExecutedUtcTo() != null) {
            query.setTimestamp("startTimeTo", filter.getExecutedUtcTo());
        }
        row = query.executeUpdate();
        connection.commit();
        return row;
    }

    public int delete() throws Exception {
        int row = 0;
        if (connection == null) {
            initConnection(getConfigurationFileName());
        }
        String hql = "delete from SchedulerTaskHistoryDBItem " + getWhereFromTo();
        connection.beginTransaction();
        Query query = connection.createQuery(hql);
        if (filter.getSchedulerId() != null && !"".equalsIgnoreCase(filter.getSchedulerId())) {
            query.setText("schedulerId", filter.getSchedulerId());
        }
        if (filter.getSeverity() != null) {
            query.setInteger("severity", filter.getSeverity().getIntValue());
        }
        if (filter.getJobname() != null && !"".equalsIgnoreCase(filter.getJobname())) {
            query.setText("jobName", filter.getJobname());
        }
        if (filter.getExecutedUtcFrom() != null) {
            query.setTimestamp("startTimeFrom", filter.getExecutedUtcFrom());
        }
        if (filter.getExecutedUtcTo() != null) {
            query.setTimestamp("startTimeTo", filter.getExecutedUtcTo());
        }
        row = query.executeUpdate();
        connection.commit();
        return row;
    }

    private List<SchedulerTaskHistoryDBItem> executeQuery(Query query, int limit) {
        lastQuery = query.getQueryString();
        if (filter.getSchedulerId() != null && !"".equals(filter.getSchedulerId())) {
            query.setText("schedulerId", filter.getSchedulerId());
        }
        if (filter.getSeverity() != null) {
            query.setInteger("severity", filter.getSeverity().getIntValue());
        }
        if (filter.getJobname() != null && !"".equals(filter.getJobname())) {
            query.setText("jobName", filter.getJobname());
        }
        if (filter.getExecutedUtcFrom() != null) {
            query.setTimestamp("startTimeFrom", filter.getExecutedUtcFrom());
        }
        if (filter.getExecutedUtcTo() != null) {
            query.setTimestamp("startTimeTo", filter.getExecutedUtcTo());
        }
        if (limit > 0) {
            query.setMaxResults(limit);
        }
        return query.list();
    }

    public List<SchedulerTaskHistoryDBItem> getSchedulerHistoryListFromTo() throws Exception {
        int limit = this.getFilter().getLimit();
        if (connection == null) {
            initConnection(getConfigurationFileName());
        }
        Query query = null;
        connection.beginTransaction();
        query = connection.createQuery("from SchedulerTaskHistoryDBItem " + getWhereFromTo() + filter.getOrderCriteria() + filter.getSortMode());
        return executeQuery(query, limit);
    }

    public List<SchedulerTaskHistoryDBItem> getUnassignedSchedulerHistoryListFromTo() throws Exception {
        int limit = this.getFilter().getLimit();
        if (connection == null) {
            initConnection(getConfigurationFileName());
        }
        Query query = null;
        connection.beginTransaction();
        query =
                connection.createQuery("from SchedulerTaskHistoryDBItem " + getWhereFromTo() + " and id NOT IN (select schedulerHistoryId from "
                        + "DailyScheduleDBItem where not schedulerHistoryId is null and  status=1 and schedulerId=:schedulerId) "
                        + filter.getOrderCriteria() + filter.getSortMode());
        return executeQuery(query, limit);
    }

    public List<SchedulerTaskHistoryDBItem> getSchedulerHistoryListFromToStart() throws Exception {
        int limit = this.getFilter().getLimit();
        if (connection == null) {
            initConnection(getConfigurationFileName());
        }
        Query query = null;
        connection.beginTransaction();
        query = connection.createQuery("from SchedulerTaskHistoryDBItem " + getWhereFromToStart() + filter.getOrderCriteria()
                + filter.getSortMode());
        return executeQuery(query, limit);
    }

    public List<SchedulerTaskHistoryDBItem> getSchedulerHistoryListFromToEnd() throws Exception {
        int limit = this.getFilter().getLimit();
        if (connection == null) {
            initConnection(getConfigurationFileName());
        }
        Query query = null;
        connection.beginTransaction();
        query = connection.createQuery("from SchedulerTaskHistoryDBItem " + getWhereFromToStart() + filter.getOrderCriteria()
                + filter.getSortMode());
        return executeQuery(query, limit);
    }

    public List<SchedulerTaskHistoryDBItem> getSchedulerHistoryListSchedulersFromTo() throws Exception {
        int limit = this.getFilter().getLimit();
        String q = "from SchedulerTaskHistoryDBItem e where e.spoolerId IN (select distinct e.spoolerId from SchedulerTaskHistoryDBItem "
                        + getWhereFromTo() + ")";
        if (connection == null) {
            initConnection(getConfigurationFileName());
        }
        Query query = null;
        connection.beginTransaction();
        query = connection.createQuery(q);
        return executeQuery(query, limit);
    }

    public List<SchedulerTaskHistoryDBItem> getHistoryItems() throws Exception {
        int limit = this.getFilter().getLimit();
        if (connection == null) {
            initConnection(getConfigurationFileName());
        }
        List<SchedulerTaskHistoryDBItem> historyList = null;
        Query query = null;
        connection.beginTransaction();
        query =
                connection.createQuery("from SchedulerTaskHistoryDBItem " + getWhere() + this.filter.getOrderCriteria()
                        + this.filter.getSortMode());
        if (filter.getSchedulerId() != null && !"".equalsIgnoreCase(filter.getSchedulerId())) {
            query.setText("schedulerId", filter.getSchedulerId());
        }
        if (filter.getSeverity() != null) {
            query.setInteger("severity", filter.getSeverity().getIntValue());
        }
        if (filter.getJobname() != null && !"".equalsIgnoreCase(filter.getJobname())) {
            query.setText("jobName", filter.getJobname());
        }
        if (filter.getStartTime() != null) {
            query.setTimestamp("startTime", filter.getStartTime());
        }
        if (filter.getEndTime() != null && !"".equals(filter.getEndTime())) {
            query.setTimestamp("endTime", filter.getEndTime());
        }
        if (limit > 0) {
            query.setMaxResults(limit);
        }
        historyList = query.list();
        return historyList;
    }

    public SchedulerTaskHistoryDBItem getHistoryItem() throws Exception {
        this.filter.setLimit(1);
        if (connection == null) {
            initConnection(getConfigurationFileName());
        }
        List<SchedulerTaskHistoryDBItem> historyList = null;
        Query query = null;
        connection.beginTransaction();
        query = connection.createQuery("from SchedulerTaskHistoryDBItem " + getWhere() + this.filter.getOrderCriteria()
                        + this.filter.getSortMode());
        if (filter.getSchedulerId() != null && !"".equalsIgnoreCase(filter.getSchedulerId())) {
            query.setText("schedulerId", filter.getSchedulerId());
        }
        if (filter.getSeverity() != null) {
            query.setInteger("severity", filter.getSeverity().getIntValue());
        }
        if (filter.getJobname() != null && !"".equalsIgnoreCase(filter.getJobname())) {
            query.setText("jobName", filter.getJobname());
        }
        if (filter.getStartTime() != null) {
            query.setTimestamp("startTime", filter.getStartTime());
        }
        if (filter.getEndTime() != null && !"".equals(filter.getEndTime())) {
            query.setTimestamp("endTime", filter.getEndTime());
        }
        if (this.filter.getLimit() > 0) {
            query.setMaxResults(this.filter.getLimit());
        }
        historyList = query.list();
        if (historyList != null && !historyList.isEmpty()) {
            return historyList.get(0);
        } else {
            return null;
        }
    }

    public String getLastQuery() {
        return lastQuery;
    }

    @Override
    public void onAfterDeleting(DbItem h) {
        // Nothing to do
    }

    @Override
    public List<DbItem> getListOfItemsToDelete() throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
        int limit = this.getFilter().getLimit();
        if (connection == null) {
            initConnection(getConfigurationFileName());
        }
        Query query = null;
        List<DbItem> schedulerHistoryList = null;
        connection.beginTransaction();
        query = connection.createQuery("from SchedulerTaskHistoryDBItem " + getWhereFromTo() + filter.getOrderCriteria() + filter.getSortMode());
        if (filter.getSchedulerId() != null && !"".equals(filter.getSchedulerId())) {
            query.setText("schedulerId", filter.getSchedulerId());
        }
        if (filter.getSeverity() != null) {
            query.setInteger("severity", filter.getSeverity().getIntValue());
        }
        if (filter.getJobname() != null && !"".equals(filter.getJobname())) {
            query.setText("jobName", filter.getJobname());
        }
        if (filter.getExecutedUtcFrom() != null) {
            query.setTimestamp("startTimeFrom", filter.getExecutedUtcFrom());
        }
        if (filter.getExecutedUtcTo() != null) {
            query.setTimestamp("startTimeTo", filter.getExecutedUtcTo());
        }
        if (limit > 0) {
            query.setMaxResults(limit);
        }
        schedulerHistoryList = query.list();
        return schedulerHistoryList;
    }

}