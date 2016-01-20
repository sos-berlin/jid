package com.sos.scheduler.history;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import com.sos.scheduler.history.classes.SchedulerOrderStepHistoryTableItem;
import com.sos.scheduler.history.db.SchedulerOrderStepHistoryCompoundKey;
import com.sos.scheduler.history.db.SchedulerOrderStepHistoryDBItem;
import com.sos.scheduler.history.db.SchedulerOrderStepHistoryDBLayer;

public class SchedulerOrderStepHistoryDataProvider {

    @SuppressWarnings("unused")
    private final String conClassName = "SchedulerOrderStepHistoryDataProvider";

    private List<SchedulerOrderStepHistoryDBItem> listOfSchedulerOrderStepHistoryDBItems = null;
    private SchedulerOrderStepHistoryDBLayer schedulerOrderStepHistoryDBLayer = null;
    private static Logger logger = Logger.getLogger(SchedulerOrderStepHistoryDataProvider.class);
    private long historyId;
    private String timeZone = "";

    public SchedulerOrderStepHistoryDataProvider(File configurationFile, long historyId_) {
        this.schedulerOrderStepHistoryDBLayer = new SchedulerOrderStepHistoryDBLayer(configurationFile);
        this.historyId = historyId_;
    }

    public SchedulerOrderStepHistoryFilter getFilter() {
        return schedulerOrderStepHistoryDBLayer.getFilter();
    }

    public void resetFilter() {
        schedulerOrderStepHistoryDBLayer.resetFilter();
    }

    public void getData(int limit) {

        if (historyId >= 0) {
            schedulerOrderStepHistoryDBLayer.getFilter().setLimit(limit);
            listOfSchedulerOrderStepHistoryDBItems = schedulerOrderStepHistoryDBLayer.getOrderStepHistoryItems(0, historyId);
        }
    }

    public String getLogAsString(Table tableSchedulerOrderSteppHistory) {
        String log = "";
        if (tableSchedulerOrderSteppHistory.getSelectionIndex() >= 0) {
            TableItem t = tableSchedulerOrderSteppHistory.getItem(tableSchedulerOrderSteppHistory.getSelectionIndex());
            SchedulerOrderStepHistoryDBItem h = (SchedulerOrderStepHistoryDBItem) t.getData();
            if (h.getId() != null) {
                log = getLogAsString(h.getId());
            }
        }
        return log;
    }

    public String getLogAsString(SchedulerOrderStepHistoryCompoundKey schedulerOrderStepHistoryCompoundKey) {
        String log = "";
        try {
            SchedulerOrderStepHistoryDBItem schedulerOrderStepHistoryDBItem = schedulerOrderStepHistoryDBLayer.get(schedulerOrderStepHistoryCompoundKey);
            if (schedulerOrderStepHistoryDBItem != null && schedulerOrderStepHistoryDBItem.getSchedulerOrderHistoryDBItem().getLog() != null) {
                log = schedulerOrderStepHistoryDBItem.getSchedulerOrderHistoryDBItem().getLogAsString();
            }
        } catch (IOException e1) {
            logger.error(e1.getMessage(), e1);

        }
        return log;
    }

    public void fillTable(Table table) {

        if (listOfSchedulerOrderStepHistoryDBItems != null) {
            table.setRedraw(false);
            Iterator<SchedulerOrderStepHistoryDBItem> schedulerHistoryEntries = listOfSchedulerOrderStepHistoryDBItems.iterator();
            while (schedulerHistoryEntries.hasNext()) {
                SchedulerOrderStepHistoryDBItem h = (SchedulerOrderStepHistoryDBItem) schedulerHistoryEntries.next();
                if (schedulerOrderStepHistoryDBLayer.getFilter().isFiltered(h)) {
                } else {
                    final SchedulerOrderStepHistoryTableItem newItemTableItem = new SchedulerOrderStepHistoryTableItem(table, SWT.BORDER);
                    h.setDateTimeZone4Getters(timeZone);
                    newItemTableItem.setDBItem(h);

                    logger.debug("...creating tableItem: " + h.getTitle() + ":" + table.getItemCount());
                    newItemTableItem.setData(h);
                    newItemTableItem.setColor();
                    newItemTableItem.setColumns();
                }
            }
            table.setRedraw(true);

        }
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

}
