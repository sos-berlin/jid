package com.sos.scheduler.history;

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
import com.sos.scheduler.history.db.SchedulerTaskHistoryDBItem;
import com.sos.scheduler.history.db.SchedulerTaskHistoryDBLayer;

public class SchedulerTaskHistoryDataProvider implements ISOSHibernateDataProvider {
    private List<SchedulerTaskHistoryDBItem>    listOfSchedulerTaskHistoryDBItems   = null;
	private SchedulerTaskHistoryDBLayer			schedulerTaskHistoryDBLayer			= null;
	private static Logger						logger								= Logger.getLogger(SchedulerTaskHistoryDataProvider.class);
    private String timeZone;

    public SchedulerTaskHistoryDataProvider(String configurationFileName) {
        this.schedulerTaskHistoryDBLayer = new SchedulerTaskHistoryDBLayer(configurationFileName);
 	}

	public SchedulerTaskHistoryFilter getFilter() {
		return schedulerTaskHistoryDBLayer.getFilter();
	}

	public void resetFilter() {
		schedulerTaskHistoryDBLayer.resetFilter();
	}

	public void getData(int limit) {
	    schedulerTaskHistoryDBLayer.getFilter().setLimit(limit);
		listOfSchedulerTaskHistoryDBItems = schedulerTaskHistoryDBLayer.getSchedulerHistoryListFromTo();
	}

	public void fillSchedulerIds(CCombo cbSchedulerId) {
		if (listOfSchedulerTaskHistoryDBItems != null) {
            // Es ist schneller, einfach die gefundenen Sätze nochmal zu
            // verwenden
            // listOfSchedulerTaskHistoryDBSchedulersItems =
            // schedulerTaskHistoryDBLayer.getSchedulerHistoryListSchedulersFromTo();
			Iterator schedulerHistoryEntries = listOfSchedulerTaskHistoryDBItems.iterator();
			while (schedulerHistoryEntries.hasNext()) {
				SchedulerTaskHistoryDBItem h = (SchedulerTaskHistoryDBItem) schedulerHistoryEntries.next();
				if (cbSchedulerId.indexOf(h.getSpoolerId()) < 0) {
					logger.debug("... cbSchedulerId --> : " + h.getSpoolerId());
					cbSchedulerId.add(h.getSpoolerId());
				}
			}
		}
	}

	public String getLogAsString(Table tableSchedulerHistory) {
		String log = "";
		if (tableSchedulerHistory.getSelectionIndex() >= 0) {
			TableItem t = tableSchedulerHistory.getItem(tableSchedulerHistory.getSelectionIndex());
			SchedulerTaskHistoryDBItem h = (SchedulerTaskHistoryDBItem) t.getData();
			if (h.getId() != null) {
				log = getLogAsString(h.getId());
			}
		}
		return log;
	}

	public String getLogAsString(Long id) {
		String log = "";
		try {
			SchedulerTaskHistoryDBItem schedulerHistoryDBItem = schedulerTaskHistoryDBLayer.get(id);
			if (schedulerHistoryDBItem != null && schedulerHistoryDBItem.getLog() != null) {
				log = schedulerHistoryDBItem.getLogAsString();
			}
        } catch (IOException e1) {
            logger.error(e1.getMessage(),e1);
        }
		return log;
	}

	public void fillTableShort(Table table) {
		if (listOfSchedulerTaskHistoryDBItems != null) {
		    table.setRedraw(false);
            Iterator <SchedulerTaskHistoryDBItem> schedulerHistoryEntries = listOfSchedulerTaskHistoryDBItems.iterator();
			while (schedulerHistoryEntries.hasNext()) {
                SchedulerTaskHistoryDBItem h = schedulerHistoryEntries.next();
				if (!schedulerTaskHistoryDBLayer.getFilter().isFiltered(h)) {
					final SchedulerHistoryTableItem newItemTableItem = new SchedulerHistoryTableItem(table, SWT.BORDER);
                    h.setDateTimeZone4Getters(timeZone);
					newItemTableItem.setDBItem(h);
					logger.debug("...creating tableItem: " + h.getJobName() + ":" + table.getItemCount());
					newItemTableItem.setData(h);
					newItemTableItem.setColor();
					newItemTableItem.setColumnsShort();
				}
			}
            table.setRedraw(true);
		}
	}

	public void fillTable(Table table) {
		if (listOfSchedulerTaskHistoryDBItems != null) {
            table.setRedraw(false);
            Iterator <SchedulerTaskHistoryDBItem> schedulerHistoryEntries = listOfSchedulerTaskHistoryDBItems.iterator();
			while (schedulerHistoryEntries.hasNext()) {
                SchedulerTaskHistoryDBItem h =  schedulerHistoryEntries.next();
				if (!schedulerTaskHistoryDBLayer.getFilter().isFiltered(h)) {
					final SchedulerHistoryTableItem newItemTableItem = new SchedulerHistoryTableItem(table, SWT.BORDER);
					h.setDateTimeZone4Getters(timeZone);
					newItemTableItem.setDBItem(h);
					logger.debug("...creating tableItem: " + h.getJobName() + ":" + table.getItemCount());
					newItemTableItem.setData(h);
					newItemTableItem.setColor();
					newItemTableItem.setColumns();
				}
			}
            table.setRedraw(true);
		}
	}

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
        this.getFilter().setTimeZone(timeZone);
    }
	
 }

 
