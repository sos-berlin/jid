package com.sos.dailyschedule.db;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.hibernate.Query;
import org.joda.time.DateTime;

import com.sos.dailyschedule.DailyScheduleFilter;
import com.sos.hibernate.classes.DbItem;
import com.sos.hibernate.classes.UtcTimeHelper;
import com.sos.hibernate.layer.SOSHibernateIntervalDBLayer;
import com.sos.scheduler.history.db.SchedulerOrderHistoryDBItem;
import com.sos.scheduler.history.db.SchedulerTaskHistoryDBItem;

public class DailyScheduleDBLayer extends SOSHibernateIntervalDBLayer {

    @SuppressWarnings("unused")
    private final String conClassName = "DailySchedulerDBLayer";

    private String whereFromIso = null;
    private String whereToIso = null;
    private DailyScheduleFilter filter = null;

    public DailyScheduleDBLayer(final File configurationFile_) {
        super();
        this.setConfigurationFile(configurationFile_);
        resetFilter();
    }

    public DailyScheduleDBItem getHistory(final Long id) {
        return (DailyScheduleDBItem) this.getSession().get(DailyScheduleDBItem.class, id);
    }

    public void resetFilter() {
        filter = new DailyScheduleFilter();
        filter.setExecutedFrom(new Date());
        filter.setShowJobs(true);
        filter.setShowJobChains(true);
        filter.setLate(false);
        filter.setSchedulerId("");
        filter.getSosSearchFilterData().setSearchfield("");
        filter.setStatus("");
    }

    public int delete() {

        if (session == null) {
            beginTransaction();
        }

        String hql = "delete from DailyScheduleDBItem " + getWhere();

        Query query = session.createQuery(hql);

        if (filter.getPlannedUtcFrom() != null && !filter.getPlannedUtcFrom().equals("")) {
            query.setTimestamp("schedulePlannedFrom", filter.getPlannedUtcFrom());
        }
        if (filter.getPlannedUtcTo() != null && !filter.getPlannedUtcTo().equals("")) {
            query.setTimestamp("schedulePlannedTo", filter.getPlannedUtcTo());
        }
        if (filter.getSchedulerId() != null && !filter.getSchedulerId().equals("")) {
            query.setParameter("schedulerId", filter.getSchedulerId());
        }
        int row = query.executeUpdate();

        return row;
    }

    public long deleteInterval() {

        if (session == null) {
            beginTransaction();
        }

        String hql = "delete from DailyScheduleDBItem " + getWhere();

        Query query = session.createQuery(hql);

        if (filter.getPlannedUtcFrom() != null) {
            query.setTimestamp("schedulePlannedFrom", filter.getPlannedUtcFrom());
        }
        if (filter.getPlannedUtcTo() != null) {
            query.setTimestamp("schedulePlannedTo", filter.getPlannedUtcTo());
        }

        int row = query.executeUpdate();

        return row;
    }

    private String getWhere() {
        String where = "";
        String and = "";

        if (filter.getPlannedUtcFrom() != null && !filter.getPlannedUtcFrom().equals("")) {
            where += and + " schedulePlanned>= :schedulePlannedFrom";
            and = " and ";
        }

        if (filter.getPlannedUtcTo() != null && !filter.getPlannedUtcTo().equals("")) {
            where += and + " schedulePlanned <= :schedulePlannedTo ";
            and = " and ";
        }

        if (filter.getSchedulerId() != null && !filter.getSchedulerId().equals("")) {
            where += and + " schedulerId = :schedulerId";
            and = " and ";
        }

        if (where.trim().equals("")) {

        } else {
            where = "where " + where;
        }
        return where;

    }

    public List<DailyScheduleDBItem> getDailyScheduleList(final int limit) {
        initSession();

        Query query = session.createQuery("from DailyScheduleDBItem " + getWhere() + filter.getOrderCriteria() + filter.getSortMode());

        if (filter.getPlannedFrom() != null && !filter.getPlannedFrom().equals("")) {
            query.setTimestamp("schedulePlannedFrom", filter.getPlannedFrom());
        }
        if (filter.getPlannedTo() != null && !filter.getPlannedTo().equals("")) {
            query.setTimestamp("schedulePlannedTo", filter.getPlannedTo());
        }
        if (filter.getSchedulerId() != null && !filter.getSchedulerId().equals("")) {
            query.setParameter("schedulerId", filter.getSchedulerId());
        }

        if (limit > 0) {
            query.setMaxResults(limit);
        }

        @SuppressWarnings("unchecked")
        List<DailyScheduleDBItem> daysScheduleList = query.list();
        return daysScheduleList;

    }

    private List<DailyScheduleDBItem> executeQuery(Query query, int limit) {
        if (filter.getPlannedUtcFrom() != null && !filter.getPlannedUtcFrom().equals("")) {
            query.setTimestamp("schedulePlannedFrom", filter.getPlannedUtcFrom());
        }
        if (filter.getPlannedUtcTo() != null && !filter.getPlannedUtcTo().equals("")) {
            query.setTimestamp("schedulePlannedTo", filter.getPlannedUtcTo());
        }
        if (filter.getSchedulerId() != null && !filter.getSchedulerId().equals("")) {
            query.setParameter("schedulerId", filter.getSchedulerId());
        }

        if (limit > 0) {
            query.setMaxResults(limit);
        }

        List<DailyScheduleDBItem> daysScheduleList = query.list();
        return daysScheduleList;

    }

    public List<DailyScheduleDBItem> getDailyScheduleSchedulerList(int limit) {
        initSession();
        String q = "from DailyScheduleDBItem e where e.schedulerId IN (select DISTINCT schedulerId from DailyScheduleDBItem " + getWhere() + ")";

        Query query = session.createQuery(q);
        return executeQuery(query, limit);
    }

    public List<DailyScheduleDBItem> getWaitingDailyScheduleList(final int limit) {
        initSession();

        Query query = session.createQuery("from DailyScheduleDBItem " + getWhere() + "  and status = 0  " + filter.getOrderCriteria()
                + filter.getSortMode());

        return executeQuery(query, limit);

    }

    public DailyScheduleFilter getFilter() {
        return filter;
    }

    public void setWhereFrom(final Date whereFrom) {
        filter.setPlannedFrom(whereFrom);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        whereFromIso = formatter.format(whereFrom);
    }

    public void setWhereTo(final Date whereTo) {
        UtcTimeHelper.convertTimeZonesToDate(UtcTimeHelper.localTimeZoneString(), "UTC", new DateTime(whereTo));
        filter.setPlannedTo(whereTo);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        whereToIso = formatter.format(whereTo);
    }

    public void setWhereFromUtc(final String whereFrom) throws ParseException {
        if (whereFrom.equals("")) {
            filter.setPlannedFrom("");
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat(filter.getDateFormat());
            Date d = formatter.parse(whereFrom);
            d = UtcTimeHelper.convertTimeZonesToDate(UtcTimeHelper.localTimeZoneString(), "UTC", new DateTime(d));

            setWhereFrom(d);
        }
    }

    public void setWhereToUtc(final String whereTo) throws ParseException {
        if (whereTo.equals("")) {
            filter.setPlannedTo("");
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat(filter.getDateFormat());
            Date d = formatter.parse(whereTo);
            d = UtcTimeHelper.convertTimeZonesToDate(UtcTimeHelper.localTimeZoneString(), "UTC", new DateTime(d));
            setWhereTo(d);
        }
    }

    public void setWhereFrom(final String whereFrom) throws ParseException {
        if (whereFrom.equals("")) {
            filter.setPlannedFrom("");
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat(filter.getDateFormat());
            Date d = formatter.parse(whereFrom);

            setWhereFrom(d);
        }
    }

    public void setWhereTo(final String whereTo) throws ParseException {
        if (whereTo.equals("")) {
            filter.setPlannedTo("");
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat(filter.getDateFormat());
            Date d = formatter.parse(whereTo);
            setWhereTo(d);
        }
    }

    public Date getWhereUtcFrom() {
        return filter.getPlannedUtcFrom();
    }

    public Date getWhereUtcTo() {
        return filter.getPlannedUtcTo();
    }

    public void setWhereSchedulerId(final String whereSchedulerId) {
        filter.setSchedulerId(whereSchedulerId);
    }

    public void setDateFormat(final String dateFormat) {
        filter.setDateFormat(dateFormat);
    }

    public String getWhereFromIso() {
        return whereFromIso;
    }

    public String getWhereToIso() {
        return whereToIso;
    }

    public void setFilter(final DailyScheduleFilter filter) {
        this.filter = filter;
    }

    public boolean contains(final SchedulerTaskHistoryDBItem schedulerHistoryDBItem) {
        if (session == null) {
            initSession();
            transaction = session.beginTransaction();
        }
        Query query = session.createQuery("from DailyScheduleDBItem where schedulerId=:schedulerId and schedulerHistoryId=:schedulerHistoryId");
        query.setParameter("schedulerId", schedulerHistoryDBItem.getSpoolerId());
        query.setParameter("schedulerHistoryId", schedulerHistoryDBItem.getId());

        @SuppressWarnings("unchecked")
        List<DailyScheduleDBItem> daysScheduleList = query.list();
        return daysScheduleList.size() > 0;
    }

    public boolean contains(final SchedulerOrderHistoryDBItem schedulerOrderHistoryDBItem) {
        if (session == null) {
            initSession();
            transaction = session.beginTransaction();
        }
        Query query = session.createQuery("from DailyScheduleDBItem where schedulerId=:schedulerId and jobChain=:jobChain and schedulerOrderHistoryId=:schedulerOrderHistoryId");
        query.setParameter("schedulerId", schedulerOrderHistoryDBItem.getSpoolerId());
        query.setParameter("schedulerOrderHistoryId", schedulerOrderHistoryDBItem.getHistoryId());
        query.setParameter("jobChain", schedulerOrderHistoryDBItem.getJobChain());
        @SuppressWarnings("unchecked")
        List<DailyScheduleDBItem> daysScheduleList = query.list();
        return daysScheduleList.size() > 0;
    }

    @Override
    public void onAfterDeleting(DbItem h) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<DbItem> getListOfItemsToDelete() {
        TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));

        int limit = this.getFilter().getLimit();
        initSession();

        Query query = session.createQuery("from DailyScheduleDBItem " + getWhere() + filter.getOrderCriteria() + filter.getSortMode());

        if (filter.getSchedulerId() != null && !filter.getSchedulerId().equals("")) {
            query.setText("schedulerId", filter.getSchedulerId());
        }

        if (filter.getPlannedUtcFrom() != null) {
            query.setTimestamp("schedulePlannedFrom", filter.getExecutedUtcFrom());
        }
        if (filter.getPlannedUtcTo() != null) {
            query.setTimestamp("schedulePlannedTo", filter.getPlannedUtcTo());
        }

        if (limit > 0) {
            query.setMaxResults(limit);
        }

        List<DbItem> schedulerPlannedList = query.list();
        return schedulerPlannedList;

    }

}
