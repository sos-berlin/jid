package com.sos.scheduler.history.db;

import java.io.File;
import java.util.List;
import java.util.TimeZone;
import org.hibernate.Query;
import com.sos.hibernate.classes.DbItem;
import com.sos.hibernate.layer.SOSHibernateIntervalDBLayer;
import com.sos.scheduler.history.SchedulerTaskHistoryFilter;

public class SchedulerTaskHistoryDBLayer extends SOSHibernateIntervalDBLayer {

    protected SchedulerTaskHistoryFilter filter = null;
    private String lastQuery = "";

    public SchedulerTaskHistoryDBLayer(File configurationFile_) {
        super();
        this.setConfigurationFile(configurationFile_);
        this.resetFilter();
    }

    public SchedulerTaskHistoryDBItem get(Long id) {
        initSession();
        SchedulerTaskHistoryDBItem schedulerHistoryDBItem = null;
        try {
            schedulerHistoryDBItem = (SchedulerTaskHistoryDBItem) this.getSession().get(SchedulerTaskHistoryDBItem.class, id);
            return schedulerHistoryDBItem;
        } catch (Exception e) {
            return null;
        }
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

    public long deleteInterval() {
        if (session == null) {
            beginTransaction();
        }
        String hql = "delete from SchedulerTaskHistoryDBItem " + getWhereFromTo();
        Query query = session.createQuery(hql);
        if (filter.getExecutedUtcFrom() != null) {
            query.setTimestamp("startTimeFrom", filter.getExecutedUtcFrom());
        }
        if (filter.getExecutedUtcTo() != null) {
            query.setTimestamp("startTimeTo", filter.getExecutedUtcTo());
        }
        return query.executeUpdate();
    }

    public int delete() {
        if (session == null) {
            beginTransaction();
        }
        String hql = "delete from SchedulerTaskHistoryDBItem " + getWhereFromTo();
        Query query = session.createQuery(hql);
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
        return query.executeUpdate();
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

    public List<SchedulerTaskHistoryDBItem> getSchedulerHistoryListFromTo() {
        int limit = this.getFilter().getLimit();
        initSession();
        Query query = session.createQuery("from SchedulerTaskHistoryDBItem " + getWhereFromTo() + filter.getOrderCriteria() + filter.getSortMode());
        return executeQuery(query, limit);
    }

    public List<SchedulerTaskHistoryDBItem> getUnassignedSchedulerHistoryListFromTo() {
        int limit = this.getFilter().getLimit();
        initSession();
        Query query = session.createQuery("from SchedulerTaskHistoryDBItem " + getWhereFromTo() + " and id NOT IN (select schedulerHistoryId from "
                + "DailyScheduleDBItem where not schedulerHistoryId is null and  status=1 and schedulerId=:schedulerId) " + filter.getOrderCriteria()
                + filter.getSortMode());
        return executeQuery(query, limit);
    }

    public List<SchedulerTaskHistoryDBItem> getSchedulerHistoryListFromToStart() {
        int limit = this.getFilter().getLimit();
        initSession();
        Query query =
                session.createQuery("from SchedulerTaskHistoryDBItem " + getWhereFromToStart() + filter.getOrderCriteria() + filter.getSortMode());
        return executeQuery(query, limit);
    }

    public List<SchedulerTaskHistoryDBItem> getSchedulerHistoryListFromToEnd() {
        int limit = this.getFilter().getLimit();
        initSession();
        Query query =
                session.createQuery("from SchedulerTaskHistoryDBItem " + getWhereFromToStart() + filter.getOrderCriteria() + filter.getSortMode());
        return executeQuery(query, limit);
    }

    public List<SchedulerTaskHistoryDBItem> getSchedulerHistoryListSchedulersFromTo() {
        int limit = this.getFilter().getLimit();
        initSession();
        String q =
                "from SchedulerTaskHistoryDBItem e where e.spoolerId IN (select distinct e.spoolerId from SchedulerTaskHistoryDBItem "
                        + getWhereFromTo() + ")";
        Query query = session.createQuery(q);
        return executeQuery(query, limit);
    }

    public List<SchedulerTaskHistoryDBItem> getHistoryItems() {
        int limit = this.getFilter().getLimit();
        initSession();
        Query query =
                session.createQuery("from SchedulerTaskHistoryDBItem " + getWhere() + this.filter.getOrderCriteria() + this.filter.getSortMode());
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
        return query.list();
    }

    public SchedulerTaskHistoryDBItem getHistoryItem() {
        this.filter.setLimit(1);
        initSession();
        Query query =
                session.createQuery("from SchedulerTaskHistoryDBItem " + getWhere() + this.filter.getOrderCriteria() + this.filter.getSortMode());
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
        List<SchedulerTaskHistoryDBItem> historyList = query.list();
        if (!historyList.isEmpty()) {
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
    public List<DbItem> getListOfItemsToDelete() {
        TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
        int limit = this.getFilter().getLimit();
        initSession();
        Query query = session.createQuery("from SchedulerTaskHistoryDBItem " + getWhereFromTo() + filter.getOrderCriteria() + filter.getSortMode());
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

}