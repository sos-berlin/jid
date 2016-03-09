package com.sos.scheduler.history;

import java.io.File;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.prefs.Preferences;

import org.apache.log4j.Logger;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Table;
import org.hibernate.Session;

import com.sos.hibernate.classes.DbItem;
import com.sos.hibernate.classes.SOSSearchFilterData;
import com.sos.hibernate.interfaces.ISOSDashboardDataProvider;
import com.sos.hibernate.interfaces.ISOSHibernateFilter;
import com.sos.scheduler.history.db.SchedulerOrderHistoryDBItem;
import com.sos.scheduler.history.db.SchedulerTaskHistoryDBItem;
import com.sos.schedulerinstances.SchedulerInstancesDataProvider;

public class SchedulerHistoryDataProvider implements ISOSDashboardDataProvider {

    private static final String LIST_IGNORE_JOBS = "listIgnoreJobs";
    private static final String LIST_IGNORE_ORDERS = "listIgnoreOrders";
    private static final String SOS_DASHBOARD_EXECUTED = "sosDashboardExecuted";
    @SuppressWarnings("unused")
    private final String conClassName = "SchedulerHistoryDataProvider";

    private SchedulerTaskHistoryDataProvider schedulerTaskHistoryDataProvider;
    private SchedulerOrderHistoryDataProvider schedulerOrderHistoryDataProvider;
    private SchedulerInstancesDataProvider schedulerInstancesDataProvider;
    private String timeZone;

    public void setJobName(String jobName) {
        schedulerTaskHistoryDataProvider.getFilter().setJobname(jobName);
    }

    public void setJobChain(String jobChain) {
        schedulerOrderHistoryDataProvider.getFilter().setJobchain(jobChain);
    }

    public void setOrderId(String orderId) {
        schedulerOrderHistoryDataProvider.getFilter().setOrderid(orderId);
    }

    public void disableIgnoreList(Preferences prefs) {
        resetIgnoreList();
    }

    public SchedulerHistoryDataProvider(File configurationFile) {
        schedulerTaskHistoryDataProvider = new SchedulerTaskHistoryDataProvider(configurationFile);
        schedulerOrderHistoryDataProvider = new SchedulerOrderHistoryDataProvider(configurationFile);
        schedulerInstancesDataProvider = new SchedulerInstancesDataProvider(configurationFile);
    }

    public void setShowJobs(boolean showJobs) {
        schedulerTaskHistoryDataProvider.getFilter().setShowJobs(showJobs);
        schedulerOrderHistoryDataProvider.getFilter().setShowJobs(showJobs);
    }

    public boolean getShowJobs() {
        return schedulerTaskHistoryDataProvider.getFilter().isShowJobs();
    }

    public boolean getShowJobchains() {
        return schedulerOrderHistoryDataProvider.getFilter().isShowJobChains();
    }

    public void setShowJobChains(boolean showJobChains) {
        schedulerTaskHistoryDataProvider.getFilter().setShowJobChains(showJobChains);
        schedulerOrderHistoryDataProvider.getFilter().setShowJobChains(showJobChains);
    }

    public void setFrom(Date d) {
        schedulerTaskHistoryDataProvider.getFilter().setExecutedFrom(d);
        schedulerOrderHistoryDataProvider.getFilter().setExecutedFrom(d);
    }

    public void setTo(Date d) {
        schedulerTaskHistoryDataProvider.getFilter().setExecutedTo(d);
        schedulerOrderHistoryDataProvider.getFilter().setExecutedTo(d);
    }

    public void resetFilter() {
        schedulerTaskHistoryDataProvider.resetFilter();
        schedulerOrderHistoryDataProvider.resetFilter();
    }

    public void getData(int limit) {
        schedulerTaskHistoryDataProvider.getData(limit);
        schedulerOrderHistoryDataProvider.getData(limit);
    }

    public void fillTable(Table table) {

        schedulerTaskHistoryDataProvider.fillTable(table);
        schedulerOrderHistoryDataProvider.fillTable(table);
    }

    public void fillTableShort(Table table, boolean standalone) {
        if (standalone) {
            schedulerTaskHistoryDataProvider.fillTableShort(table);
        } else {
            schedulerOrderHistoryDataProvider.fillTableShort(table);
        }
    }

    public void setSchedulerId(String schedulerId) {
        schedulerTaskHistoryDataProvider.getFilter().setSchedulerId(schedulerId);
        schedulerOrderHistoryDataProvider.getFilter().setSchedulerId(schedulerId);
    }

    public void setJobname(String jobName) {
        schedulerTaskHistoryDataProvider.getFilter().setJobname(jobName);
    }

    public void setJobchain(String jobChain) {
        schedulerOrderHistoryDataProvider.getFilter().setJobchain(jobChain);
    }

    public void setOrderid(String orderId) {
        schedulerOrderHistoryDataProvider.getFilter().setOrderid(orderId);
    }

    public void fillSchedulerIds(CCombo cbSchedulerId) {
        schedulerInstancesDataProvider.getData(0);
        schedulerInstancesDataProvider.fillSchedulerIds(cbSchedulerId);
        schedulerTaskHistoryDataProvider.fillSchedulerIds(cbSchedulerId);
        schedulerOrderHistoryDataProvider.fillSchedulerIds(cbSchedulerId);
    }

    public String getLogAsString(DbItem dbItem) {
        if (dbItem.isStandalone()) {
            return schedulerTaskHistoryDataProvider.getLogAsString(dbItem.getLogId());
        } else {
            return schedulerOrderHistoryDataProvider.getLogAsString(dbItem.getLogId());
        }

    }

    public void addToIgnorelist(Preferences prefs, DbItem h) {
        if (h.isStandalone()) {
            String listOfJobs = prefs.node(SOS_DASHBOARD_EXECUTED).get(LIST_IGNORE_JOBS, "");
            if (!listOfJobs.contains(h.getJobName())) {
                listOfJobs = listOfJobs + "," + h.getJobName();
                prefs.node(SOS_DASHBOARD_EXECUTED).put(LIST_IGNORE_JOBS, listOfJobs);

                addToTaskIgnorelist((SchedulerTaskHistoryDBItem) h);
            }
        } else {
            String listOfOrders = prefs.node(SOS_DASHBOARD_EXECUTED).get(LIST_IGNORE_ORDERS, "");
            if (!listOfOrders.contains(h.getIdentifier())) {
                listOfOrders = listOfOrders + "," + h.getIdentifier();
                prefs.node(SOS_DASHBOARD_EXECUTED).put(LIST_IGNORE_ORDERS, listOfOrders);
                addToOrderIgnorelist((SchedulerOrderHistoryDBItem) h);
            }
        }

    }

    public void resetIgnoreList(Preferences prefs) {
        prefs.node(SOS_DASHBOARD_EXECUTED).put(LIST_IGNORE_JOBS, "");
        prefs.node(SOS_DASHBOARD_EXECUTED).put(LIST_IGNORE_ORDERS, "");
        resetIgnoreList();

    }

    public void addToTaskIgnorelist(SchedulerTaskHistoryDBItem h) {
        schedulerTaskHistoryDataProvider.getFilter().getTaskIgnoreList().add(h);
        schedulerOrderHistoryDataProvider.getFilter().getTaskIgnoreList().add(h);
    }

    public void addToOrderIgnorelist(SchedulerOrderHistoryDBItem h) {
        schedulerOrderHistoryDataProvider.getFilter().getOrderIgnoreList().add(h);
        schedulerTaskHistoryDataProvider.getFilter().getOrderIgnoreList().add(h);
    }

    public void resetIgnoreList() {
        schedulerTaskHistoryDataProvider.getFilter().getTaskIgnoreList().reset();
        schedulerTaskHistoryDataProvider.getFilter().getOrderIgnoreList().reset();
        schedulerOrderHistoryDataProvider.getFilter().getTaskIgnoreList().reset();
        schedulerOrderHistoryDataProvider.getFilter().getOrderIgnoreList().reset();
    }

    public void setIgnoreList(Preferences prefs) {
        String listOfJobs = prefs.node(SOS_DASHBOARD_EXECUTED).get(LIST_IGNORE_JOBS, "");
        String listOfOrders = prefs.node(SOS_DASHBOARD_EXECUTED).get(LIST_IGNORE_ORDERS, "");

        StringTokenizer st = new StringTokenizer(listOfJobs, ",");
        while (st.hasMoreTokens()) {
            String jobname = st.nextToken();
            SchedulerTaskHistoryDBItem h = new SchedulerTaskHistoryDBItem();
            h.setJobName(jobname);
            addToTaskIgnorelist(h);
        }

        st = new StringTokenizer(listOfOrders, ",");
        while (st.hasMoreTokens()) {
            String order = st.nextToken();

            File f = new File(order);
            String jobChain = f.getParent();
            jobChain = jobChain.replaceAll("\\\\", "/");
            String orderId = f.getName();

            SchedulerOrderHistoryDBItem h = new SchedulerOrderHistoryDBItem();
            h.setJobChain(jobChain);
            h.setOrderId(orderId);
            addToOrderIgnorelist(h);
        }

    }

    @Override
    public ISOSHibernateFilter getFilter() {
        return schedulerTaskHistoryDataProvider.getFilter();
    }

    @Override
    public void setLate(boolean b) {
    }

    @Override
    public void setStatus(String statusExecuted) {
    }

    @Override
    public void beginTransaction() {
    }

    @Override
    public void update(DbItem h) {
    }

    @Override
    public void commit() {
        schedulerTaskHistoryDataProvider.commit();
    }

    @Override
    public Session getSession() {
        return null;
    }

    public void closeSession() {
        schedulerTaskHistoryDataProvider.closeSession();
        schedulerOrderHistoryDataProvider.closeSession();
    }

    @Override
    public void setShowWithError(boolean b) {
        schedulerTaskHistoryDataProvider.getFilter().setShowWithError(b);
        schedulerOrderHistoryDataProvider.getFilter().setShowWithError(b);
    }

    @Override
    public void setShowRunning(boolean b) {
        schedulerTaskHistoryDataProvider.getFilter().setShowRunning(b);
        schedulerOrderHistoryDataProvider.getFilter().setShowRunning(b);
    }

    @Override
    public void setSearchField(SOSSearchFilterData s) {
        schedulerOrderHistoryDataProvider.getFilter().setSosSearchFilterData(s);
        schedulerTaskHistoryDataProvider.getFilter().setSosSearchFilterData(s);
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
        schedulerTaskHistoryDataProvider.setTimeZone(timeZone);
        schedulerOrderHistoryDataProvider.setTimeZone(timeZone);
        schedulerInstancesDataProvider.setTimeZone(timeZone);

    }

}
