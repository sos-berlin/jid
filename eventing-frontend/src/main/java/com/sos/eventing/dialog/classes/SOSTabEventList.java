package com.sos.eventing.dialog.classes;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import sos.util.SOSStandardLogger;

import com.sos.eventing.frontend.EventShowDialog;
import com.sos.eventing.frontend.Scheduler;

@SuppressWarnings("deprecation")
public class SOSTabEventList extends CTabItem {
	private final Composite	parentComposite;
	private final GridLayout	layout;
	private Table table;
	private final Scheduler scheduler;
	EventShowDialog eventList;

	public SOSTabEventList(final String caption, final CTabFolder parentTabfolder, final String host, final int port) {
		super(parentTabfolder, SWT.NONE);
		setText(caption);

		scheduler = new Scheduler();
		scheduler.setHostName(host);
		scheduler.setTcpPort(String.valueOf(port));

		parentComposite = new Composite(parentTabfolder, SWT.NONE);
		layout = new GridLayout(1, false);
		parentComposite.setLayout(layout);
	    parentComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		createContent();

		parentComposite.layout();
		this.setControl(parentComposite);
	}

	private void createContent(){

		try {

		    SOSStandardLogger sosLogger = new SOSStandardLogger(9);
		    eventList = new EventShowDialog(parentComposite, scheduler, sosLogger);
		    eventList.showComposite();
            table = eventList.getTable();
		} catch (Exception e) {
 			e.printStackTrace();
		}
	}

	public Composite getParentComposite() {
		return parentComposite;
	}

	public Table getTable() {
		return table;
	}

    public void setPort(final int port){
        scheduler.setTcpPort(String.valueOf(port));
        eventList.setScheduler(scheduler);
    }

    public void setHost(final String host){
        scheduler.setHostName(host);
        eventList.setScheduler(scheduler);
    }

	public void disableRefresh(){
		eventList.disableRefresh();
	}

	public void enableRefresh(){
		eventList.resetRefreshTimer();
	}

}
