package com.sos.scheduler.history;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import com.sos.hibernate.interfaces.ISOSHibernateDataProvider;
import com.sos.scheduler.history.classes.SchedulerHistoryTableItem;
import com.sos.scheduler.history.db.SchedulerOrderHistoryDBItem;
import com.sos.scheduler.history.db.SchedulerOrderHistoryDBLayer;

public class SchedulerOrderHistoryDataProvider implements ISOSHibernateDataProvider {


    @SuppressWarnings("unused")
    private final String conClassName = "SchedulerOrderHistoryDataProvider";

    private List<SchedulerOrderHistoryDBItem> listOfSchedulerOrderHistoryDBItems = null;
 
    private SchedulerOrderHistoryDBLayer schedulerOrderHistoryDBLayer = null;
    private static Logger logger = Logger.getLogger(SchedulerOrderHistoryDataProvider.class);
    private String timeZone;

    public SchedulerOrderHistoryDataProvider(final File configurationFile) {
        schedulerOrderHistoryDBLayer = new SchedulerOrderHistoryDBLayer(configurationFile);

    }

    @Override
    public SchedulerOrderHistoryFilter getFilter() {
        return schedulerOrderHistoryDBLayer.getFilter();
    }

    @Override
    public void resetFilter() {
        schedulerOrderHistoryDBLayer.resetFilter();
    }

    @Override
    public void getData(int limit) {
        schedulerOrderHistoryDBLayer.getFilter().setLimit(limit);
        listOfSchedulerOrderHistoryDBItems = schedulerOrderHistoryDBLayer.getSchedulerOrderHistoryListFromTo();
    }

    public void fillSchedulerIds(final CCombo cbSchedulerId) {
        if (listOfSchedulerOrderHistoryDBItems != null) {
            // Es ist schneller, die vorhandenen Sätze zu verwenden.
            // listOfSchedulerOrderHistoryDBSchedulersItems =
            // schedulerOrderHistoryDBLayer.getSchedulerOrderHistoryListSchedulersFromTo();
            Iterator<SchedulerOrderHistoryDBItem> schedulerOrderHistoryEntries = listOfSchedulerOrderHistoryDBItems.iterator();
            while (schedulerOrderHistoryEntries.hasNext()) {
                SchedulerOrderHistoryDBItem h = schedulerOrderHistoryEntries.next();
                if (cbSchedulerId.indexOf(h.getSpoolerId()) < 0) {
                    logger.debug("... cbSchedulerId --> : " + h.getSpoolerId());
                    cbSchedulerId.add(h.getSpoolerId());
                }
            }
        }
    }

    public String getLogAsString(final Table tableSchedulerOrderHistory) {
        String log = "";
        if (tableSchedulerOrderHistory.getSelectionIndex() >= 0) {
            TableItem t = tableSchedulerOrderHistory.getItem(tableSchedulerOrderHistory.getSelectionIndex());
            SchedulerOrderHistoryDBItem h = (SchedulerOrderHistoryDBItem) t.getData();
            if (h.getHistoryId() != null) {
                log = getLogAsString(h.getHistoryId());
            }
        }
        return log;
    }

    public String getLogAsString(final Long id) {
        String log = "";
        try {
            SchedulerOrderHistoryDBItem schedulerOrderHistoryDBItem = schedulerOrderHistoryDBLayer.get(id);
            if (schedulerOrderHistoryDBItem != null && schedulerOrderHistoryDBItem.getLog() != null) {
                log = schedulerOrderHistoryDBItem.getLogAsString();
            }
        } catch (IOException e1) {
            logger.error(e1.getMessage(), e1);
        }
        return log;
    }

    public void fillTableShort(final Table table) {
        if (listOfSchedulerOrderHistoryDBItems != null) {

            Iterator schedulerOrderHistoryEntries = listOfSchedulerOrderHistoryDBItems.iterator();
            while (schedulerOrderHistoryEntries.hasNext()) {
                SchedulerOrderHistoryDBItem h = (SchedulerOrderHistoryDBItem) schedulerOrderHistoryEntries.next();
                if (schedulerOrderHistoryDBLayer.getFilter().isFiltered(h)) {
                } else {
                    final SchedulerHistoryTableItem newItemTableItem = new SchedulerHistoryTableItem(table, SWT.BORDER);
                    h.setDateTimeZone4Getters(timeZone);
                    newItemTableItem.setDBItem(h);

                    logger.debug("...creating tableItem: " + h.getJobChain() + "/" + h.getOrderId() + ":" + table.getItemCount());
                    newItemTableItem.setData(h);
                    newItemTableItem.setColor();
                    newItemTableItem.setColumnsShort();

                }
            }
        }
    }

    @Override
    public void fillTable(final Table table) {

        if (listOfSchedulerOrderHistoryDBItems != null) {
            Iterator schedulerOrderHistoryEntries = listOfSchedulerOrderHistoryDBItems.iterator();
            while (schedulerOrderHistoryEntries.hasNext()) {
                SchedulerOrderHistoryDBItem h = (SchedulerOrderHistoryDBItem) schedulerOrderHistoryEntries.next();
                if (schedulerOrderHistoryDBLayer.getFilter().isFiltered(h)) {
                } else {
                    final SchedulerHistoryTableItem newItemTableItem = new SchedulerHistoryTableItem(table, SWT.BORDER);
                    h.setDateTimeZone4Getters(timeZone);

                    newItemTableItem.setDBItem(h);

                    logger.debug("...creating tableItem: " + h.getJobChain() + "/" + h.getOrderId() + ":" + table.getItemCount());
                    newItemTableItem.setData(h);
                    newItemTableItem.setColor();
                    newItemTableItem.setColumns();

                }
            }
        }
    }

    @Override
    public void commit() {
        schedulerOrderHistoryDBLayer.commit();
    }
 
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
        this.getFilter().setTimeZone(timeZone);
    }

    public void closeSession() {
        schedulerOrderHistoryDBLayer.closeSession();
    }

}
