package com.sos.schedulerinstances;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.prefs.Preferences;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Table;
import org.hibernate.Session;

import com.sos.hibernate.classes.DbItem;
import com.sos.hibernate.classes.SOSSearchFilterData;
import com.sos.hibernate.interfaces.ISOSDashboardDataProvider;
import com.sos.hibernate.interfaces.ISOSHibernateDataProvider;
import com.sos.scheduler.db.SchedulerInstancesDBItem;
import com.sos.scheduler.db.SchedulerInstancesDBLayer;
import com.sos.scheduler.db.SchedulerInstancesFilter;
import com.sos.schedulerinstances.classes.SosSchedulerInstancesTableItem;

/** @author Uwe Risse */
public class SchedulerInstancesDataProvider implements ISOSHibernateDataProvider, ISOSDashboardDataProvider {

    private static final Logger LOGGER = Logger.getLogger(SchedulerInstancesDataProvider.class);
    private List<SchedulerInstancesDBItem> listOfSchedulerInstancesDBItems = null;
    private SchedulerInstancesDBLayer schedulerInstancesDBLayer = null;
    private String timeZone = "";

    public SchedulerInstancesDataProvider(File configurationFile) {
        this.schedulerInstancesDBLayer = new SchedulerInstancesDBLayer(configurationFile);
    }

    public SchedulerInstancesFilter getFilter() {
        return schedulerInstancesDBLayer.getFilter();
    }

    public void resetFilter() {
        schedulerInstancesDBLayer.initFilter();
    }

    public void getData(int limit) {
        listOfSchedulerInstancesDBItems = schedulerInstancesDBLayer.getSchedulerInstancesList();
    }

    public void fillSchedulerIds(CCombo cbSchedulerId) {
        if (listOfSchedulerInstancesDBItems != null) {
            Iterator<SchedulerInstancesDBItem> schedulerInstancesEntries = listOfSchedulerInstancesDBItems.iterator();
            while (schedulerInstancesEntries.hasNext()) {
                SchedulerInstancesDBItem h = (SchedulerInstancesDBItem) schedulerInstancesEntries.next();
                if (cbSchedulerId.indexOf(h.getSchedulerId()) < 0) {
                    LOGGER.debug("... cbSchedulerId --> : " + h.getSchedulerId());
                    cbSchedulerId.add(h.getSchedulerId());
                }
            }
        }
    }

    public void fillTable(Table table) {
        Iterator<SchedulerInstancesDBItem> dailyScheduleEntries = listOfSchedulerInstancesDBItems.iterator();
        while (dailyScheduleEntries.hasNext()) {
            DbItem h = dailyScheduleEntries.next();
            if (h != null) {
                if (schedulerInstancesDBLayer.getFilter().isFiltered(h)) {
                } else {
                    final SosSchedulerInstancesTableItem newItemTableItem = new SosSchedulerInstancesTableItem(table, SWT.BORDER);
                    h.setDateTimeZone4Getters(timeZone);
                    newItemTableItem.setDBItem(h);
                    newItemTableItem.setData(h);
                    newItemTableItem.setColor();
                    newItemTableItem.setColumns();
                }
            }
        }
    }

    public File getConfigurationFile() {
        return schedulerInstancesDBLayer.getConfigurationFile();
    }

    @Override
    public void beginTransaction() {
        schedulerInstancesDBLayer.beginTransaction();
    }

    public void save(SchedulerInstancesDBItem dbItem) {
        schedulerInstancesDBLayer.save(dbItem);
    }

    @Override
    public void update(DbItem dbItem) {
        schedulerInstancesDBLayer.update(dbItem);
    }

    public void closeSession() {
        schedulerInstancesDBLayer.closeSession();
    }

    public Session getSession() {
        return schedulerInstancesDBLayer.getSession();
    }

    @Override
    public void commit() {
        schedulerInstancesDBLayer.commit();
    }

    @Override
    public void setSchedulerId(String schedulerId) {
        this.getFilter().setSchedulerId(schedulerId);
    }

    @Override
    public void setFrom(Date d) {

    }

    @Override
    public void setTo(Date d) {
    }

    @Override
    public void setSearchField(SOSSearchFilterData s) {
        //
    }

    @Override
    public void setShowJobs(boolean b) {
    }

    @Override
    public void setShowJobChains(boolean b) {
    }

    @Override
    public void setIgnoreList(Preferences prefs) {
    }

    @Override
    public void addToIgnorelist(Preferences prefs, DbItem h) {
    }

    @Override
    public void disableIgnoreList(Preferences prefs) {
    }

    @Override
    public void resetIgnoreList(Preferences prefs) {
    }

    @Override
    public void setLate(boolean b) {
    }

    @Override
    public void setStatus(String status) {
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
    }

}