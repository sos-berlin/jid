package com.sos.dailyschedule;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.prefs.Preferences;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.hibernate.Session;

import com.sos.dailyschedule.classes.SosDailyScheduleTableItem;
import com.sos.dailyschedule.db.DailyScheduleDBItem;
import com.sos.dailyschedule.db.DailyScheduleDBLayer;
import com.sos.hibernate.classes.DbItem;
import com.sos.hibernate.classes.SOSSearchFilterData;
import com.sos.hibernate.interfaces.ISOSDashboardDataProvider;
import com.sos.hibernate.interfaces.ISOSHibernateDataProvider;
import com.sos.scheduler.history.SchedulerOrderHistoryDataProvider;
import com.sos.scheduler.history.SchedulerTaskHistoryDataProvider;
import com.sos.schedulerinstances.SchedulerInstancesDataProvider;

public class DailyScheduleDataProvider implements ISOSHibernateDataProvider, ISOSDashboardDataProvider {

    private static final String SOS_DASHBOARD_PLANNED = "sosDashboardPlanned";
    private static final String LIST_IGNORE_JOBS = "listIgnoreJobs";
    private static final String LIST_IGNORE_ORDERS = "listIgnoreOrders";
    private static final Logger LOGGER = Logger.getLogger(DailyScheduleDataProvider.class);
    private List<DailyScheduleDBItem> listOfDaysScheduleDBItems = null;
    private DailyScheduleDBLayer dailySchedulerDBLayer = null;
    private Table tableDailySchedule = null;
    private String timeZone = "";

    public DailyScheduleDataProvider(File configurationFile) {
        this.dailySchedulerDBLayer = new DailyScheduleDBLayer(configurationFile);
    }

    public DailyScheduleFilter getFilter() {
        return dailySchedulerDBLayer.getFilter();
    }

    public void resetFilter() {
        dailySchedulerDBLayer.resetFilter();
    }

    public void getData(int limit) {
        listOfDaysScheduleDBItems = dailySchedulerDBLayer.getDailyScheduleList(limit);
    }

    public void fillSchedulerIds(CCombo cbSchedulerId) {
        SchedulerInstancesDataProvider schedulerInstancesDataProvider = new SchedulerInstancesDataProvider(this.getConfigurationFileName());
        schedulerInstancesDataProvider.getData(0);
        schedulerInstancesDataProvider.fillSchedulerIds(cbSchedulerId);
        if (listOfDaysScheduleDBItems != null) {
            Iterator<DailyScheduleDBItem> dailyScheduleEntries = listOfDaysScheduleDBItems.iterator();
            while (dailyScheduleEntries.hasNext()) {
                DailyScheduleDBItem h = (DailyScheduleDBItem) dailyScheduleEntries.next();
                if (cbSchedulerId.indexOf(h.getSchedulerId()) < 0) {
                    LOGGER.debug("... cbSchedulerId --> : " + h.getSchedulerId());
                    cbSchedulerId.add(h.getSchedulerId());
                }
            }
        }
    }

    public String getLogAsString(Table tableDailySchedule, SchedulerTaskHistoryDataProvider schedulerTaskHistoryDataProvider,
            SchedulerOrderHistoryDataProvider schedulerOrderHistoryDataProvider) {
        String log = "";
        if (tableDailySchedule.getSelectionIndex() >= 0) {
            TableItem t = tableDailySchedule.getItem(tableDailySchedule.getSelectionIndex());
            DailyScheduleDBItem h = (DailyScheduleDBItem) t.getData();
            if (h.isStandalone()) {
                if (h.getSchedulerHistoryId() != null) {
                    log = schedulerTaskHistoryDataProvider.getLogAsString(h.getSchedulerHistoryId());
                }
            } else {
                if (h.getSchedulerOrderHistoryId() != null) {
                    log = schedulerOrderHistoryDataProvider.getLogAsString(h.getSchedulerOrderHistoryId());
                }
            }
        }
        return log;
    }

    public void fillTable(Table table) {
        this.tableDailySchedule = table;
        Iterator<DailyScheduleDBItem> dailyScheduleEntries = listOfDaysScheduleDBItems.iterator();
        while (dailyScheduleEntries.hasNext()) {
            DbItem h = dailyScheduleEntries.next();
            if (!dailySchedulerDBLayer.getFilter().isFiltered(h)) {
                final SosDailyScheduleTableItem newItemTableItem = new SosDailyScheduleTableItem(table, SWT.BORDER);
                h.setDateTimeZone4Getters(this.getTimeZone());
                newItemTableItem.setDBItem(h);
                newItemTableItem.setData(h);
                newItemTableItem.setColor();
                newItemTableItem.setColumns();
            }
        }
    }

    public String getConfigurationFileName() {
        return dailySchedulerDBLayer.getConfigurationFileName();
    }

    public File getConfigurationFile() {
        return dailySchedulerDBLayer.getConfigurationFile();
    }

    @Override
    @Deprecated
    public void beginTransaction() {
        // dailySchedulerDBLayer.beginTransaction();
    }

    @Override
    @Deprecated
    public void update(DbItem dbItem) {
        // dailySchedulerDBLayer.update(dbItem);
    }

    @Override
    @Deprecated
    public void commit() {
        // dailySchedulerDBLayer.commit();
    }

    @Override
    public void setSchedulerId(String schedulerId) {
        this.getFilter().setSchedulerId(schedulerId);
    }

    @Override
    public void setFrom(Date d) {
        this.getFilter().setPlannedFrom(d);
    }

    @Override
    public void setTo(Date d) {
        this.getFilter().setPlannedTo(d);
    }

    @Override
    public void setSearchField(SOSSearchFilterData s) {
        this.getFilter().setSosSearchFilterData(s);
    }

    @Override
    public void setShowJobs(boolean b) {
        this.getFilter().setShowJobs(b);
    }

    @Override
    public void setShowJobChains(boolean b) {
        this.getFilter().setShowJobChains(b);
    }

    public void disableIgnoreList(Preferences prefs) {
        this.getFilter().getIgnoreList().reset();
    }

    @Override
    public void addToIgnorelist(Preferences prefs, DbItem h) {
        if (h.isStandalone()) {
            String listOfJobs = prefs.node(SOS_DASHBOARD_PLANNED).get(LIST_IGNORE_JOBS, "");
            if (!listOfJobs.contains(h.getJobName())) {
                listOfJobs = listOfJobs + "," + h.getJobName();
                prefs.node(SOS_DASHBOARD_PLANNED).put(LIST_IGNORE_JOBS, listOfJobs);
            }
        } else {
            String listOfOrders = prefs.node(SOS_DASHBOARD_PLANNED).get(LIST_IGNORE_ORDERS, "");
            if (!listOfOrders.contains(h.getIdentifier())) {
                listOfOrders = listOfOrders + "," + h.getIdentifier();
                prefs.node(SOS_DASHBOARD_PLANNED).put(LIST_IGNORE_ORDERS, listOfOrders);
            }
        }
        this.getFilter().getIgnoreList().add(h);
    }

    @Override
    public void resetIgnoreList(Preferences prefs) {
        prefs.node(SOS_DASHBOARD_PLANNED).put(LIST_IGNORE_JOBS, "");
        this.getFilter().getIgnoreList().reset();
    }

    public void setIgnoreList(Preferences prefs) {
        String listOfJobs = prefs.node(SOS_DASHBOARD_PLANNED).get(LIST_IGNORE_JOBS, "");
        String listOfOrders = prefs.node(SOS_DASHBOARD_PLANNED).get(LIST_IGNORE_ORDERS, "");
        StringTokenizer st = new StringTokenizer(listOfJobs, ",");
        while (st.hasMoreTokens()) {
            String jobname = st.nextToken();
            DailyScheduleDBItem h = new DailyScheduleDBItem();
            h.setJob(jobname);
            this.getFilter().getIgnoreList().add(h);
        }
        st = new StringTokenizer(listOfOrders, ",");
        while (st.hasMoreTokens()) {
            String order = st.nextToken();
            File f = new File(order);
            String jobChain = f.getParent();
            jobChain = jobChain.replaceAll("\\\\", "/");
            String orderId = f.getName();
            DailyScheduleDBItem h = new DailyScheduleDBItem();
            h.setJobChain(jobChain);
            h.setOrderId(orderId);
            this.getFilter().getIgnoreList().add(h);
        }
    }

    @Override
    public void setLate(boolean b) {
        this.getFilter().setLate(b);
    }

    @Override
    public void setStatus(String status) {
        this.getFilter().setStatus(status);
    }

    @Override
    public void setShowWithError(boolean b) {
    }

    @Override
    public void setShowRunning(boolean b) {
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
        this.getFilter().setTimeZone(timeZone);
    }

    public DailyScheduleDBLayer getDailyScheduleDBLayer() {
        return this.dailySchedulerDBLayer;
    }

    @Override
    public Session getSession() {
        try {
            return (Session) this.dailySchedulerDBLayer.getConnection().getCurrentSession();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

}